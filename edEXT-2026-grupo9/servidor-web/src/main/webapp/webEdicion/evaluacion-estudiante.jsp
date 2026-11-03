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
                    <p>Evaluaciones de las ediciones a las que te has inscripto</p>
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
            
            <form action="${pageContext.request.contextPath}/evaluacionEstudiante" method="get">
            
            </form>
    </div>
</body>
</html>
