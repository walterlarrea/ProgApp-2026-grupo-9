package com.grupo9.edext.grupo9;

import com.grupo9.edext.grupo9.web_service.BusquedaWebService;
import com.grupo9.edext.grupo9.web_service.CursoWebService;
import com.grupo9.edext.grupo9.web_service.EdicionDeCursoWebService;
import com.grupo9.edext.grupo9.web_service.InstitutoWebService;
import com.grupo9.edext.grupo9.web_service.ProgramaDeFormacionWebService;
import com.grupo9.edext.grupo9.web_service.UsuarioWebService;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.xml.ws.Endpoint;

public class EdEXT2026Grupo9 {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        String host = dotenv.get("ED_EXT_WS_HOST", "http://127.0.0.1");
        String port = dotenv.get("ED_EXT_WS_PORT", "9128");

        String baseUrl = host + ":" + port + "/";

        System.out.println("Iniciando servicios SOAP de Servidor Central...");

        Endpoint.publish(baseUrl + "programa_de_formacion", new ProgramaDeFormacionWebService());
        System.out.println("ProgramaDeFormacionWebService publicado en: " + baseUrl + "programa_de_formacion?wsdl");

        Endpoint.publish(baseUrl + "curso", new CursoWebService());
        System.out.println("CursoWebService publicado en: " + baseUrl + "curso?wsdl");

        Endpoint.publish(baseUrl + "edicion_de_curso", new EdicionDeCursoWebService());
        System.out.println("EdicionDeCursoWebService publicado en: " + baseUrl + "edicion_de_curso?wsdl");

        Endpoint.publish(baseUrl + "instituto", new InstitutoWebService());
        System.out.println("InstitutoWebService publicado en: " + baseUrl + "instituto?wsdl");

        Endpoint.publish(baseUrl + "usuario", new UsuarioWebService());
        System.out.println("UsuarioWebService publicado en: " + baseUrl + "usuario?wsdl");

        Endpoint.publish(baseUrl + "busqueda", new BusquedaWebService());
        System.out.println("BusquedaWebService publicado en: " + baseUrl + "busqueda?wsdl");

        System.out.println("Todos los servicios SOAP han sido publicados exitosamente.");
    }
}
