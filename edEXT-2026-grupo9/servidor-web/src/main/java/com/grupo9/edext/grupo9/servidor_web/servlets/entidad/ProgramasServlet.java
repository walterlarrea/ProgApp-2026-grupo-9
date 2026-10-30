package com.grupo9.edext.grupo9.servidor_web.servlets.entidad;

import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.CursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.DataCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.programa_de_formacion.DataProgramaFormacionDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.programa_de_formacion.ErrorNoExiste_Exception;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.programa_de_formacion.ErrorRepetidos_Exception;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.programa_de_formacion.ProgramaDeFormacionWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataEstudianteDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataUsuarioDTO;

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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
@WebServlet(urlPatterns = { "/programas", "/programa" })
public class ProgramasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProgramaDeFormacionWebService progPort = SoapClientFactory.getInstance().getProgramaPort();
        CursoWebService cursoPort = SoapClientFactory.getInstance().getCursoPort();

        if ("/programas".equals(request.getServletPath())) {
            if ("alta".equals(request.getParameter("accion"))) {
                if (!esDocente(request)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo docentes pueden crear programas.");
                    return;
                }
                request.setAttribute("cursos", safeCourses(cursoPort));
                request.getRequestDispatcher("/webPrograma/alta.jsp").forward(request, response);
                return;
            }

            List<DataProgramaFormacionDTO> programas = new ArrayList<>();
            try {
                if (progPort != null) {
                    programas = progPort.consultarTodosLosProgramas();
                }
            } catch (Exception ignored) {
            }

            request.setAttribute("programas", programas);
            request.getRequestDispatcher("/webPrograma/lista.jsp").forward(request, response);
            return;
        }

        String nombre = request.getParameter("nombre");
        if (nombre == null || nombre.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el nombre del programa.");
            return;
        }
        DataProgramaFormacionDTO programa = null;
        if (progPort != null) {
            programa = progPort.traerPorNombreId(nombre);
        }
        if (programa == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Programa no encontrado.");
            return;
        }

        HttpSession session = request.getSession(false);
        DataUsuarioDTO usuario = session == null ? null : (DataUsuarioDTO) session.getAttribute("usuarioLogueado");
        boolean inscripto = false;
        if (usuario instanceof DataEstudianteDTO estudiante && estudiante.getInscripcionesProgramas() != null) {
            inscripto = estudiante.getInscripcionesProgramas().stream()
                    .anyMatch(insc -> nombre.equals(insc.getProgramaNombre()));
        }

        request.setAttribute("programa", programa);
        request.setAttribute("inscripto", inscripto);
        request.setAttribute("puedeGestionar", usuario != null && "Docente".equalsIgnoreCase(usuario.getTipo()));
        request.setAttribute("cursosDisponibles", safeUnrelatedCourses(cursoPort, nombre));
        request.getRequestDispatcher("/webPrograma/detalle.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("/programas".equals(request.getServletPath())) {
            crearPrograma(request, response);
        } else {
            procesarPrograma(request, response);
        }
    }

    private void crearPrograma(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!esDocente(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo docentes pueden crear programas.");
            return;
        }
        ProgramaDeFormacionWebService progPort = SoapClientFactory.getInstance().getProgramaPort();
        CursoWebService cursoPort = SoapClientFactory.getInstance().getCursoPort();

        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String fechaInicioTexto = request.getParameter("fechaInicio");
        String fechaFinTexto = request.getParameter("fechaFin");

        if (nombre == null || nombre.isBlank() || descripcion == null || descripcion.isBlank()
                || fechaInicioTexto == null || fechaFinTexto == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Completa los datos obligatorios.");
            return;
        }
        nombre = nombre.trim();
        if (progPort != null && progPort.existeProgramaDeFormacion(nombre)) {
            request.setAttribute("error", "Ya existe un programa con ese nombre.");
            request.setAttribute("cursos", safeCourses(cursoPort));
            request.getRequestDispatcher("/webPrograma/alta.jsp").forward(request, response);
            return;
        }

        String imagen = guardarImagen(request.getPart("imagen"));
        DataProgramaFormacionDTO guardado = null;
        if (progPort != null) {
            guardado = progPort.guardarProgramaDeFormacion(nombre, descripcion.trim(), fechaInicioTexto, fechaFinTexto, imagen);
        }
        if (guardado == null) {
            throw new ServletException("No se pudo guardar el programa.");
        }

        String[] cursos = request.getParameterValues("cursos");
        if (cursos != null && progPort != null) {
            for (String curso : cursos) {
                progPort.agregarCursoAProgramaDeFormacion(nombre, curso);
            }
        }
        response.sendRedirect(request.getContextPath() + "/programa?nombre="
                + URLEncoder.encode(nombre, StandardCharsets.UTF_8));
    }

    private void procesarPrograma(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String accion = request.getParameter("accion");
        String destino = request.getContextPath() + "/programa?nombre="
                + URLEncoder.encode(nombre == null ? "" : nombre, StandardCharsets.UTF_8);
        HttpSession session = request.getSession(false);
        DataUsuarioDTO usuario = session == null ? null : (DataUsuarioDTO) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        ProgramaDeFormacionWebService progPort = SoapClientFactory.getInstance().getProgramaPort();
        try {
            if ("inscribir".equals(accion)) {
                if (!(usuario instanceof DataEstudianteDTO)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo estudiantes pueden inscribirse.");
                    return;
                }
                if (progPort != null) {
                    progPort.inscribirEstudianteEnPrograma(LocalDate.now().toString(), usuario.getNickname(), nombre);
                }
            } else if ("agregarCurso".equals(accion)) {
                if (!"Docente".equalsIgnoreCase(usuario.getTipo())) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo docentes pueden gestionar cursos.");
                    return;
                }
                String nombreCurso = request.getParameter("curso");
                if (nombreCurso == null || nombreCurso.isBlank()
                        || progPort == null
                        || !progPort.agregarCursoAProgramaDeFormacion(nombre, nombreCurso)) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No se pudo asociar el curso.");
                    return;
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
                return;
            }
            response.sendRedirect(
                    destino + "&mensaje=" + URLEncoder.encode("Cambios guardados.", StandardCharsets.UTF_8));
        } catch (ErrorRepetidos_Exception | ErrorNoExiste_Exception e) {
            response.sendRedirect(destino + "&error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
        }
    }

    private List<DataCursoDTO> safeCourses(CursoWebService cursoPort) {
        if (cursoPort == null) return Collections.emptyList();
        try {
            List<DataCursoDTO> cursos = cursoPort.consultarTodosLosCursos();
            return cursos == null ? Collections.emptyList() : cursos;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private boolean esDocente(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        DataUsuarioDTO usuario = session == null ? null : (DataUsuarioDTO) session.getAttribute("usuarioLogueado");
        return usuario != null && "Docente".equalsIgnoreCase(usuario.getTipo());
    }

    private List<DataCursoDTO> safeUnrelatedCourses(CursoWebService cursoPort, String nombrePrograma) {
        if (cursoPort == null) return Collections.emptyList();
        try {
            List<DataCursoDTO> cursos = cursoPort.cursosNoRelacionadosConUnProgDeFormacion(nombrePrograma);
            return cursos == null ? Collections.emptyList() : cursos;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private String guardarImagen(Part imagen) throws IOException {
        if (imagen == null || imagen.getSize() == 0) {
            return null;
        }
        String extension = "";
        String nombreOriginal = imagen.getSubmittedFileName();
        if (nombreOriginal != null && nombreOriginal.lastIndexOf('.') >= 0) {
            String candidata = nombreOriginal.substring(nombreOriginal.lastIndexOf('.') + 1);
            if (candidata.matches("[A-Za-z0-9]{1,8}")) {
                extension = "." + candidata.toLowerCase(java.util.Locale.ROOT);
            }
        }
        String nombreArchivo = UUID.randomUUID() + extension;
        String directorio = getServletContext().getRealPath("/uploads/programa");
        if (directorio == null) {
            throw new IOException("No se pudo resolver el directorio de imágenes.");
        }
        File carpeta = new File(directorio);
        if (!carpeta.exists() && !carpeta.mkdirs()) {
            throw new IOException("No se pudo crear el directorio de imágenes.");
        }
        imagen.write(directorio + File.separator + nombreArchivo);
        return "uploads/programa/" + nombreArchivo;
    }
}
