<%@ page import="com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario" %>
<%
    DataUsuario usuarioLogueado = (DataUsuario) session.getAttribute("usuarioLogueado");
    String userImgSrc = "";
    if (usuarioLogueado != null && usuarioLogueado.getImagen() != null && !usuarioLogueado.getImagen().trim().isEmpty()) {
        String img = usuarioLogueado.getImagen().trim();
        if (img.startsWith("http://") || img.startsWith("https://")) {
            userImgSrc = img;
        } else {
            userImgSrc = request.getContextPath() + "/imagen?path=" + java.net.URLEncoder.encode(img, "UTF-8");
        }
    }
%>
<header class="barra-navegacion">
    <div class="container-barra-navegacion">
        <a href="<%= request.getContextPath() %>/home" class="brand">
            ed<span class="brand-accent">EXT</span>
        </a>

        <%@ include file="../webBusqueda/busquedaPrincipal.jsp" %>

        <div class="nav-actions">
            <% if (usuarioLogueado == null) { %>
                <!-- Visitante -->
                <a href="<%= request.getContextPath() %>/login" class="btn btn-outline">Iniciar Sesion</a>
                <a href="<%= request.getContextPath() %>/alta-usuario" class="btn btn-primary">Registrarse</a>
            <% } else { %>
                <!-- Usuario Autenticado -->
                <a href="<%= request.getContextPath() %>/perfil" class="btn btn-outline" style="display: flex; align-items: center; gap: 8px;">
                    <% if (!userImgSrc.isEmpty()) { %>
                        <img src="<%= userImgSrc %>" alt="<%= usuarioLogueado.getNickname() %>" width="24" height="24" style="border-radius: 50%; object-fit: cover;" title="Perfil de <%= usuarioLogueado.getNickname() %>" onerror="this.style.display='none';">
                    <% } else { %>
                        <span style="font-size: 0.8rem; background-color: #cbd5e1; color: #475569; padding: 2px 6px; border-radius: 4px;" title="Sin foto de perfil">Sin foto</span>
                    <% } %>
                    <span><%= usuarioLogueado.getNombre() != null ? usuarioLogueado.getNombre() : usuarioLogueado.getNickname() %></span>
                </a>
                <a href="<%= request.getContextPath() %>/logout" class="btn btn-outline" style="color: #dc2626; border-color: #fca5a5;">Cerrar Sesion</a>
            <% } %>
        </div>
    </div>
</header>
