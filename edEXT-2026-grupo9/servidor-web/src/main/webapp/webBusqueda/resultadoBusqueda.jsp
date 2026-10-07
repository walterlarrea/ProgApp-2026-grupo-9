<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList, com.grupo9.edext.grupo9.servidor_web.servlets.ResultadoBusquedaExtendida"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Cursos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/busqueda.js"></script>
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
                    ArrayList<ResultadoBusquedaExtendida> resultados = (ArrayList<ResultadoBusquedaExtendida>) request.getAttribute("listaResultados");
                    for (ResultadoBusquedaExtendida resultado : resultados) {
                %>
                        <li>
                            <a href="<%= resultado.href() %>">
                                <span class="resultado-tipo" data-tipo="<%= resultado.tipoCss() %>"><%= resultado.tipoVisible() %></span>
                                <%= resultado.nombreVisibleHighlighted() %>
                            </a>
                        </li>
                <%
                    }
                %>
            </ol>
        </main>
    </div>
</body>
</html>
