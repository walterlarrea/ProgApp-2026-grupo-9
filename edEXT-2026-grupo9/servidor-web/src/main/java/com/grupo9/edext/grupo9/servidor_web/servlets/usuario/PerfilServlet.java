package com.grupo9.edext.grupo9.servidor_web.servlets.usuario;

import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.CursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.DataCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.DataInstitutoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.InstitutoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataEstudianteDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataUsuarioDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.UsuarioWebService;

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

@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        DataUsuarioDTO usuarioLogueado = (session != null) ? (DataUsuarioDTO) session.getAttribute("usuarioLogueado") : null;

        if (usuarioLogueado == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        UsuarioWebService usuarioPort = SoapClientFactory.getInstance().getUsuarioPort();
        InstitutoWebService instPort = SoapClientFactory.getInstance().getInstitutoPort();
        CursoWebService cursoPort = SoapClientFactory.getInstance().getCursoPort();

        List<DataInstitutoDTO> institutos = Collections.emptyList();
        List<DataCursoDTO> cursos = Collections.emptyList();
        String estadoDb = "Conectado al Servidor Central";
        DataUsuarioDTO usuarioActualizado = usuarioLogueado;
        List<DataUsuarioDTO> usuariosSeguidos = new ArrayList<>();

        try {
            if (usuarioPort != null) {
                usuarioActualizado = usuarioPort.consultarUsuario(usuarioLogueado.getNickname());
                session.setAttribute("usuarioLogueado", usuarioActualizado);

                List<String> seguidosNicks = usuarioPort.obtenerSeguidos(usuarioLogueado.getNickname());
                if (seguidosNicks != null) {
                    for (String segNick : seguidosNicks) {
                        try {
                            DataUsuarioDTO segUser = usuarioPort.consultarUsuario(segNick);
                            if (segUser != null) {
                                usuariosSeguidos.add(segUser);
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
            if (instPort != null) {
                institutos = instPort.consultarTodosLosInstitutos();
            }
            if (cursoPort != null) {
                cursos = cursoPort.consultarTodosLosCursos();
            }
        } catch (Exception e) {
            estadoDb = "Servidor Central activo (" + e.getMessage() + ")";
        }

        request.setAttribute("usuario", usuarioActualizado);
        request.setAttribute("usuariosSeguidos", usuariosSeguidos);
        request.setAttribute("institutos", institutos);
        request.setAttribute("cursos", cursos);
        request.setAttribute("estadoDb", estadoDb);
        request.setAttribute("inscripcionesProgramas", usuarioActualizado instanceof DataEstudianteDTO estudiante
                ? estudiante.getInscripcionesProgramas()
                : Collections.emptyList());

        request.getRequestDispatcher("/webUsuario/perfil.jsp").forward(request, response);
    }
}
