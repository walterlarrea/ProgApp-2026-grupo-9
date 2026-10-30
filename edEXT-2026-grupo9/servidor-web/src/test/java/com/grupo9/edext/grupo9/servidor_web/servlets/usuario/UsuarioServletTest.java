package com.grupo9.edext.grupo9.servidor_web.servlets.usuario;

import com.grupo9.edext.grupo9.servidor_web.soap.SoapClientFactory;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.CursoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.instituto.InstitutoWebService;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataUsuarioDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.ErrorNoExiste_Exception;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.ErrorRepetidos_Exception;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.UsuarioWebService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServletTest {

    private UsuarioServlet servlet;
    private UsuarioWebService usuarioPort;
    private InstitutoWebService instPort;
    private CursoWebService cursoPort;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    public void setUp() {
        servlet = new UsuarioServlet();
        usuarioPort = mock(UsuarioWebService.class);
        instPort = mock(InstitutoWebService.class);
        cursoPort = mock(CursoWebService.class);

        SoapClientFactory.getInstance().setUsuarioPort(usuarioPort);
        SoapClientFactory.getInstance().setInstitutoPort(instPort);
        SoapClientFactory.getInstance().setCursoPort(cursoPort);

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
        when(request.getParameter("nickname")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el nickname del usuario.");
    }

    @Test
    public void testDoGetNoExiste() throws Exception {
        when(request.getParameter("nombre")).thenReturn("userInexistente");
        when(usuarioPort.consultarUsuario("userInexistente")).thenThrow(new ErrorNoExiste_Exception("No existe", null));

        servlet.doGet(request, response);

        verify(response).sendError(eq(HttpServletResponse.SC_NOT_FOUND), anyString());
    }

    @Test
    public void testDoGetPerfilPropio() throws Exception {
        String userA = "usrPropio";
        DataUsuarioDTO dataA = new DataUsuarioDTO();
        dataA.setNickname(userA);

        when(usuarioPort.consultarUsuario(userA)).thenReturn(dataA);
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
        String userA = "usrA2";
        String userB = "usrB2";
        DataUsuarioDTO dataA = new DataUsuarioDTO();
        dataA.setNickname(userA);
        DataUsuarioDTO dataB = new DataUsuarioDTO();
        dataB.setNickname(userB);

        when(usuarioPort.consultarUsuario(userB)).thenReturn(dataB);
        when(usuarioPort.estaSiguiendoUsuario(userA, userB)).thenReturn(false);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("usuarioLogueado")).thenReturn(dataA);

        when(request.getParameter("nombre")).thenReturn(userB);
        servlet.doGet(request, response);

        verify(request).setAttribute("propio", false);
        verify(request).setAttribute("siguiendo", false);
    }

    @Test
    public void testDoGetPerfilAjenoSiguiendo() throws Exception {
        String userA = "usrA3";
        String userB = "usrB3";
        DataUsuarioDTO dataA = new DataUsuarioDTO();
        dataA.setNickname(userA);
        DataUsuarioDTO dataB = new DataUsuarioDTO();
        dataB.setNickname(userB);

        when(usuarioPort.consultarUsuario(userB)).thenReturn(dataB);
        when(usuarioPort.estaSiguiendoUsuario(userA, userB)).thenReturn(true);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("usuarioLogueado")).thenReturn(dataA);

        when(request.getParameter("nombre")).thenReturn(userB);
        servlet.doGet(request, response);

        verify(request).setAttribute("propio", false);
        verify(request).setAttribute("siguiendo", true);
    }

    @Test
    public void testDoPostSeguimientoCompleto() throws Exception {
        String seguidorNick = "segWeb";
        String seguidoNick = "targetWeb";

        DataUsuarioDTO seguidorData = new DataUsuarioDTO();
        seguidorData.setNickname(seguidorNick);

        when(request.getParameter("nickname")).thenReturn(seguidoNick);

        // 1. Sin sesión -> redirect /login
        when(request.getSession(false)).thenReturn(null);
        when(request.getParameter("accion")).thenReturn("seguir");
        servlet.doPost(request, response);
        verify(response).sendRedirect("/edext/login");

        // 2. Con sesión pero intentar seguirse a sí mismo -> 400
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("usuarioLogueado")).thenReturn(seguidorData);
        when(request.getParameter("nickname")).thenReturn(seguidorNick);
        servlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "No se puede seguir a este usuario.");

        // 3. Seguir usuario válido -> redirect con mensaje
        when(request.getParameter("nickname")).thenReturn(seguidoNick);
        when(request.getParameter("accion")).thenReturn("seguir");
        servlet.doPost(request, response);
        verify(usuarioPort).seguirUsuario(seguidorNick, seguidoNick);
        verify(response).sendRedirect(contains("mensaje=Seguimiento+actualizado."));

        // 4. Seguir nuevamente (duplicado) -> redirect con error
        doThrow(new ErrorRepetidos_Exception("Ya lo sigue", null)).when(usuarioPort).seguirUsuario(seguidorNick, seguidoNick);
        servlet.doPost(request, response);
        verify(response).sendRedirect(contains("error="));

        // 5. Dejar de seguir -> redirect con mensaje
        when(request.getParameter("accion")).thenReturn("dejar");
        servlet.doPost(request, response);
        verify(usuarioPort).dejarDeSeguirUsuario(seguidorNick, seguidoNick);

        // 6. Acción inválida -> 400
        when(request.getParameter("accion")).thenReturn("otraCosa");
        servlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
    }
}
