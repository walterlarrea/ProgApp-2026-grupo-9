package com.grupo9.edext.grupo9.servidor_web.servlets.usuario;

import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.CursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.DataCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.DataInstitutoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.InstitutoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataUsuarioDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.UsuarioWebService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet("/modificarUsuario")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 20
)
public class ModificarUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        DataUsuarioDTO usuarioLogueado = (session != null) ? (DataUsuarioDTO) session.getAttribute("usuarioLogueado") : null;

        if (usuarioLogueado == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        InstitutoWebService instPort = SoapClientFactory.getInstance().getInstitutoPort();
        CursoWebService cursoPort = SoapClientFactory.getInstance().getCursoPort();

        List<DataInstitutoDTO> institutos = Collections.emptyList();
        List<DataCursoDTO> cursos = Collections.emptyList();
        String estadoDb = "Conectado al Servidor Central";

        try {
            if (instPort != null) {
                institutos = instPort.consultarTodosLosInstitutos();
            }
            if (cursoPort != null) {
                cursos = cursoPort.consultarTodosLosCursos();
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
        DataUsuarioDTO usuarioLogueado = (session != null) ? (DataUsuarioDTO) session.getAttribute("usuarioLogueado") : null;

        if (usuarioLogueado == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String nombre = obtenerParametro(request, "nombre");
        String apellido = obtenerParametro(request, "apellido");
        String fechaNacStr = obtenerParametro(request, "fechaNac");
        String passActual = obtenerParametro(request, "passActual");
        String passNueva = obtenerParametro(request, "passNueva");
        String passNuevaConfirm = obtenerParametro(request, "passNuevaConfirm");

        if (nombre == null || nombre.trim().isEmpty()) {
            nombre = usuarioLogueado.getNombre();
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            apellido = usuarioLogueado.getApellido();
        }

        if (passNueva != null && !passNueva.trim().isEmpty()) {
            if (!passNueva.equals(passNuevaConfirm)) {
                request.setAttribute("error", "La nueva contraseña y su confirmación no coinciden.");
                doGet(request, response);
                return;
            }
        }

        if (fechaNacStr == null || fechaNacStr.trim().isEmpty()) {
            fechaNacStr = usuarioLogueado.getFechaNac();
        }

        String rutaImagen = usuarioLogueado.getImagen();
        try {
            Part filePart = request.getPart("imagenArchivo");
            if (filePart != null && filePart.getSize() > 0) {
                String submittedName = filePart.getSubmittedFileName();
                if (submittedName != null && !submittedName.trim().isEmpty()) {
                    String ext = "";
                    int dotIdx = submittedName.lastIndexOf('.');
                    if (dotIdx >= 0) {
                        ext = submittedName.substring(dotIdx);
                    }
                    String fileName = "avatar_" + usuarioLogueado.getNickname() + "_" + System.currentTimeMillis() + ext;
                    String uploadDirPath = getServletContext().getRealPath("/uploads/usuarios");
                    File uploadDir = new File(uploadDirPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }
                    String fullPath = uploadDirPath + File.separator + fileName;
                    filePart.write(fullPath);
                    rutaImagen = fullPath;
                }
            }
        } catch (Exception ignored) {
        }

        UsuarioWebService usuarioPort = SoapClientFactory.getInstance().getUsuarioPort();
        try {
            if (usuarioPort == null) {
                throw new ServletException("Servicio de usuarios no disponible");
            }

            usuarioPort.modificarUsuario(
                usuarioLogueado.getNickname(),
                nombre != null ? nombre.trim() : "",
                apellido != null ? apellido.trim() : "",
                fechaNacStr != null ? fechaNacStr.trim() : null,
                rutaImagen != null ? rutaImagen.trim() : "",
                passActual,
                passNueva
            );

            DataUsuarioDTO usuarioActualizado = usuarioPort.consultarUsuario(usuarioLogueado.getNickname());
            session.setAttribute("usuarioLogueado", usuarioActualizado);

            response.sendRedirect(request.getContextPath() + "/perfil?mensaje=Perfil+actualizado+correctamente");
        } catch (Exception e) {
            request.setAttribute("error", "Error al actualizar perfil: " + e.getMessage());
            doGet(request, response);
        }
    }

    private String obtenerParametro(HttpServletRequest request, String nombre) {
        String valor = request.getParameter(nombre);
        if (valor != null) {
            return valor;
        }
        try {
            Part part = request.getPart(nombre);
            if (part != null) {
                try (java.io.InputStream is = part.getInputStream()) {
                    return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8).trim();
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
