<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Plataforma de Educación</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/busqueda.js"></script>
</head>
<body>
    <!-- cabezal fijo -->
    <%@ include file="webMiscelanea/header.jsp" %>
    <!-- Layout Principal: Contenido Dinámico -->
    <div class="layout-container">
        <!-- Sidebar / Menú Lateral -->
        <%@ include file="webMiscelanea/sidebar.jsp" %>
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
