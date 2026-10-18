package com.grupo9.edext.grupo9.servidor_web.servlets.inscripcion;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EstadoInscripcion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEstudiante;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInscEdicion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class InscripcionServletTest {

    private InscripcionServlet servlet;
    private IServidorCentral servidor;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        servlet = mock(InscripcionServlet.class, CALLS_REAL_METHODS);
        servidor = mock(IServidorCentral.class);
        servlet.servidorCentral = servidor;
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
        DataUsuario usuarioEnSesion = estudiante("ana", new HashSet<>());
        DataEstudiante usuarioActualizado = estudiante("ana", new HashSet<>(Arrays.asList("edicion-1", "edicion-2")));
        DataEdicionCurso edicion1 = mock(DataEdicionCurso.class);
        DataEdicionCurso edicion2 = mock(DataEdicionCurso.class);
        DataInscEdicion inscripcion1 = inscripcion("ana", "edicion-1");
        DataInscEdicion inscripcion2 = inscripcion("ana", "edicion-2");
        DataInscEdicion deOtroUsuario = inscripcion("bruno", "edicion-1");

        when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioEnSesion);
        when(servidor.consultarUsuario("ana")).thenReturn(usuarioActualizado);
        when(servidor.consultarUnaEdicionCurso("edicion-1")).thenReturn(edicion1);
        when(servidor.consultarUnaEdicionCurso("edicion-2")).thenReturn(edicion2);
        when(servidor.buscarInscriptos(edicion1))
                .thenReturn(new HashSet<>(Arrays.asList(inscripcion1, deOtroUsuario)));
        when(servidor.buscarInscriptos(edicion2)).thenReturn(new HashSet<>(Set.of(inscripcion2)));

        servlet.doGet(request, response);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(request).setAttribute(eq("inscripciones"), captor.capture());
        assertEquals(new HashSet<>(Arrays.asList(inscripcion1, inscripcion2)), captor.getValue());
        verify(session).setAttribute("usuarioLogueado", usuarioActualizado);
        verify(request).getRequestDispatcher("/webEdicion/verRes-inscripcion.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void redirigeAlLoginCuandoNoHayUsuarioAutenticado() throws Exception {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/edext/login");
        verifyNoInteractions(servidor);
    }

    private DataEstudiante estudiante(String nickname, Set<String> ediciones) {
        return new DataEstudiante(nickname, "Nombre", "Apellido", nickname + "@test.com",
                LocalDate.of(2000, 1, 1), null, ediciones);
    }

    private DataInscEdicion inscripcion(String nickname, String edicion) {
        return new DataInscEdicion(LocalDate.now(), estudiante(nickname, new HashSet<>()),
                edicion, EstadoInscripcion.INSCRIPTO);
    }
}
