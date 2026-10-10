<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Curso</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>

<body>
    <!-- cabezal fijo -->
    <%@ include file="../webMiscelanea/header.jsp" %>
    <!-- Layout Principal: Contenido Dinámico -->
    <div class="layout-container">
        <!-- Sidebar / Menú Lateral -->
        <%@ include file="../webMiscelanea/sidebar.jsp" %>
        <main class="main-content">
            <section class="hero-banner">
                <h1>${curso.nombreCurso()}</h1>
                <p>${curso.descCurso()}</p>
            </section>
                <div class="dato-space">
                    <h4>Instituto</h4> <p>${curso.instituto().nombreI()}</p>
                </div>
                <div class="dato-space">
                    <h4>Créditos</h4> <p>${curso.cantCred()}</p>
                </div>
                <div class="dato-space">
                    <h4>Cantidad de horas</h4> <p>${curso.cantHoras()}</p>
                </div>
                <div class="dato-space">
                    <h4>Duración</h4> <p>${curso.duracion()} semanas</p>
                </div>
                <div class="dato-space">
                    
                </div>
                <h4>Previas del curso</h4> 
                <ol class="nav-list">
                <c:choose>
                    <c:when test="${not empty previas}">
                        <c:forEach var="prev" items="${previas}">
                            <li>
                                <a href="${pageContext.request.contextPath}/curso?nombre=${prev.nombreCurso()}">
                                    <c:out value="${prev.nombreCurso()}"/>
                                </a>
                            </li>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <li class="empty-hint">No contiene previas.</li>
                    </c:otherwise>
                </c:choose>
            </ol>
                <div class="dato-space">
                    <h4>URL</h4> <p>${curso.url()}</p>
                </div>
            <div class="dato-space">
            <h2>Ediciones</h2> 
            <ol class="nav-list">
                <c:choose>
                    <c:when test="${not empty ediciones}">
                        <c:forEach var="edi" items="${ediciones}">
                            <li>
                                <a href="${pageContext.request.contextPath}/edicionCurso?nombre=${edi.getNombreEdi()}">
                                    <c:out value="${edi.getNombreEdi()}"/>
                                </a>
                            </li>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <li class="empty-hint">Sin ediciones disponibles</li>
                    </c:otherwise>
                </c:choose>
            </ol>
            </div>
        </main>
    </div>
</body>
</html>
