package com.grupo9.edext.grupo9.servidor_web.servlets.entidad;

import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.CursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.DataCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.ErrorRepetidos_Exception;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.InstitutoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataDocenteDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataEstudianteDTO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MisCursosServletTest {

    private MisCursosServlet servlet;
    private CursoWebService cursoPort;
    private InstitutoWebService institutoPort;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        servlet = new MisCursosServlet();
        cursoPort = mock(CursoWebService.class);
        institutoPort = mock(InstitutoWebService.class);

        SoapClientFactory.getInstance().setCursoPort(cursoPort);
        SoapClientFactory.getInstance().setInstitutoPort(institutoPort);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getContextPath()).thenReturn("/edext");
        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    @Test
    void doGetSinSesionRedirigeALogin() throws Exception {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/edext/login");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void doGetConDocenteRetornaForbidden() throws Exception {
        DataDocenteDTO docente = new DataDocenteDTO();
        docente.setNickname("docente1");
        when(session.getAttribute("usuarioLogueado")).thenReturn(docente);

        servlet.doGet(request, response);

        verify(response).sendError(eq(HttpServletResponse.SC_FORBIDDEN), anyString());
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void doGetConEstudianteMuestraCursos() throws Exception {
        DataEstudianteDTO estudiante = new DataEstudianteDTO();
        estudiante.setNickname("estudiante1");
        when(session.getAttribute("usuarioLogueado")).thenReturn(estudiante);

        DataCursoDTO c1 = new DataCursoDTO();
        c1.setNombreCurso("Curso 1");
        c1.setPuntajePromedio(4.5);
        c1.setCantValoraciones(2);
        c1.setValoracionEstudiante(5);

        when(cursoPort.cursosParticipaEstudiante("estudiante1")).thenReturn(List.of(c1));

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("cursos"), any());
        verify(request).getRequestDispatcher("/webCurso/mis-cursos.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPostAjaxValoraCursosExitosamente() throws Exception {
        DataEstudianteDTO estudiante = new DataEstudianteDTO();
        estudiante.setNickname("estudiante1");
        when(session.getAttribute("usuarioLogueado")).thenReturn(estudiante);
        when(request.getHeader("X-Requested-With")).thenReturn("XMLHttpRequest");

        Map<String, String[]> params = new HashMap<>();
        params.put("valoracion-Curso A", new String[]{"4"});
        when(request.getParameterMap()).thenReturn(params);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        servlet.doPost(request, response);

        verify(cursoPort).valorarCurso("Curso A", "estudiante1", 4);
        verify(response).setStatus(HttpServletResponse.SC_OK);

        pw.flush();
        String json = sw.toString();
        assertTrue(json.contains("\"success\":true"));
        assertTrue(json.contains("Curso A"));
    }

    @Test
    void doPostAjaxRetornaPromedioActualizado() throws Exception {
        DataEstudianteDTO estudiante = new DataEstudianteDTO();
        estudiante.setNickname("estudiante1");
        when(session.getAttribute("usuarioLogueado")).thenReturn(estudiante);
        when(request.getHeader("X-Requested-With")).thenReturn("XMLHttpRequest");

        Map<String, String[]> params = new HashMap<>();
        params.put("valoracion-Curso A", new String[]{"4"});
        when(request.getParameterMap()).thenReturn(params);

        DataCursoDTO cActualizado = new DataCursoDTO();
        cActualizado.setNombreCurso("Curso A");
        cActualizado.setPuntajePromedio(4.0);
        cActualizado.setCantValoraciones(1);
        when(cursoPort.cursosParticipaEstudiante("estudiante1")).thenReturn(List.of(cActualizado));

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        servlet.doPost(request, response);

        verify(cursoPort).valorarCurso("Curso A", "estudiante1", 4);
        verify(response).setStatus(HttpServletResponse.SC_OK);

        pw.flush();
        String json = sw.toString();
        assertTrue(json.contains("\"success\":true"));
        assertTrue(json.contains("promedios"));
        assertTrue(json.contains("4.0"));
    }

    @Test
    void doPostAjaxManejaErrorYaValorado() throws Exception {
        DataEstudianteDTO estudiante = new DataEstudianteDTO();
        estudiante.setNickname("estudiante1");
        when(session.getAttribute("usuarioLogueado")).thenReturn(estudiante);
        when(request.getHeader("X-Requested-With")).thenReturn("XMLHttpRequest");

        Map<String, String[]> params = new HashMap<>();
        params.put("valoracion-Curso A", new String[]{"5"});
        when(request.getParameterMap()).thenReturn(params);

        doThrow(new ErrorRepetidos_Exception("Ya valorado", null))
                .when(cursoPort).valorarCurso("Curso A", "estudiante1", 5);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        pw.flush();
        String json = sw.toString();
        assertTrue(json.contains("\"success\":false"));
        assertTrue(json.contains("No es posible modificar"));
    }

    @Test
    void doPostNoAjaxRedirigeConMensaje() throws Exception {
        DataEstudianteDTO estudiante = new DataEstudianteDTO();
        estudiante.setNickname("estudiante1");
        when(session.getAttribute("usuarioLogueado")).thenReturn(estudiante);
        when(request.getHeader("X-Requested-With")).thenReturn(null);
        when(request.getHeader("Accept")).thenReturn("text/html");

        Map<String, String[]> params = new HashMap<>();
        params.put("valoracion-Curso A", new String[]{"3"});
        when(request.getParameterMap()).thenReturn(params);

        servlet.doPost(request, response);

        verify(cursoPort).valorarCurso("Curso A", "estudiante1", 3);
        verify(session).setAttribute(eq("mensajeExito"), anyString());
        verify(response).sendRedirect("/edext/misCursos");
    }
}
