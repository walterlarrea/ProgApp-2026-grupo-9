package com.grupo9.edext.grupo9.servidor_web.servlets.entidad;

import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.CursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.programa_de_formacion.DataProgramaFormacionDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.programa_de_formacion.ErrorRepetidos_Exception;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.programa_de_formacion.ProgramaDeFormacionWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataDocenteDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataEstudianteDTO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ProgramasServletTest {

    private ProgramasServlet servlet;
    private ProgramaDeFormacionWebService progPort;
    private CursoWebService cursoPort;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    public void setUp() {
        servlet = new ProgramasServlet();
        progPort = mock(ProgramaDeFormacionWebService.class);
        cursoPort = mock(CursoWebService.class);

        SoapClientFactory.getInstance().setProgramaPort(progPort);
        SoapClientFactory.getInstance().setCursoPort(cursoPort);

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

        DataDocenteDTO docente = new DataDocenteDTO();
        docente.setNickname("docenteTest");
        docente.setTipo("Docente");
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

        DataEstudianteDTO estudiante = new DataEstudianteDTO();
        estudiante.setNickname("estTest");
        estudiante.setTipo("Estudiante");
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
        when(request.getParameter("nombre")).thenReturn("ProgNoExiste");
        when(progPort.traerPorNombreId("ProgNoExiste")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Programa no encontrado.");
    }

    @Test
    public void testDoGetDetalleExiste() throws Exception {
        String progNombre = "ProgDetalle";
        DataProgramaFormacionDTO dto = new DataProgramaFormacionDTO();
        dto.setNombre(progNombre);

        when(progPort.traerPorNombreId(progNombre)).thenReturn(dto);
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
        String progNombre = "ProgCreadoWeb";

        when(request.getServletPath()).thenReturn("/programas");
        when(request.getSession(false)).thenReturn(session);

        DataDocenteDTO docente = new DataDocenteDTO();
        docente.setNickname("docenteWeb");
        docente.setTipo("Docente");
        when(session.getAttribute("usuarioLogueado")).thenReturn(docente);

        when(request.getParameter("nombre")).thenReturn(progNombre);
        when(request.getParameter("descripcion")).thenReturn("Descripcion prog web");
        when(request.getParameter("fechaInicio")).thenReturn("2026-04-01");
        when(request.getParameter("fechaFin")).thenReturn("2026-10-31");
        when(request.getParameterValues("cursos")).thenReturn(null);
        when(request.getPart("imagen")).thenReturn(null);
        when(progPort.existeProgramaDeFormacion(progNombre)).thenReturn(false);

        DataProgramaFormacionDTO guardado = new DataProgramaFormacionDTO();
        guardado.setNombre(progNombre);
        when(progPort.guardarProgramaDeFormacion(any(), any(), any(), any(), any())).thenReturn(guardado);

        servlet.doPost(request, response);

        verify(response).sendRedirect(contains("/programa?nombre="));
    }

    @Test
    public void testDoPostCrearProgramaDuplicado() throws Exception {
        String progNombre = "ProgDupWeb";

        when(request.getServletPath()).thenReturn("/programas");
        when(request.getSession(false)).thenReturn(session);

        DataDocenteDTO docente = new DataDocenteDTO();
        docente.setNickname("docenteDup");
        docente.setTipo("Docente");
        when(session.getAttribute("usuarioLogueado")).thenReturn(docente);

        when(request.getParameter("nombre")).thenReturn(progNombre);
        when(request.getParameter("descripcion")).thenReturn("Descripcion prog web");
        when(request.getParameter("fechaInicio")).thenReturn("2026-04-01");
        when(request.getParameter("fechaFin")).thenReturn("2026-10-31");
        when(progPort.existeProgramaDeFormacion(progNombre)).thenReturn(true);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Ya existe un programa con ese nombre."));
        verify(request).getRequestDispatcher("/webPrograma/alta.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostInscribirEstudianteYSesionAusente() throws Exception {
        String progNombre = "ProgInscWeb";

        when(request.getServletPath()).thenReturn("/programa");
        when(request.getParameter("nombre")).thenReturn(progNombre);
        when(request.getParameter("accion")).thenReturn("inscribir");

        // 1. Caso sin sesión -> redirect /login
        when(request.getSession(false)).thenReturn(null);
        servlet.doPost(request, response);
        verify(response).sendRedirect("/edext/login");

        // 2. Caso con sesión de docente -> 403 forbidden
        when(request.getSession(false)).thenReturn(session);
        DataDocenteDTO docente = new DataDocenteDTO();
        docente.setNickname("docInsc");
        docente.setTipo("Docente");
        when(session.getAttribute("usuarioLogueado")).thenReturn(docente);
        servlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Solo estudiantes pueden inscribirse.");

        // 3. Caso estudiante válido -> redirect con mensaje
        DataEstudianteDTO estudiante = new DataEstudianteDTO();
        estudiante.setNickname("estWeb");
        estudiante.setTipo("Estudiante");
        when(session.getAttribute("usuarioLogueado")).thenReturn(estudiante);
        servlet.doPost(request, response);
        verify(response).sendRedirect(contains("mensaje=Cambios+guardados."));

        // 4. Caso duplicado -> redirect con error
        doThrow(new ErrorRepetidos_Exception("Ya inscripto", null)).when(progPort)
                .inscribirEstudianteEnPrograma(any(), eq("estWeb"), eq(progNombre));
        servlet.doPost(request, response);
        verify(response).sendRedirect(contains("error="));
    }

    @Test
    public void testDoPostAgregarCursoAPrograma() throws Exception {
        String cursoNombre = "CursoParaAsocWeb";
        String progNombre = "ProgAgregCursoWeb";

        when(request.getServletPath()).thenReturn("/programa");
        when(request.getParameter("nombre")).thenReturn(progNombre);
        when(request.getParameter("accion")).thenReturn("agregarCurso");
        when(request.getParameter("curso")).thenReturn(cursoNombre);
        when(request.getSession(false)).thenReturn(session);

        DataDocenteDTO docente = new DataDocenteDTO();
        docente.setNickname("docAgreg");
        docente.setTipo("Docente");
        when(session.getAttribute("usuarioLogueado")).thenReturn(docente);
        when(progPort.agregarCursoAProgramaDeFormacion(progNombre, cursoNombre)).thenReturn(true);

        servlet.doPost(request, response);

        verify(response).sendRedirect(contains("mensaje=Cambios+guardados."));
    }

    @Test
    public void testDoPostAccionInvalida() throws Exception {
        when(request.getServletPath()).thenReturn("/programa");
        when(request.getParameter("nombre")).thenReturn("ProgValido");
        when(request.getParameter("accion")).thenReturn("accionInexistente");
        when(request.getSession(false)).thenReturn(session);

        DataDocenteDTO docente = new DataDocenteDTO();
        docente.setNickname("docInvalido");
        docente.setTipo("Docente");
        when(session.getAttribute("usuarioLogueado")).thenReturn(docente);

        servlet.doPost(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
    }
}
