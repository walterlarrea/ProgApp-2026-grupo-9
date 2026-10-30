package com.grupo9.edext.grupo9.servidor_web.servlets.entidad;

import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.CursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.DataCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.ErrorRepetidos_Exception;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataEdicionCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.EdicionDeCursoWebService;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@MultipartConfig
@WebServlet(name = "CursoServlet", urlPatterns = { "/curso" })
public class CursoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String accion = request.getParameter("accion");
        String nombreInst = request.getParameter("instituto");

        List<DataInstitutoDTO> institutos = Collections.emptyList();
        List<DataCursoDTO> cursos = Collections.emptyList();
        List<DataEdicionCursoDTO> ediciones = Collections.emptyList();
        List<DataCursoDTO> previas = Collections.emptyList();
        DataCursoDTO curso = null;
        String estadoDb = "Conectado al Servidor Central";

        CursoWebService cursoPort = SoapClientFactory.getInstance().getCursoPort();
        InstitutoWebService instPort = SoapClientFactory.getInstance().getInstitutoPort();
        EdicionDeCursoWebService edicionPort = SoapClientFactory.getInstance().getEdicionCursoPort();

        if ("alta".equals(accion)) {
            try {
                if (instPort != null) {
                    institutos = instPort.consultarTodosLosInstitutos();
                }
                if (cursoPort != null && nombreInst != null && !nombreInst.isBlank()) {
                    previas = cursoPort.cursosPorInstituto(nombreInst);
                }
            } catch (Exception e) {
                estadoDb = "Servidor Central activo (sin conexión a base de datos o vacía: " + e.getMessage() + ")";
            }

            request.setAttribute("institutos", institutos);
            request.setAttribute("previas", previas);
            request.setAttribute("estadoDb", estadoDb);

            request.getRequestDispatcher("/webCurso/alta-curso.jsp").forward(request, response);
            return;
        }

        try {
            if (cursoPort != null && nombre != null && !nombre.isBlank()) {
                curso = cursoPort.buscarCurso(nombre);
                cursos = cursoPort.consultarTodosLosCursos();
                if (curso != null && curso.getPrevias() != null && !curso.getPrevias().isEmpty()) {
                    List<DataCursoDTO> previasEncontradas = new ArrayList<>();
                    for (String previaNom : curso.getPrevias()) {
                        DataCursoDTO cPrevia = cursoPort.buscarCurso(previaNom);
                        if (cPrevia != null) {
                            previasEncontradas.add(cPrevia);
                        }
                    }
                    previas = previasEncontradas;
                }
            }
            if (instPort != null) {
                institutos = instPort.consultarTodosLosInstitutos();
            }
            if (edicionPort != null && nombre != null && !nombre.isBlank()) {
                ediciones = edicionPort.traerEdicionesPorCurso(nombre);
            }
        } catch (Exception e) {
            estadoDb = "Servidor Central activo (sin conexión a base de datos o vacía: " + e.getMessage() + ")";
        }

        if (curso == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Curso no encontrado");
            return;
        }

        request.setAttribute("curso", curso);
        request.setAttribute("institutos", institutos);
        request.setAttribute("cursos", cursos);
        request.setAttribute("ediciones", ediciones);
        request.setAttribute("previas", previas);
        request.setAttribute("estadoDb", estadoDb);

        request.getRequestDispatcher("/webCurso/verInfo-curso.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String desc = request.getParameter("desc");
        String nombreInst = request.getParameter("instituto");
        int duracion = Integer.parseInt(request.getParameter("duracion"));
        int cantCred = Integer.parseInt(request.getParameter("cantCred"));
        int cantHoras = Integer.parseInt(request.getParameter("cantHoras"));
        String url = request.getParameter("url");
        Part imagenPart = request.getPart("imagen");
        String[] nombresPrevias = request.getParameterValues("previas");

        String imagen = "curso-placeholder.jpg";
        List<String> previasList = nombresPrevias != null ? Arrays.asList(nombresPrevias) : Collections.emptyList();

        // Procesar y guardar imagen.
        if (imagenPart != null && imagenPart.getSize() > 0) {
            imagen = imagenPart.getSubmittedFileName();
            String rutaUploads = getServletContext().getRealPath("/imagenes/curso");
            File carpeta = new File(rutaUploads);

            if (!carpeta.exists() && !carpeta.mkdirs()) {
                throw new IOException("No se pudo crear la carpeta de imágenes");
            }
            imagenPart.write(rutaUploads + File.separator + imagen);
        }

        CursoWebService cursoPort = SoapClientFactory.getInstance().getCursoPort();
        try {
            if (cursoPort == null) {
                throw new ServletException("Servicio de cursos no disponible");
            }
            if (cursoPort.existeCurso(nombre)) {
                throw new ErrorRepetidos_Exception("Ya existe un curso con ese nombre.", null);
            }

            cursoPort.guardarCurso(nombreInst, nombre, desc, duracion, cantHoras, cantCred,
                    LocalDate.now().toString(), url, previasList, imagen);
            request.getSession().setAttribute("mensajeExito", "😎");
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (ErrorRepetidos_Exception e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("nombre", nombre);
            request.setAttribute("desc", desc);
            request.setAttribute("duracion", duracion);
            request.setAttribute("cantHoras", cantHoras);
            request.setAttribute("cantCred", cantCred);
            request.setAttribute("institutoSeleccionado", nombreInst);
            request.setAttribute("url", url);
            request.setAttribute("imagen", imagen);

            InstitutoWebService instPort = SoapClientFactory.getInstance().getInstitutoPort();
            if (instPort != null) {
                try {
                    request.setAttribute("institutos", instPort.consultarTodosLosInstitutos());
                } catch (Exception ignored) {
                }
            }
            if (cursoPort != null && nombreInst != null && !nombreInst.isBlank()) {
                try {
                    request.setAttribute("previas", cursoPort.cursosPorInstituto(nombreInst));
                } catch (Exception ignored) {
                }
            }
            request.getRequestDispatcher("/webCurso/alta-curso.jsp").forward(request, response);
        }
    }
}
