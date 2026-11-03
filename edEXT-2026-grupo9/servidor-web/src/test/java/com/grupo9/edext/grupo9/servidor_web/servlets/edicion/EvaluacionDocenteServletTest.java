package com.grupo9.edext.grupo9.servidor_web.servlets.edicion;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.CursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.DataCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataEdicionCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataEstudianteDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataInscEdicionDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.EdicionDeCursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.EstadoInscripcion;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.InstitutoWebService;
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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class EvaluacionDocenteServletTest {

    private EvaluacionDocenteServlet servlet;
    private UsuarioWebService usuarioPort;
    private InstitutoWebService institutoPort;
    private CursoWebService cursoPort;
    private EdicionDeCursoWebService edicionPort;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        servlet = new EvaluacionDocenteServlet();
        usuarioPort = mock(UsuarioWebService.class);
        institutoPort = mock(InstitutoWebService.class);
        cursoPort = mock(CursoWebService.class);
        edicionPort = mock(EdicionDeCursoWebService.class);

        SoapClientFactory.getInstance().setUsuarioPort(usuarioPort);
        SoapClientFactory.getInstance().setInstitutoPort(institutoPort);
        SoapClientFactory.getInstance().setCursoPort(cursoPort);
        SoapClientFactory.getInstance().setEdicionCursoPort(edicionPort);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getContextPath()).thenReturn("/edext");
        when(request.getSession(false)).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    @Test
    void redirigeALoginSiNoEstaAutenticado() throws Exception {
        when(session.getAttribute("usuarioLogueado")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/edext/login");
    }

    @Test
    void responde403SiUsuarioNoEsDocente() throws Exception {
        DataUsuarioDTO estudiante = new DataUsuarioDTO();
        estudiante.setTipo("Estudiante");
        when(session.getAttribute("usuarioLogueado")).thenReturn(estudiante);

        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    void cargaEdicionVigenteYFiltraEstudiantesAceptados() throws Exception {
        DataDocenteDTO docente = new DataDocenteDTO();
        docente.setNickname("profeJuan");
        docente.getEdiciones().add("ProgApp-2026");
        when(session.getAttribute("usuarioLogueado")).thenReturn(docente);
        when(usuarioPort.consultarUsuario("profeJuan")).thenReturn(docente);

        when(request.getParameter("instituto")).thenReturn("INCO");
        when(request.getParameter("curso")).thenReturn("ProgApp");
        when(request.getParameter("edicion")).thenReturn("ProgApp-2026");

        DataEdicionCursoDTO edicion = new DataEdicionCursoDTO();
        edicion.setNombreEdi("ProgApp-2026");
        edicion.getDocentes().add("profeJuan");
        edicion.setCerrada(false);

        com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataCursoDTO cDto =
                new com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataCursoDTO();
        cDto.setNombreCurso("ProgApp");
        edicion.setCursoAsoc(cDto);

        when(edicionPort.consultarUnaEdicionCurso("ProgApp-2026")).thenReturn(edicion);

        DataCursoDTO curso = curso("ProgApp", "INCO");
        when(cursoPort.consultarTodosLosCursos()).thenReturn(List.of(curso));

        DataInscEdicionDTO inscAceptada = new DataInscEdicionDTO();
        DataEstudianteDTO est1 = new DataEstudianteDTO();
        est1.setNickname("alumno1");
        inscAceptada.setEstudiante(est1);
        inscAceptada.setEstado(EstadoInscripcion.ACEPTADA);
        inscAceptada.setNota(10);

        DataInscEdicionDTO inscPendiente = new DataInscEdicionDTO();
        DataEstudianteDTO est2 = new DataEstudianteDTO();
        est2.setNickname("alumno2");
        inscPendiente.setEstudiante(est2);
        inscPendiente.setEstado(EstadoInscripcion.INSCRIPTO);

        when(edicionPort.buscarInscriptosEdicion("ProgApp-2026")).thenReturn(List.of(inscAceptada, inscPendiente));

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("edicionSeleccionada"), eq(edicion));
        verify(request).setAttribute(eq("docenteParticipa"), eq(true));

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(request).setAttribute(eq("inscriptosAceptados"), captor.capture());
        @SuppressWarnings("unchecked")
        List<DataInscEdicionDTO> aceptados = (List<DataInscEdicionDTO>) captor.getValue();
        assertEquals(1, aceptados.size());
        assertEquals("alumno1", aceptados.get(0).getEstudiante().getNickname());
        assertEquals(10, aceptados.get(0).getNota());

        verify(request).getRequestDispatcher("/webEdicion/evaluacion-docente.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void ignoraCursosYEdicionesQueNoPertenecenAlDocente() throws Exception {
        DataDocenteDTO docenteSesion = new DataDocenteDTO();
        docenteSesion.setNickname("profesor");
        DataDocenteDTO docenteActualizado = new DataDocenteDTO();
        docenteActualizado.setNickname("profesor");
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
        DataCursoDTO cursoAjeno = curso("Curso ajeno", "Instituto ajeno");

        when(cursoPort.consultarTodosLosCursos()).thenReturn(List.of(cursoDocente, cursoAjeno));
        when(request.getParameter("instituto")).thenReturn("Instituto ajeno");
        when(request.getParameter("curso")).thenReturn("Curso ajeno");
        when(request.getParameter("edicion")).thenReturn("edicion-ajena");

        servlet.doGet(request, response);

        verify(edicionPort, never()).buscarInscriptosEdicion(any());
        verify(request).setAttribute(eq("cursos"), eq(java.util.Collections.emptyList()));
        verify(request).setAttribute(eq("ediciones"), eq(java.util.Collections.emptyList()));
        verify(request).setAttribute("edicionSeleccionada", null);
    }

    @Test
    void guardarNotasEnDoPost() throws Exception {
        DataDocenteDTO docente = new DataDocenteDTO();
        docente.setNickname("profeJuan");
        when(session.getAttribute("usuarioLogueado")).thenReturn(docente);

        when(request.getParameter("accion")).thenReturn("guardar");
        when(request.getParameter("nombreEdi")).thenReturn("ProgApp-2026");
        when(request.getParameter("instituto")).thenReturn("INCO");
        when(request.getParameter("curso")).thenReturn("ProgApp");

        Map<String, String[]> params = new HashMap<>();
        params.put("nota-alumno1", new String[]{"9"});
        when(request.getParameterMap()).thenReturn(params);

        servlet.doPost(request, response);

        verify(edicionPort).registrarCalificacion("ProgApp-2026", "alumno1", 9);
        verify(session).setAttribute(eq("mensajeExito"), anyString());
        verify(response).sendRedirect(contains("/evaluacionDocente?instituto=INCO&curso=ProgApp"));
    }

    @Test
    void cerrarEdicionEnDoPost() throws Exception {
        DataDocenteDTO docente = new DataDocenteDTO();
        docente.setNickname("profeJuan");
        when(session.getAttribute("usuarioLogueado")).thenReturn(docente);

        when(request.getParameter("accion")).thenReturn("cerrar");
        when(request.getParameter("nombreEdi")).thenReturn("ProgApp-2026");
        when(request.getParameter("instituto")).thenReturn("INCO");
        when(request.getParameter("curso")).thenReturn("ProgApp");

        Map<String, String[]> params = new HashMap<>();
        params.put("nota-alumno1", new String[]{"11"});
        when(request.getParameterMap()).thenReturn(params);

        servlet.doPost(request, response);

        verify(edicionPort).registrarCalificacion("ProgApp-2026", "alumno1", 11);
        verify(edicionPort).cerrarEdicionCurso("ProgApp-2026");
        verify(session).setAttribute(eq("mensajeExito"), contains("cerrado exitosamente"));
        verify(response).sendRedirect(contains("/evaluacionDocente?instituto=INCO&curso=ProgApp"));
    }

    @Test
    void guardarNotasViaAjaxRespondeJson() throws Exception {
        DataDocenteDTO docente = new DataDocenteDTO();
        docente.setNickname("profeJuan");
        when(session.getAttribute("usuarioLogueado")).thenReturn(docente);

        when(request.getHeader("X-Requested-With")).thenReturn("XMLHttpRequest");
        when(request.getParameter("accion")).thenReturn("guardar");
        when(request.getParameter("nombreEdi")).thenReturn("ProgApp-2026");
        when(request.getParameter("instituto")).thenReturn("INCO");
        when(request.getParameter("curso")).thenReturn("ProgApp");

        Map<String, String[]> params = new HashMap<>();
        params.put("nota-alumno1", new String[]{"10"});
        when(request.getParameterMap()).thenReturn(params);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        servlet.doPost(request, response);

        verify(edicionPort).registrarCalificacion("ProgApp-2026", "alumno1", 10);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json;charset=UTF-8");

        JsonObject json = new Gson().fromJson(sw.toString(), JsonObject.class);
        assertTrue(json.get("success").getAsBoolean());
        assertEquals("guardar", json.get("accion").getAsString());
        assertFalse(json.get("cerrada").getAsBoolean());
        assertTrue(json.get("mensaje").getAsString().contains("correctamente"));
    }

    @Test
    void cerrarEdicionViaAjaxRespondeJsonConCerradaTrue() throws Exception {
        DataDocenteDTO docente = new DataDocenteDTO();
        docente.setNickname("profeJuan");
        when(session.getAttribute("usuarioLogueado")).thenReturn(docente);

        when(request.getHeader("X-Requested-With")).thenReturn("XMLHttpRequest");
        when(request.getParameter("accion")).thenReturn("cerrar");
        when(request.getParameter("nombreEdi")).thenReturn("ProgApp-2026");
        when(request.getParameter("instituto")).thenReturn("INCO");
        when(request.getParameter("curso")).thenReturn("ProgApp");

        Map<String, String[]> params = new HashMap<>();
        params.put("nota-alumno1", new String[]{"12"});
        when(request.getParameterMap()).thenReturn(params);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        servlet.doPost(request, response);

        verify(edicionPort).registrarCalificacion("ProgApp-2026", "alumno1", 12);
        verify(edicionPort).cerrarEdicionCurso("ProgApp-2026");
        verify(response).setStatus(HttpServletResponse.SC_OK);

        JsonObject json = new Gson().fromJson(sw.toString(), JsonObject.class);
        assertTrue(json.get("success").getAsBoolean());
        assertEquals("cerrar", json.get("accion").getAsString());
        assertTrue(json.get("cerrada").getAsBoolean());
        assertTrue(json.get("mensaje").getAsString().contains("cerrado exitosamente"));
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
