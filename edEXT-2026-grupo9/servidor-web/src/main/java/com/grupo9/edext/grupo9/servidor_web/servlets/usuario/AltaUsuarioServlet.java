package com.grupo9.edext.grupo9.servidor_web.servlets.usuario;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.miscelanea.Fabrica;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

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
        IServidorCentral servidorCentral = Fabrica.getInstance().getIServidorCentral();
        Set<DataInstituto> institutos = Collections.emptySet();
        Set<DataCurso> cursos = Collections.emptySet();

        try {
            if (servidorCentral != null) {
                institutos = servidorCentral.consultarTodosLosInstitutos();
                cursos = servidorCentral.consultarTodosLosCursos();
            }
        } catch (Exception e) {
            // Ignorar error de carga de menú
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

        LocalDate fechaNac = null;
        if (fechaNacStr != null && !fechaNacStr.trim().isEmpty()) {
            try {
                fechaNac = LocalDate.parse(fechaNacStr.trim());
            } catch (Exception e) {
                request.setAttribute("error", "El formato de fecha de nacimiento es inválido.");
                doGet(request, response);
                return;
            }
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
        } catch (Exception e) {
            // Si falla la subida de foto, no detener el registro
        }

        IServidorCentral servidorCentral = Fabrica.getInstance().getIServidorCentral();
        try {
            if ("Docente".equalsIgnoreCase(tipo)) {
                servidorCentral.registrarDocente(
                    nickname.trim(),
                    nombre.trim(),
                    apellido.trim(),
                    email.trim(),
                    fechaNac,
                    rutaImagen,
                    nombreInst.trim(),
                    password
                );
            } else {
                servidorCentral.registrarEstudiante(
                    nickname.trim(),
                    nombre.trim(),
                    apellido.trim(),
                    email.trim(),
                    fechaNac,
                    rutaImagen,
                    password
                );
            }

            response.sendRedirect(request.getContextPath() + "/login?mensaje=Usuario+registrado+con+exito.+Inicie+sesion.");
        } catch (ErrorRepetidos e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error al registrar usuario: " + e.getMessage());
            doGet(request, response);
        }
    }
}
