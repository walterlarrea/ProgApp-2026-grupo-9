package com.grupo9.edext.grupo9.servidor_web.servlets.entidad;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.miscelanea.Fabrica;
import com.grupo9.edext.grupo9.servidor_central.dominio.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProgramasServletTest {

    private ProgramasServlet servlet;
    private IServidorCentral servidor;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    public void setUp() {
        servlet = new ProgramasServlet();
        servidor = Fabrica.getInstance().getIServidorCentral();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getContextPath()).thenReturn("/edext");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    @Test
    public void testDoGetListarProgramas() throws Exception {
        when(request.getServletPath()).thenReturn("/programas");
        when(request.getParameter("accion")).thenReturn(null);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("programas"), any());
        verify(request).getRequestDispatcher("/webPrograma/lista.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoGetAltaDocente() throws Exception {
        when(request.getServletPath()).thenReturn("/programas");
        when(request.getParameter("accion")).thenReturn("alta");
        when(request.getSession(false)).thenReturn(session);

        DataDocente docente = new DataDocente("docenteTest", "Doc", "Test", "doc@test.com", LocalDate.of(1980, 1, 1), null, "INCO");
        when(session.getAttribute("usuarioLogueado")).thenReturn(docente);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("cursos"), any());
        verify(request).getRequestDispatcher("/webPrograma/alta.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoGetAltaNoDocenteForbidden() throws Exception {
        when(request.getServletPath()).thenReturn("/programas");
        when(request.getParameter("accion")).thenReturn("alta");
        when(request.getSession(false)).thenReturn(session);

        DataEstudiante estudiante = new DataEstudiante("estTest", "Est", "Test", "est@test.com", LocalDate.of(2000, 1, 1), null);
        when(session.getAttribute("usuarioLogueado")).thenReturn(estudiante);

        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Solo docentes pueden crear programas.");
    }

    @Test
    public void testDoGetDetalleFaltaNombre() throws Exception {
        when(request.getServletPath()).thenReturn("/programa");
        when(request.getParameter("nombre")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el nombre del programa.");
    }

    @Test
    public void testDoGetDetalleNoExiste() throws Exception {
        when(request.getServletPath()).thenReturn("/programa");
        when(request.getParameter("nombre")).thenReturn("ProgNoExiste_" + System.currentTimeMillis());

        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Programa no encontrado.");
    }

    @Test
    public void testDoGetDetalleExiste() throws Exception {
        String sufijo = Long.toString(System.currentTimeMillis());
        String progNombre = "ProgDetalle_" + sufijo;
        servidor.guardarProgramaDeFormacion(new DataProgramaFormacion(
                progNombre, "Desc", new HashSet<DataCurso>(), LocalDate.now(), LocalDate.now().plusMonths(1), LocalDate.now(), null));

        when(request.getServletPath()).thenReturn("/programa");
        when(request.getParameter("nombre")).thenReturn(progNombre);
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("programa"), any());
        verify(request).setAttribute(eq("inscripto"), eq(false));
        verify(request).setAttribute(eq("puedeGestionar"), eq(false));
        verify(request).getRequestDispatcher("/webPrograma/detalle.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostCrearProgramaDocente() throws Exception {
        String sufijo = Long.toString(System.currentTimeMillis());
        String progNombre = "ProgCreadoWeb_" + sufijo;

        when(request.getServletPath()).thenReturn("/programas");
        when(request.getSession(false)).thenReturn(session);

        DataDocente docente = new DataDocente("docenteWeb", "Doc", "Web", "docw@test.com", LocalDate.of(1980, 1, 1), null, "INCO");
        when(session.getAttribute("usuarioLogueado")).thenReturn(docente);

        when(request.getParameter("nombre")).thenReturn(progNombre);
        when(request.getParameter("descripcion")).thenReturn("Descripcion prog web");
        when(request.getParameter("fechaInicio")).thenReturn("2026-04-01");
        when(request.getParameter("fechaFin")).thenReturn("2026-10-31");
        when(request.getParameterValues("cursos")).thenReturn(null);
        when(request.getPart("imagen")).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).sendRedirect(contains("/programa?nombre="));
        assertTrue(servidor.existeProgramaDeFormacion(progNombre));
    }

    @Test
    public void testDoPostCrearProgramaDuplicado() throws Exception {
        String sufijo = Long.toString(System.currentTimeMillis());
        String progNombre = "ProgDupWeb_" + sufijo;

        servidor.guardarProgramaDeFormacion(new DataProgramaFormacion(
                progNombre, "Desc", new HashSet<>(), LocalDate.now(), LocalDate.now().plusMonths(1), LocalDate.now(), null));

        when(request.getServletPath()).thenReturn("/programas");
        when(request.getSession(false)).thenReturn(session);

        DataDocente docente = new DataDocente("docenteDup", "Doc", "Dup", "docd@test.com", LocalDate.of(1980, 1, 1), null, "INCO");
        when(session.getAttribute("usuarioLogueado")).thenReturn(docente);

        when(request.getParameter("nombre")).thenReturn(progNombre);
        when(request.getParameter("descripcion")).thenReturn("Descripcion prog web");
        when(request.getParameter("fechaInicio")).thenReturn("2026-04-01");
        when(request.getParameter("fechaFin")).thenReturn("2026-10-31");

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Ya existe un programa con ese nombre."));
        verify(request).getRequestDispatcher("/webPrograma/alta.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostInscribirEstudianteYSesionAusente() throws Exception {
        String sufijo = Long.toString(System.currentTimeMillis());
        String progNombre = "ProgInscWeb_" + sufijo;
        String estNick = "estWeb_" + sufijo;

        servidor.guardarProgramaDeFormacion(new DataProgramaFormacion(
                progNombre, "Desc", new HashSet<>(), LocalDate.now(), LocalDate.now().plusMonths(1), LocalDate.now(), null));
        servidor.registrarEstudiante(estNick, "Est", "Web", estNick + "@test.com", LocalDate.of(2001, 1, 1), null);

        when(request.getServletPath()).thenReturn("/programa");
        when(request.getParameter("nombre")).thenReturn(progNombre);
        when(request.getParameter("accion")).thenReturn("inscribir");

        // 1. Caso sin sesión -> redirect /login
        when(request.getSession(false)).thenReturn(null);
        servlet.doPost(request, response);
        verify(response).sendRedirect("/edext/login");

        // 2. Caso con sesión de docente -> 403 forbidden
        when(request.getSession(false)).thenReturn(session);
        DataDocente docente = new DataDocente("docInsc", "D", "I", "docinsc@test.com", LocalDate.of(1980, 1, 1), null, "INCO");
        when(session.getAttribute("usuarioLogueado")).thenReturn(docente);
        servlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Solo estudiantes pueden inscribirse.");

        // 3. Caso estudiante válido -> redirect con mensaje
        DataEstudiante estudiante = (DataEstudiante) servidor.consultarUsuario(estNick);
        when(session.getAttribute("usuarioLogueado")).thenReturn(estudiante);
        servlet.doPost(request, response);
        verify(response).sendRedirect(contains("mensaje=Cambios+guardados."));

        // 4. Caso duplicado -> redirect con error
        servlet.doPost(request, response);
        verify(response).sendRedirect(contains("error="));
    }

    @Test
    public void testDoPostAgregarCursoAPrograma() throws Exception {
        String sufijo = Long.toString(System.currentTimeMillis());
        String instNombre = "InstAgregCurso_" + sufijo;
        DataInstituto inst = servidor.guardarInstituto(new DataInstituto(instNombre));

        String cursoNombre = "CursoParaAsocWeb_" + sufijo;
        servidor.guardarCurso(new DataCurso(
                inst, cursoNombre, "Desc", 4, 20, 2, LocalDate.now(), "http://c.com",
                new HashSet<>(), new HashSet<>(), new HashSet<>(), null));

        String progNombre = "ProgAgregCursoWeb_" + sufijo;
        servidor.guardarProgramaDeFormacion(new DataProgramaFormacion(
                progNombre, "Desc", new HashSet<>(), LocalDate.now(), LocalDate.now().plusMonths(2), LocalDate.now(), null));

        when(request.getServletPath()).thenReturn("/programa");
        when(request.getParameter("nombre")).thenReturn(progNombre);
        when(request.getParameter("accion")).thenReturn("agregarCurso");
        when(request.getParameter("curso")).thenReturn(cursoNombre);
        when(request.getSession(false)).thenReturn(session);

        DataDocente docente = new DataDocente("docAgreg", "D", "A", "docagreg@test.com", LocalDate.of(1980, 1, 1), null, "INCO");
        when(session.getAttribute("usuarioLogueado")).thenReturn(docente);

        servlet.doPost(request, response);

        verify(response).sendRedirect(contains("mensaje=Cambios+guardados."));
    }

    @Test
    public void testDoPostAccionInvalida() throws Exception {
        when(request.getServletPath()).thenReturn("/programa");
        when(request.getParameter("nombre")).thenReturn("ProgValido");
        when(request.getParameter("accion")).thenReturn("accionInexistente");
        when(request.getSession(false)).thenReturn(session);

        DataDocente docente = new DataDocente("docInvalido", "D", "I", "doci@test.com", LocalDate.of(1980, 1, 1), null, "INCO");
        when(session.getAttribute("usuarioLogueado")).thenReturn(docente);

        servlet.doPost(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
    }
}
