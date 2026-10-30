package com.grupo9.edext.grupo9.servidor_web.soap;

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

    public synchronized ProgramaDeFormacionWebService getProgramaPort() {
        if (programaPort == null) {
            try {
                ProgramaDeFormacionWebService_Service service = new ProgramaDeFormacionWebService_Service();
                programaPort = service.getProgramaDeFormacionWebServicePort();
            } catch (Exception e) {
                return null;
            }
        }
        return programaPort;
    }

    public synchronized CursoWebService getCursoPort() {
        if (cursoPort == null) {
            try {
                CursoWebService_Service service = new CursoWebService_Service();
                cursoPort = service.getCursoWebServicePort();
            } catch (Exception e) {
                return null;
            }
        }
        return cursoPort;
    }

    public synchronized EdicionDeCursoWebService getEdicionCursoPort() {
        if (edicionCursoPort == null) {
            try {
                EdicionDeCursoWebService_Service service = new EdicionDeCursoWebService_Service();
                edicionCursoPort = service.getEdicionDeCursoWebServicePort();
            } catch (Exception e) {
                return null;
            }
        }
        return edicionCursoPort;
    }

    public synchronized InstitutoWebService getInstitutoPort() {
        if (institutoPort == null) {
            try {
                InstitutoWebService_Service service = new InstitutoWebService_Service();
                institutoPort = service.getInstitutoWebServicePort();
            } catch (Exception e) {
                return null;
            }
        }
        return institutoPort;
    }

    public synchronized UsuarioWebService getUsuarioPort() {
        if (usuarioPort == null) {
            try {
                UsuarioWebService_Service service = new UsuarioWebService_Service();
                usuarioPort = service.getUsuarioWebServicePort();
            } catch (Exception e) {
                return null;
            }
        }
        return usuarioPort;
    }

    public synchronized BusquedaWebService getBusquedaPort() {
        if (busquedaPort == null) {
            try {
                BusquedaWebService_Service service = new BusquedaWebService_Service();
                busquedaPort = service.getBusquedaWebServicePort();
            } catch (Exception e) {
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
