package com.grupo9.edext.grupo9.servidor_web.servlets.inscripcion;

import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.CursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.DataCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataEdicionCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataInscEdicionDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.EdicionDeCursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.EstadoInscripcion;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.DataInstitutoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataDocenteDTO;
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
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class InscriptosDocenteServletTest {

    private InscriptosDocenteServlet servlet;
    private UsuarioWebService usuarioPort;
    private EdicionDeCursoWebService edicionPort;
    private CursoWebService cursoPort;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        servlet = new InscriptosDocenteServlet();
        usuarioPort = mock(UsuarioWebService.class);
        edicionPort = mock(EdicionDeCursoWebService.class);
        cursoPort = mock(CursoWebService.class);

        SoapClientFactory.getInstance().setUsuarioPort(usuarioPort);
        SoapClientFactory.getInstance().setEdicionCursoPort(edicionPort);
        SoapClientFactory.getInstance().setCursoPort(cursoPort);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getContextPath()).thenReturn("/edext");
        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    @Test
    void muestraSoloInscripcionesAceptadasDeLaEdicionSeleccionada() throws Exception {
        DataDocenteDTO docenteSesion = docente("profesor");
        DataDocenteDTO docenteActualizado = docente("profesor");
        docenteActualizado.getEdiciones().add("edicion-docente");

        DataEdicionCursoDTO edicionDocente = new DataEdicionCursoDTO();
        edicionDocente.setNombreEdi("edicion-docente");

        com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataCursoDTO cEdi =
                new com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataCursoDTO();
        cEdi.setNombreCurso("Curso docente");
        edicionDocente.setCursoAsoc(cEdi);

        DataCursoDTO curso = curso("Curso docente", "Instituto docente");
        DataInscEdicionDTO aceptada = inscripcion("aceptado", "edicion-docente", EstadoInscripcion.ACEPTADA);
        DataInscEdicionDTO pendiente = inscripcion("pendiente", "edicion-docente", EstadoInscripcion.INSCRIPTO);

        when(session.getAttribute("usuarioLogueado")).thenReturn(docenteSesion);
        when(usuarioPort.consultarUsuario("profesor")).thenReturn(docenteActualizado);
        when(edicionPort.consultarUnaEdicionCurso("edicion-docente")).thenReturn(edicionDocente);
        when(cursoPort.consultarTodosLosCursos()).thenReturn(List.of(curso));
        when(request.getParameter("instituto")).thenReturn("Instituto docente");
        when(request.getParameter("curso")).thenReturn("Curso docente");
        when(request.getParameter("edicion")).thenReturn("edicion-docente");
        when(edicionPort.buscarInscriptosEdicion("edicion-docente")).thenReturn(List.of(aceptada, pendiente));

        servlet.doGet(request, response);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(request).setAttribute(eq("inscriptosDoc"), captor.capture());
        assertEquals(List.of(aceptada), captor.getValue());

        verify(session).setAttribute("usuarioLogueado", docenteActualizado);
        verify(request).getRequestDispatcher("/webInscripciones/verInscriptos-docente.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void ignoraCursosYEdicionesQueNoPertenecenAlDocente() throws Exception {
        DataDocenteDTO docenteSesion = docente("profesor");
        DataDocenteDTO docenteActualizado = docente("profesor");
        docenteActualizado.getEdiciones().add("edicion-docente");

        DataEdicionCursoDTO edicionDocente = new DataEdicionCursoDTO();
        edicionDocente.setNombreEdi("edicion-docente");
        com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataCursoDTO cEdi =
                new com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataCursoDTO();
        cEdi.setNombreCurso("Curso docente");
        edicionDocente.setCursoAsoc(cEdi);

        when(session.getAttribute("usuarioLogueado")).thenReturn(docenteSesion);
        when(usuarioPort.consultarUsuario("profesor")).thenReturn(docenteActualizado);
        when(edicionPort.consultarUnaEdicionCurso("edicion-docente")).thenReturn(edicionDocente);

        DataCursoDTO cursoDocente = curso("Curso docente", "Instituto docente");
        when(cursoPort.consultarTodosLosCursos()).thenReturn(List.of(
                cursoDocente, curso("Curso ajeno", "Instituto ajeno")));
        when(request.getParameter("instituto")).thenReturn("Instituto ajeno");
        when(request.getParameter("curso")).thenReturn("Curso ajeno");
        when(request.getParameter("edicion")).thenReturn("edicion-ajena");

        servlet.doGet(request, response);

        verify(edicionPort, never()).buscarInscriptosEdicion(any());
        verify(request).setAttribute(eq("cursosDoc"), eq(java.util.Collections.emptyList()));
        verify(request).setAttribute(eq("edicionesDoc"), eq(java.util.Collections.emptyList()));
        verify(request).setAttribute("edicionSeleccionadaDoc", null);
    }

    @Test
    void redirigeAlLoginCuandoNoHayUsuarioAutenticado() throws Exception {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/edext/login");
        verifyNoInteractions(usuarioPort);
    }

    @Test
    void rechazaUsuariosQueNoSonDocentes() throws Exception {
        when(session.getAttribute("usuarioLogueado")).thenReturn(new DataUsuarioDTO());

        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN);
        verifyNoInteractions(usuarioPort);
    }

    private DataDocenteDTO docente(String nickname) {
        DataDocenteDTO doc = new DataDocenteDTO();
        doc.setNickname(nickname);
        doc.setNombre("Nombre");
        doc.setApellido("Apellido");
        doc.setEmail(nickname + "@test.com");
        doc.setTipo("Docente");
        return doc;
    }

    private DataInscEdicionDTO inscripcion(String nickname, String edicion, EstadoInscripcion estado) {
        DataInscEdicionDTO insc = new DataInscEdicionDTO();
        insc.setEdicion(edicion);
        insc.setEstado(estado);
        com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataEstudianteDTO est =
                new com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataEstudianteDTO();
        est.setNickname(nickname);
        insc.setEstudiante(est);
        return insc;
    }

    private DataCursoDTO curso(String nombreCurso, String nombreInstituto) {
        DataCursoDTO c = new DataCursoDTO();
        c.setNombreCurso(nombreCurso);
        com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.DataInstitutoDTO inst =
                new com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.DataInstitutoDTO();
        inst.setNombreI(nombreInstituto);
        c.setInstituto(inst);
        return c;
    }
}
