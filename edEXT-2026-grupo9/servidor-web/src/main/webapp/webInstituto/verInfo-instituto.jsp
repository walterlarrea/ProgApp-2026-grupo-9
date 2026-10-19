<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Instituto</title>
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
                <h1>${instituto.nombreI()}</h1>
            </section>
            <h2>Cursos de este instituto</h2>
            <ol class="nav-list">
                <c:choose>
                    <c:when test="${not empty cursosInstituto}">
                        <c:forEach var="curso" items="${cursosInstituto}">
                            <li>
                                <a href="${pageContext.request.contextPath}/curso?nombre=${curso.nombreCurso()}">
                                    <c:out value="${curso.nombreCurso()}"/>
                                </a>
                            </li>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <li class="empty-hint">Sin cursos disponibles</li>
                    </c:otherwise>
                </c:choose>
            </ol>   
            <h2>Docentes</h2>
            <ol class="nav-list">
                <c:choose>
                    <c:when test="${not empty docentes}">
                        <c:forEach var="doc" items="${docentes}">
                            <li>
                                <a href="${pageContext.request.contextPath}/usuario?nickname=${doc.nickname}">
                                    <c:out value="${doc.nombre}"/> 
                                    <c:out value="${doc.apellido}"/>
                                </a>
                            </li>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <li class="empty-hint">Sin docentes disponibles</li>
                    </c:otherwise>
                </c:choose>
            </ol>    
        </main>
    </div>
</body>
</html>
