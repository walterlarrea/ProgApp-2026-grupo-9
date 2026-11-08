package com.grupo9.edext.grupo9.miscelanea;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigLoaderTest {

    @Test
    void testGetDefaultValueWhenKeyNotFound() {
        String value = ConfigLoader.get("clave_inexistente_12345", "default_val");
        assertEquals("default_val", value);
    }

    @Test
    void testSystemPropertyOverridesDefault() {
        System.setProperty("test.custom.key", "custom_value");
        try {
            String value = ConfigLoader.get("test.custom.key", "default_val");
            assertEquals("custom_value", value);
        } finally {
            System.clearProperty("test.custom.key");
        }
    }

    @Test
    void testLoadFromCustomFileSpecifiedBySystemProperty() throws IOException {
        File tempFile = File.createTempFile("test_config", ".properties");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("test.file.key=loaded_from_file\n");
            writer.write("ws.host=http://192.168.1.100\n");
            writer.write("ws.port=9999\n");
        }

        System.setProperty("config.path", tempFile.getAbsolutePath());
        try {
            ConfigLoader.reload();
            assertEquals("loaded_from_file", ConfigLoader.get("test.file.key", "default"));
            assertEquals("http://192.168.1.100", ConfigLoader.get("ws.host", "default"));
            assertEquals("9999", ConfigLoader.get("ws.port", "default"));
        } finally {
            System.clearProperty("config.path");
            ConfigLoader.reload();
            tempFile.delete();
        }
    }
}
