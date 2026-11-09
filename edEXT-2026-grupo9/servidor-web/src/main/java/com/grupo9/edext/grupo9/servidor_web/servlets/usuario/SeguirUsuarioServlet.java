package com.grupo9.edext.grupo9.servidor_web.servlets.usuario;

import com.google.gson.JsonObject;
import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataUsuarioDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.UsuarioWebService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/seguir-usuario")
public class SeguirUsuarioServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        DataUsuarioDTO usuarioLogueado = (session != null) ? (DataUsuarioDTO) session.getAttribute("usuarioLogueado") : null;

        boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))
                || (request.getHeader("Accept") != null && request.getHeader("Accept").contains("application/json"))
                || "true".equalsIgnoreCase(request.getParameter("ajax"));

        if (usuarioLogueado == null) {
            if (isAjax) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                JsonObject json = new JsonObject();
                json.addProperty("success", false);
                json.addProperty("error", "Debe iniciar sesión para realizar esta acción.");
                response.getWriter().write(json.toString());
                return;
            }
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String targetNick = request.getParameter("nickname");
        String accion = request.getParameter("accion");
        boolean exito = false;
        String errorMsg = null;
        boolean nuevoEstadoSiguiendo = false;

        if (targetNick != null && !targetNick.trim().isEmpty() && !targetNick.trim().equalsIgnoreCase(usuarioLogueado.getNickname())) {
            UsuarioWebService usuarioPort = SoapClientFactory.getInstance().getUsuarioPort();
            try {
                if (usuarioPort != null) {
                    if ("dejar".equalsIgnoreCase(accion)) {
                        usuarioPort.dejarDeSeguirUsuario(usuarioLogueado.getNickname(), targetNick.trim());
                        nuevoEstadoSiguiendo = false;
                    } else {
                        usuarioPort.seguirUsuario(usuarioLogueado.getNickname(), targetNick.trim());
                        nuevoEstadoSiguiendo = true;
                    }
                    exito = true;
                } else {
                    errorMsg = "Servicio de usuarios no disponible.";
                }
            } catch (Exception e) {
                errorMsg = e.getMessage();
            }
        } else {
            errorMsg = "Usuario no válido.";
        }

        if (isAjax) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JsonObject json = new JsonObject();
            json.addProperty("success", exito);
            json.addProperty("siguiendo", nuevoEstadoSiguiendo);
            json.addProperty("targetNick", targetNick);
            if (errorMsg != null) {
                json.addProperty("error", errorMsg);
            }
            try {
                UsuarioWebService usuarioPort = SoapClientFactory.getInstance().getUsuarioPort();
                if (usuarioPort != null) {
                    List<String> seguidos = usuarioPort.obtenerSeguidos(usuarioLogueado.getNickname());
                    json.addProperty("seguidosCount", seguidos != null ? seguidos.size() : 0);
                }
            } catch (Exception ignored) {
            }
            response.getWriter().write(json.toString());
            return;
        }

        String referer = request.getHeader("referer");
        if (referer != null && !referer.isEmpty()) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(request.getContextPath() + "/usuario?nickname=" + (targetNick != null ? targetNick : ""));
        }
    }
}
