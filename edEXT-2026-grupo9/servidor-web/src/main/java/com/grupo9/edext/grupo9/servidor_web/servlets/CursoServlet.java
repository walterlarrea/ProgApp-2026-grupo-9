package com.grupo9.edext.grupo9.servidor_web.servlets;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.miscelanea.Fabrica;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.io.File;

@MultipartConfig
@WebServlet(name = "CursoServlet", urlPatterns = {"/curso"})
public class CursoServlet extends HttpServlet {
    Set<DataInstituto> institutos = Collections.emptySet();
    Set<DataCurso> cursos = Collections.emptySet();
    Set<DataEdicionCurso> ediciones = Collections.emptySet();
    Set<DataCurso> previas = new HashSet<>();
    String estadoDb = "Conectado al Servidor Central";
    IServidorCentral servidorCentral = Fabrica.getInstance().getIServidorCentral();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
        String nombre = request.getParameter("nombre");
        DataCurso curso = null;
        String accion = request.getParameter("accion");
        String nombreInst = request.getParameter("instituto");

        if("alta".equals(accion)) {
            try {
                if(servidorCentral != null) {
                    institutos = servidorCentral.consultarTodosLosInstitutos();
                    if(nombreInst != null && !nombreInst.isBlank()){
                        previas = new HashSet<>(servidorCentral.cursosPorInstituto(nombreInst));
            }else {
                previas = new HashSet<>();
            }
                }
            } catch (Exception e) {
                estadoDb = "Servidor Central activo (sin conexión a base de datos o vacía: "+ e.getMessage() + ")";
            }

            request.setAttribute("institutos", institutos);
            request.setAttribute("previas", previas);

            request.getRequestDispatcher("/webCurso/alta-curso.jsp").forward(request, response);
            return;
        }
        
        try {
            if (servidorCentral != null) {
                curso = servidorCentral.buscarCurso(nombre);
                institutos = servidorCentral.consultarTodosLosInstitutos();
                cursos = servidorCentral.consultarTodosLosCursos();
                ediciones = servidorCentral.traerEdiciones(curso, true);
                // Recuperar las previas asociadas al curso consultado.
                previas = curso.previas();
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

        String imagen = null;
        LocalDate fechaReg = LocalDate.now();
        DataInstituto instituto = null;
        //las previas pertenecen únicamente a esta solicitud.
        Set<DataCurso> previasSeleccionadas = new HashSet<>();

        try {
            if (servidorCentral != null) {
                instituto = servidorCentral.buscarInstituto(nombreInst);
                if (nombresPrevias != null) {
                    for (String nombrePrevia : nombresPrevias) {
                        DataCurso previa = servidorCentral.buscarCurso(nombrePrevia);
                        if (previa != null) {
                            previasSeleccionadas.add(previa);
                        }
                    }
                }
            }
        } catch (Exception e) {
            estadoDb = "Error al buscar el instituto o las previas: " + e.getMessage();
            e.printStackTrace();
        }

        // Procesar y guardar imagen.
        if (imagenPart != null && imagenPart.getSize() > 0) {
            imagen = imagenPart.getSubmittedFileName();
            String rutaUploads = getServletContext().getRealPath("/uploads/curso");
            File carpeta = new File(rutaUploads);

            if (!carpeta.exists() && !carpeta.mkdirs()) {
                throw new IOException("nao nao imagen");
            }
            imagenPart.write(rutaUploads + File.separator + imagen);
        }
        //registro el curso..
        DataCurso nuevoCurso = new DataCurso(instituto, nombre, desc, duracion, cantHoras, cantCred, fechaReg, url, previasSeleccionadas, new HashSet<>(), imagen);
        servidorCentral.guardarCurso(nuevoCurso);
        //vuekvo al menú principal..
        response.sendRedirect(request.getContextPath() + "/home");
    }
}
