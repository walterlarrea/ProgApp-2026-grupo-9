package com.grupo9.edext.grupo9.servidor_web.servlets.usuario;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.miscelanea.Fabrica;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEstudiante;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServletTest {

    private UsuarioServlet servlet;
    private IServidorCentral servidor;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    public void setUp() {
        servlet = new UsuarioServlet();
        servidor = Fabrica.getInstance().getIServidorCentral();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getContextPath()).thenReturn("/edext");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    @Test
    public void testDoGetFaltaNombre() throws Exception {
        when(request.getParameter("nombre")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el nickname del usuario.");
    }

    @Test
    public void testDoGetNoExiste() throws Exception {
        when(request.getParameter("nombre")).thenReturn("userInexistente_" + System.currentTimeMillis());

        servlet.doGet(request, response);

        verify(response).sendError(eq(HttpServletResponse.SC_NOT_FOUND), anyString());
    }

    @Test
    public void testDoGetPerfilPropio() throws Exception {
        String sufijo = Long.toString(System.currentTimeMillis());
        String userA = "usrPropio_" + sufijo;
        servidor.registrarEstudiante(userA, "User", "Propio", userA + "@test.com", LocalDate.of(2000, 1, 1), null);

        DataUsuario dataA = servidor.consultarUsuario(userA);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("usuarioLogueado")).thenReturn(dataA);

        when(request.getParameter("nombre")).thenReturn(userA);
        servlet.doGet(request, response);

        verify(request).setAttribute("propio", true);
        verify(request).setAttribute("siguiendo", false);
        verify(request).getRequestDispatcher("/webUsuario/verInfo-usuario.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoGetPerfilAjenoNoSiguiendo() throws Exception {
        String sufijo = Long.toString(System.currentTimeMillis());
        String userA = "usrA2_" + sufijo;
        String userB = "usrB2_" + sufijo;

        servidor.registrarEstudiante(userA, "User", "A2", userA + "@test.com", LocalDate.of(2000, 1, 1), null);
        servidor.registrarEstudiante(userB, "User", "B2", userB + "@test.com", LocalDate.of(2000, 2, 2), null);

        DataUsuario dataA = servidor.consultarUsuario(userA);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("usuarioLogueado")).thenReturn(dataA);

        when(request.getParameter("nombre")).thenReturn(userB);
        servlet.doGet(request, response);

        verify(request).setAttribute("propio", false);
        verify(request).setAttribute("siguiendo", false);
    }

    @Test
    public void testDoGetPerfilAjenoSiguiendo() throws Exception {
        String sufijo = Long.toString(System.currentTimeMillis());
        String userA = "usrA3_" + sufijo;
        String userB = "usrB3_" + sufijo;

        servidor.registrarEstudiante(userA, "User", "A3", userA + "@test.com", LocalDate.of(2000, 1, 1), null);
        servidor.registrarEstudiante(userB, "User", "B3", userB + "@test.com", LocalDate.of(2000, 2, 2), null);

        servidor.seguirUsuario(userA, userB);

        DataUsuario dataA = servidor.consultarUsuario(userA);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("usuarioLogueado")).thenReturn(dataA);

        when(request.getParameter("nombre")).thenReturn(userB);
        servlet.doGet(request, response);

        verify(request).setAttribute("propio", false);
        verify(request).setAttribute("siguiendo", true);
    }

    @Test
    public void testDoPostSeguimientoCompleto() throws Exception {
        String sufijo = Long.toString(System.currentTimeMillis());
        String seguidorNick = "segWeb_" + sufijo;
        String seguidoNick = "targetWeb_" + sufijo;

        servidor.registrarEstudiante(seguidorNick, "Seg", "Web", seguidorNick + "@test.com", LocalDate.of(2000, 1, 1), null);
        servidor.registrarEstudiante(seguidoNick, "Tgt", "Web", seguidoNick + "@test.com", LocalDate.of(2000, 2, 2), null);

        when(request.getParameter("nickname")).thenReturn(seguidoNick);

        // 1. Sin sesión -> redirect /login
        when(request.getSession(false)).thenReturn(null);
        when(request.getParameter("accion")).thenReturn("seguir");
        servlet.doPost(request, response);
        verify(response).sendRedirect("/edext/login");

        // 2. Con sesión pero intentar seguirse a sí mismo -> 400
        when(request.getSession(false)).thenReturn(session);
        DataUsuario seguidorData = servidor.consultarUsuario(seguidorNick);
        when(session.getAttribute("usuarioLogueado")).thenReturn(seguidorData);
        when(request.getParameter("nickname")).thenReturn(seguidorNick);
        servlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "No se puede seguir a este usuario.");

        // 3. Seguir usuario válido -> redirect con mensaje
        when(request.getParameter("nickname")).thenReturn(seguidoNick);
        when(request.getParameter("accion")).thenReturn("seguir");
        servlet.doPost(request, response);
        verify(response).sendRedirect(contains("mensaje=Seguimiento+actualizado."));
        assertTrue(servidor.estaSiguiendoUsuario(seguidorNick, seguidoNick));

        // 4. Seguir nuevamente (duplicado) -> redirect con error
        servlet.doPost(request, response);
        verify(response).sendRedirect(contains("error="));

        // 5. Dejar de seguir -> redirect con mensaje
        when(request.getParameter("accion")).thenReturn("dejar");
        servlet.doPost(request, response);
        assertFalse(servidor.estaSiguiendoUsuario(seguidorNick, seguidoNick));

        // 6. Acción inválida -> 400
        when(request.getParameter("accion")).thenReturn("otraCosa");
        servlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
    }
}
