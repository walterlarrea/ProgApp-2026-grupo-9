<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Acta estudiantil</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <%@ include file="../webMiscelanea/header.jsp" %>
    <div class="layout-container">
        <%@ include file="../webMiscelanea/sidebar.jsp" %>
        <main class="main-content">
            <section class="hero-banner">
                <div class="hero-contenido">
                    <h1>Acta estudiantil</h1>
                    <p>Notas de ediciones a las que te hayas inscripto</p>
                </div>
            </section>

            <c:if test="${not empty mensajeError}">
                <div class="error" role="alert">
                    <c:out value="${mensajeError}"/>
                </div>
            </c:if>

            <div class="table-container">
                <table class="inscriptos-table">
                    <thead>
                        <tr>
                            <th>Edición</th>
                            <th>Finalización</th>
                            <th>Duración</th>
                            <th>Créditos</th>
                            <th>Evaluación</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty acta}">
                                <c:forEach var="fila" items="${acta}">
                                    <tr>
                                        <td class="nav-list">
                                            <a href="${pageContext.request.contextPath}/edicionCurso?nombre=${fn:escapeXml(fila.edicion)}">
                                                <c:out value="${fila.edicion}"/>
                                            </a>
                                        </td>
                                        <td><c:out value="${fila.fechaFin}"/></td>
                                        <td><c:out value="${fila.duracion}"/> semanas</td>
                                        <td><c:out value="${fila.creditos}"/></td>
                                        <td><strong><c:out value="${fila.nota}"/></strong></td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="5" class="empty-table">
                                        Aún no tienes ediciones evaluadas.
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>

            <div class="btn-der">
                <button type="button" class="btn btn-primary">Descargar en PDF</button>
            </div>
        </main>
    </div>
</body>
</html>
