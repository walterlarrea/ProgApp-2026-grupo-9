<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Mis Cursos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>

<body>
    <%@ include file="../webMiscelanea/header.jsp" %>
    <div class="layout-container">
        <%@ include file="../webMiscelanea/sidebar.jsp" %>
        <main class="main-content">
            <section class="hero-banner">
                <div class="hero-contenido">
                    <h1>Mis Cursos</h1>
                    <p>Aquí puedes consultar los cursos en los que participas o has participado y registrar tu valoración de satisfacción</p>
                </div>
            </section>

            <c:if test="${not empty mensajeExito}">
                <div class="mensaje-exito" role="status">
                    <c:out value="${mensajeExito}" escapeXml="false"/>
                </div>
            </c:if>

            <c:if test="${not empty mensajeError}">
                <div class="error" role="alert">
                    <c:out value="${mensajeError}" escapeXml="false"/>
                </div>
            </c:if>

            <c:choose>
                <c:when test="${empty cursos}">
                    <div class="dato-space">
                        <p class="empty-hint">Actualmente no estás inscripto con estado aceptado en ninguna edición de cursos.</p>
                    </div>
                </c:when>

                <c:otherwise>
                    <form id="formValoraciones" action="${pageContext.request.contextPath}/misCursos" method="post">
                        <div class="mis-cursos-lista">
                            <c:forEach var="c" items="${cursos}">
                                <article class="mis-curso-card" id="fila-curso-${fn:escapeXml(c.nombreCurso)}">
                                    <!-- Foto del Curso -->
                                    <div class="mis-curso-media">
                                        <img src="${pageContext.request.contextPath}/imagenes/curso/${c.imagen}"
                                             alt="Imagen del curso ${fn:escapeXml(c.nombreCurso)}"
                                             class="mis-curso-img"
                                             onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/imagenes/curso/curso-placeholder.jpg';" />
                                    </div>

                                    <!-- Información Principal: Título grande, Instituto, Descripción amplia -->
                                    <div class="mis-curso-info">
                                        <div class="mis-curso-header">
                                            <c:if test="${not empty c.instituto && not empty c.instituto.nombreI}">
                                                <span class="tag"><c:out value="${c.instituto.nombreI}"/></span>
                                            </c:if>
                                            <h2 class="mis-curso-titulo">
                                                <a href="${pageContext.request.contextPath}/curso?nombre=${fn:escapeXml(c.nombreCurso)}">
                                                    <c:out value="${c.nombreCurso}"/>
                                                </a>
                                            </h2>
                                        </div>
                                        <p class="mis-curso-desc">
                                            <c:out value="${c.descCurso}"/>
                                        </p>
                                    </div>

                                    <!-- Sección Lateral Compacta: Puntajes y Calificación -->
                                    <div class="mis-curso-stats">
                                        <div class="mis-curso-stat-item">
                                            <span class="stat-titulo">Promedio</span>
                                            <div class="stat-promedio-valor">
                                                <c:choose>
                                                    <c:when test="${c.cantValoraciones > 0}">
                                                        <span class="stat-estrellas">★ <c:out value="${c.puntajePromedio}"/></span>
                                                        <span class="stat-votos">(<c:out value="${c.cantValoraciones}"/> <c:out value="${c.cantValoraciones == 1 ? 'voto' : 'votos'}"/>)</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="stat-sin-votos">Sin valoraciones</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>

                                        <div class="mis-curso-stat-item">
                                            <span class="stat-titulo">Tu Valoración</span>
                                            <div id="col-valoracion-${fn:escapeXml(c.nombreCurso)}" class="stat-valoracion-contenedor">
                                                <c:choose>
                                                    <c:when test="${c.valoracionEstudiante != null && c.valoracionEstudiante > 0}">
                                                        <span class="badge badge-success stat-badge-enviada">
                                                            ★ <c:out value="${c.valoracionEstudiante}"/> / 5
                                                        </span>
                                                        <small class="stat-subtexto-enviado">(Valoración enviada)</small>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <select name="valoracion-${fn:escapeXml(c.nombreCurso)}"
                                                                class="select-valoracion stat-select-valoracion"
                                                                data-curso="${fn:escapeXml(c.nombreCurso)}">
                                                            <option value="">-- Asignar puntaje --</option>
                                                            <option value="1">★ 1 - Muy insatisfecho</option>
                                                            <option value="2">★ 2 - Insatisfecho</option>
                                                            <option value="3">★ 3 - Regular</option>
                                                            <option value="4">★ 4 - Satisfecho</option>
                                                            <option value="5">★ 5 - Muy satisfecho</option>
                                                        </select>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </div>
                                </article>
                            </c:forEach>
                        </div>

                        <div class="btn-der" style="margin-top: 25px; margin-bottom: 30px;">
                            <button type="button"
                                    id="btnGuardarValoraciones"
                                    class="btn btn-primary"
                                    onclick="guardarValoracionesAjax()">
                                Guardar
                            </button>
                        </div>
                    </form>
                </c:otherwise>
            </c:choose>
        </main>
    </div>

    <%@ include file="../webMiscelanea/popup-mensaje.jsp" %>

    <script src="${pageContext.request.contextPath}/js/misCursos.js"></script>
</body>
</html>
