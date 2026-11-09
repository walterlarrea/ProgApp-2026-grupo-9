package com.grupo9.edext.grupo9.servidor_web.servlets.edicion;

import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataEdicionCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataInscEdicionDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.EdicionDeCursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.EstadoInscripcion;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataEstudianteDTO;
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
import java.util.List;


@WebServlet(name = "EvaluacionEstudianteServlet", urlPatterns = {"/evaluacionEstudiante"})
public class EvaluacionEstudianteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Object usuarioEnSesion = session == null ? null : session.getAttribute("usuarioLogueado");
        if (usuarioEnSesion == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (!(usuarioEnSesion instanceof DataEstudianteDTO estudianteEnSesion)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        UsuarioWebService usuarioPort = SoapClientFactory.getInstance().getUsuarioPort();
        EdicionDeCursoWebService edicionPort = SoapClientFactory.getInstance().getEdicionCursoPort();

        // Inscripciones ACEPTADAS del estudiante a ediciones cerradas, con su nota
        List<DataInscEdicionDTO> evaluaciones = new ArrayList<>();
        try {
            if (usuarioPort == null || edicionPort == null) {
                throw new IllegalStateException("Servicios web no disponibles.");
            }

            DataEstudianteDTO estudiante = estudianteEnSesion;
            DataUsuarioDTO actualizado = usuarioPort.consultarUsuario(estudianteEnSesion.getNickname());
            if (actualizado instanceof DataEstudianteDTO e) {
                estudiante = e;
                session.setAttribute("usuarioLogueado", e);
            }

            List<String> edicionesInscriptas = estudiante.getEdicionesInscriptas();
            if (edicionesInscriptas != null) {
                for (String nombreEdicion : edicionesInscriptas) {
                    if (nombreEdicion == null || nombreEdicion.isBlank()) {
                        continue;
                    }
                    DataEdicionCursoDTO edicion = edicionPort.consultarUnaEdicionCurso(nombreEdicion);
                    if (edicion == null || !Boolean.TRUE.equals(edicion.isCerrada())) {
                        continue;
                    }
                    List<DataInscEdicionDTO> inscriptos = edicionPort.buscarInscriptosEdicion(nombreEdicion);
                    if (inscriptos == null) {
                        continue;
                    }
                    for (DataInscEdicionDTO insc : inscriptos) {
                        if (insc != null && insc.getEstado() == EstadoInscripcion.ACEPTADA
                                && insc.getEstudiante() != null
                                && estudiante.getNickname().equals(insc.getEstudiante().getNickname())) {
                            evaluaciones.add(insc);
                        }
                    }
                }
            }
        } catch (Exception e) {
            evaluaciones.clear();
            request.setAttribute("mensajeError", "No se pudieron cargar tus evaluaciones: " + e.getMessage());
        }

        request.setAttribute("evaluaciones", evaluaciones);
        request.getRequestDispatcher("/webEdicion/evaluacion-estudiante.jsp").forward(request, response);
    }

}
