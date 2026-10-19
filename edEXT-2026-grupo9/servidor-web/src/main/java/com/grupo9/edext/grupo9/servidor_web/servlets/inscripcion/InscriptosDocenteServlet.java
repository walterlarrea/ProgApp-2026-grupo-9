package com.grupo9.edext.grupo9.servidor_web.servlets.inscripcion;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.miscelanea.Fabrica;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EstadoInscripcion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInscEdicion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
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
    IServidorCentral servidorCentral = Fabrica.getInstance().getIServidorCentral();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object usuarioEnSesion = session == null ? null : session.getAttribute("usuarioLogueado");
        if (!(usuarioEnSesion instanceof DataDocente docenteEnSesion)) {
            if (usuarioEnSesion == null) {
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
            return;
        }

        Set<DataInscEdicion> inscriptos = new HashSet<>();
        Map<String, DataInstituto> institutosPorNombre = new LinkedHashMap<>();
        Map<String, DataCurso> cursosPorNombre = new LinkedHashMap<>();
        Set<String> nombresCursosDelDocente = new HashSet<>();
        Set<DataEdicionCurso> edicionesDelDocente = new HashSet<>();
        List<DataCurso> cursos = Collections.emptyList();
        Set<DataEdicionCurso> ediciones = Collections.emptySet();
        DataEdicionCurso edicionSeleccionada = null;
        String nombreInstituto = request.getParameter("instituto");
        String nombreCurso = request.getParameter("curso");
        String nombreEdicion = request.getParameter("edicion");
        boolean manejarInscripciones = "/inscripcionesDocente".equals(request.getServletPath());
        
        try {
            if (servidorCentral == null) {
                throw new IllegalStateException("No hay conexión con el Servidor Central.");
            }

            DataUsuario usuarioActualizado = servidorCentral.consultarUsuario(docenteEnSesion.getNickname());
            if (!(usuarioActualizado instanceof DataDocente docenteActualizado)) {
                throw new IllegalStateException("No se encontró el docente autenticado.");
            }
            session.setAttribute("usuarioLogueado", docenteActualizado);

            Set<String> nombresEdicionesDocente = docenteActualizado.getEdiciones();
            if (nombresEdicionesDocente != null) {
                for (String nombreEdicionDocente : nombresEdicionesDocente) {
                    if (nombreEdicionDocente == null || nombreEdicionDocente.isBlank()) {
                        continue;
                    }

                    DataEdicionCurso edicion = servidorCentral.consultarUnaEdicionCurso(nombreEdicionDocente);
                    if (edicion == null) {
                        continue;
                    }
                    edicionesDelDocente.add(edicion);

                    DataCurso curso = edicion.getCursoAsoc();
                    if (curso != null && curso.nombreCurso() != null) {
                        nombresCursosDelDocente.add(curso.nombreCurso());
                    }
                }
            }

            Set<DataCurso> todosLosCursos = servidorCentral.consultarTodosLosCursos();
            if (todosLosCursos == null) {
                throw new IllegalStateException("No se pudieron consultar los cursos.");
            }
            for (DataCurso curso : todosLosCursos) {
                if (curso != null && nombresCursosDelDocente.contains(curso.nombreCurso())
                        && curso.instituto() != null && curso.instituto().nombreI() != null) {
                    cursosPorNombre.putIfAbsent(curso.nombreCurso(), curso);
                    institutosPorNombre.putIfAbsent(curso.instituto().nombreI(), curso.instituto());
                }
            }

            DataInstituto institutoSeleccionado = institutosPorNombre.get(nombreInstituto);
            if (institutoSeleccionado != null) {
                cursos = new ArrayList<>();
                for (DataCurso curso : cursosPorNombre.values()) {
                    if (Objects.equals(curso.instituto().nombreI(), institutoSeleccionado.nombreI())) {
                        cursos.add(curso);
                    }
                }
            }

            DataCurso cursoSeleccionado = null;
            for (DataCurso curso : cursos) {
                if (nombreCurso != null && nombreCurso.equals(curso.nombreCurso())) {
                    cursoSeleccionado = curso;
                    break;
                }
            }

            if (cursoSeleccionado != null) {
                Set<DataEdicionCurso> edicionesDelCurso = new HashSet<>();
                for (DataEdicionCurso edicion : edicionesDelDocente) {
                    DataCurso cursoDeEdicion = edicion.getCursoAsoc();
                    if (cursoDeEdicion != null
                            && cursoDeEdicion.instituto() != null
                            && Objects.equals(cursoDeEdicion.instituto().nombreI(),
                                    cursoSeleccionado.instituto().nombreI())
                            && Objects.equals(cursoDeEdicion.nombreCurso(), cursoSeleccionado.nombreCurso())) {
                        edicionesDelCurso.add(edicion);
                    }
                }
                ediciones = edicionesDelCurso;
            }

            for (DataEdicionCurso edicion : ediciones) {
                if (nombreEdicion != null && nombreEdicion.equals(edicion.getNombreEdi())) {
                    edicionSeleccionada = edicion;
                    break;
                }
            }

            if (edicionSeleccionada != null) {
                Set<DataInscEdicion> inscriptosEdicion = servidorCentral.buscarInscriptos(edicionSeleccionada);
                if (inscriptosEdicion != null) {
                    for (DataInscEdicion inscripcion : inscriptosEdicion) {
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
            ediciones = Collections.emptySet();
            edicionSeleccionada = null;
            getServletContext().log("No se pudieron cargar las inscripciones del docente: "
                    + docenteEnSesion.getNickname(), e);
            request.setAttribute("errorCarga", "No fue posible cargar los estudiantes inscriptos.");
        }
        
        request.setAttribute("manejarInscripciones", manejarInscripciones);
        
        request.setAttribute("institutos", institutosPorNombre.values());
        request.setAttribute("cursos", cursos);
        request.setAttribute("ediciones", ediciones);
        request.setAttribute("edicionSeleccionada", edicionSeleccionada);
        request.setAttribute("inscriptos", inscriptos);
        request.getRequestDispatcher("/webInscripciones/verInscriptos-docente.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object usuarioEnSesion = session == null ? null : session.getAttribute("usuarioLogueado");
        if (!(usuarioEnSesion instanceof DataDocente docenteEnSesion)) {
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

        try {
            if (servidorCentral == null) {
                throw new IllegalStateException("No hay conexión con el Servidor Central.");
            }

            Map<String, String[]> parameterMap = request.getParameterMap();
            boolean huboActualizaciones = false;

            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String paramName = entry.getKey();
                if (paramName.startsWith("estado-")) {
                    String nickname = paramName.substring("estado-".length());
                    String[] values = entry.getValue();
                    if (values != null && values.length > 0 && values[0] != null) {
                        String valorEstado = values[0].trim();
                        EstadoInscripcion nuevoEstado = null;
                        if ("aceptado".equalsIgnoreCase(valorEstado) || "ACEPTADA".equalsIgnoreCase(valorEstado)) {
                            nuevoEstado = EstadoInscripcion.ACEPTADA;
                        } else if ("rechazado".equalsIgnoreCase(valorEstado) || "RECHAZADA".equalsIgnoreCase(valorEstado)) {
                            nuevoEstado = EstadoInscripcion.RECHAZADA;
                        }

                        if (nuevoEstado != null) {
                            try {
                                servidorCentral.cambiarEstadoInscripcion(nombreEdicion, nickname, nuevoEstado);
                                huboActualizaciones = true;
                            } catch (com.grupo9.edext.grupo9.mensajes.ErrorNoExiste e) {
                                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                                return;
                            } catch (com.grupo9.edext.grupo9.mensajes.ErrorEstadoInvalido e) {
                                // Si ya fue procesada o estado inválido, se ignora o se registra
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
                response.sendRedirect(request.getContextPath() + "/inscripcionesDocente?edicion=" + java.net.URLEncoder.encode(nombreEdicion, java.nio.charset.StandardCharsets.UTF_8));
            }
        } catch (IllegalStateException e) {
            getServletContext().log("Error de conexión al servidor central en doPost: ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            getServletContext().log("Error al procesar inscripciones en doPost: ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno al guardar las inscripciones.");
        }
    }
}
