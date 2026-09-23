package com.grupo9.edext.grupo9.servidor_web.servlets;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.servidor_central.controller.ServidorCentralController;
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

@WebServlet(name = "HomeServlet", urlPatterns = {"/home", ""})
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Set<DataInstituto> institutos = Collections.emptySet();
        Set<DataCurso> cursos = Collections.emptySet();
        String estadoDb = "Conectado al Servidor Central";

        try {
            IServidorCentral servidorCentral = ServidorCentralController.getInstance();
            if (servidorCentral != null) {
                institutos = servidorCentral.consultarTodosLosInstitutos();
                cursos = servidorCentral.consultarTodosLosCursos();
            }
        } catch (Exception e) {
            estadoDb = "Servidor Central activo (sin conexión a base de datos o vacía: " + e.getMessage() + ")";
        }

        request.setAttribute("institutos", institutos);
        request.setAttribute("cursos", cursos);
        request.setAttribute("estadoDb", estadoDb);

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
