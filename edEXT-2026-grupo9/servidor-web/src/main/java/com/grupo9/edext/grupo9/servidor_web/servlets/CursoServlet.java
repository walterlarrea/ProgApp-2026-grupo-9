package com.grupo9.edext.grupo9.servidor_web.servlets;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.miscelanea.Fabrica;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@WebServlet("/curso")
public class CursoServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
        Set<DataInstituto> institutos = Collections.emptySet();
        Set<DataCurso> cursos = Collections.emptySet();
        String estadoDb = "Conectado al Servidor Central";
        String nombre = request.getParameter("nombre");
        IServidorCentral servidorCentral = Fabrica.getInstance().getIServidorCentral();

        try {
            if (servidorCentral != null) {
                institutos = servidorCentral.consultarTodosLosInstitutos();
                cursos = servidorCentral.consultarTodosLosCursos();
                DataCurso curso = servidorCentral.buscarCurso(nombre);
        if (curso == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Curso no encontrado");
            return;
        }
            }
        } catch (Exception e) {
            estadoDb = "Servidor Central activo (sin conexión a base de datos o vacía: " + e.getMessage() + ")";
        }
        
        // busco el curso
        DataCurso curso = servidorCentral.buscarCurso(nombre);
        if (curso == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Curso no encontrado");
            return;
        }
        request.setAttribute("curso", curso);
        request.setAttribute("institutos", institutos);
        request.setAttribute("cursos", cursos);
        request.setAttribute("estadoDb", estadoDb);

        request.getRequestDispatcher("/webCurso/verInfo-curso.jsp").forward(request, response);
    }
}
