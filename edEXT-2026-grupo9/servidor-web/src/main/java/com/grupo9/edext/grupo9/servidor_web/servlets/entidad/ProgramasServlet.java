package com.grupo9.edext.grupo9.servidor_web.servlets.entidad;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.miscelanea.Fabrica;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEstudiante;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.*;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
@WebServlet(urlPatterns = { "/programas", "/programa" })
public class ProgramasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        IServidorCentral servidorCentral = Fabrica.getInstance().getIServidorCentral();
        if ("/programas".equals(request.getServletPath())) {
            if ("alta".equals(request.getParameter("accion"))) {
                if (!esDocente(request)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo docentes pueden crear programas.");
                    return;
                }
                request.setAttribute("cursos", safeCourses(servidorCentral));
                request.getRequestDispatcher("/webPrograma/alta.jsp").forward(request, response);
                return;
            }

            List<DataProgramaFormacionDTO> programas = new ArrayList<>();
            try {
                ServidorCentralWebService_Service servicioWeb = new ServidorCentralWebService_Service();
                ServidorCentralWebService port = servicioWeb.getServidorCentralWebServicePort();
                programas = port.consultarTodosLosProgramas();
            } catch (Exception | NoClassDefFoundError | NoSuchFieldError e) {
                // Si el servicio web no está disponible durante tests o inicio
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
        DataProgramaFormacion programa = servidorCentral.traerPorNombreId(nombre);
        if (programa == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Programa no encontrado.");
            return;
        }

        HttpSession session = request.getSession(false);
        DataUsuario usuario = session == null ? null : (DataUsuario) session.getAttribute("usuarioLogueado");
        boolean inscripto = usuario instanceof DataEstudiante estudiante
                && estudiante.getInscripcionesProgramas().stream()
                        .anyMatch(inscripcion -> nombre.equals(inscripcion.getProgramaNombre()));
        request.setAttribute("programa", programa);
        request.setAttribute("inscripto", inscripto);
        request.setAttribute("puedeGestionar", usuario != null && "Docente".equals(usuario.getTipo()));
        request.setAttribute("cursosDisponibles", safeUnrelatedCourses(servidorCentral, nombre));
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
        IServidorCentral servidor = Fabrica.getInstance().getIServidorCentral();
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
        if (servidor.existeProgramaDeFormacion(nombre)) {
            request.setAttribute("error", "Ya existe un programa con ese nombre.");
            request.setAttribute("cursos", safeCourses(servidor));
            request.getRequestDispatcher("/webPrograma/alta.jsp").forward(request, response);
            return;
        }

        String imagen = guardarImagen(request.getPart("imagen"));
        DataProgramaFormacion nuevo = new DataProgramaFormacion(nombre, descripcion.trim(), new HashSet<>(),
                LocalDate.parse(fechaInicioTexto), LocalDate.parse(fechaFinTexto), LocalDate.now(), imagen);
        DataProgramaFormacion guardado = servidor.guardarProgramaDeFormacion(nuevo);
        if (guardado == null) {
            throw new ServletException("No se pudo guardar el programa.");
        }
        String[] cursos = request.getParameterValues("cursos");
        if (cursos != null) {
            for (String curso : cursos) {
                servidor.agregarCursoAProgramaDeFormacion(nombre, curso);
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
        DataUsuario usuario = session == null ? null : (DataUsuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        IServidorCentral servidor = Fabrica.getInstance().getIServidorCentral();
        try {
            if ("inscribir".equals(accion)) {
                if (!(usuario instanceof DataEstudiante)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo estudiantes pueden inscribirse.");
                    return;
                }
                servidor.inscribirEstudianteEnPrograma(LocalDate.now(), usuario.getNickname(), nombre);
            } else if ("agregarCurso".equals(accion)) {
                if (!"Docente".equals(usuario.getTipo())) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo docentes pueden gestionar cursos.");
                    return;
                }
                String nombreCurso = request.getParameter("curso");
                if (nombreCurso == null || nombreCurso.isBlank()
                        || !Boolean.TRUE.equals(servidor.agregarCursoAProgramaDeFormacion(nombre, nombreCurso))) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No se pudo asociar el curso.");
                    return;
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
                return;
            }
            response.sendRedirect(
                    destino + "&mensaje=" + URLEncoder.encode("Cambios guardados.", StandardCharsets.UTF_8));
        } catch (ErrorRepetidos | ErrorNoExiste e) {
            response.sendRedirect(destino + "&error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
        }
    }

    private Set<DataCurso> safeCourses(IServidorCentral servidor) {
        Set<DataCurso> cursos = servidor.consultarTodosLosCursos();
        return cursos == null ? Collections.emptySet() : cursos;
    }

    private boolean esDocente(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        DataUsuario usuario = session == null ? null : (DataUsuario) session.getAttribute("usuarioLogueado");
        return usuario != null && "Docente".equals(usuario.getTipo());
    }

    private Set<DataCurso> safeUnrelatedCourses(IServidorCentral servidor, String nombrePrograma) {
        Set<DataCurso> cursos = servidor.cursosNoRelacionadosConUnProgDeFormacion(nombrePrograma);
        return cursos == null ? Collections.emptySet() : cursos;
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
