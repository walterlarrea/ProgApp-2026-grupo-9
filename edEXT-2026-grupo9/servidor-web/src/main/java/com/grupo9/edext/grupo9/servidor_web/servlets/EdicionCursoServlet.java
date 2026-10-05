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
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@WebServlet("/edicionCurso")
public class EdicionCursoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
        IServidorCentral servidorCentral = Fabrica.getInstance().getIServidorCentral();
        //sidebar (en todo momento)
        Set<DataInstituto> institutos = Collections.emptySet();
        Set<DataCurso> cursos = Collections.emptySet();
        String estadoDb = "Conectado al Servidor Central";
        //exclusivo edición
        DataDocente[] docentes = new DataDocente[0];
        Set<DataInscEdicion> inscriptos = Collections.emptySet();
        String nombre = request.getParameter("nombre");
        DataEdicionCurso edicion = null;

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
        
    }
}
