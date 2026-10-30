package com.grupo9.edext.grupo9.servidor_web.servlets.usuario;

import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.CursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.DataCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.DataInstitutoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.InstitutoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.ErrorRepetidos_Exception;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.UsuarioWebService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet("/alta-usuario")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 20
)
public class AltaUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        InstitutoWebService instPort = SoapClientFactory.getInstance().getInstitutoPort();
        CursoWebService cursoPort = SoapClientFactory.getInstance().getCursoPort();

        List<DataInstitutoDTO> institutos = Collections.emptyList();
        List<DataCursoDTO> cursos = Collections.emptyList();

        try {
            if (instPort != null) {
                institutos = instPort.consultarTodosLosInstitutos();
            }
            if (cursoPort != null) {
                cursos = cursoPort.consultarTodosLosCursos();
            }
        } catch (Exception ignored) {
        }

        request.setAttribute("institutos", institutos);
        request.setAttribute("cursos", cursos);
        request.getRequestDispatcher("/webUsuario/alta-usuario.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String tipo = request.getParameter("tipo");
        String nickname = request.getParameter("nickname");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fechaNacStr = request.getParameter("fechaNac");
        String nombreInst = request.getParameter("instituto");

        if (nickname == null || nickname.trim().isEmpty() ||
            nombre == null || nombre.trim().isEmpty() ||
            apellido == null || apellido.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            confirmPassword == null || confirmPassword.trim().isEmpty()) {
            request.setAttribute("error", "Todos los campos obligatorios deben ser completados.");
            doGet(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "La contraseña y su confirmación no coinciden.");
            doGet(request, response);
            return;
        }

        if ("Docente".equalsIgnoreCase(tipo) && (nombreInst == null || nombreInst.trim().isEmpty())) {
            request.setAttribute("error", "Debe seleccionar un instituto para el docente.");
            doGet(request, response);
            return;
        }

        String rutaImagen = "";
        try {
            Part filePart = request.getPart("imagen");
            if (filePart != null && filePart.getSize() > 0) {
                String submittedName = filePart.getSubmittedFileName();
                if (submittedName != null && !submittedName.trim().isEmpty()) {
                    String ext = "";
                    int dotIdx = submittedName.lastIndexOf('.');
                    if (dotIdx >= 0) {
                        ext = submittedName.substring(dotIdx);
                    }
                    String fileName = "avatar_" + nickname.trim() + "_" + System.currentTimeMillis() + ext;
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
            if ("Docente".equalsIgnoreCase(tipo)) {
                usuarioPort.registrarDocente(
                    nickname.trim(),
                    nombre.trim(),
                    apellido.trim(),
                    email.trim(),
                    fechaNacStr != null ? fechaNacStr.trim() : null,
                    rutaImagen,
                    nombreInst.trim(),
                    password
                );
            } else {
                usuarioPort.registrarEstudiante(
                    nickname.trim(),
                    nombre.trim(),
                    apellido.trim(),
                    email.trim(),
                    fechaNacStr != null ? fechaNacStr.trim() : null,
                    rutaImagen,
                    password
                );
            }

            response.sendRedirect(request.getContextPath() + "/login?mensaje=Usuario+registrado+con+exito.+Inicie+sesion.");
        } catch (ErrorRepetidos_Exception e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error al registrar usuario: " + e.getMessage());
            doGet(request, response);
        }
    }
}
