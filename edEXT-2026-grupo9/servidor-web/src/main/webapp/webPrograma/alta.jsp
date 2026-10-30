<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Alta de programa - edEXT</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <jsp:include page="/webMiscelanea/header.jsp" />
    <div class="layout-container">
        <jsp:include page="/webMiscelanea/sidebar.jsp" />
        <main class="main-content">
            <section class="hero-banner"><h1>Crear programa</h1></section>
            <c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>
            <form action="${pageContext.request.contextPath}/programas" method="post" enctype="multipart/form-data">
                <label for="nombre">Nombre</label>
                <input id="nombre" name="nombre" required maxlength="120">
                <label for="descripcion">Descripción</label>
                <textarea id="descripcion" name="descripcion" required rows="4"></textarea>
                <label for="fechaInicio">Fecha de inicio</label>
                <input id="fechaInicio" name="fechaInicio" type="date" required>
                <label for="fechaFin">Fecha de fin</label>
                <input id="fechaFin" name="fechaFin" type="date" required>
                <label for="imagen">Imagen</label>
                <input id="imagen" name="imagen" type="file" accept="image/*">
                <fieldset>
                    <legend>Cursos del programa</legend>
                    <c:forEach var="curso" items="${cursos}">
                        <label class="alta-checkbox"><input type="checkbox" name="cursos" value="${curso.nombreCurso}"> <c:out value="${curso.nombreCurso}"/></label>
                    </c:forEach>
                    <c:if test="${empty cursos}"><p>No hay cursos disponibles.</p></c:if>
                </fieldset>
                <button type="submit" class="btn btn-primary">Guardar programa</button>
            </form>
        </main>
    </div>
</body>
</html>