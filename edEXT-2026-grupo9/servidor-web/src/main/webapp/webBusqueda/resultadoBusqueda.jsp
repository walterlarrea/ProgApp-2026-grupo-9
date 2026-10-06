<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList, com.grupo9.edext.grupo9.servidor_central.dominio.ResultadoBusqueda, com.grupo9.edext.grupo9.servidor_central.controller.busqueda.TipoBusqueda, com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso, com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario, com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Cursos</title>
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
<!--            <section class="hero-banner">
                <h1>${curso.nombreCurso()}</h1>
                <p>${curso.descCurso()}</p>
            </section>-->

            <ol class="nav-list">
                <%
                    ArrayList<ResultadoBusqueda> resultados = (ArrayList<ResultadoBusqueda>) request.getAttribute("listaResultados");
                    for (ResultadoBusqueda res : resultados) {
                        TipoBusqueda tipo = res.tipo();

                        switch (tipo) {
                            case CURSO:
                                DataCurso curso = (DataCurso) res.data();
                %>
                                <li>
                                    <a href="${pageContext.request.contextPath}/curso?nombre=<%= curso.nombreCurso() %>">
                                        <%= curso.nombreCurso() %>
                                    </a>
                                </li>
                <%
                            break;
                            case USUARIO:
                                DataUsuario usuario = (DataUsuario) res.data();
                %>
                                <li>
                                    <a href="${pageContext.request.contextPath}/usuario?nombre=<%= usuario.getNombre() %>">
                                        <%= usuario.getNombre() %>
                                    </a>
                                </li>
                <%
                            break;
                            case PROGRAMA_FORMACION:
                                DataProgramaFormacion programa = (DataProgramaFormacion) res.data();
                %>
                                <li>
                                    <a href="${pageContext.request.contextPath}/programa?nombre=<%= programa.nombre() %>">
                                        <%= programa.nombre() %>
                                    </a>
                                </li>
                <%
                            break;
                            default:
                                System.err.println("[ALERTA] Tipo de resultado no manejado");
                            break;
                        }
                    }
                %>
            </ol>

<!--            <h3>Cursos</h3>
            <ol class="nav-list">
                <c:choose>
                    <c:when test="${not empty cursos}">
                        <c:forEach var="curso" items="${cursos}">
                            <li>
                                <a href="${pageContext.request.contextPath}/curso?nombre=${curso.nombreCurso()}">
                                    <c:out value="${curso.nombreCurso()}"/>
                                </a>
                            </li>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <li class="empty-hint">No encontramos lo que estás buscando</li>
                    </c:otherwise>
                </c:choose>
            </ol>
            
            <h3>Usuarios</h3>
            <ol class="nav-list">
                <c:choose>
                    <c:when test="${not empty usuarios}">
                        <c:forEach var="usuario" items="${usuarios}">
                            <li>
                                <a href="${pageContext.request.contextPath}/usuario?nombre=${usuario.nombre}">
                                    <c:out value="${usuario.nombre}"/>
                                </a>
                            </li>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <li class="empty-hint">No encontramos lo que estás buscando</li>
                    </c:otherwise>
                </c:choose>
            </ol>
            
            <h3>Programas</h3>
            <ol class="nav-list">
                <c:choose>
                    <c:when test="${not empty programas}">
                        <c:forEach var="programa" items="${programas}">
                            <li>
                                <a href="${pageContext.request.contextPath}/programa?nombre=${programa.nombre()}">
                                    <c:out value="${programa.nombre()}"/>
                                </a>
                            </li>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <li class="empty-hint">No encontramos lo que estás buscando</li>
                    </c:otherwise>
                </c:choose>
            </ol>-->
        </main>
    </div>
</body>
</html>
