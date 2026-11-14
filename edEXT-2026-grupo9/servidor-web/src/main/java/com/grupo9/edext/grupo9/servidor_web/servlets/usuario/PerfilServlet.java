package com.grupo9.edext.grupo9.servidor_web.servlets.usuario;

import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.CursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.DataCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataEdicionCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.EdicionDeCursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.DataInstitutoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.InstitutoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataDocenteDTO;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        EdicionDeCursoWebService edicionPort = SoapClientFactory.getInstance().getEdicionCursoPort();

        List<DataInstitutoDTO> institutos = Collections.emptyList();
        List<DataCursoDTO> cursos = Collections.emptyList();
        String estadoDb = "Conectado al Servidor Central";
        DataUsuarioDTO usuarioActualizado = usuarioLogueado;
        List<DataUsuarioDTO> usuariosSeguidos = new ArrayList<>();
        List<DataUsuarioDTO> usuariosSeguidores = new ArrayList<>();
        Set<String> seguidosNicks = new HashSet<>();
        List<DataEdicionCursoDTO> listaEdiciones = new ArrayList<>();
        List<DataCursoDTO> listaCursosDocente = new ArrayList<>();

        try {
            if (usuarioPort != null) {
                usuarioActualizado = usuarioPort.consultarUsuario(usuarioLogueado.getNickname());
                session.setAttribute("usuarioLogueado", usuarioActualizado);

                // 1. Usuarios que sigo
                List<String> seguidosObtenidos = usuarioPort.obtenerSeguidos(usuarioActualizado.getNickname());
                if (seguidosObtenidos != null) {
                    seguidosNicks.addAll(seguidosObtenidos);
                    for (String segNick : seguidosObtenidos) {
                        try {
                            DataUsuarioDTO segUser = usuarioPort.consultarUsuario(segNick);
                            if (segUser != null) {
                                usuariosSeguidos.add(segUser);
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }

                // 2. Usuarios que me siguen
                List<String> seguidoresObtenidos = usuarioPort.obtenerSeguidores(usuarioActualizado.getNickname());
                if (seguidoresObtenidos != null) {
                    for (String segNick : seguidoresObtenidos) {
                        try {
                            DataUsuarioDTO segUser = usuarioPort.consultarUsuario(segNick);
                            if (segUser != null) {
                                usuariosSeguidores.add(segUser);
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }

                // 3. Cursos / Ediciones según tipo de usuario
                if (usuarioActualizado instanceof DataEstudianteDTO estudiante) {
                    List<String> edicionesInscriptas = estudiante.getEdicionesInscriptas();
                    if (edicionesInscriptas != null && edicionPort != null) {
                        for (String nomEdi : edicionesInscriptas) {
                            try {
                                DataEdicionCursoDTO edicion = edicionPort.consultarUnaEdicionCurso(nomEdi);
                                if (edicion != null) {
                                    listaEdiciones.add(edicion);
                                }
                            } catch (Exception ignored) {
                            }
                        }
                    }
                } else if (usuarioActualizado instanceof DataDocenteDTO docente) {
                    List<String> edicionesDocente = docente.getEdiciones();
                    if (edicionesDocente != null && edicionPort != null) {
                        for (String nomEdi : edicionesDocente) {
                            try {
                                DataEdicionCursoDTO edicion = edicionPort.consultarUnaEdicionCurso(nomEdi);
                                if (edicion != null) {
                                    listaEdiciones.add(edicion);
                                }
                            } catch (Exception ignored) {
                            }
                        }
                    }
                    List<String> cursosDocente = docente.getCursos();
                    if (cursosDocente != null && cursoPort != null) {
                        for (String nomCur : cursosDocente) {
                            try {
                                DataCursoDTO curso = cursoPort.buscarCurso(nomCur);
                                if (curso != null) {
                                    listaCursosDocente.add(curso);
                                }
                            } catch (Exception ignored) {
                            }
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
        request.setAttribute("usuariosSeguidores", usuariosSeguidores);
        request.setAttribute("seguidosNicks", seguidosNicks);
        request.setAttribute("listaEdiciones", listaEdiciones);
        request.setAttribute("listaCursosDocente", listaCursosDocente);
        request.setAttribute("institutos", institutos);
        request.setAttribute("cursos", cursos);
        request.setAttribute("estadoDb", estadoDb);
        request.setAttribute("inscripcionesProgramas", usuarioActualizado instanceof DataEstudianteDTO estudiante
                ? estudiante.getInscripcionesProgramas()
                : Collections.emptyList());

        request.getRequestDispatcher("/webUsuario/perfil.jsp").forward(request, response);
    }
}
