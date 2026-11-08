package com.grupo9.edext.grupo9;

import com.grupo9.edext.grupo9.miscelanea.ConfigLoader;
import com.grupo9.edext.grupo9.web_service.BusquedaWebService;
import com.grupo9.edext.grupo9.web_service.CursoWebService;
import com.grupo9.edext.grupo9.web_service.EdicionDeCursoWebService;
import com.grupo9.edext.grupo9.web_service.InstitutoWebService;
import com.grupo9.edext.grupo9.web_service.ProgramaDeFormacionWebService;
import com.grupo9.edext.grupo9.web_service.UsuarioWebService;

import jakarta.xml.ws.Endpoint;

public class EdEXT2026Grupo9 {

    public static void main(String[] args) {
        String host = ConfigLoader.get("ws.host", ConfigLoader.get("ED_EXT_WS_HOST", "http://0.0.0.0")).trim();
        if (!host.startsWith("http://") && !host.startsWith("https://")) {
            host = "http://" + host;
        }
        if (host.endsWith("/")) {
            host = host.substring(0, host.length() - 1);
        }

        String port = ConfigLoader.get("ws.port", ConfigLoader.get("ED_EXT_WS_PORT", "9128")).trim();

        String baseUrl = host + ":" + port + "/";

        System.out.println("=================================================");
        System.out.println("  Iniciando servicios SOAP de Servidor Central  ");
        System.out.println("=================================================");
        System.out.println("Origen de configuracion: " + ConfigLoader.getConfigSource());
        System.out.println("Host de escucha : " + host);
        System.out.println("Puerto          : " + port);
        System.out.println("URL base        : " + baseUrl);
        System.out.println("-------------------------------------------------");

        Endpoint.publish(baseUrl + "programa_de_formacion", new ProgramaDeFormacionWebService());
        System.out.println("ProgramaDeFormacionWebService : " + baseUrl + "programa_de_formacion?wsdl");

        Endpoint.publish(baseUrl + "curso", new CursoWebService());
        System.out.println("CursoWebService               : " + baseUrl + "curso?wsdl");

        Endpoint.publish(baseUrl + "edicion_de_curso", new EdicionDeCursoWebService());
        System.out.println("EdicionDeCursoWebService       : " + baseUrl + "edicion_de_curso?wsdl");

        Endpoint.publish(baseUrl + "instituto", new InstitutoWebService());
        System.out.println("InstitutoWebService            : " + baseUrl + "instituto?wsdl");

        Endpoint.publish(baseUrl + "usuario", new UsuarioWebService());
        System.out.println("UsuarioWebService              : " + baseUrl + "usuario?wsdl");

        Endpoint.publish(baseUrl + "busqueda", new BusquedaWebService());
        System.out.println("BusquedaWebService             : " + baseUrl + "busqueda?wsdl");

        System.out.println("-------------------------------------------------");
        System.out.println("Todos los servicios SOAP han sido publicados exitosamente.");
        if (host.contains("0.0.0.0")) {
            System.out.println("Escuchando en todas las interfaces de red.");
            System.out.println("Acceso local: http://localhost:" + port + "/");
            System.out.println("Acceso desde otras maquinas: http://<IP_DE_ESTA_MAQUINA>:" + port + "/");
        }
        System.out.println("=================================================");
    }
}
