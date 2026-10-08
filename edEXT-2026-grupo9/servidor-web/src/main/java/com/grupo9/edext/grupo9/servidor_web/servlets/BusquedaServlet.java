package com.grupo9.edext.grupo9.servidor_web.servlets;

import com.google.gson.Gson;
import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.miscelanea.Fabrica;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import com.grupo9.edext.grupo9.servidor_central.dominio.ResultadoBusqueda;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


@WebServlet(name = "BusquedaServlet", urlPatterns = {"/buscar"})
public class BusquedaServlet extends HttpServlet {
    IServidorCentral servidorCentral = Fabrica.getInstance().getIServidorCentral();

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
        HashMap<String, String> filtros = new HashMap<>();
        
        ArrayList<ResultadoBusquedaExtendida> listaResultados = new ArrayList<>();
        
        try {
            if (servidorCentral != null){
                resultados = servidorCentral.busquedaPrincipal(query);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] com.grupo9.edext.grupo9.servidor_web.servlets.BusquedaServlet.doGet()");
        }
        
        for (ResultadoBusqueda res : resultados) {
            ResultadoBusquedaExtendida resEnriquecido = enriquecerResultado(res, request.getContextPath(), query);
            listaResultados.add(resEnriquecido);
            filtros.put(resEnriquecido.tipoCss(), resEnriquecido.tipoVisible());
        }
        
        if (formatResponse == null || !formatResponse.equals("json")) {
            request.setAttribute("listaResultados", listaResultados);
            request.setAttribute("filtros", filtros);
            request.setAttribute("query-param", query);
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

    private String highlightQueryInText(String text, String query) {
        if (text == null || query == null || query.isEmpty()) {
            return text;
        }
        String escapedQuery = java.util.regex.Pattern.quote(query);
        return text.replaceAll("(?i)" + escapedQuery, "<b>$0</b>");
    }

    private ResultadoBusquedaExtendida enriquecerResultado(ResultadoBusqueda resultado, String contextPath, String query) {
        String tipoVisible;
        String nombreVisible;
        String nombreVisibleHighlighted;
        String descVisibleHighlighted;
        String ruta;
        String parametro;
        String href;
        String fechaCreacion;

        switch (resultado.tipo()) {
            case CURSO -> {
                DataCurso curso = (DataCurso) resultado.data();
                tipoVisible = "Curso";
                nombreVisible = curso.nombreCurso();
                nombreVisibleHighlighted = highlightQueryInText(nombreVisible, query);
                descVisibleHighlighted = highlightQueryInText(curso.descCurso(), query);

                ruta = "/curso";
                parametro = curso.nombreCurso();
                href = contextPath + ruta + "?nombre="
                        + URLEncoder.encode(parametro, StandardCharsets.UTF_8);
                fechaCreacion = curso.fechaReg().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
            case USUARIO -> {
                DataUsuario usuario = (DataUsuario) resultado.data();
                tipoVisible = "Usuario";
                nombreVisible = usuario.getNombre() + " " + usuario.getApellido();
                nombreVisibleHighlighted = highlightQueryInText(nombreVisible, query);
                descVisibleHighlighted = "";

                ruta = "/usuario";
                parametro = usuario.getNickname();
                href = contextPath + ruta + "?nombre="
                        + URLEncoder.encode(parametro, StandardCharsets.UTF_8);
                fechaCreacion = "-";
            }
            case PROGRAMA_FORMACION -> {
                DataProgramaFormacion programa = (DataProgramaFormacion) resultado.data();
                tipoVisible = "Programa";
                nombreVisible = programa.nombre();
                nombreVisibleHighlighted = highlightQueryInText(nombreVisible, query);
                descVisibleHighlighted = highlightQueryInText(programa.descripcion(), query);

                ruta = "/programa";
                parametro = programa.nombre();
                href = contextPath + ruta + "?nombre="
                        + URLEncoder.encode(parametro, StandardCharsets.UTF_8);
                fechaCreacion = programa.fechaDeCreacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
            default -> throw new IllegalArgumentException("Tipo de búsqueda no soportado: " + resultado.tipo());
        }

        String tipoCss = resultado.tipo().name().toLowerCase(java.util.Locale.ROOT);
        return new ResultadoBusquedaExtendida(
                resultado.tipo(), resultado.data(), tipoVisible, nombreVisible, nombreVisibleHighlighted, descVisibleHighlighted, href, tipoCss, fechaCreacion);
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
