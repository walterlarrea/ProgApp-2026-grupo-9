package com.grupo9.edext.grupo9.servidor_web.servlets.entidad;

import com.google.gson.Gson;
import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.CursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.DataCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.ErrorNoExiste_Exception;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.ErrorRepetidos_Exception;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.InstitutoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataEstudianteDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "MisCursosServlet", urlPatterns = {"/misCursos", "/mis-cursos"})
public class MisCursosServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object usuarioEnSesion = session == null ? null : session.getAttribute("usuarioLogueado");

        if (usuarioEnSesion == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (!(usuarioEnSesion instanceof DataEstudianteDTO estudiante)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo los estudiantes pueden acceder a Mis Cursos.");
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
        if (instPort != null) {
            try {
                request.setAttribute("institutos", instPort.consultarTodosLosInstitutos());
            } catch (Exception ignored) {
            }
        }

        CursoWebService cursoPort = SoapClientFactory.getInstance().getCursoPort();
        List<DataCursoDTO> cursos = Collections.emptyList();
        if (cursoPort != null) {
            try {
                cursos = cursoPort.cursosParticipaEstudiante(estudiante.getNickname());
            } catch (Exception e) {
                request.setAttribute("mensajeError", "Error al cargar tus cursos: " + e.getMessage());
            }
        } else {
            request.setAttribute("mensajeError", "Servicio web de cursos no disponible.");
        }

        request.setAttribute("cursos", cursos);
        request.getRequestDispatcher("/webCurso/mis-cursos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))
                || (request.getHeader("Accept") != null && request.getHeader("Accept").contains("application/json"))
                || "true".equalsIgnoreCase(request.getParameter("ajax"));

        HttpSession session = request.getSession(false);
        Object usuarioEnSesion = session == null ? null : session.getAttribute("usuarioLogueado");

        if (!(usuarioEnSesion instanceof DataEstudianteDTO estudiante)) {
            if (usuarioEnSesion == null) {
                if (isAjax) {
                    responderJson(response, HttpServletResponse.SC_UNAUTHORIZED, false,
                            "Debe iniciar sesión para realizar esta acción.", Collections.emptyList());
                } else {
                    response.sendRedirect(request.getContextPath() + "/login");
                }
            } else {
                if (isAjax) {
                    responderJson(response, HttpServletResponse.SC_FORBIDDEN, false,
                            "Acceso denegado: solo los estudiantes pueden valorar cursos.", Collections.emptyList());
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
            }
            return;
        }

        CursoWebService cursoPort = SoapClientFactory.getInstance().getCursoPort();
        if (cursoPort == null) {
            if (isAjax) {
                responderJson(response, HttpServletResponse.SC_SERVICE_UNAVAILABLE, false,
                        "Servicio web de cursos no disponible.", Collections.emptyList());
            } else {
                if (session != null) {
                    session.setAttribute("mensajeError", "Servicio web de cursos no disponible.");
                }
                response.sendRedirect(request.getContextPath() + "/misCursos");
            }
            return;
        }

        Map<String, Integer> valoracionesAProcesar = new LinkedHashMap<>();

        // Opción 1: parámetros directos (un único curso)
        String unCurso = request.getParameter("nombreCurso");
        if (unCurso == null || unCurso.isBlank()) {
            unCurso = request.getParameter("curso");
        }
        String unPuntaje = request.getParameter("puntaje");
        if (unCurso != null && !unCurso.isBlank() && unPuntaje != null && !unPuntaje.isBlank()) {
            try {
                int p = Integer.parseInt(unPuntaje.trim());
                valoracionesAProcesar.put(unCurso.trim(), p);
            } catch (NumberFormatException ignored) {
            }
        }

        // Opción 2: formulario con múltiples cursos (valoracion-{nombreCurso})
        Map<String, String[]> params = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("valoracion-")) {
                String nombreCurso = key.substring("valoracion-".length());
                String[] vals = entry.getValue();
                if (vals != null && vals.length > 0 && vals[0] != null && !vals[0].trim().isEmpty()) {
                    try {
                        int p = Integer.parseInt(vals[0].trim());
                        valoracionesAProcesar.put(nombreCurso, p);
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }

        if (valoracionesAProcesar.isEmpty()) {
            if (isAjax) {
                responderJson(response, HttpServletResponse.SC_BAD_REQUEST, false,
                        "Debe seleccionar un puntaje (de 1 a 5) para al menos un curso.", Collections.emptyList());
            } else {
                if (session != null) {
                    session.setAttribute("mensajeError", "Debe seleccionar un puntaje (de 1 a 5) para al menos un curso.");
                }
                response.sendRedirect(request.getContextPath() + "/misCursos");
            }
            return;
        }

        List<String> exitosos = new ArrayList<>();
        List<String> errores = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : valoracionesAProcesar.entrySet()) {
            String cursoNom = entry.getKey();
            int puntaje = entry.getValue();

            if (puntaje < 1 || puntaje > 5) {
                errores.add("El puntaje para el curso '" + cursoNom + "' debe estar comprendido entre 1 y 5.");
                continue;
            }

            try {
                cursoPort.valorarCurso(cursoNom, estudiante.getNickname(), puntaje);
                exitosos.add(cursoNom);
            } catch (ErrorRepetidos_Exception e) {
                errores.add("Ya has valorado el curso '" + cursoNom + "'. No es posible modificar la valoración.");
            } catch (ErrorNoExiste_Exception e) {
                errores.add("No se encontró el curso o estudiante para '" + cursoNom + "': " + e.getMessage());
            } catch (Exception e) {
                errores.add("Error al valorar el curso '" + cursoNom + "': " + e.getMessage());
            }
        }

        boolean exito = !exitosos.isEmpty() && errores.isEmpty();
        String mensaje;
        if (exito) {
            mensaje = exitosos.size() == 1
                    ? "La valoración para el curso '" + exitosos.get(0) + "' ha sido registrada con éxito."
                    : "Se han registrado con éxito las valoraciones para " + exitosos.size() + " cursos.";
        } else if (!exitosos.isEmpty()) {
            mensaje = "Se registraron " + exitosos.size() + " valoración(es), pero se presentaron observaciones:\n"
                    + String.join("\n", errores);
        } else {
            mensaje = String.join("\n", errores);
        }

        if (isAjax) {
            int statusCode = !exitosos.isEmpty() ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST;
            responderJson(response, statusCode, !exitosos.isEmpty(), mensaje, exitosos);
        } else {
            if (session != null) {
                if (!exitosos.isEmpty()) {
                    session.setAttribute("mensajeExito", mensaje);
                } else {
                    session.setAttribute("mensajeError", mensaje.replace("\n", "<br>"));
                }
            }
            response.sendRedirect(request.getContextPath() + "/misCursos");
        }
    }

    private void responderJson(HttpServletResponse response, int status, boolean success,
                              String mensaje, List<String> actualizados) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> respMap = new HashMap<>();
        respMap.put("success", success);
        respMap.put("mensaje", mensaje);
        respMap.put("actualizados", actualizados);
        gson.toJson(respMap, response.getWriter());
    }
}
