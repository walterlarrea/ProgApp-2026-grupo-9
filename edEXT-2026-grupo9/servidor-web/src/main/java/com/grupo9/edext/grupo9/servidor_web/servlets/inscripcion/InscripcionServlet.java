package com.grupo9.edext.grupo9.servidor_web.servlets.inscripcion;

import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.CursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.DataCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataDocenteDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataEdicionCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataInscEdicionDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.EdicionDeCursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.ErrorNoExiste_Exception;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.ErrorRepetidos_Exception;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.DataInstitutoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.InstitutoWebService;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "InscripcionServlet", urlPatterns = {"/inscripcion"})
public class InscripcionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        String nombreInst = request.getParameter("instituto");
        String nombreCurso = request.getParameter("curso");
        String nombre = request.getParameter("edicion");

        InstitutoWebService instPort = SoapClientFactory.getInstance().getInstitutoPort();
        CursoWebService cursoPort = SoapClientFactory.getInstance().getCursoPort();
        EdicionDeCursoWebService edicionPort = SoapClientFactory.getInstance().getEdicionCursoPort();
        UsuarioWebService usuarioPort = SoapClientFactory.getInstance().getUsuarioPort();

        if ("insc".equals(accion)) {
            List<DataInstitutoDTO> institutos = Collections.emptyList();
            List<DataCursoDTO> cursos = Collections.emptyList();
            List<DataEdicionCursoDTO> ediciones = Collections.emptyList();
            List<DataDocenteDTO> docentes = Collections.emptyList();
            DataEdicionCursoDTO edicion = null;

            try {
                if (instPort != null) {
                    institutos = instPort.consultarTodosLosInstitutos();
                    if (nombreInst != null && !nombreInst.isBlank()) {
                        if (cursoPort != null) {
                            cursos = cursoPort.cursosPorInstituto(nombreInst);
                            if (nombreCurso != null && !nombreCurso.isBlank() && edicionPort != null) {
                                ediciones = edicionPort.traerEdicionesPorCurso(nombreCurso);
                                if (nombre != null && !nombre.isBlank()) {
                                    edicion = edicionPort.consultarUnaEdicionCurso(nombre);
                                    if (edicion != null) {
                                        docentes = edicionPort.buscarDocentesEdicion(nombre);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                request.setAttribute("error", "Error al cargar cursos y docentes: " + e.getMessage());
            }

            request.setAttribute("institutos", institutos);
            request.setAttribute("cursos", cursos);
            request.setAttribute("ediciones", ediciones);
            request.setAttribute("edicion", edicion);
            request.setAttribute("docentes", docentes);

            request.setAttribute("institutoSeleccionado", nombreInst);
            request.setAttribute("cursoSeleccionado", nombreCurso);
            request.setAttribute("edicionSeleccionada", nombre);

            request.getRequestDispatcher("/webEdicion/inscribir-edicion.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        Object usuarioEnSesion = session == null ? null : session.getAttribute("usuarioLogueado");
        if (!(usuarioEnSesion instanceof DataUsuarioDTO usuarioLogueado)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<DataInscEdicionDTO> inscripciones = new ArrayList<>();
        try {
            if (usuarioPort == null || edicionPort == null) {
                throw new IllegalStateException("Servicios web no disponibles.");
            }

            DataUsuarioDTO usuarioActualizado = usuarioPort.consultarUsuario(usuarioLogueado.getNickname());
            if (usuarioActualizado == null) {
                throw new IllegalStateException("No se encontró el usuario autenticado.");
            }
            session.setAttribute("usuarioLogueado", usuarioActualizado);

            if (usuarioActualizado instanceof DataEstudianteDTO estudiante) {
                List<String> edicionesInscriptas = estudiante.getEdicionesInscriptas();
                if (edicionesInscriptas != null) {
                    for (String nombreEdicion : edicionesInscriptas) {
                        if (nombreEdicion == null || nombreEdicion.isBlank()) {
                            continue;
                        }
                        List<DataInscEdicionDTO> inscriptosEdicion = edicionPort.buscarInscriptosEdicion(nombreEdicion);
                        if (inscriptosEdicion != null) {
                            for (DataInscEdicionDTO insc : inscriptosEdicion) {
                                if (insc != null && insc.getEstudiante() != null
                                        && usuarioActualizado.getNickname().equals(insc.getEstudiante().getNickname())) {
                                    inscripciones.add(insc);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            inscripciones.clear();
            request.setAttribute("error", "No se pudieron cargar tus inscripciones: " + e.getMessage());
        }

        request.setAttribute("inscripciones", inscripciones);
        request.getRequestDispatcher("/webEdicion/verRes-inscripcion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String nombreInst = request.getParameter("instituto");
        String nombreCurso = request.getParameter("curso");
        String nombreEdi = request.getParameter("edicion");

        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        DataUsuarioDTO usuario = (DataUsuarioDTO) session.getAttribute("usuarioLogueado");
        String nickname = usuario.getNickname();
        EdicionDeCursoWebService edicionPort = SoapClientFactory.getInstance().getEdicionCursoPort();

        try {
            if (edicionPort == null) {
                throw new ServletException("Servicio de ediciones no disponible");
            }
            edicionPort.inscribirEstudiante(LocalDate.now().toString(), nickname, nombreEdi, "INSCRIPTO");
            request.getSession().setAttribute("mensajeExito", "😎");
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (ErrorRepetidos_Exception | ErrorNoExiste_Exception e) {
            request.setAttribute("error", e.getMessage());
            cargarDatosFormulario(request, nombreInst, nombreCurso, nombreEdi);
            request.getRequestDispatcher("/webEdicion/inscribir-edicion.jsp").forward(request, response);
        }
    }

    private void cargarDatosFormulario(HttpServletRequest request, String nombreInst, String nombreCurso, String nombreEdi) {
        InstitutoWebService instPort = SoapClientFactory.getInstance().getInstitutoPort();
        CursoWebService cursoPort = SoapClientFactory.getInstance().getCursoPort();
        EdicionDeCursoWebService edicionPort = SoapClientFactory.getInstance().getEdicionCursoPort();

        try {
            if (instPort != null) {
                request.setAttribute("institutos", instPort.consultarTodosLosInstitutos());
            }
            if (nombreInst != null && !nombreInst.isBlank() && cursoPort != null) {
                request.setAttribute("cursos", cursoPort.cursosPorInstituto(nombreInst));
                if (nombreCurso != null && !nombreCurso.isBlank() && edicionPort != null) {
                    request.setAttribute("ediciones", edicionPort.traerEdicionesPorCurso(nombreCurso));
                    if (nombreEdi != null && !nombreEdi.isBlank()) {
                        DataEdicionCursoDTO ed = edicionPort.consultarUnaEdicionCurso(nombreEdi);
                        request.setAttribute("edicion", ed);
                        if (ed != null) {
                            request.setAttribute("docentes", edicionPort.buscarDocentesEdicion(nombreEdi));
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }

        request.setAttribute("institutoSeleccionado", nombreInst);
        request.setAttribute("cursoSeleccionado", nombreCurso);
        request.setAttribute("edicionSeleccionada", nombreEdi);
    }
}
