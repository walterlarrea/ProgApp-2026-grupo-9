package com.grupo9.edext.grupo9.servidor_web.servlets.inscripcion;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EstadoInscripcion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEstudiante;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInscEdicion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class InscriptosDocenteServletTest {

    private InscriptosDocenteServlet servlet;
    private IServidorCentral servidor;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        servlet = mock(InscriptosDocenteServlet.class, CALLS_REAL_METHODS);
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
    void muestraSoloInscripcionesAceptadasDeLaEdicionSeleccionada() throws Exception {
        DataDocente docenteSesion = docente("profesor");
        DataDocente docenteActualizado = docente("profesor");
        docenteActualizado.getEdiciones().add("edicion-docente");
        DataEdicionCurso edicionDocente = mock(DataEdicionCurso.class);
        DataCurso curso = curso("Curso docente", "Instituto docente");
        DataInscEdicion aceptada = inscripcion("aceptado", "edicion-docente", EstadoInscripcion.ACEPTADA);
        DataInscEdicion pendiente = inscripcion("pendiente", "edicion-docente", EstadoInscripcion.INSCRIPTO);

        when(session.getAttribute("usuarioLogueado")).thenReturn(docenteSesion);
        when(servidor.consultarUsuario("profesor")).thenReturn(docenteActualizado);
        when(servidor.consultarUnaEdicionCurso("edicion-docente")).thenReturn(edicionDocente);
        when(servidor.consultarTodosLosCursos()).thenReturn(new HashSet<>(Set.of(curso)));
        when(edicionDocente.getCursoAsoc()).thenReturn(curso);
        when(edicionDocente.getNombreEdi()).thenReturn("edicion-docente");
        when(request.getParameter("instituto")).thenReturn("Instituto docente");
        when(request.getParameter("curso")).thenReturn("Curso docente");
        when(request.getParameter("edicion")).thenReturn("edicion-docente");
        when(servidor.buscarInscriptos(edicionDocente)).thenReturn(Set.of(aceptada, pendiente));

        servlet.doGet(request, response);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(request).setAttribute(eq("inscriptosDoc"), captor.capture());
        assertEquals(Set.of(aceptada), captor.getValue());
        ArgumentCaptor<Object> institutosCaptor = ArgumentCaptor.forClass(Object.class);
        verify(request).setAttribute(eq("institutosDoc"), institutosCaptor.capture());
        assertEquals(java.util.List.of(new DataInstituto("Instituto docente")),
                new ArrayList<>((Collection<?>) institutosCaptor.getValue()));
        ArgumentCaptor<Object> cursosCaptor = ArgumentCaptor.forClass(Object.class);
        verify(request).setAttribute(eq("cursosDoc"), cursosCaptor.capture());
        assertEquals(java.util.List.of(curso), cursosCaptor.getValue());
        verify(request).setAttribute("edicionSeleccionadaDoc", edicionDocente);
        verify(servidor).consultarUnaEdicionCurso("edicion-docente");
        verify(servidor, never()).consultarUnaEdicionCurso("otra-edicion");
        verify(servidor, never()).consultarTodosLosInstitutos();
        verify(session).setAttribute("usuarioLogueado", docenteActualizado);
        verify(request).getRequestDispatcher("/webInscripciones/verInscriptos-docente.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void ignoraCursosYEdicionesQueNoPertenecenAlDocente() throws Exception {
        DataDocente docenteSesion = docente("profesor");
        DataDocente docenteActualizado = docente("profesor");
        docenteActualizado.getEdiciones().add("edicion-docente");
        DataEdicionCurso edicionDocente = mock(DataEdicionCurso.class);

        when(session.getAttribute("usuarioLogueado")).thenReturn(docenteSesion);
        when(servidor.consultarUsuario("profesor")).thenReturn(docenteActualizado);
        when(servidor.consultarUnaEdicionCurso("edicion-docente")).thenReturn(edicionDocente);
        DataCurso cursoDocente = curso("Curso docente", "Instituto docente");
        when(servidor.consultarTodosLosCursos()).thenReturn(new HashSet<>(Set.of(
                cursoDocente, curso("Curso ajeno", "Instituto ajeno"))));
        when(edicionDocente.getCursoAsoc()).thenReturn(cursoDocente);
        when(request.getParameter("instituto")).thenReturn("Instituto ajeno");
        when(request.getParameter("curso")).thenReturn("Curso ajeno");
        when(request.getParameter("edicion")).thenReturn("edicion-ajena");

        servlet.doGet(request, response);

        verify(servidor, never()).buscarInscriptos(any());
        verify(request).setAttribute(eq("cursosDoc"), eq(java.util.Collections.emptyList()));
        verify(request).setAttribute(eq("edicionesDoc"), eq(java.util.Collections.emptySet()));
        verify(request).setAttribute("edicionSeleccionadaDoc", null);
    }

    @Test
    void ofreceSoloCursosEInstitutosDeEdicionesDelDocente() throws Exception {
        DataDocente docenteSesion = docente("profesor");
        DataDocente docenteActualizado = docente("profesor");
        docenteActualizado.getEdiciones().addAll(Set.of("edicion-a", "edicion-b"));
        DataCurso cursoA = curso("Curso A", "Instituto A");
        DataCurso cursoB = curso("Curso B", "Instituto B");
        DataCurso cursoSinEdicionDocente = curso("Curso C", "Instituto C");
        DataEdicionCurso edicionA = edicion("edicion-a", cursoA);
        DataEdicionCurso edicionB = edicion("edicion-b", cursoB);

        when(session.getAttribute("usuarioLogueado")).thenReturn(docenteSesion);
        when(servidor.consultarUsuario("profesor")).thenReturn(docenteActualizado);
        when(servidor.consultarUnaEdicionCurso("edicion-a")).thenReturn(edicionA);
        when(servidor.consultarUnaEdicionCurso("edicion-b")).thenReturn(edicionB);
        when(servidor.consultarTodosLosCursos()).thenReturn(
                new HashSet<>(Set.of(cursoA, cursoB, cursoSinEdicionDocente)));
        when(request.getParameter("instituto")).thenReturn("Instituto A");
        when(request.getParameter("curso")).thenReturn("Curso A");
        when(request.getParameter("edicion")).thenReturn("edicion-b");

        servlet.doGet(request, response);

        ArgumentCaptor<Object> institutosCaptor = ArgumentCaptor.forClass(Object.class);
        verify(request).setAttribute(eq("institutosDoc"), institutosCaptor.capture());
        assertEquals(Set.of(new DataInstituto("Instituto A"), new DataInstituto("Instituto B")),
                new HashSet<>((java.util.Collection<?>) institutosCaptor.getValue()));
        ArgumentCaptor<Object> cursosCaptor = ArgumentCaptor.forClass(Object.class);
        verify(request).setAttribute(eq("cursosDoc"), cursosCaptor.capture());
        assertEquals(java.util.List.of(cursoA), cursosCaptor.getValue());
        ArgumentCaptor<Object> edicionesCaptor = ArgumentCaptor.forClass(Object.class);
        verify(request).setAttribute(eq("edicionesDoc"), edicionesCaptor.capture());
        assertEquals(Set.of(edicionA), edicionesCaptor.getValue());
        verify(request).setAttribute("edicionSeleccionadaDoc", null);
    }

    @Test
    void redirigeAlLoginCuandoNoHayUsuarioAutenticado() throws Exception {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/edext/login");
        verifyNoInteractions(servidor);
    }

    @Test
    void rechazaUsuariosQueNoSonDocentes() throws Exception {
        when(session.getAttribute("usuarioLogueado")).thenReturn(mock(DataUsuario.class));

        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN);
        verifyNoInteractions(servidor);
    }

    private DataDocente docente(String nickname) {
        return new DataDocente(nickname, "Nombre", "Apellido", nickname + "@test.com",
                LocalDate.of(1980, 1, 1), null);
    }

    private DataInscEdicion inscripcion(String nickname, String edicion, EstadoInscripcion estado) {
        DataEstudiante estudiante = new DataEstudiante(nickname, "Nombre", "Apellido", nickname + "@test.com",
                LocalDate.of(2000, 1, 1), null);
        return new DataInscEdicion(LocalDate.now(), estudiante, edicion, estado);
    }

    private DataCurso curso(String nombreCurso, String nombreInstituto) {
        return new DataCurso(new DataInstituto(nombreInstituto), nombreCurso, "", 1, 1, 1,
                LocalDate.now(), "", Set.of(), Set.of(), Set.of(), null);
    }

    private DataEdicionCurso edicion(String nombre, DataCurso curso) {
        DataEdicionCurso edicion = mock(DataEdicionCurso.class);
        when(edicion.getNombreEdi()).thenReturn(nombre);
        when(edicion.getCursoAsoc()).thenReturn(curso);
        return edicion;
    }
}
