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
                <h1>Crear una nueva edición</h1>
                <p>Crea y agrega una edición a un curso existente</p>
            </section>
            <form action="${pageContext.request.contextPath}/edicionCurso" method="post" enctype="multipart/form-data">
                <label><br>Nombre de la edición</br></label> <input type="text" name="nombreEdi">
                <label for="instituto"><br>Instituto</br></label>
                <select id="instituto" name="instituto" onchange="this.form.submit()" required>
                    <option value="">Seleccione un instituto</option>
                    <c:forEach var="instituto" items="${institutos}">
                        <option value="${instituto.nombreI()}">
                            ${instituto.nombreI()}
                        </option>
                    </c:forEach>
                </select>
                <label for="curso"><br>Curso perteneciente</br></label>
                <select id="curso" name="curso">
                    <option value="">Seleccione un curso</option>
                    <c:forEach var="curso" items="${cursos}">
                        <option value="${curso.nombreCurso()}">
                            ${curso.nombreCurso()}
                        </option>
                    </c:forEach>
                </select>
                <label for="docentes"><br>Docente(s)</br>
                <select id="docentes" name="docentes" multiple>
                    <option value="">Docentes</option>
                    <c:forEach var="docentes" items="${docentes}">
                        <option value="${docentes.nombre()}">
                            ${docentes.nombre()}
                            ${docentes.apellido()}
                        </option>
                    </c:forEach>
                </select>
                <label><br>Fecha de inicio</br></label> <input type="date" name="fInicio">
                <label><br>Fecha de finalización</br></label> <input type="date" name="fFin">
                <label><br>Cupo</br></label><input type="text" name="cupo">
                
                <label><br>Imagen representativa</br></label>
                <img src="${pageContext.request.contextPath}/uploads/edicionCurso/${curso.imagen()}">
                <input type="file" name="imagen" accept="image/*">
                <br>
                    <button type="submit">Guardar</button>
                </br>    
        </main>
    </div>
</body>
</html>
