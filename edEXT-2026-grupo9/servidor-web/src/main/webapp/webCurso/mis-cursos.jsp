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
                        <div class="dato-space">
                            <table class="inscriptos-table" style="width: 100%; border-collapse: collapse; margin-top: 10px;">
                                <thead>
                                    <tr>
                                        <th style="width: 100px;">Imagen</th>
                                        <th>Curso</th>
                                        <th>Instituto</th>
                                        <th style="text-align: center;">Puntaje Promedio</th>
                                        <th style="text-align: center;">Tu Valoración (1 a 5)</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="c" items="${cursos}">
                                        <tr id="fila-curso-${fn:escapeXml(c.nombreCurso)}" style="border-bottom: 1px solid #e2e8f0;">
                                            <td style="padding: 10px; text-align: center;">
                                                <img src="${pageContext.request.contextPath}/imagenes/curso/${c.imagen}"
                                                     alt="Imagen del curso"
                                                     style="width: 65px; height: 45px; object-fit: cover; border-radius: 4px;"
                                                     onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/imagenes/curso/curso-placeholder.jpg';" />
                                            </td>
                                            <td style="padding: 10px;" class="nav-list">
                                                <a href="${pageContext.request.contextPath}/curso?nombre=${fn:escapeXml(c.nombreCurso)}" style="font-weight: 600; font-size: 1rem;">
                                                    <c:out value="${c.nombreCurso}"/>
                                                </a>
                                                <p style="margin: 4px 0 0; color: #64748b; font-size: 0.85rem; max-width: 450px;">
                                                    <c:out value="${c.descCurso}"/>
                                                </p>
                                            </td>
                                            <td style="padding: 10px;">
                                                <c:out value="${c.instituto != null ? c.instituto.nombreI : '-'}"/>
                                            </td>
                                            <td style="padding: 10px; text-align: center;">
                                                <c:choose>
                                                    <c:when test="${c.cantValoraciones > 0}">
                                                        <span style="font-size: 1.1rem; font-weight: bold; color: #d97706;">
                                                            ★ <c:out value="${c.puntajePromedio}"/>
                                                        </span>
                                                        <br>
                                                        <small style="color: #64748b;">
                                                            (<c:out value="${c.cantValoraciones}"/> <c:out value="${c.cantValoraciones == 1 ? 'voto' : 'votos'}"/>)
                                                        </small>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span style="color: #64748b; font-style: italic;">Sin valoraciones</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td style="padding: 10px; text-align: center;" id="col-valoracion-${fn:escapeXml(c.nombreCurso)}">
                                                <c:choose>
                                                    <c:when test="${c.valoracionEstudiante != null && c.valoracionEstudiante > 0}">
                                                        <span class="badge badge-success" style="font-size: 0.95rem; padding: 6px 12px; font-weight: 600;">
                                                            ★ <c:out value="${c.valoracionEstudiante}"/> / 5
                                                        </span>
                                                        <br>
                                                        <small style="color: #64748b; font-style: italic;">(Valoración enviada)</small>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <select name="valoracion-${fn:escapeXml(c.nombreCurso)}"
                                                                class="select-valoracion"
                                                                data-curso="${fn:escapeXml(c.nombreCurso)}"
                                                                style="padding: 6px 10px; border: 1px solid #cbd5e1; border-radius: 4px; font-size: 0.9rem;">
                                                            <option value="">-- Asignar puntaje --</option>
                                                            <option value="1">★ 1 - Muy insatisfecho</option>
                                                            <option value="2">★ 2 - Insatisfecho</option>
                                                            <option value="3">★ 3 - Regular</option>
                                                            <option value="4">★ 4 - Satisfecho</option>
                                                            <option value="5">★ 5 - Muy satisfecho</option>
                                                        </select>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
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
