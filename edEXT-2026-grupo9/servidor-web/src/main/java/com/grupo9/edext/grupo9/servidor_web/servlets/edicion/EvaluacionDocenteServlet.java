package com.grupo9.edext.grupo9.servidor_web.servlets.edicion;

import com.google.gson.Gson;
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

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@WebServlet(name = "EvaluacionDocenteServlet", urlPatterns = {"/evaluacionDocente", "/evaluacionesDocente", "/verEvaluacionesDocente"})
public class EvaluacionDocenteServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Caso de uso "Ver resultados de evaluaciones": mismo flujo, pero solo ediciones cerradas y vista de solo lectura
        boolean modoVer = "/verEvaluacionesDocente".equals(request.getServletPath());

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

        if (session != null) {
            if (session.getAttribute("mensajeExito") != null) {
                request.setAttribute("mensajeExito", session.getAttribute("mensajeExito"));
                session.removeAttribute("mensajeExito");
            }
            if (session.getAttribute("mensajeError") != null) {
                request.setAttribute("mensajeError", session.getAttribute("mensajeError"));
                session.removeAttribute("mensajeError");
            }
        }

        UsuarioWebService usuarioPort = SoapClientFactory.getInstance().getUsuarioPort();
        EdicionDeCursoWebService edicionPort = SoapClientFactory.getInstance().getEdicionCursoPort();
        CursoWebService cursoPort = SoapClientFactory.getInstance().getCursoPort();

        Map<String, DataInstitutoDTO> institutosPorNombre = new LinkedHashMap<>();
        Map<String, DataCursoDTO> cursosPorNombre = new LinkedHashMap<>();
        Set<String> nombresCursosDelDocente = new HashSet<>();
        List<DataEdicionCursoDTO> edicionesDelDocente = new ArrayList<>();
        List<DataCursoDTO> cursos = Collections.emptyList();
        List<DataEdicionCursoDTO> ediciones = Collections.emptyList();
        DataEdicionCursoDTO edicionSeleccionada = null;
        List<DataInscEdicionDTO> inscriptosAceptados = new ArrayList<>();
        boolean docenteParticipa = false;
        boolean todasEvaluadas = false;
        boolean edicionCerrada = false;

        String nombreInstituto = request.getParameter("instituto");
        String nombreCurso = request.getParameter("curso");
        String nombreEdicion = request.getParameter("edicion");
        if (nombreEdicion == null || nombreEdicion.isBlank()) {
            nombreEdicion = request.getParameter("nombreEdi");
        }

        try {
            if (usuarioPort == null || edicionPort == null || cursoPort == null) {
                throw new IllegalStateException("Servicios web no disponibles.");
            }

            DataDocenteDTO docenteActualizado = docenteEnSesion;
            try {
                DataUsuarioDTO u = usuarioPort.consultarUsuario(docenteEnSesion.getNickname());
                if (u instanceof DataDocenteDTO d) {
                    docenteActualizado = d;
                    if (session != null) {
                        session.setAttribute("usuarioLogueado", docenteActualizado);
                    }
                }
            } catch (Exception ignored) {
                // Si la consulta falla, utilizamos el docente en sesión
            }

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
                    if (modoVer && !Boolean.TRUE.equals(edicion.isCerrada())) {
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
                edicionCerrada = Boolean.TRUE.equals(edicionSeleccionada.isCerrada());
                docenteParticipa = true;

                List<DataInscEdicionDTO> todasInsc = edicionPort.buscarInscriptosEdicion(edicionSeleccionada.getNombreEdi());
                if (todasInsc != null) {
                    todasEvaluadas = true;
                    for (DataInscEdicionDTO insc : todasInsc) {
                        if (insc.getEstado() == EstadoInscripcion.ACEPTADA) {
                            inscriptosAceptados.add(insc);
                            if (insc.getNota() == null) {
                                todasEvaluadas = false;
                            }
                        }
                    }
                    if (inscriptosAceptados.isEmpty()) {
                        todasEvaluadas = false;
                    }
                }
            }
        } catch (Exception e) {
            institutosPorNombre.clear();
            cursos = Collections.emptyList();
            ediciones = Collections.emptyList();
            edicionSeleccionada = null;
            inscriptosAceptados.clear();
            request.setAttribute("mensajeError", "Error al cargar la información: " + e.getMessage());
        }

        request.setAttribute("institutos", institutosPorNombre.values());
        request.setAttribute("institutosDoc", institutosPorNombre.values());
        request.setAttribute("cursos", cursos);
        request.setAttribute("cursosDoc", cursos);
        request.setAttribute("ediciones", ediciones);
        request.setAttribute("edicionesDoc", ediciones);
        request.setAttribute("edicionSeleccionada", edicionSeleccionada);
        request.setAttribute("edicionVigente", edicionSeleccionada);
        request.setAttribute("edicionSeleccionadaDoc", edicionSeleccionada);
        request.setAttribute("docenteParticipa", docenteParticipa);
        request.setAttribute("inscriptosAceptados", inscriptosAceptados);
        request.setAttribute("todasEvaluadas", todasEvaluadas);
        request.setAttribute("edicionCerrada", edicionCerrada);

        String vista = modoVer ? "/webEdicion/ver-evaluaciones-docente.jsp" : "/webEdicion/evaluacion-docente.jsp";
        request.getRequestDispatcher(vista).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))
                || (request.getHeader("Accept") != null && request.getHeader("Accept").contains("application/json"))
                || "true".equalsIgnoreCase(request.getParameter("ajax"));

        HttpSession session = request.getSession(false);
        Object usuarioEnSesion = session == null ? null : session.getAttribute("usuarioLogueado");
        if (!(usuarioEnSesion instanceof DataDocenteDTO)) {
            if (usuarioEnSesion == null) {
                if (isAjax) {
                    responderJson(response, HttpServletResponse.SC_UNAUTHORIZED, false,
                            "Debe iniciar sesión para realizar esta acción.", null, false);
                } else {
                    response.sendRedirect(request.getContextPath() + "/login");
                }
            } else {
                if (isAjax) {
                    responderJson(response, HttpServletResponse.SC_FORBIDDEN, false,
                            "Acceso denegado: solo docentes pueden realizar esta acción.", null, false);
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
            }
            return;
        }

        String accion = request.getParameter("accion");
        String nombreEdicion = request.getParameter("nombreEdi");
        if (nombreEdicion == null || nombreEdicion.isBlank()) {
            nombreEdicion = request.getParameter("edicion");
        }
        String nombreInstituto = request.getParameter("instituto");
        String nombreCurso = request.getParameter("curso");

        if (nombreEdicion == null || nombreEdicion.isBlank()) {
            if (isAjax) {
                responderJson(response, HttpServletResponse.SC_BAD_REQUEST, false,
                        "Edición no especificada.", accion, false);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Edición no especificada.");
            }
            return;
        }

        EdicionDeCursoWebService edicionPort = SoapClientFactory.getInstance().getEdicionCursoPort();
        if (edicionPort == null) {
            if (isAjax) {
                responderJson(response, HttpServletResponse.SC_SERVICE_UNAVAILABLE, false,
                        "Servicio web de ediciones no disponible.", accion, false);
            } else {
                if (session != null) {
                    session.setAttribute("mensajeError", "Servicio web de ediciones no disponible.");
                }
                redirigir(request, response, nombreInstituto, nombreCurso, nombreEdicion);
            }
            return;
        }

        List<String> errores = new ArrayList<>();
        Map<String, String[]> params = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("nota-")) {
                String nickname = key.substring("nota-".length());
                String[] vals = entry.getValue();
                if (vals != null && vals.length > 0 && vals[0] != null && !vals[0].trim().isEmpty()) {
                    try {
                        int nota = Integer.parseInt(vals[0].trim());
                        if (nota < 1 || nota > 12) {
                            errores.add("La nota para " + nickname + " debe estar comprendida entre 1 y 12.");
                        } else {
                            edicionPort.registrarCalificacion(nombreEdicion, nickname, nota);
                        }
                    } catch (NumberFormatException e) {
                        errores.add("Nota inválida para el estudiante " + nickname + ".");
                    } catch (Exception e) {
                        errores.add("Error al guardar nota de " + nickname + ": " + e.getMessage());
                    }
                }
            }
        }

        boolean exito;
        String mensajeResultado;
        boolean cerrada = false;

        if ("cerrar".equalsIgnoreCase(accion)) {
            if (errores.isEmpty()) {
                try {
                    edicionPort.cerrarEdicionCurso(nombreEdicion);
                    exito = true;
                    cerrada = true;
                    mensajeResultado = "El curso/edición ha sido cerrado exitosamente. Las evaluaciones ya no podrán ser modificadas.";
                } catch (Exception e) {
                    exito = false;
                    mensajeResultado = "No se pudo cerrar la edición: " + e.getMessage();
                }
            } else {
                exito = false;
                mensajeResultado = String.join("\n", errores);
            }
        } else {
            if (errores.isEmpty()) {
                exito = true;
                mensajeResultado = "Evaluaciones registradas correctamente.";
            } else {
                exito = false;
                mensajeResultado = String.join("\n", errores);
            }
        }

        if (isAjax) {
            int statusCode = exito ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST;
            responderJson(response, statusCode, exito, mensajeResultado, accion, cerrada);
        } else {
            if (session != null) {
                if (exito) {
                    session.setAttribute("mensajeExito", mensajeResultado);
                } else {
                    session.setAttribute("mensajeError", mensajeResultado.replace("\n", "<br>"));
                }
            }
            redirigir(request, response, nombreInstituto, nombreCurso, nombreEdicion);
        }
    }

    private void responderJson(HttpServletResponse response, int status, boolean success,
                              String mensaje, String accion, boolean cerrada) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> respMap = new HashMap<>();
        respMap.put("success", success);
        respMap.put("mensaje", mensaje);
        if (accion != null) {
            respMap.put("accion", accion);
        }
        respMap.put("cerrada", cerrada);
        gson.toJson(respMap, response.getWriter());
    }

    private void redirigir(HttpServletRequest request, HttpServletResponse response,
                           String instituto, String curso, String edicion) throws IOException {
        StringBuilder url = new StringBuilder(request.getContextPath()).append("/evaluacionDocente?");
        url.append("instituto=").append(URLEncoder.encode(instituto != null ? instituto : "", StandardCharsets.UTF_8));
        url.append("&curso=").append(URLEncoder.encode(curso != null ? curso : "", StandardCharsets.UTF_8));
        if (edicion != null && !edicion.isBlank()) {
            url.append("&edicion=").append(URLEncoder.encode(edicion, StandardCharsets.UTF_8));
        }
        response.sendRedirect(url.toString());
    }
}
