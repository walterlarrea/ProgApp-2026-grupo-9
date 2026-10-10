package com.grupo9.edext.grupo9.servidor_web.servlets;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.miscelanea.Fabrica;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@WebServlet("/instituto")
public class InstitutoServlet extends HttpServlet {
    IServidorCentral servidorCentral = Fabrica.getInstance().getIServidorCentral();
    Set<DataInstituto> institutos = Collections.emptySet();
    Set<DataCurso> cursos = Collections.emptySet();
    DataDocente[] docentes = new DataDocente[0];
    String estadoDb = "Conectado al Servidor Central";
        
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String nombre = request.getParameter("nombre");
        String nombreInst = request.getParameter("instituto");
        Set<DataCurso> cursosInstituto = Collections.emptySet();
        DataInstituto instituto = null;

        try {
            if (servidorCentral != null) {
                institutos = servidorCentral.consultarTodosLosInstitutos();
                cursos = servidorCentral.consultarTodosLosCursos();
                instituto = servidorCentral.buscarInstituto(nombre);
                docentes = servidorCentral.traerDocentes(instituto);
                cursosInstituto = servidorCentral.cursosPorInstituto(nombre);
            }
        } catch (Exception e) {
            estadoDb = "Servidor Central activo (sin conexión a base de datos o vacía: " + e.getMessage() + ")";
        }

        request.setAttribute("institutos", institutos);
        request.setAttribute("cursos", cursos);
        request.setAttribute("docentes", docentes);
        request.setAttribute("cursosInstituto", cursosInstituto);
        request.setAttribute("instituto", instituto);
        request.setAttribute("estadoDb", estadoDb);

        request.getRequestDispatcher("/webInstituto/verInfo-instituto.jsp").forward(request, response);
    }
}
