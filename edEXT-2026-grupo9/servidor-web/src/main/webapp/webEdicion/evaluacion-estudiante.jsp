<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Evaluaciones</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <%@ include file="../webMiscelanea/header.jsp" %>
    <div class="layout-container">
        <%@ include file="../webMiscelanea/sidebar.jsp" %>
        <main class="main-content">
            <section class="hero-banner">
                <div class="hero-contenido">
                    <h1>Tus Evaluaciones</h1>
                    <p>Resultados de las ediciones cerradas en las que fuiste aceptado</p>
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

            <div class="table-container">
                <table class="inscriptos-table">
                    <thead>
                        <tr>
                            <th>Edición</th>
                            <th>Fecha <br>inscripción</th>
                            <th>Evaluación</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty evaluaciones}">
                                <c:forEach var="insc" items="${evaluaciones}">
                                    <tr>
                                        <td class="nav-list">
                                            <a href="${pageContext.request.contextPath}/edicionCurso?nombre=${fn:escapeXml(insc.edicion)}">
                                                <c:out value="${insc.edicion}"/>
                                            </a>
                                        </td>
                                        <td>
                                            <c:out value="${insc.fechaInscE}"/>
                                        </td>
                                        <td>
                                            <span class="estado-badge">
                                                <c:out value="${insc.nota != null ? insc.nota : 'Sin calificar'}"/>
                                            </span>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="3" class="empty-table">
                                        No tienes evaluaciones en ediciones cerradas.
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </main>
    </div>
</body>
</html>
