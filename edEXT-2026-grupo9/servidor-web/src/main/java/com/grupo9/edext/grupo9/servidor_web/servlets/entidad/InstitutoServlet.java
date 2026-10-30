package com.grupo9.edext.grupo9.servidor_web.servlets.entidad;

import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.CursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.DataCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataDocenteDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.EdicionDeCursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.DataInstitutoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.InstitutoWebService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet("/instituto")
public class InstitutoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        List<DataInstitutoDTO> institutos = Collections.emptyList();
        List<DataCursoDTO> cursos = Collections.emptyList();
        List<DataCursoDTO> cursosInstituto = Collections.emptyList();
        List<DataDocenteDTO> docentes = Collections.emptyList();
        DataInstitutoDTO instituto = null;
        String estadoDb = "Conectado al Servidor Central";

        try {
            InstitutoWebService instPort = SoapClientFactory.getInstance().getInstitutoPort();
            CursoWebService cursoPort = SoapClientFactory.getInstance().getCursoPort();
            EdicionDeCursoWebService edicionPort = SoapClientFactory.getInstance().getEdicionCursoPort();

            if (instPort != null) {
                institutos = instPort.consultarTodosLosInstitutos();
                if (nombre != null && !nombre.isBlank()) {
                    instituto = instPort.buscarInstituto(nombre);
                }
            }
            if (cursoPort != null) {
                cursos = cursoPort.consultarTodosLosCursos();
                if (nombre != null && !nombre.isBlank()) {
                    cursosInstituto = cursoPort.cursosPorInstituto(nombre);
                }
            }
            if (edicionPort != null && nombre != null && !nombre.isBlank()) {
                docentes = edicionPort.traerDocentesPorInstituto(nombre);
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
