<!-- Cabezal Fijo (Header) -->
    <header class="navbar">
        <div class="nav-container">
            <a href="${pageContext.request.contextPath}/" class="brand">
                <span class="brand-accent">ed</span>EXT
            </a>

            <%@ include file="../webBusqueda/busquedaPrincipal.jsp" %>

            <nav class="nav-actions">
                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline">Iniciar Sesión</a>
                <a href="${pageContext.request.contextPath}/alta-usuario" class="btn btn-primary">Registrarse</a>
            </nav>
        </div>
    </header>

