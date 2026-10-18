package com.grupo9.edext.grupo9.servidor_web.servlets.usuario;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.miscelanea.Fabrica;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEstudiante;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PerfilServletTest {

    private PerfilServlet servlet;
    private IServidorCentral servidor;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    public void setUp() {
        servlet = new PerfilServlet();
        servidor = Fabrica.getInstance().getIServidorCentral();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getContextPath()).thenReturn("/edext");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    @Test
    public void testDoGetSinSesion() throws Exception {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/edext/login");
    }

    @Test
    public void testDoGetEstudianteConInscripciones() throws Exception {
        String sufijo = Long.toString(System.currentTimeMillis());
        String nick = "estPerfil_" + sufijo;
        String prog = "ProgPerfil_" + sufijo;

        servidor.registrarEstudiante(nick, "Est", "Perfil", nick + "@test.com", LocalDate.of(2000, 1, 1), null);
        servidor.guardarProgramaDeFormacion(new DataProgramaFormacion(
                prog, "Desc", new HashSet<>(), LocalDate.now(), LocalDate.now().plusMonths(2), LocalDate.now(), null));
        servidor.inscribirseAPrograma(LocalDate.now(), nick, prog);

        DataUsuario usuario = servidor.consultarUsuario(nick);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("usuarioLogueado")).thenReturn(usuario);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("usuario"), any());
        verify(request).setAttribute(eq("inscripcionesProgramas"), argThat(arg -> {
            if (arg instanceof Collection<?> col) {
                return !col.isEmpty();
            }
            return false;
        }));
        verify(request).getRequestDispatcher("/webUsuario/perfil.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoGetDocenteInscripcionesVacias() throws Exception {
        String sufijo = Long.toString(System.currentTimeMillis());
        String instNombre = "InstPerfilDoc_" + sufijo;
        servidor.guardarInstituto(new com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto(instNombre));

        String nick = "docPerfil_" + sufijo;
        servidor.registrarDocente(nick, "Doc", "Perfil", nick + "@test.com", LocalDate.of(1980, 1, 1), null, instNombre);

        DataUsuario usuario = servidor.consultarUsuario(nick);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("usuarioLogueado")).thenReturn(usuario);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("inscripcionesProgramas"), eq(java.util.Collections.emptySet()));
        verify(request).getRequestDispatcher("/webUsuario/perfil.jsp");
        verify(dispatcher).forward(request, response);
    }
}
