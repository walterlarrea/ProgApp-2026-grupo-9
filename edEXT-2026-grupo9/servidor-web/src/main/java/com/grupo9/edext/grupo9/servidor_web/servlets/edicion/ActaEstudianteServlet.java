package com.grupo9.edext.grupo9.servidor_web.servlets.edicion;

import com.grupo9.edext.grupo9.servidor_web.pdf.ActaPdfGenerator;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Acta del estudiante: ediciones CERRADAS en las que fue aceptado y ya tiene nota.
 * GET muestra el acta; POST la descarga como PDF generado dinámicamente.
 */
@WebServlet(name = "ActaEstudianteServlet", urlPatterns = {"/actaEstudiante"})
public class ActaEstudianteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DataEstudianteDTO estudiante = obtenerEstudianteLogueado(request, response);
        if (estudiante == null) {
            return;
        }

        Map<DataEdicionCursoDTO, Integer> acta = new LinkedHashMap<>();
        try {
            estudiante = actualizarEstudiante(request, estudiante);
            acta = cargarActa(estudiante);
        } catch (Exception e) {
            request.setAttribute("mensajeError", "No se pudo cargar tu acta: " + e.getMessage());
        }

        request.setAttribute("acta", acta);
        request.getRequestDispatcher("/webUsuario/acta-estudiante.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DataEstudianteDTO estudiante = obtenerEstudianteLogueado(request, response);
        if (estudiante == null) {
            return;
        }

        Map<DataEdicionCursoDTO, Integer> acta;
        try {
            estudiante = actualizarEstudiante(request, estudiante);
            acta = cargarActa(estudiante);
        } catch (Exception e) {
            request.setAttribute("acta", new LinkedHashMap<DataEdicionCursoDTO, Integer>());
            request.setAttribute("mensajeError", "No se pudo generar el PDF: " + e.getMessage());
            request.getRequestDispatcher("/webUsuario/acta-estudiante.jsp").forward(request, response);
            return;
        }

        try {
            // Se genera en memoria primero: si falla, no queda una respuesta PDF a medias.
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ActaPdfGenerator.generar(estudiante, acta, buffer);
            //fuerza la descarga
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + nombreArchivo(estudiante) + "\"");//genera el pdf con el nombre del estudiante
            response.setContentLength(buffer.size());
            buffer.writeTo(response.getOutputStream());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No se pudo generar el PDF.");
        }
    }

    //valida la sesión: devuelve el estudiante en sesión, o tira redirect/403 y devuelve null
    private DataEstudianteDTO obtenerEstudianteLogueado(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        Object usuarioEnSesion = session == null ? null : session.getAttribute("usuarioLogueado");
        if (usuarioEnSesion == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        if (!(usuarioEnSesion instanceof DataEstudianteDTO estudiante)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        return estudiante;
    }

    //refresca el estudiante desde el servidor central y lo guarda en sesión, así ediciones inscriptas están al día
    private DataEstudianteDTO actualizarEstudiante(HttpServletRequest request, DataEstudianteDTO estudiante)
            throws Exception {
        UsuarioWebService usuarioPort = SoapClientFactory.getInstance().getUsuarioPort();
        if (usuarioPort == null) {
            throw new IllegalStateException("Servicios web no disponibles.");
        }
        DataUsuarioDTO actualizado = usuarioPort.consultarUsuario(estudiante.getNickname());
        if (actualizado instanceof DataEstudianteDTO e) {
            request.getSession().setAttribute("usuarioLogueado", e);
            return e;
        }
        return estudiante;
    }

    //ediciones cerradas con inscripción aceptada y nota del estudiante: edición -> nota
    private Map<DataEdicionCursoDTO, Integer> cargarActa(DataEstudianteDTO estudiante) throws Exception {
        EdicionDeCursoWebService edicionPort = SoapClientFactory.getInstance().getEdicionCursoPort();
        if (edicionPort == null) {
            throw new IllegalStateException("Servicios web no disponibles.");
        }

        Map<DataEdicionCursoDTO, Integer> acta = new LinkedHashMap<>();
        if (estudiante.getEdicionesInscriptas() == null) {
            return acta;
        }
        for (String nombreEdicion : estudiante.getEdicionesInscriptas()) {
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
                    acta.put(edicion, insc.getNota());
                }
            }
        }
        return acta;
    }

    //acta_Nombre_Apellido.pdf, sin tildes ni caracteres raros
    private String nombreArchivo(DataEstudianteDTO estudiante) {
        String base = estudiante.getNombre() + "_" + estudiante.getApellido();
        base = Normalizer.normalize(base, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return "acta_" + base.replaceAll("[^A-Za-z0-9_-]", "_") + ".pdf";
    }
}
