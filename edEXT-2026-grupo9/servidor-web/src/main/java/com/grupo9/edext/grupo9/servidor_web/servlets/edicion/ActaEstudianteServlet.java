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

/**
 * Acta del estudiante: ediciones CERRADAS en las que fue aceptado y ya tiene nota.
 */
@WebServlet(name = "ActaEstudianteServlet", urlPatterns = {"/actaEstudiante"})
public class ActaEstudianteServlet extends HttpServlet {

    /** Fila del acta (getters para EL). */
    public static class FilaActa {
        private final String edicion;
        private final String fechaFin;
        private final int duracion;
        private final int creditos;
        private final Integer nota;

        public FilaActa(String edicion, String fechaFin, int duracion, int creditos, Integer nota) {
            this.edicion = edicion;
            this.fechaFin = fechaFin;
            this.duracion = duracion;
            this.creditos = creditos;
            this.nota = nota;
        }

        public String getEdicion() { return edicion; }
        public String getFechaFin() { return fechaFin; }
        public int getDuracion() { return duracion; }
        public int getCreditos() { return creditos; }
        public Integer getNota() { return nota; }
    }

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

        List<FilaActa> acta = new ArrayList<>();
        try {
            UsuarioWebService usuarioPort = SoapClientFactory.getInstance().getUsuarioPort();
            EdicionDeCursoWebService edicionPort = SoapClientFactory.getInstance().getEdicionCursoPort();
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
                                && insc.getNota() != null
                                && insc.getEstudiante() != null
                                && estudiante.getNickname().equals(insc.getEstudiante().getNickname())) {
                            int duracion = edicion.getCursoAsoc() != null ? edicion.getCursoAsoc().getDuracion() : 0;
                            int creditos = edicion.getCursoAsoc() != null ? edicion.getCursoAsoc().getCantCred() : 0;
                            acta.add(new FilaActa(edicion.getNombreEdi(), edicion.getFechaFin(),
                                    duracion, creditos, insc.getNota()));
                        }
                    }
                }
            }
        } catch (Exception e) {
            acta.clear();
            request.setAttribute("mensajeError", "No se pudo cargar tu acta: " + e.getMessage());
        }

        request.setAttribute("acta", acta);
        request.getRequestDispatcher("/webUsuario/acta-estudiante.jsp").forward(request, response);
    }
}
