<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Curso</title>
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
                <h1>Registrar nuevo curso</h1>
                <p>Crea y agrega un curso a un instituto</p>
            </section>
            <form action="${pageContext.request.contextPath}/curso" method="post" enctype="multipart/form-data">
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
                <script src="${pageContext.request.contextPath}/js/cargarInstCursos.js"></script>
                
                <label><br>Nombre</br></label> <input type="text" name="nombre">
                <label><br>Descripción</br></label> <input type="text" name="desc">
                <label><br>Duración (Semanas)</br></label> <input type="text" name="duracion">
                <label><br>Cantidad de horas (Semestral)</br></label> <input type="text" name="cantHoras">
                <label><br>Créditos</br></label> <input type="text" name="cantCred">
                
                <label><br>Previas</br></label>
                <div class="lista-checkbox">
                    <c:forEach var="prev" items="${previas}">
                        <label class="alta-checkbox ">
                            <input type="checkbox" name="previas" value="${prev.nombreCurso}">
                                <c:out value="${prev.nombreCurso}"/>
                        </label>
                    </c:forEach>

                    <c:if test="${empty previas}">
                        <p>No contiene previas.</p>
                    </c:if>
                </div>
                
                <label><br>URL</br></label> <input type="text" name="url">
                <label><br>Imagen representativa</br></label> 
                <img src="${pageContext.request.contextPath}/uploads/curso/${curso.imagen()}">
                <input type="file" name="imagen" accept="image/*">
                <br>
                    <button type="submit">Guardar</button>
                </br> 
            </form>
        </main>
    </div>
</body>
</html>
