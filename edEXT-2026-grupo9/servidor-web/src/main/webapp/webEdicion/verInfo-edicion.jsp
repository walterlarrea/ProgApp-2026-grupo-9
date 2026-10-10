<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Edición de curso</title>
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
                <h1>${edicion.nombreEdi}</h1>
                <h2>${edicion.cursoAsoc.instituto().nombreI()}</h2>
                <h3>${edicion.cursoAsoc.nombreCurso()}</h3>
            </section>
                <div class="dato-space">
                    <h4>Fecha de inicio</h4> <p>${edicion.fechaInicio}</p>
                </div>      
                <div class="dato-space">
                    <h4>Fecha de finalización</h4> <p>${edicion.fechaFin}</p>
                </div>
                <div class="dato-space">
                    <h4>Cupo</h4>
                    <p>
                        <c:choose>
                            <c:when test="${edicion.cupo == null}">
                                Libre
                            </c:when>
                            <c:otherwise>
                                <c:out value="${edicion.cupo}"/>
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
                
            <div class="dato-space">
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
            </div>
            <h2>Inscriptos</h2>
            <div class="table-container">
                <table class="inscriptos-table">
                    <thead>
                        <tr>
                            <th>Estudiante</th>
                            <th>Fecha <br>inscripción</th>
                            <th>Inscripto</th>
                            <th>Estado</th>
                        </tr>
                    </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty inscriptos}">
                            <c:forEach var="insc" items="${inscriptos}">
                                <tr>
                                    <td class="nav-list">
                                        <a href="${pageContext.request.contextPath}/usuario?nickname=${insc.estudiante.nickname}">
                                            <c:out value="${insc.estudiante.nombre}"/>
                                            <c:out value="${insc.estudiante.apellido}"/>
                                        </a>
                                    </td>
                                    <td>
                                        <c:out value="${insc.fechaInscE}"/>
                                    </td>

                                    <td class="checkbox-cell">
                                        <input type="checkbox" checked disabled>
                                    </td>

                                    <td>
                                        <span class="estado-badge">
                                            <c:out value="${insc.estado}"/>
                                        </span>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="5" class="empty-table">
                                    No hay estudiantes inscriptos en esta edición.
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
