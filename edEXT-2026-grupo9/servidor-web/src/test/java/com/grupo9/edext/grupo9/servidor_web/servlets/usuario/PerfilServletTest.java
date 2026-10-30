package com.grupo9.edext.grupo9.servidor_web.servlets.usuario;

import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.CursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.InstitutoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataDocenteDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataEstudianteDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataInscProgramaDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataUsuarioDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.UsuarioWebService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PerfilServletTest {

    private PerfilServlet servlet;
    private UsuarioWebService usuarioPort;
    private InstitutoWebService instPort;
    private CursoWebService cursoPort;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    public void setUp() {
        servlet = new PerfilServlet();
        usuarioPort = mock(UsuarioWebService.class);
        instPort = mock(InstitutoWebService.class);
        cursoPort = mock(CursoWebService.class);

        SoapClientFactory.getInstance().setUsuarioPort(usuarioPort);
        SoapClientFactory.getInstance().setInstitutoPort(instPort);
        SoapClientFactory.getInstance().setCursoPort(cursoPort);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getContextPath()).thenReturn("/edext");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    @Test
    public void testDoGetSinSesion() throws Exception {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/edext/login");
    }

    @Test
    public void testDoGetEstudianteConInscripciones() throws Exception {
        String nick = "estPerfil";
        DataEstudianteDTO estudiante = new DataEstudianteDTO();
        estudiante.setNickname(nick);
        estudiante.setTipo("Estudiante");

        DataInscProgramaDTO insc = new DataInscProgramaDTO();
        insc.setProgramaNombre("ProgPerfil");
        estudiante.getInscripcionesProgramas().add(insc);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("usuarioLogueado")).thenReturn(estudiante);
        when(usuarioPort.consultarUsuario(nick)).thenReturn(estudiante);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("usuario"), any());
        verify(request).setAttribute(eq("inscripcionesProgramas"), argThat(arg -> {
            if (arg instanceof Collection<?> col) {
                return !col.isEmpty();
            }
            return false;
        }));
        verify(request).getRequestDispatcher("/webUsuario/perfil.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoGetDocenteInscripcionesVacias() throws Exception {
        String nick = "docPerfil";
        DataDocenteDTO docente = new DataDocenteDTO();
        docente.setNickname(nick);
        docente.setTipo("Docente");

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("usuarioLogueado")).thenReturn(docente);
        when(usuarioPort.consultarUsuario(nick)).thenReturn(docente);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("inscripcionesProgramas"), eq(Collections.emptyList()));
        verify(request).getRequestDispatcher("/webUsuario/perfil.jsp");
        verify(dispatcher).forward(request, response);
    }
}
