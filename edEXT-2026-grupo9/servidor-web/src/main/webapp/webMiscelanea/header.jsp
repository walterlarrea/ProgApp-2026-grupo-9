<%@ page import="com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario" %>
<%
    DataUsuario usuarioLogueado = (DataUsuario) session.getAttribute("usuarioLogueado");
%>
<header class="navbar">
    <div class="nav-container">
        <a href="<%= request.getContextPath() %>/home" class="brand">
            ed<span class="brand-accent">EXT</span>
        </a>

        <div class="search-box">
            <form action="<%= request.getContextPath() %>/cursos" method="GET">
                <input type="text" name="query" placeholder="Buscar cursos, programas o institutos...">
                <button type="submit">Buscar</button>
            </form>
        </div>

        <div class="nav-actions">
            <% if (usuarioLogueado == null) { %>
                <!-- Visitante -->
                <a href="<%= request.getContextPath() %>/login" class="btn btn-outline">Iniciar Sesion</a>
                <a href="<%= request.getContextPath() %>/alta-usuario" class="btn btn-primary">Registrarse</a>
            <% } else { %>
                <!-- Usuario Autenticado -->
                <a href="<%= request.getContextPath() %>/perfil" class="btn btn-outline" style="display: flex; align-items: center; gap: 8px;">
                    <% if (usuarioLogueado.getImagen() != null && !usuarioLogueado.getImagen().trim().isEmpty()) { %>
                        <img src="<%= usuarioLogueado.getImagen() %>" alt="<%= usuarioLogueado.getNickname() %>" width="24" height="24" style="border-radius: 50%; object-fit: cover;">
                    <% } %>
                    <span><%= usuarioLogueado.getNombre() != null ? usuarioLogueado.getNombre() : usuarioLogueado.getNickname() %></span>
                </a>
                <a href="<%= request.getContextPath() %>/logout" class="btn btn-outline" style="color: #dc2626; border-color: #fca5a5;">Cerrar Sesion</a>
            <% } %>
        </div>
    </div>
</header>
