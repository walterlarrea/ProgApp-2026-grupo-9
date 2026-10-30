package com.grupo9.edext.grupo9.servidor_web.servlets.entidad;

import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.CursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.DataCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataDocenteDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataEdicionCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataInscEdicionDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.EdicionDeCursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.ErrorRepetidos_Exception;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.DataInstitutoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.InstitutoWebService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@MultipartConfig
@WebServlet(name = "EdicionCursoServlet", urlPatterns = {"/edicionCurso"})
public class EdicionCursoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String accion = request.getParameter("accion");
        String nombreInst = request.getParameter("instituto");

        List<DataInstitutoDTO> institutos = Collections.emptyList();
        List<DataCursoDTO> cursos = Collections.emptyList();
        List<DataDocenteDTO> docentes = Collections.emptyList();
        List<DataInscEdicionDTO> inscriptos = Collections.emptyList();
        DataEdicionCursoDTO edicion = null;
        String estadoDb = "Conectado al Servidor Central";

        InstitutoWebService instPort = SoapClientFactory.getInstance().getInstitutoPort();
        CursoWebService cursoPort = SoapClientFactory.getInstance().getCursoPort();
        EdicionDeCursoWebService edicionPort = SoapClientFactory.getInstance().getEdicionCursoPort();

        if ("alta".equals(accion)) {
            try {
                if (instPort != null) {
                    institutos = instPort.consultarTodosLosInstitutos();
                }
                if (nombreInst != null && !nombreInst.isBlank()) {
                    if (cursoPort != null) {
                        cursos = cursoPort.cursosPorInstituto(nombreInst);
                    }
                    if (edicionPort != null) {
                        docentes = edicionPort.traerDocentesPorInstituto(nombreInst);
                    }
                }
            } catch (Exception e) {
                estadoDb = "Error al cargar cursos y docentes: " + e.getMessage();
            }

            request.setAttribute("institutos", institutos);
            request.setAttribute("cursos", cursos);
            request.setAttribute("docentes", docentes);
            request.setAttribute("estadoDb", estadoDb);

            request.getRequestDispatcher("/webEdicion/alta-edicion.jsp").forward(request, response);
            return;
        }

        try {
            if (instPort != null) {
                institutos = instPort.consultarTodosLosInstitutos();
            }
            if (cursoPort != null) {
                cursos = cursoPort.consultarTodosLosCursos();
            }
            if (edicionPort != null && nombre != null && !nombre.isBlank()) {
                edicion = edicionPort.consultarUnaEdicionCurso(nombre);
                if (edicion != null) {
                    docentes = edicionPort.buscarDocentesEdicion(nombre);
                    inscriptos = edicionPort.buscarInscriptosEdicion(nombre);
                }
            }
        } catch (Exception e) {
            estadoDb = "Servidor Central activo (sin conexión a base de datos o vacía: " + e.getMessage() + ")";
            request.setAttribute("errorCarga", "No fue posible cargar los datos de la edición solicitada.");
        }

        request.setAttribute("edicion", edicion);
        request.setAttribute("institutos", institutos);
        request.setAttribute("cursos", cursos);
        request.setAttribute("docentes", docentes);
        request.setAttribute("inscriptos", inscriptos);
        request.setAttribute("estadoDb", estadoDb);

        request.getRequestDispatcher("/webEdicion/verInfo-edicion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombreEdi = request.getParameter("nombreEdi");
        String nombreCurso = request.getParameter("curso");
        String nombreInst = request.getParameter("instituto");
        String[] nombresDocentes = request.getParameterValues("docentes");
        String fInicio = request.getParameter("fInicio");
        String fFin = request.getParameter("fFin");
        String cupoParam = request.getParameter("cupo");
        Part imagenPart = request.getPart("imagen");

        String imagen = "edicion-placeholder.jpg";
        Integer cupo = (cupoParam == null || cupoParam.isBlank()) ? null : Integer.valueOf(cupoParam);

        if (imagenPart != null && imagenPart.getSize() > 0) {
            imagen = imagenPart.getSubmittedFileName();
            String rutaUploads = getServletContext().getRealPath("/imagenes/edicionCurso");
            File carpeta = new File(rutaUploads);
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            imagenPart.write(rutaUploads + File.separator + imagen);
        }

        EdicionDeCursoWebService edicionPort = SoapClientFactory.getInstance().getEdicionCursoPort();
        InstitutoWebService instPort = SoapClientFactory.getInstance().getInstitutoPort();
        CursoWebService cursoPort = SoapClientFactory.getInstance().getCursoPort();

        if (nombresDocentes == null || nombresDocentes.length == 0) {
            request.setAttribute("error", "Debe seleccionar al menos un docente.");
            cargarDatosFormulario(request, instPort, cursoPort, edicionPort, nombreInst);
            request.getRequestDispatcher("/webEdicion/alta-edicion.jsp").forward(request, response);
            return;
        }

        try {
            if (edicionPort == null) {
                throw new ServletException("Servicio de ediciones no disponible");
            }
            if (edicionPort.existeEdicionCurso(nombreEdi)) {
                throw new ErrorRepetidos_Exception("Ya existe una edición con ese nombre.", null);
            }

            List<String> docsList = Arrays.asList(nombresDocentes);
            edicionPort.guardarEdicionCurso(nombreEdi, nombreCurso, fInicio, fFin, cupo, docsList, imagen);
            request.getSession().setAttribute("mensajeExito", "😎");
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (ErrorRepetidos_Exception e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("nombreEdi", nombreEdi);
            request.setAttribute("cursoSeleccionado", nombreCurso);
            request.setAttribute("institutoSeleccionado", nombreInst);
            request.setAttribute("fInicio", fInicio);
            request.setAttribute("fFin", fFin);
            request.setAttribute("cupo", cupoParam);

            cargarDatosFormulario(request, instPort, cursoPort, edicionPort, nombreInst);
            request.getRequestDispatcher("/webEdicion/alta-edicion.jsp").forward(request, response);
        }
    }

    private void cargarDatosFormulario(HttpServletRequest request, InstitutoWebService instPort,
                                       CursoWebService cursoPort, EdicionDeCursoWebService edicionPort,
                                       String nombreInst) {
        try {
            if (instPort != null) {
                request.setAttribute("institutos", instPort.consultarTodosLosInstitutos());
            }
            if (nombreInst != null && !nombreInst.isBlank()) {
                if (edicionPort != null) {
                    request.setAttribute("docentes", edicionPort.traerDocentesPorInstituto(nombreInst));
                }
                if (cursoPort != null) {
                    request.setAttribute("cursos", cursoPort.cursosPorInstituto(nombreInst));
                }
            }
        } catch (Exception ignored) {
        }
    }
}
