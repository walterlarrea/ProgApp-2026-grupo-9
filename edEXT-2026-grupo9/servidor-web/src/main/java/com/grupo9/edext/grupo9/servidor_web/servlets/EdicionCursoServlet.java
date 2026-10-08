package com.grupo9.edext.grupo9.servidor_web.servlets;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.miscelanea.Fabrica;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInscEdicion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import java.time.LocalDate;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.io.File;

@MultipartConfig
@WebServlet(name = "EdicionCursoServlet", urlPatterns = {"/edicionCurso"})
public class EdicionCursoServlet extends HttpServlet {
    IServidorCentral servidorCentral = Fabrica.getInstance().getIServidorCentral();
    //sidebar (en todo momento)
    Set<DataInstituto> institutos = Collections.emptySet();
    Set<DataCurso> cursos = Collections.emptySet();
    String estadoDb = "Conectado al Servidor Central";
    //exclusivo edición
    DataDocente[] docentes = new DataDocente[0];
    Set<DataInscEdicion> inscriptos = Collections.emptySet();
        
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
        String nombre = request.getParameter("nombre");
        String accion = request.getParameter("accion");
        String nombreInst = request.getParameter("instituto");
        DataEdicionCurso edicion = null;
        DataInstituto instituto = null;
        
        if ("alta".equals(accion)) {
            try {
                if (servidorCentral != null) {
                    institutos = servidorCentral.consultarTodosLosInstitutos();
                    //si ya se seleccionó un instituto
                    if (nombreInst != null && !nombreInst.isBlank()) {
                        instituto = servidorCentral.buscarInstituto(nombreInst);
                        cursos = servidorCentral.cursosPorInstituto(nombreInst);
                        docentes = servidorCentral.traerDocentes(instituto);
                    } else {
                        //todavía no se seleccionó instituto
                        cursos = Collections.emptySet();
                        docentes = new DataDocente[0];
                    }
                }
            } catch (Exception e) {
                estadoDb = "Servidor Central activo (sin conexión a base de datos o vacía: "+ e.getMessage() + ")";
            }

            request.setAttribute("institutos", institutos);
            request.setAttribute("cursos", cursos);    
            request.getRequestDispatcher("/webEdicion/alta-edicion.jsp").forward(request, response);
            return;
        }
        
        try {
            if (servidorCentral != null) {
                edicion = servidorCentral.consultarUnaEdicionCurso(nombre);
                institutos = servidorCentral.consultarTodosLosInstitutos();
                cursos = servidorCentral.consultarTodosLosCursos();
                if (edicion != null) {
                    docentes = servidorCentral.buscarDocentes(edicion);
                    inscriptos = servidorCentral.buscarInscriptos(edicion);
                }
            } 
        } catch (Exception e) {
            estadoDb = "Servidor Central activo (sin conexión a base de datos o vacía: " + e.getMessage() + ")";
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
        String nombreEdi = request.getParameter("nombre");
        String nombreCurso = request.getParameter("curso");
        String nombreInst = request.getParameter("instituto");
        String[] nombresDocentes = request.getParameterValues("docentes");
        LocalDate fInicio = LocalDate.parse(request.getParameter("fInicio"));
        LocalDate fFin = LocalDate.parse(request.getParameter("fFin"));
        int cupo = Integer.parseInt(request.getParameter("cupo"));
        Part imagenPart = request.getPart("imagen");
        
        LocalDate fechaPub = LocalDate.now();
        String imagen = null;
        DataInstituto instituto = null;
        DataDocente[] docentes = null;
        Set<DataDocente> docentesSeleccionados = new HashSet<>();
        Set<DataCurso> cursos = null;
        DataCurso dataCurso = null;
         
        try{
            if(servidorCentral != null){
                instituto = servidorCentral.buscarInstituto(nombreInst);
                docentes = servidorCentral.traerDocentes(instituto);
                cursos = servidorCentral.cursosPorInstituto(nombreInst);
                //buscar un curso asociado
                for (DataCurso curso : cursos) {
                    if (curso.nombreCurso().equals(nombreCurso)) {
                        dataCurso = curso;
                        break;
                    }
                }
            }
        }catch (Exception e){
            estadoDb = "Servidor Central activo (sin conexión a base de datos o vacía: " + e.getMessage() + ")";
        }
        //seleccionar docentes...
        if (nombresDocentes != null) {
            for (String nombreDocente : nombresDocentes) {
                for (DataDocente docente : docentes) {
                    if (docente.getNombre().equals(nombreDocente)) {
                        docentesSeleccionados.add(docente);
                        break;
                    }
                }
            }
        }
        //procesar/guardar imagen
        if (imagenPart != null && imagenPart.getSize() > 0) {
            imagen = imagenPart.getSubmittedFileName();
            String rutaUploads = getServletContext().getRealPath("/uploads/edicionCurso");
            File carpeta = new File(rutaUploads);

            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            imagenPart.write(rutaUploads + File.separator + imagen);
        }
        //registrar edición...
        DataEdicionCurso nuevaEdicion = new DataEdicionCurso(nombreEdi, dataCurso, fInicio, fFin, cupo, docentesSeleccionados, new HashSet<>(), fechaPub, imagen);
        servidorCentral.guardarEdicionCurso(nuevaEdicion);
        //vuelve a los detalles
        response.sendRedirect(request.getContextPath() + "/edicionCurso");
    }
}
