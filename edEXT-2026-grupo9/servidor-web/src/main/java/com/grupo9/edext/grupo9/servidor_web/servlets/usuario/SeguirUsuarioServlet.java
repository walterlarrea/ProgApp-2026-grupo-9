package com.grupo9.edext.grupo9.servidor_web.servlets.usuario;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.miscelanea.Fabrica;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/seguir-usuario")
public class SeguirUsuarioServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        DataUsuario usuarioLogueado = (session != null) ? (DataUsuario) session.getAttribute("usuarioLogueado") : null;

        if (usuarioLogueado == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String targetNick = request.getParameter("nickname");
        String accion = request.getParameter("accion");

        if (targetNick != null && !targetNick.trim().isEmpty()) {
            IServidorCentral servidorCentral = Fabrica.getInstance().getIServidorCentral();
            try {
                if ("dejar".equalsIgnoreCase(accion)) {
                    servidorCentral.dejarDeSeguirUsuario(usuarioLogueado.getNickname(), targetNick.trim());
                } else {
                    servidorCentral.seguirUsuario(usuarioLogueado.getNickname(), targetNick.trim());
                }
            } catch (Exception e) {
                // Registrar o ignorar error
            }
        }

        String referer = request.getHeader("referer");
        if (referer != null && !referer.isEmpty()) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(request.getContextPath() + "/usuario?nickname=" + (targetNick != null ? targetNick : ""));
        }
    }
}
