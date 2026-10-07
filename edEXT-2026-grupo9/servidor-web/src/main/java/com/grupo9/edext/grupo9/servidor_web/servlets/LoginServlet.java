package com.grupo9.edext.grupo9.servidor_web.servlets;

import com.grupo9.edext.grupo9.miscelanea.Fabrica;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/webUsuario/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nicknameOEmail = request.getParameter("nicknameOEmail");
        String password = request.getParameter("password");

        if (nicknameOEmail == null || nicknameOEmail.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Debe completar todos los campos.");
            request.getRequestDispatcher("/webUsuario/login.jsp").forward(request, response);
            return;
        }

        try {
            DataUsuario usuario = Fabrica.getInstance().getIServidorCentral().iniciarSesion(nicknameOEmail.trim(), password.trim());
            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioLogueado", usuario);
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (ErrorNoExiste e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/webUsuario/login.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error interno al intentar iniciar sesión: " + e.getMessage());
            request.getRequestDispatcher("/webUsuario/login.jsp").forward(request, response);
        }
    }
}
