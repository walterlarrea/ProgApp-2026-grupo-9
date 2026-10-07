package com.grupo9.edext.grupo9.servidor_web.servlets;

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
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

@WebServlet("/modificarUsuario")
public class ModificarUsuarioServlet extends HttpServlet {

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

        try {
            if (servidorCentral != null) {
                institutos = servidorCentral.consultarTodosLosInstitutos();
                cursos = servidorCentral.consultarTodosLosCursos();
            }
        } catch (Exception e) {
            estadoDb = "Servidor Central activo (" + e.getMessage() + ")";
        }

        request.setAttribute("usuario", usuarioLogueado);
        request.setAttribute("institutos", institutos);
        request.setAttribute("cursos", cursos);
        request.setAttribute("estadoDb", estadoDb);

        request.getRequestDispatcher("/webUsuario/mod-usuario.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        DataUsuario usuarioLogueado = (session != null) ? (DataUsuario) session.getAttribute("usuarioLogueado") : null;

        if (usuarioLogueado == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String fechaNacStr = request.getParameter("fechaNac");
        String imagen = request.getParameter("imagen");
        String passActual = request.getParameter("passActual");
        String passNueva = request.getParameter("passNueva");
        String passNuevaConfirm = request.getParameter("passNuevaConfirm");

        if (passNueva != null && !passNueva.trim().isEmpty()) {
            if (!passNueva.equals(passNuevaConfirm)) {
                request.setAttribute("error", "La nueva contraseña y su confirmación no coinciden.");
                doGet(request, response);
                return;
            }
        }

        LocalDate fechaNac = usuarioLogueado.getFechaNac();
        if (fechaNacStr != null && !fechaNacStr.trim().isEmpty()) {
            try {
                fechaNac = LocalDate.parse(fechaNacStr.trim());
            } catch (Exception e) {
                // conservar fecha anterior si formato inválido
            }
        }

        IServidorCentral servidorCentral = Fabrica.getInstance().getIServidorCentral();
        try {
            servidorCentral.modificarUsuario(
                usuarioLogueado.getNickname(),
                nombre != null ? nombre.trim() : "",
                apellido != null ? apellido.trim() : "",
                fechaNac,
                imagen != null ? imagen.trim() : "",
                passActual,
                passNueva
            );

            DataUsuario usuarioActualizado = servidorCentral.consultarUsuario(usuarioLogueado.getNickname());
            session.setAttribute("usuarioLogueado", usuarioActualizado);

            response.sendRedirect(request.getContextPath() + "/perfil?mensaje=Perfil+actualizado+correctamente");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error al actualizar perfil: " + e.getMessage());
            doGet(request, response);
        }
    }
}
