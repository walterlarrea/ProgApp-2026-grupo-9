package com.grupo9.edext.grupo9.servidor_web.servlets.edicion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "EvaluacionEstudianteServlet", urlPatterns = {"/evaluacionEstudiante"})
public class EvaluacionEstudianteServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/webEdicion/evaluacion-estudiante.jsp").forward(request, response);
    }
    
}
