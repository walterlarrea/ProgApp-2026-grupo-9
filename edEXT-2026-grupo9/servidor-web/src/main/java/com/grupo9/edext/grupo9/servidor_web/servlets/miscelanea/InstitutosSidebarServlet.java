package com.grupo9.edext.grupo9.servidor_web.servlets.miscelanea;

import com.google.gson.Gson;
import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.DataInstitutoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.InstitutoWebService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Devuelve en JSON los nombres de todos los institutos para que la sidebar los
 * cargue por JavaScript (js/sidebarInstitutos.js), sin que cada servlet deba
 * cargar el atributo "institutos" solo para la barra lateral.
 */
@WebServlet(name = "InstitutosSidebarServlet", urlPatterns = {"/institutosSidebar"})
public class InstitutosSidebarServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<String> nombres = new ArrayList<>();
        try {
            InstitutoWebService instPort = SoapClientFactory.getInstance().getInstitutoPort();
            if (instPort != null) {
                List<DataInstitutoDTO> institutos = instPort.consultarTodosLosInstitutos();
                if (institutos != null) {
                    for (DataInstitutoDTO i : institutos) {
                        if (i != null && i.getNombreI() != null) {
                            nombres.add(i.getNombreI());
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Si el servicio falla se devuelve la lista vacía y la sidebar muestra "Sin institutos disponibles"
        }
        response.setContentType("application/json;charset=UTF-8");
        gson.toJson(nombres, response.getWriter());
    }
}
