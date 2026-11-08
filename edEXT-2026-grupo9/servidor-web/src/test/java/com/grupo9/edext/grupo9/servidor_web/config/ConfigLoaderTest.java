package com.grupo9.edext.grupo9.servidor_web.config;

import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigLoaderTest {

    @Test
    void testGetDefaultValueWhenKeyNotFound() {
        String value = ConfigLoader.get("inexistente_abc", "default_valor");
        assertEquals("default_valor", value);
    }

    @Test
    void testBaseEndpointUrlDefault() {
        String base = SoapClientFactory.getBaseEndpointUrl();
        assertNotNull(base);
        assertTrue(base.startsWith("http://"));
        assertTrue(base.endsWith("/"));
    }

    @Test
    void testCustomHostAndPortConfigured() throws IOException {
        File tempFile = File.createTempFile("web_config", ".properties");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("ws.central.host=192.168.1.88\n");
            writer.write("ws.central.port=9130\n");
        }

        System.setProperty("config.path", tempFile.getAbsolutePath());
        try {
            ConfigLoader.reload();
            assertEquals("192.168.1.88", ConfigLoader.get("ws.central.host", "default"));
            assertEquals("9130", ConfigLoader.get("ws.central.port", "default"));
            assertEquals("http://192.168.1.88:9130/", SoapClientFactory.getBaseEndpointUrl());
        } finally {
            System.clearProperty("config.path");
            ConfigLoader.reload();
            tempFile.delete();
        }
    }

    @Test
    void testFullUrlConfigured() throws IOException {
        File tempFile = File.createTempFile("web_config_full", ".properties");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("ws.central.url=http://servidor-central.instituto.edu:9128/\n");
        }

        System.setProperty("config.path", tempFile.getAbsolutePath());
        try {
            ConfigLoader.reload();
            assertEquals("http://servidor-central.instituto.edu:9128/", SoapClientFactory.getBaseEndpointUrl());
        } finally {
            System.clearProperty("config.path");
            ConfigLoader.reload();
            tempFile.delete();
        }
    }
}
