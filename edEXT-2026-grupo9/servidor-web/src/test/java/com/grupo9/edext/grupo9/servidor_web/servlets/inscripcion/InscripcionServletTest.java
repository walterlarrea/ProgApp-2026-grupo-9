package com.grupo9.edext.grupo9.servidor_web.servlets.inscripcion;

import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataEdicionCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataInscEdicionDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.EdicionDeCursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.EstadoInscripcion;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataEstudianteDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataUsuarioDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.UsuarioWebService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class InscripcionServletTest {

    private InscripcionServlet servlet;
    private UsuarioWebService usuarioPort;
    private EdicionDeCursoWebService edicionPort;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        servlet = new InscripcionServlet();
        usuarioPort = mock(UsuarioWebService.class);
        edicionPort = mock(EdicionDeCursoWebService.class);

        SoapClientFactory.getInstance().setUsuarioPort(usuarioPort);
        SoapClientFactory.getInstance().setEdicionCursoPort(edicionPort);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getContextPath()).thenReturn("/edext");
        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    @Test
    void muestraTodasLasInscripcionesDelEstudianteLogueado() throws Exception {
        DataUsuarioDTO usuarioEnSesion = estudiante("ana", new ArrayList<>());
        DataEstudianteDTO usuarioActualizado = estudiante("ana", Arrays.asList("edicion-1", "edicion-2"));

        DataInscEdicionDTO inscripcion1 = inscripcion("ana", "edicion-1");
        DataInscEdicionDTO inscripcion2 = inscripcion("ana", "edicion-2");
        DataInscEdicionDTO deOtroUsuario = inscripcion("bruno", "edicion-1");

        when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioEnSesion);
        when(usuarioPort.consultarUsuario("ana")).thenReturn(usuarioActualizado);
        when(edicionPort.buscarInscriptosEdicion("edicion-1"))
                .thenReturn(Arrays.asList(inscripcion1, deOtroUsuario));
        when(edicionPort.buscarInscriptosEdicion("edicion-2")).thenReturn(List.of(inscripcion2));

        servlet.doGet(request, response);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(request).setAttribute(eq("inscripciones"), captor.capture());
        assertEquals(Arrays.asList(inscripcion1, inscripcion2), captor.getValue());
        verify(session).setAttribute("usuarioLogueado", usuarioActualizado);
        verify(request).getRequestDispatcher("/webEdicion/verRes-inscripcion.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void redirigeAlLoginCuandoNoHayUsuarioAutenticado() throws Exception {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/edext/login");
        verifyNoInteractions(usuarioPort);
    }

    private DataEstudianteDTO estudiante(String nickname, List<String> ediciones) {
        DataEstudianteDTO est = new DataEstudianteDTO();
        est.setNickname(nickname);
        est.setNombre("Nombre");
        est.setApellido("Apellido");
        est.setEmail(nickname + "@test.com");
        est.setTipo("Estudiante");
        est.getEdicionesInscriptas().addAll(ediciones);
        return est;
    }

    private DataInscEdicionDTO inscripcion(String nickname, String edicion) {
        DataInscEdicionDTO insc = new DataInscEdicionDTO();
        insc.setEdicion(edicion);
        insc.setFechaInscE("2026-10-30");
        insc.setEstado(EstadoInscripcion.INSCRIPTO);

        com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataEstudianteDTO e =
                new com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataEstudianteDTO();
        e.setNickname(nickname);
        insc.setEstudiante(e);
        return insc;
    }
}
