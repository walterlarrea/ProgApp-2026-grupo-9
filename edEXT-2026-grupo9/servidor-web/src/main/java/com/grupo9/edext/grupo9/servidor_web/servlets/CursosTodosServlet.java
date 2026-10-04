package com.grupo9.edext.grupo9.servidor_web.servlets;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.miscelanea.Fabrica;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@WebServlet("/cursos")
public class CursosTodosServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Set<DataCurso> cursos = Collections.emptySet();
        String estadoDb = "Conectado al Servidor Central";
        // Obtener cursos
        try {
            IServidorCentral servidorCentral = Fabrica.getInstance().getIServidorCentral();
            if (servidorCentral != null) {
                cursos = servidorCentral.consultarTodosLosCursos();
            }
        } catch (Exception e) {
            estadoDb = "Servidor Central activo (sin conexión a base de datos o vacía: " + e.getMessage() + ")";
        }
        // yada yada

        request.setAttribute("cursos", cursos);

        request.getRequestDispatcher("/webCurso/verTodos-curso.jsp").forward(request, response);
    }
}
