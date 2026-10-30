package com.grupo9.edext.grupo9.servidor_web.servlets.usuario;

import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.CursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.DataCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.DataInstitutoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.InstitutoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataUsuarioDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.ErrorNoExiste_Exception;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.ErrorRepetidos_Exception;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.UsuarioWebService;

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
import java.util.List;

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

        UsuarioWebService usuarioPort = SoapClientFactory.getInstance().getUsuarioPort();
        InstitutoWebService instPort = SoapClientFactory.getInstance().getInstitutoPort();
        CursoWebService cursoPort = SoapClientFactory.getInstance().getCursoPort();

        try {
            if (usuarioPort == null) {
                throw new ServletException("Servicio de usuarios no disponible");
            }
            DataUsuarioDTO usuarioConsultado = usuarioPort.consultarUsuario(nickname.trim());
            HttpSession session = request.getSession(false);
            DataUsuarioDTO usuarioLogueado = (session != null) ? (DataUsuarioDTO) session.getAttribute("usuarioLogueado") : null;
            boolean propio = usuarioLogueado != null && nickname.trim().equals(usuarioLogueado.getNickname());
            boolean siguiendo = false;
            if (usuarioLogueado != null && !propio) {
                try {
                    siguiendo = usuarioPort.estaSiguiendoUsuario(usuarioLogueado.getNickname(), nickname.trim());
                } catch (Exception ignored) {
                }
            }

            request.setAttribute("usuario", usuarioConsultado);
            request.setAttribute("propio", propio);
            request.setAttribute("siguiendo", siguiendo);
            request.setAttribute("loSigue", siguiendo);

            List<DataInstitutoDTO> institutos = Collections.emptyList();
            List<DataCursoDTO> cursos = Collections.emptyList();
            if (instPort != null) {
                institutos = instPort.consultarTodosLosInstitutos();
            }
            if (cursoPort != null) {
                cursos = cursoPort.consultarTodosLosCursos();
            }
            request.setAttribute("institutos", institutos != null ? institutos : Collections.emptyList());
            request.setAttribute("cursos", cursos != null ? cursos : Collections.emptyList());

            request.getRequestDispatcher("/webUsuario/verInfo-usuario.jsp").forward(request, response);
        } catch (ErrorNoExiste_Exception e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ServletException("No se pudo consultar el usuario.", e);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        DataUsuarioDTO seguidor = (session == null) ? null : (DataUsuarioDTO) session.getAttribute("usuarioLogueado");
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

        UsuarioWebService usuarioPort = SoapClientFactory.getInstance().getUsuarioPort();
        try {
            if (usuarioPort == null) {
                throw new ServletException("Servicio de usuarios no disponible");
            }
            if ("seguir".equals(accion)) {
                usuarioPort.seguirUsuario(seguidor.getNickname(), seguido);
            } else if ("dejar".equals(accion)) {
                usuarioPort.dejarDeSeguirUsuario(seguidor.getNickname(), seguido);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
                return;
            }
            response.sendRedirect(destino + "&mensaje=" + URLEncoder.encode("Seguimiento actualizado.", StandardCharsets.UTF_8));
        } catch (ErrorRepetidos_Exception | ErrorNoExiste_Exception e) {
            response.sendRedirect(destino + "&error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new ServletException("No se pudo actualizar el seguimiento.", e);
        }
    }
}
