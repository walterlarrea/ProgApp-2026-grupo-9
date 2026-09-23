<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Plataforma de Educación</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

    <!-- Cabezal Fijo (Header) -->
    <header class="navbar">
        <div class="nav-container">
            <a href="${pageContext.request.contextPath}/" class="brand">
                <span class="brand-accent">ed</span>EXT
            </a>

            <div class="search-box">
                <form action="${pageContext.request.contextPath}/buscar" method="get">
                    <input type="text" name="q" placeholder="Buscar cursos y programas...">
                    <button type="submit">Buscar</button>
                </form>
            </div>

            <nav class="nav-actions">
                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline">Iniciar Sesión</a>
                <a href="${pageContext.request.contextPath}/alta-usuario" class="btn btn-primary">Registrarse</a>
            </nav>
        </div>
    </header>

    <!-- Layout Principal: Sidebar + Contenido Dinámico -->
    <div class="layout-container">
        <!-- Sidebar / Menú Lateral -->
        <aside class="sidebar">
            <h3>Institutos</h3>
            <ul class="nav-list">
                <c:choose>
                    <c:when test="${not empty institutos}">
                        <c:forEach var="inst" items="${institutos}">
                            <li>
                                <a href="${pageContext.request.contextPath}/cursos?instituto=${inst.nombreI()}">
                                    <c:out value="${inst.nombreI()}"/>
                                </a>
                            </li>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <li class="empty-hint">Sin institutos disponibles</li>
                    </c:otherwise>
                </c:choose>
            </ul>

            <h3 style="margin-top: 1.5rem;">Explorar</h3>
            <ul class="nav-list">
                <li><a href="${pageContext.request.contextPath}/cursos">Todos los Cursos</a></li>
                <li><a href="${pageContext.request.contextPath}/programas">Programas de Formación</a></li>
            </ul>

            <div class="status-box">
                <small>Estado:</small>
                <span class="badge badge-success"><c:out value="${estadoDb}"/></span>
            </div>
        </aside>

        <!-- Contenido Central Dinámico -->
        <main class="main-content">
            <section class="hero-banner">
                <h1>Bienvenido a edEXT</h1>
                <p>Plataforma universitaria para la gestión de cursos, programas y ediciones de extensión académica.</p>
            </section>

            <section class="section">
                <h2>Cursos Disponibles</h2>
                <div class="cards-grid">
                    <c:choose>
                        <c:when test="${not empty cursos}">
                            <c:forEach var="curso" items="${cursos}">
                                <div class="card">
                                    <div class="card-header">
                                        <span class="tag"><c:out value="${curso.instituto().nombreI()}"/></span>
                                        <h3><c:out value="${curso.nombreCurso()}"/></h3>
                                    </div>
                                    <div class="card-body">
                                        <p><c:out value="${curso.descCurso()}"/></p>
                                        <div class="meta-info">
                                            <span><strong>Créditos:</strong> <c:out value="${curso.cantCred()}"/></span>
                                            <span><strong>Duración:</strong> <c:out value="${curso.duracion()}"/> semanas</span>
                                        </div>
                                    </div>
                                    <div class="card-footer">
                                        <a href="${pageContext.request.contextPath}/curso?nombre=${curso.nombreCurso()}" class="btn btn-sm">Ver Detalle</a>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <p>No se encontraron cursos activos en este momento.</p>
                                <p class="text-muted">Los cursos registrados en el Servidor Central se listarán automáticamente aquí.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </section>
        </main>
    </div>

</body>
</html>
