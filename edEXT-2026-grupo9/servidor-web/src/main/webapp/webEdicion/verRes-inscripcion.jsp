<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Inscripción</title>
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
                <div class="hero-contenido">
                    <h1>Tus inscripciones</h1>
                    <p>Aquí podrás ver el estado de tus inscripciones</p>
                </div>
            </section>

            <div class="table-container">
                <table class="inscriptos-table">
                    <thead>
                        <tr>
                            <th>Edición</th>
                            <th>Fecha <br>inscripción</th>
                            <th>Estado</th>
                        </tr>
                    </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty error}">
                            <tr>
                                <td colspan="3" class="empty-table" role="alert">
                                    <c:out value="${error}"/>
                                </td>
                            </tr>
                        </c:when>
                        <c:when test="${not empty inscripciones}">
                            <c:forEach var="insc" items="${inscripciones}">
                                <tr>
                                    <td class="nav-list">
                                        <a href="${pageContext.request.contextPath}/edicionCurso?nombre=${insc.edicion}">
                                            <c:out value="${insc.edicion}"/>
                                        </a>
                                    </td>
                                    <td>
                                        <c:out value="${insc.fechaInscE}"/>
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
                                <td colspan="3" class="empty-table">
                                    No has realizado inscripciones.
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