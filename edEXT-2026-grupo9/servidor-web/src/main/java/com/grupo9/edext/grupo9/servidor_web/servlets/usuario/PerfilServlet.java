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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        DataUsuario usuarioLogueado = (session != null) ? (DataUsuario) session.getAttribute("usuarioLogueado") : null;

        if (usuarioLogueado == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        IServidorCentral servidorCentral = Fabrica.getInstance().getIServidorCentral();
        Set<DataInstituto> institutos = Collections.emptySet();
        Set<DataCurso> cursos = Collections.emptySet();
        String estadoDb = "Conectado al Servidor Central";
        DataUsuario usuarioActualizado = usuarioLogueado;
        List<DataUsuario> usuariosSeguidos = new ArrayList<>();

        try {
            if (servidorCentral != null) {
                usuarioActualizado = servidorCentral.consultarUsuario(usuarioLogueado.getNickname());
                session.setAttribute("usuarioLogueado", usuarioActualizado);
                institutos = servidorCentral.consultarTodosLosInstitutos();
                cursos = servidorCentral.consultarTodosLosCursos();

                Set<String> seguidosNicks = servidorCentral.obtenerSeguidos(usuarioLogueado.getNickname());
                if (seguidosNicks != null) {
                    for (String segNick : seguidosNicks) {
                        try {
                            DataUsuario segUser = servidorCentral.consultarUsuario(segNick);
                            if (segUser != null) {
                                usuariosSeguidos.add(segUser);
                            }
                        } catch (Exception ex) {
                            // Si no se encuentra el usuario seguido, continuar
                        }
                    }
                }
            }
        } catch (Exception e) {
            estadoDb = "Servidor Central activo (" + e.getMessage() + ")";
        }

        request.setAttribute("usuario", usuarioActualizado);
        request.setAttribute("usuariosSeguidos", usuariosSeguidos);
        request.setAttribute("institutos", institutos);
        request.setAttribute("cursos", cursos);
        request.setAttribute("estadoDb", estadoDb);

        request.getRequestDispatcher("/webUsuario/perfil.jsp").forward(request, response);
    }
}
