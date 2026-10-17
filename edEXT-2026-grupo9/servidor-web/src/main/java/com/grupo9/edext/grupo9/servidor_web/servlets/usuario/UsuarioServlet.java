package com.grupo9.edext.grupo9.servidor_web.servlets.usuario;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.miscelanea.Fabrica;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@WebServlet("/usuario")
public class UsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nickname = request.getParameter("nickname");

        if (nickname == null || nickname.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        IServidorCentral servidorCentral = Fabrica.getInstance().getIServidorCentral();
        Set<DataInstituto> institutos = Collections.emptySet();
        Set<DataCurso> cursos = Collections.emptySet();

        try {
            if (servidorCentral != null) {
                institutos = servidorCentral.consultarTodosLosInstitutos();
                cursos = servidorCentral.consultarTodosLosCursos();
                DataUsuario usuarioConsultado = servidorCentral.consultarUsuario(nickname.trim());
                request.setAttribute("usuario", usuarioConsultado);

                HttpSession session = request.getSession(false);
                DataUsuario usuarioLogueado = (session != null) ? (DataUsuario) session.getAttribute("usuarioLogueado") : null;
                boolean loSigue = false;
                if (usuarioLogueado != null) {
                    loSigue = servidorCentral.esSeguidor(usuarioLogueado.getNickname(), nickname.trim());
                }
                request.setAttribute("loSigue", loSigue);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error al consultar usuario: " + e.getMessage());
        }

        request.setAttribute("institutos", institutos);
        request.setAttribute("cursos", cursos);
        request.getRequestDispatcher("/webUsuario/verInfo-usuario.jsp").forward(request, response);
    }
}
