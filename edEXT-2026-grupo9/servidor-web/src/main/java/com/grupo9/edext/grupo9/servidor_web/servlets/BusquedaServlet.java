package com.grupo9.edext.grupo9.servidor_web.servlets;

import com.google.gson.Gson;
import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.miscelanea.Fabrica;
import com.grupo9.edext.grupo9.servidor_central.controller.busqueda.TipoBusqueda;
import static com.grupo9.edext.grupo9.servidor_central.controller.busqueda.TipoBusqueda.USUARIO;
import static com.grupo9.edext.grupo9.servidor_central.controller.busqueda.TipoBusqueda.CURSO;
import static com.grupo9.edext.grupo9.servidor_central.controller.busqueda.TipoBusqueda.PROGRAMA_FORMACION;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import com.grupo9.edext.grupo9.servidor_central.dominio.ResultadoBusqueda;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@WebServlet(name = "BusquedaServlet", urlPatterns = {"/buscar"})
public class BusquedaServlet extends HttpServlet {
    IServidorCentral servidorCentral = Fabrica.getInstance().getIServidorCentral();

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String query = request.getParameter("q");
        String formatResponse = request.getParameter("format");
        List<ResultadoBusqueda> resultados = Collections.emptyList();
        ArrayList<DataCurso> cursos = new ArrayList<>();
        ArrayList<DataUsuario> usuarios = new ArrayList<>();
        ArrayList<DataProgramaFormacion> programas = new ArrayList<>();
        
        ArrayList<ResultadoBusqueda> listaResultados = new ArrayList<>();
        
        try {
            if (servidorCentral != null){
                resultados = servidorCentral.busquedaPrincipal(query);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] com.grupo9.edext.grupo9.servidor_web.servlets.BusquedaServlet.doGet()");
        }
        
        for (ResultadoBusqueda res : resultados) {
            listaResultados.add(res);
        }
        
        if (formatResponse == null || !formatResponse.equals("json")) {
            request.setAttribute("listaResultados", listaResultados);
            //        processRequest(request, response);
            request.getRequestDispatcher("/webBusqueda/resultadoBusqueda.jsp").forward(request, response);
        } else {
            Gson gson = new Gson();
            String jsonArray = gson.toJson(listaResultados);

            response.setContentType("application/json");
            // Get the printwriter object from response to write the required json object to the output stream      
            PrintWriter out = response.getWriter();
            // Assuming your json object is **jsonObject**, perform the following, it will return your json object  
            out.print(jsonArray);
            out.flush();
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet BusquedaServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BusquedaServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
