<!-- Cabezal Fijo (Header) -->
    <header class="navbar">
        <div class="nav-container">
            <a href="${pageContext.request.contextPath}/" class="brand">
                <span class="brand-accent">ed</span>EXT
            </a>

            <div class="search-box">
                <form action="${pageContext.request.contextPath}/buscar" method="get">
                    <input type="text" name="q" placeholder="Buscar cursos, usuarios y programas...">
                    <button type="submit">Buscar</button>
                </form>
            </div>

            <nav class="nav-actions">
                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline">Iniciar Sesión</a>
                <a href="${pageContext.request.contextPath}/alta-usuario" class="btn btn-primary">Registrarse</a>
            </nav>
        </div>
    </header>

