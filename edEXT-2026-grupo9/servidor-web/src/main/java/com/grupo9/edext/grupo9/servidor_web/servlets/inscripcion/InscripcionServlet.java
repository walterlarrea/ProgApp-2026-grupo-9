package com.grupo9.edext.grupo9.servidor_web.servlets.inscripcion;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.miscelanea.Fabrica;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EstadoInscripcion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInscEdicion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.mensajes.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@WebServlet(name = "InscripcionServlet", urlPatterns = {"/inscripcion"})
public class InscripcionServlet extends HttpServlet  {
    IServidorCentral servidorCentral = Fabrica.getInstance().getIServidorCentral();
    //sidebar (en todo momento)
    Set<DataInstituto> institutos = Collections.emptySet();
    Set<DataCurso> cursos = Collections.emptySet();
    String estadoDb = "Conectado al Servidor Central";
    //exclusivo inscripción
    Set<DataEdicionCurso> ediciones = Collections.emptySet();
    DataDocente[] docentes = new DataDocente[0];
    Set<DataInscEdicion> inscriptos = Collections.emptySet();
    LocalDate fechaReg = LocalDate.now();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
        String accion = request.getParameter("accion");
        String nombreInst = request.getParameter("instituto");
        String nombreCurso = request.getParameter("curso");
        String nombre = request.getParameter("edicion");
        DataInstituto instituto = null;
        DataCurso curso = null;
        DataEdicionCurso edicion = null;
        
        if ("insc".equals(accion)) { //si voy a inscribir un estudiante a una edición
            try {
                if(servidorCentral != null){
                    institutos = servidorCentral.consultarTodosLosInstitutos();
                    if(nombreInst != null && !nombreInst.isBlank()){
                        instituto = servidorCentral.buscarInstituto(nombreInst);
                        if(instituto != null){
                            cursos = servidorCentral.cursosPorInstituto(nombreInst);
                            if(nombreCurso != null && !nombreCurso.isBlank()){
                                curso = servidorCentral.buscarCurso(nombreCurso);
                                if(curso != null){
                                    ediciones = servidorCentral.traerEdiciones(curso, true);
                                    if(nombre != null && !nombre.isBlank()){
                                        edicion = servidorCentral.consultarUnaEdicionCurso(nombre);
                                        if(edicion != null){
                                            docentes = servidorCentral.buscarDocentes(edicion);
                                        }
                                    }
                                }else{
                                    ediciones = Collections.emptySet();
                                }
                            }else{
                                ediciones = Collections.emptySet();
                            }
                        }else{
                            cursos = Collections.emptySet();
                        }
                    }else{
                        cursos = Collections.emptySet();
                    }
                }
            }catch (Exception e){
                estadoDb = "Error al cargar cursos y docentes: " + e.getMessage();e.printStackTrace();
            }
            
            request.setAttribute("institutos", institutos);
            request.setAttribute("cursos", cursos);
            request.setAttribute("ediciones", ediciones);
            request.setAttribute("edicion", edicion);
            request.setAttribute("docentes", docentes);

            request.getRequestDispatcher("/webEdicion/inscribir-edicion.jsp").forward(request, response);
            return;
        }
        
            try {
                if (servidorCentral != null) {
                    institutos = servidorCentral.consultarTodosLosInstitutos();
                    cursos = servidorCentral.consultarTodosLosCursos();
                } 
            } catch (Exception e) {
                estadoDb = "Servidor Central activo (sin conexión a base de datos o vacía: " + e.getMessage() + ")";
            }
                  
        request.setAttribute("institutos", institutos);
        request.setAttribute("cursos", cursos);
        
        request.getRequestDispatcher("/webEdicion/verRes-inscripcion.jsp").forward(request, response);
    }
    
    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String nombreEdi = request.getParameter("edicion");

        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        DataUsuario usuario = (DataUsuario) session.getAttribute("usuarioLogueado");
        String nickname = usuario.getNickname();
        
        try {
            //registrar estudiante.
            servidorCentral.inscribirEstudiante(fechaReg, nickname, nombreEdi, EstadoInscripcion.INSCRIPTO);
            //vuelvo al menú principal..   
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (ErrorRepetidos e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/webEdicion/inscribir-edicion.jsp").forward(request, response);

        } catch (ErrorNoExiste e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/webEdicion/inscribir-edicion.jsp").forward(request, response);
        }
    }
}


