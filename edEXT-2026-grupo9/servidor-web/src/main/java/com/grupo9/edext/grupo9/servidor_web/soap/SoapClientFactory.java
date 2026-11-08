package com.grupo9.edext.grupo9.servidor_web.soap;

import com.grupo9.edext.grupo9.servidor_web.config.ConfigLoader;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.busqueda.BusquedaWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.busqueda.BusquedaWebService_Service;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.CursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.CursoWebService_Service;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.EdicionDeCursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.EdicionDeCursoWebService_Service;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.InstitutoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.InstitutoWebService_Service;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.programa_de_formacion.ProgramaDeFormacionWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.programa_de_formacion.ProgramaDeFormacionWebService_Service;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.UsuarioWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.UsuarioWebService_Service;

import jakarta.xml.ws.BindingProvider;

public class SoapClientFactory {

    private static SoapClientFactory instance;

    private ProgramaDeFormacionWebService programaPort;
    private CursoWebService cursoPort;
    private EdicionDeCursoWebService edicionCursoPort;
    private InstitutoWebService institutoPort;
    private UsuarioWebService usuarioPort;
    private BusquedaWebService busquedaPort;

    private SoapClientFactory() {
    }

    public static synchronized SoapClientFactory getInstance() {
        if (instance == null) {
            instance = new SoapClientFactory();
        }
        return instance;
    }

    public static String getBaseEndpointUrl() {
        String fullUrl = ConfigLoader.get("ws.central.url", ConfigLoader.get("servidor.central.url", null));
        if (fullUrl != null && !fullUrl.isBlank()) {
            return fullUrl.endsWith("/") ? fullUrl : fullUrl + "/";
        }
        String host = ConfigLoader.get("ws.central.host", ConfigLoader.get("servidor.central.host", "127.0.0.1")).trim();
        if (!host.startsWith("http://") && !host.startsWith("https://")) {
            host = "http://" + host;
        }
        if (host.endsWith("/")) {
            host = host.substring(0, host.length() - 1);
        }
        String port = ConfigLoader.get("ws.central.port", ConfigLoader.get("servidor.central.port", "9128")).trim();
        return host + ":" + port + "/";
    }

    private <T> T configurePort(T port, String servicePath) {
        if (port instanceof BindingProvider bp) {
            String endpointUrl = getBaseEndpointUrl() + servicePath;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);
        }
        return port;
    }

    public synchronized ProgramaDeFormacionWebService getProgramaPort() {
        if (programaPort == null) {
            try {
                ProgramaDeFormacionWebService_Service service = new ProgramaDeFormacionWebService_Service();
                programaPort = configurePort(service.getProgramaDeFormacionWebServicePort(), "programa_de_formacion");
            } catch (Exception e) {
                System.err.println("[SoapClientFactory] Error al inicializar ProgramaDeFormacionWebService: " + e.getMessage());
                return null;
            }
        }
        return programaPort;
    }

    public synchronized CursoWebService getCursoPort() {
        if (cursoPort == null) {
            try {
                CursoWebService_Service service = new CursoWebService_Service();
                cursoPort = configurePort(service.getCursoWebServicePort(), "curso");
            } catch (Exception e) {
                System.err.println("[SoapClientFactory] Error al inicializar CursoWebService: " + e.getMessage());
                return null;
            }
        }
        return cursoPort;
    }

    public synchronized EdicionDeCursoWebService getEdicionCursoPort() {
        if (edicionCursoPort == null) {
            try {
                EdicionDeCursoWebService_Service service = new EdicionDeCursoWebService_Service();
                edicionCursoPort = configurePort(service.getEdicionDeCursoWebServicePort(), "edicion_de_curso");
            } catch (Exception e) {
                System.err.println("[SoapClientFactory] Error al inicializar EdicionDeCursoWebService: " + e.getMessage());
                return null;
            }
        }
        return edicionCursoPort;
    }

    public synchronized InstitutoWebService getInstitutoPort() {
        if (institutoPort == null) {
            try {
                InstitutoWebService_Service service = new InstitutoWebService_Service();
                institutoPort = configurePort(service.getInstitutoWebServicePort(), "instituto");
            } catch (Exception e) {
                System.err.println("[SoapClientFactory] Error al inicializar InstitutoWebService: " + e.getMessage());
                return null;
            }
        }
        return institutoPort;
    }

    public synchronized UsuarioWebService getUsuarioPort() {
        if (usuarioPort == null) {
            try {
                UsuarioWebService_Service service = new UsuarioWebService_Service();
                usuarioPort = configurePort(service.getUsuarioWebServicePort(), "usuario");
            } catch (Exception e) {
                System.err.println("[SoapClientFactory] Error al inicializar UsuarioWebService: " + e.getMessage());
                return null;
            }
        }
        return usuarioPort;
    }

    public synchronized BusquedaWebService getBusquedaPort() {
        if (busquedaPort == null) {
            try {
                BusquedaWebService_Service service = new BusquedaWebService_Service();
                busquedaPort = configurePort(service.getBusquedaWebServicePort(), "busqueda");
            } catch (Exception e) {
                System.err.println("[SoapClientFactory] Error al inicializar BusquedaWebService: " + e.getMessage());
                return null;
            }
        }
        return busquedaPort;
    }

    // Setters para testing o mocks
    public synchronized void setProgramaPort(ProgramaDeFormacionWebService port) {
        this.programaPort = port;
    }

    public synchronized void setCursoPort(CursoWebService port) {
        this.cursoPort = port;
    }

    public synchronized void setEdicionCursoPort(EdicionDeCursoWebService port) {
        this.edicionCursoPort = port;
    }

    public synchronized void setInstitutoPort(InstitutoWebService port) {
        this.institutoPort = port;
    }

    public synchronized void setUsuarioPort(UsuarioWebService port) {
        this.usuarioPort = port;
    }

    public synchronized void setBusquedaPort(BusquedaWebService port) {
        this.busquedaPort = port;
    }
}
