package com.grupo9.edext.grupo9.servidor_web.servlets.busqueda;

import com.google.gson.Gson;
import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.busqueda.BusquedaWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.busqueda.DataCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.busqueda.DataProgramaFormacionDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.busqueda.DataUsuarioDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.busqueda.ResultadoBusquedaDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@WebServlet(name = "BusquedaServlet", urlPatterns = {"/buscar"})
public class BusquedaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String query = request.getParameter("q");
        String formatResponse = request.getParameter("format");
        List<ResultadoBusquedaDTO> resultados = Collections.emptyList();
        HashMap<String, String> filtros = new HashMap<>();

        ArrayList<ResultadoBusquedaExtendida> listaResultados = new ArrayList<>();

        try {
            BusquedaWebService busquedaPort = SoapClientFactory.getInstance().getBusquedaPort();
            if (busquedaPort != null && query != null) {
                resultados = busquedaPort.busquedaPrincipal(query);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] BusquedaServlet.doGet(): " + e.getMessage());
        }

        if (resultados != null) {
            for (ResultadoBusquedaDTO res : resultados) {
                ResultadoBusquedaExtendida resEnriquecido = enriquecerResultado(res, request.getContextPath(), query);
                listaResultados.add(resEnriquecido);
                filtros.put(resEnriquecido.tipoCss(), resEnriquecido.tipoVisible());
            }
        }

        if (formatResponse == null || !formatResponse.equals("json")) {
            request.setAttribute("listaResultados", listaResultados);
            request.setAttribute("filtros", filtros);
            request.setAttribute("query-param", query);
            request.getRequestDispatcher("/webBusqueda/resultadoBusqueda.jsp").forward(request, response);
        } else {
            Gson gson = new Gson();
            String jsonArray = gson.toJson(listaResultados);

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
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

    private ResultadoBusquedaExtendida enriquecerResultado(ResultadoBusquedaDTO resultado, String contextPath, String query) {
        String tipoVisible;
        String nombreVisible;
        String nombreVisibleHighlighted;
        String descVisibleHighlighted;
        String ruta;
        String parametro;
        String href;
        String fechaCreacion;
        Object data;

        switch (resultado.getTipo()) {
            case CURSO -> {
                DataCursoDTO curso = resultado.getCurso();
                data = curso;
                tipoVisible = "Curso";
                nombreVisible = curso != null ? curso.getNombreCurso() : "";
                nombreVisibleHighlighted = highlightQueryInText(nombreVisible, query);
                descVisibleHighlighted = highlightQueryInText(curso != null ? curso.getDescCurso() : "", query);

                ruta = "/curso";
                parametro = nombreVisible;
                href = contextPath + ruta + "?nombre="
                        + URLEncoder.encode(parametro, StandardCharsets.UTF_8);
                fechaCreacion = curso != null && curso.getFechaReg() != null ? curso.getFechaReg() : "-";
            }
            case USUARIO -> {
                DataUsuarioDTO usuario = resultado.getUsuario();
                data = usuario;
                tipoVisible = "Usuario";
                nombreVisible = (usuario != null ? usuario.getNombre() : "") + " " + (usuario != null ? usuario.getApellido() : "");
                nombreVisibleHighlighted = highlightQueryInText(nombreVisible, query);
                descVisibleHighlighted = "";

                ruta = "/usuario";
                parametro = usuario != null ? usuario.getNickname() : "";
                href = contextPath + ruta + "?nombre="
                        + URLEncoder.encode(parametro, StandardCharsets.UTF_8);
                fechaCreacion = "-";
            }
            case PROGRAMA_FORMACION -> {
                DataProgramaFormacionDTO programa = resultado.getPrograma();
                data = programa;
                tipoVisible = "Programa";
                nombreVisible = programa != null ? programa.getNombre() : "";
                nombreVisibleHighlighted = highlightQueryInText(nombreVisible, query);
                descVisibleHighlighted = highlightQueryInText(programa != null ? programa.getDescripcion() : "", query);

                ruta = "/programa";
                parametro = nombreVisible;
                href = contextPath + ruta + "?nombre="
                        + URLEncoder.encode(parametro, StandardCharsets.UTF_8);
                fechaCreacion = programa != null && programa.getFechaDeCreacion() != null ? programa.getFechaDeCreacion() : "-";
            }
            default -> throw new IllegalArgumentException("Tipo de búsqueda no soportado: " + resultado.getTipo());
        }

        String tipoCss = resultado.getTipo().name().toLowerCase(java.util.Locale.ROOT);
        return new ResultadoBusquedaExtendida(
                resultado.getTipo(), data, tipoVisible, nombreVisible, nombreVisibleHighlighted, descVisibleHighlighted, href, tipoCss, fechaCreacion);
    }
}
