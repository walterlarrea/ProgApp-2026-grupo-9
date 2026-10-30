package com.grupo9.edext.grupo9;

import com.grupo9.edext.grupo9.web_service.ServidorCentralWebService;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.xml.ws.Endpoint;

public class EdEXT2026Grupo9 {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        String host = dotenv.get("ED_EXT_WS_HOST", "http://127.0.0.1");
        String port = dotenv.get("ED_EXT_WS_PORT", "9128");
        String path = dotenv.get("ED_EXT_WS_PATH", "servidor-central");

        String url = host + ":" + port + "/" + path;

        System.out.println("Iniciando Servidor Central Web Service...");
        Endpoint endpoint = Endpoint.publish(url, new ServidorCentralWebService());
        System.out.println("Web Service publicado exitosamente en: " + url + "?wsdl");
    }
}
