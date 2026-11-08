package com.grupo9.edext.grupo9.servidor_web.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private static final Properties properties = new Properties();
    private static String configSource = "Valores por defecto / Variables de entorno";

    static {
        loadConfiguration();
    }

    private static synchronized void loadConfiguration() {
        File targetFile = findConfigFile();
        if (targetFile != null && targetFile.isFile() && targetFile.canRead()) {
            try (InputStream in = new FileInputStream(targetFile)) {
                properties.load(in);
                configSource = targetFile.getAbsolutePath();
                System.out.println("[ConfigLoader Web] Configuracion cargada desde: " + configSource);
            } catch (IOException e) {
                System.err.println("[ConfigLoader Web] Error al leer archivo de configuracion: " + e.getMessage());
            }
        } else {
            System.out.println("[ConfigLoader Web] No se encontro archivo config.properties externo. Usando " + configSource);
        }
    }

    private static File findConfigFile() {
        // 1. Propiedad de sistema -Dconfig.path=/ruta/a/config.properties
        String sysProp = System.getProperty("config.path");
        if (sysProp != null && !sysProp.isBlank()) {
            File f = new File(sysProp);
            if (f.exists()) return f;
        }

        // 2. Variable de entorno CONFIG_PATH
        String envPath = System.getenv("CONFIG_PATH");
        if (envPath != null && !envPath.isBlank()) {
            File f = new File(envPath);
            if (f.exists()) return f;
        }

        // 3. Directorio de ejecucion actual ./config.properties
        File currentDirFile = new File("config.properties");
        if (currentDirFile.exists()) {
            return currentDirFile;
        }

        // 4. Directorio ~/.edext/config.properties en home de usuario
        String userHome = System.getProperty("user.home");
        if (userHome != null && !userHome.isBlank()) {
            File edextDirFile = new File(userHome + File.separator + ".edext" + File.separator + "config.properties");
            if (edextDirFile.exists()) {
                return edextDirFile;
            }

            // 5. ~/config.properties en home de usuario
            File homeFile = new File(userHome + File.separator + "config.properties");
            if (homeFile.exists()) {
                return homeFile;
            }
        }

        return null;
    }

    public static String get(String key, String defaultValue) {
        // 1. System property (-Dkey=value)
        String val = System.getProperty(key);
        if (val != null && !val.isBlank()) {
            return val;
        }

        // 2. config.properties
        val = properties.getProperty(key);
        if (val != null && !val.isBlank()) {
            return val.trim();
        }

        // 3. Environment variable
        val = System.getenv(key);
        if (val != null && !val.isBlank()) {
            return val;
        }

        return defaultValue;
    }

    public static String getConfigSource() {
        return configSource;
    }

    public static void reload() {
        properties.clear();
        loadConfiguration();
    }
}
