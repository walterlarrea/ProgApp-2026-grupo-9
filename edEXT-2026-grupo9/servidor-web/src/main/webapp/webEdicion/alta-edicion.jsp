<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Edición de curso</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/busqueda.js"></script>
</head>

<body data-context-path="${pageContext.request.contextPath}">
    <!-- cabezal fijo -->
    <%@ include file="../webMiscelanea/header.jsp" %>
    <!-- Layout Principal: Contenido Dinámico -->
    <div class="layout-container">
        <!-- Sidebar / Menú Lateral -->
        <%@ include file="../webMiscelanea/sidebar.jsp" %>
        <main class="main-content">
            <section class="hero-banner">
                <h1>Registrar nueva edición de curso</h1>
                <p>Crea y agrega una edición a un curso existente</p>
            </section>
            <form action="${pageContext.request.contextPath}/edicionCurso" method="post" enctype="multipart/form-data">
                <h3>Datos</h3>
                
                <label for="instituto"><br>Instituto</br></label>
                <select id="instituto" name="instituto" required>
                <option value="" ${empty param.instituto ? 'selected' : ''}>Seleccione un instituto</option>
                    <c:forEach var="instituto" items="${institutos}">
                        <option value="${instituto.nombreI()}" ${param.instituto eq instituto.nombreI() ? 'selected' : ''}>
                            <c:out value="${instituto.nombreI()}"/>
                        </option>
                    </c:forEach>
                </select>
                <script src="${pageContext.request.contextPath}/js/cargarInstitutos.js"></script>
                
                <label for="curso"><br>Curso perteneciente</br></label>
                <select id="curso" name="curso">
                    <option value="">Seleccione un curso</option>
                    <c:forEach var="curso" items="${cursos}">
                        <option value="${curso.nombreCurso()}">
                            ${curso.nombreCurso()}
                        </option>
                    </c:forEach>
                </select>
                
                <label><br>Docente(s)</br></label>
                <div class="lista-checkbox">
                    <c:forEach var="docente" items="${docentes}">
                        <label class="alta-checkbox ">
                            <input type="checkbox" name="docentes" value="${docente.nickname}">
                                <c:out value="${docente.nombre}"/>
                                <c:out value="${docente.apellido}"/>
                        </label>
                    </c:forEach>

                    <c:if test="${empty docentes}">
                        <p>No hay docentes disponibles.</p>
                    </c:if>
                </div>
                
                <label><br>Nombre de la edición</br></label> <input type="text" name="nombreEdi">
                <label><br>Fecha de inicio</br></label> <input type="date" name="fInicio">
                <label><br>Fecha de finalización</br></label> <input type="date" name="fFin">
                <label><br>Cupo</br></label> <input type="number" id="cupo" name="cupo" min="1">
                
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
