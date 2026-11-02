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
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.InstitutoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataDocenteDTO;

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
import java.util.List;
import java.util.Map;

@WebServlet(name = "EvaluacionDocenteServlet", urlPatterns = {"/evaluacionDocente", "/evaluacionesDocente"})
public class EvaluacionDocenteServlet extends HttpServlet {

    private final Gson gson = new Gson();

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

        InstitutoWebService instPort = SoapClientFactory.getInstance().getInstitutoPort();
        CursoWebService cursoPort = SoapClientFactory.getInstance().getCursoPort();
        EdicionDeCursoWebService edicionPort = SoapClientFactory.getInstance().getEdicionCursoPort();

        List<DataInstitutoDTO> institutos = Collections.emptyList();
        List<DataCursoDTO> cursos = Collections.emptyList();
        DataEdicionCursoDTO edicionVigente = null;
        List<DataInscEdicionDTO> inscriptosAceptados = new ArrayList<>();
        boolean docenteParticipa = false;
        boolean todasEvaluadas = false;
        boolean edicionCerrada = false;

        String nombreInstituto = request.getParameter("instituto");
        String nombreCurso = request.getParameter("curso");

        try {
            if (instPort != null) {
                institutos = instPort.consultarTodosLosInstitutos();
            }

            if (nombreInstituto != null && !nombreInstituto.isBlank() && cursoPort != null) {
                cursos = cursoPort.cursosPorInstituto(nombreInstituto);
            }

            if (nombreCurso != null && !nombreCurso.isBlank() && edicionPort != null) {
                edicionVigente = edicionPort.obtenerEdicionVigente(nombreCurso);

                if (edicionVigente != null) {
                    edicionCerrada = Boolean.TRUE.equals(edicionVigente.isCerrada());

                    if (edicionVigente.getDocentes() != null) {
                        for (String nickDoc : edicionVigente.getDocentes()) {
                            if (docenteEnSesion.getNickname() != null
                                    && docenteEnSesion.getNickname().equalsIgnoreCase(nickDoc)) {
                                docenteParticipa = true;
                                break;
                            }
                        }
                    }

                    if (docenteParticipa) {
                        List<DataInscEdicionDTO> todasInsc = edicionPort.buscarInscriptosEdicion(edicionVigente.getNombreEdi());
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
                }
            }
        } catch (Exception e) {
            request.setAttribute("mensajeError", "Error al cargar la información: " + e.getMessage());
        }

        request.setAttribute("institutos", institutos);
        request.setAttribute("cursos", cursos);
        request.setAttribute("edicionVigente", edicionVigente);
        request.setAttribute("docenteParticipa", docenteParticipa);
        request.setAttribute("inscriptosAceptados", inscriptosAceptados);
        request.setAttribute("todasEvaluadas", todasEvaluadas);
        request.setAttribute("edicionCerrada", edicionCerrada);

        request.getRequestDispatcher("/webEdicion/evaluacion-docente.jsp").forward(request, response);
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
                redirigir(request, response, nombreInstituto, nombreCurso);
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
            redirigir(request, response, nombreInstituto, nombreCurso);
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

    private void redirigir(HttpServletRequest request, HttpServletResponse response, String instituto, String curso)
            throws IOException {
        String url = request.getContextPath() + "/evaluacionDocente?instituto="
                + URLEncoder.encode(instituto != null ? instituto : "", StandardCharsets.UTF_8)
                + "&curso=" + URLEncoder.encode(curso != null ? curso : "", StandardCharsets.UTF_8);
        response.sendRedirect(url);
    }
}
