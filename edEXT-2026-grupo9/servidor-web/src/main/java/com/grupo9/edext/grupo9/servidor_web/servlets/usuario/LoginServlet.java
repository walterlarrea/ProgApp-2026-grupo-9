package com.grupo9.edext.grupo9.servidor_web.servlets.usuario;

import com.google.gson.JsonObject;
import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataUsuarioDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.ErrorNoExiste_Exception;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.UsuarioWebService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/webUsuario/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nicknameOEmail = request.getParameter("nicknameOEmail");
        String password = request.getParameter("password");

        boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))
                || (request.getHeader("Accept") != null && request.getHeader("Accept").contains("application/json"))
                || "true".equalsIgnoreCase(request.getParameter("ajax"));

        if (nicknameOEmail == null || nicknameOEmail.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            if (isAjax) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                JsonObject json = new JsonObject();
                json.addProperty("success", false);
                json.addProperty("error", "Debe completar todos los campos.");
                response.getWriter().write(json.toString());
                return;
            }
            request.setAttribute("error", "Debe completar todos los campos.");
            request.getRequestDispatcher("/webUsuario/login.jsp").forward(request, response);
            return;
        }

        UsuarioWebService usuarioPort = SoapClientFactory.getInstance().getUsuarioPort();
        try {
            if (usuarioPort == null) {
                throw new ServletException("Servicio de usuarios no disponible");
            }
            DataUsuarioDTO usuario = usuarioPort.iniciarSesion(nicknameOEmail.trim(), password.trim());
            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioLogueado", usuario);

            if (isAjax) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                JsonObject json = new JsonObject();
                json.addProperty("success", true);
                json.addProperty("redirectUrl", request.getContextPath() + "/home");
                response.getWriter().write(json.toString());
                return;
            }

            response.sendRedirect(request.getContextPath() + "/home");
        } catch (ErrorNoExiste_Exception e) {
            if (isAjax) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                JsonObject json = new JsonObject();
                json.addProperty("success", false);
                json.addProperty("error", e.getMessage());
                response.getWriter().write(json.toString());
                return;
            }
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/webUsuario/login.jsp").forward(request, response);
        } catch (Exception e) {
            if (isAjax) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                JsonObject json = new JsonObject();
                json.addProperty("success", false);
                json.addProperty("error", "Error interno al intentar iniciar sesión: " + e.getMessage());
                response.getWriter().write(json.toString());
                return;
            }
            request.setAttribute("error", "Error interno al intentar iniciar sesión: " + e.getMessage());
            request.getRequestDispatcher("/webUsuario/login.jsp").forward(request, response);
        }
    }
}
