package com.grupo9.edext.grupo9.servidor_web.servlets.usuario;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;

@WebServlet("/usuario")
public class UsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nickname = request.getParameter("nombre");
        if (nickname == null || nickname.isBlank()) {
            nickname = request.getParameter("nickname");
        }
        if (nickname == null || nickname.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el nickname del usuario.");
            return;
        }

        try {
            IServidorCentral servidorCentral = Fabrica.getInstance().getIServidorCentral();
            DataUsuario usuarioConsultado = servidorCentral.consultarUsuario(nickname.trim());
            HttpSession session = request.getSession(false);
            DataUsuario usuarioLogueado = (session != null) ? (DataUsuario) session.getAttribute("usuarioLogueado") : null;
            boolean propio = usuarioLogueado != null && nickname.trim().equals(usuarioLogueado.getNickname());
            boolean siguiendo = usuarioLogueado != null && !propio
                    && servidorCentral.estaSiguiendoUsuario(usuarioLogueado.getNickname(), nickname.trim());

            request.setAttribute("usuario", usuarioConsultado);
            request.setAttribute("propio", propio);
            request.setAttribute("siguiendo", siguiendo);
            request.setAttribute("loSigue", siguiendo);

            Set<DataInstituto> institutos = servidorCentral.consultarTodosLosInstitutos();
            Set<DataCurso> cursos = servidorCentral.consultarTodosLosCursos();
            request.setAttribute("institutos", institutos != null ? institutos : Collections.emptySet());
            request.setAttribute("cursos", cursos != null ? cursos : Collections.emptySet());

            request.getRequestDispatcher("/webUsuario/verInfo-usuario.jsp").forward(request, response);
        } catch (ErrorNoExiste e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ServletException("No se pudo consultar el usuario.", e);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        DataUsuario seguidor = (session == null) ? null : (DataUsuario) session.getAttribute("usuarioLogueado");
        if (seguidor == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String seguido = request.getParameter("nickname");
        String accion = request.getParameter("accion");
        String destino = request.getContextPath() + "/usuario?nombre="
                + URLEncoder.encode(seguido == null ? "" : seguido, StandardCharsets.UTF_8);
        if (seguido == null || seguido.isBlank() || seguido.equals(seguidor.getNickname())) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No se puede seguir a este usuario.");
            return;
        }

        try {
            IServidorCentral servidor = Fabrica.getInstance().getIServidorCentral();
            if ("seguir".equals(accion)) {
                servidor.seguirUsuario(seguidor.getNickname(), seguido);
            } else if ("dejar".equals(accion)) {
                servidor.dejarDeSeguirUsuario(seguidor.getNickname(), seguido);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
                return;
            }
            response.sendRedirect(destino + "&mensaje=" + URLEncoder.encode("Seguimiento actualizado.", StandardCharsets.UTF_8));
        } catch (ErrorRepetidos | ErrorNoExiste e) {
            response.sendRedirect(destino + "&error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new ServletException("No se pudo actualizar el seguimiento.", e);
        }
    }
}
