<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Ediciones de curso</title>
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
    </div>
</body>
</html>
