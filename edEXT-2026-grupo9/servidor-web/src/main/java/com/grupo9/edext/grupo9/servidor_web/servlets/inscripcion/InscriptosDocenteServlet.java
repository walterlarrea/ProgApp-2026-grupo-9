package com.grupo9.edext.grupo9.servidor_web.servlets.inscripcion;

import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.CursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.DataCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataEdicionCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataInscEdicionDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.EdicionDeCursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.EstadoInscripcion;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.DataInstitutoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.InstitutoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataDocenteDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataUsuarioDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.UsuarioWebService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@WebServlet(name = "InscriptosDocenteServlet", urlPatterns = {"/inscriptosDocente", "/inscripcionesDocente"})
public class InscriptosDocenteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object usuarioEnSesion = session == null ? null : session.getAttribute("usuarioLogueado");
        if (!(usuarioEnSesion instanceof DataDocenteDTO docenteEnSesion)) {
            if (usuarioEnSesion == null) {
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
            return;
        }

        List<DataInscEdicionDTO> inscriptos = new ArrayList<>();
        Map<String, DataInstitutoDTO> institutosPorNombre = new LinkedHashMap<>();
        Map<String, DataCursoDTO> cursosPorNombre = new LinkedHashMap<>();
        Set<String> nombresCursosDelDocente = new HashSet<>();
        List<DataEdicionCursoDTO> edicionesDelDocente = new ArrayList<>();
        List<DataCursoDTO> cursos = Collections.emptyList();
        List<DataEdicionCursoDTO> ediciones = Collections.emptyList();
        DataEdicionCursoDTO edicionSeleccionada = null;
        String nombreInstituto = request.getParameter("instituto");
        String nombreCurso = request.getParameter("curso");
        String nombreEdicion = request.getParameter("edicion");
        boolean manejarInscripciones = "/inscripcionesDocente".equals(request.getServletPath());

        UsuarioWebService usuarioPort = SoapClientFactory.getInstance().getUsuarioPort();
        EdicionDeCursoWebService edicionPort = SoapClientFactory.getInstance().getEdicionCursoPort();
        CursoWebService cursoPort = SoapClientFactory.getInstance().getCursoPort();
        InstitutoWebService instPort = SoapClientFactory.getInstance().getInstitutoPort();

        try {
            if (usuarioPort == null || edicionPort == null || cursoPort == null) {
                throw new IllegalStateException("Servicios web no disponibles.");
            }

            DataUsuarioDTO usuarioActualizado = usuarioPort.consultarUsuario(docenteEnSesion.getNickname());
            if (!(usuarioActualizado instanceof DataDocenteDTO docenteActualizado)) {
                throw new IllegalStateException("No se encontró el docente autenticado.");
            }
            session.setAttribute("usuarioLogueado", docenteActualizado);

            List<String> nombresEdicionesDocente = docenteActualizado.getEdiciones();
            if (nombresEdicionesDocente != null) {
                for (String nombreEdicionDocente : nombresEdicionesDocente) {
                    if (nombreEdicionDocente == null || nombreEdicionDocente.isBlank()) {
                        continue;
                    }

                    DataEdicionCursoDTO edicion = edicionPort.consultarUnaEdicionCurso(nombreEdicionDocente);
                    if (edicion == null) {
                        continue;
                    }
                    edicionesDelDocente.add(edicion);

                    if (edicion.getCursoAsoc() != null && edicion.getCursoAsoc().getNombreCurso() != null) {
                        nombresCursosDelDocente.add(edicion.getCursoAsoc().getNombreCurso());
                    }
                }
            }

            List<DataCursoDTO> todosLosCursos = cursoPort.consultarTodosLosCursos();
            if (todosLosCursos != null) {
                for (DataCursoDTO curso : todosLosCursos) {
                    if (curso != null && nombresCursosDelDocente.contains(curso.getNombreCurso())
                            && curso.getInstituto() != null && curso.getInstituto().getNombreI() != null) {
                        cursosPorNombre.putIfAbsent(curso.getNombreCurso(), curso);
                        DataInstitutoDTO di = new DataInstitutoDTO();
                        di.setNombreI(curso.getInstituto().getNombreI());
                        institutosPorNombre.putIfAbsent(curso.getInstituto().getNombreI(), di);
                    }
                }
            }

            DataInstitutoDTO institutoSeleccionado = institutosPorNombre.get(nombreInstituto);
            if (institutoSeleccionado != null) {
                cursos = new ArrayList<>();
                for (DataCursoDTO curso : cursosPorNombre.values()) {
                    if (curso.getInstituto() != null && Objects.equals(curso.getInstituto().getNombreI(), institutoSeleccionado.getNombreI())) {
                        cursos.add(curso);
                    }
                }
            }

            DataCursoDTO cursoSeleccionado = null;
            for (DataCursoDTO curso : cursos) {
                if (nombreCurso != null && nombreCurso.equals(curso.getNombreCurso())) {
                    cursoSeleccionado = curso;
                    break;
                }
            }

            if (cursoSeleccionado != null) {
                List<DataEdicionCursoDTO> edicionesDelCurso = new ArrayList<>();
                for (DataEdicionCursoDTO edicion : edicionesDelDocente) {
                    if (edicion.getCursoAsoc() != null
                            && Objects.equals(edicion.getCursoAsoc().getNombreCurso(), cursoSeleccionado.getNombreCurso())) {
                        edicionesDelCurso.add(edicion);
                    }
                }
                ediciones = edicionesDelCurso;
            }

            for (DataEdicionCursoDTO edicion : ediciones) {
                if (nombreEdicion != null && nombreEdicion.equals(edicion.getNombreEdi())) {
                    edicionSeleccionada = edicion;
                    break;
                }
            }

            if (edicionSeleccionada != null) {
                List<DataInscEdicionDTO> inscriptosEdicion = edicionPort.buscarInscriptosEdicion(edicionSeleccionada.getNombreEdi());
                if (inscriptosEdicion != null) {
                    for (DataInscEdicionDTO inscripcion : inscriptosEdicion) {
                        if (manejarInscripciones || inscripcion.getEstado() == EstadoInscripcion.ACEPTADA) {
                            inscriptos.add(inscripcion);
                        }
                    }
                }
            }
        } catch (Exception e) {
            inscriptos.clear();
            institutosPorNombre.clear();
            cursos = Collections.emptyList();
            ediciones = Collections.emptyList();
            edicionSeleccionada = null;
            request.setAttribute("errorCarga", "No fue posible cargar los estudiantes inscriptos.");
        }

        request.setAttribute("manejarInscripciones", manejarInscripciones);
        request.setAttribute("institutosDoc", institutosPorNombre.values());
        request.setAttribute("cursosDoc", cursos);
        request.setAttribute("edicionesDoc", ediciones);
        request.setAttribute("edicionSeleccionadaDoc", edicionSeleccionada);
        request.setAttribute("inscriptosDoc", inscriptos);

        request.getRequestDispatcher("/webInscripciones/verInscriptos-docente.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object usuarioEnSesion = session == null ? null : session.getAttribute("usuarioLogueado");
        if (!(usuarioEnSesion instanceof DataDocenteDTO docenteEnSesion)) {
            if (usuarioEnSesion == null) {
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
            return;
        }

        String format = request.getParameter("format");
        String nombreEdicion = request.getParameter("edicion");

        if (nombreEdicion == null || nombreEdicion.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nombre de edición no especificado.");
            return;
        }

        EdicionDeCursoWebService edicionPort = SoapClientFactory.getInstance().getEdicionCursoPort();
        try {
            if (edicionPort == null) {
                throw new IllegalStateException("Servicio de ediciones no disponible.");
            }

            Map<String, String[]> parameterMap = request.getParameterMap();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String paramName = entry.getKey();
                if (paramName.startsWith("estado-")) {
                    String nickname = paramName.substring("estado-".length());
                    String[] values = entry.getValue();
                    if (values != null && values.length > 0 && values[0] != null) {
                        String valorEstado = values[0].trim();
                        String nuevoEstado = null;
                        if ("aceptado".equalsIgnoreCase(valorEstado) || "ACEPTADA".equalsIgnoreCase(valorEstado)) {
                            nuevoEstado = "ACEPTADA";
                        } else if ("rechazado".equalsIgnoreCase(valorEstado) || "RECHAZADA".equalsIgnoreCase(valorEstado)) {
                            nuevoEstado = "RECHAZADA";
                        }

                        if (nuevoEstado != null) {
                            try {
                                edicionPort.cambiarEstadoInscripcion(nombreEdicion, nickname, nuevoEstado);
                            } catch (Exception e) {
                                getServletContext().log("Aviso al cambiar estado de inscripción de " + nickname + ": " + e.getMessage());
                            }
                        }
                    }
                }
            }

            if ("json".equalsIgnoreCase(format)) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"success\":true}");
                response.getWriter().flush();
            } else {
                response.sendRedirect(request.getContextPath() + "/inscripcionesDocente?edicion="
                        + java.net.URLEncoder.encode(nombreEdicion, java.nio.charset.StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno al guardar las inscripciones.");
        }
    }
}
