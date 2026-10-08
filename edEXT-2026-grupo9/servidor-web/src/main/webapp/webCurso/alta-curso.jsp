<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Curso</title>
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
                <h1>Crear un nuevo curso</h1>
                <p>Crea y agrega un curso a un instituto</p>
            </section>
            <form action="${pageContext.request.contextPath}/curso" method="post" enctype="multipart/form-data">
                <label><br>Nombre</br></label> <input type="text" name="nombre">
                <label for="instituto"><br>Instituto</br></label>
                <select id="instituto" name="instituto">
                    <option value="">Seleccione un instituto</option>
                    <c:forEach var="instituto" items="${institutos}">
                        <option value="${instituto.nombreI()}">
                            ${instituto.nombreI()}
                        </option>
                    </c:forEach>
                </select>
                <label><br>Descripción</br></label> <input type="text" name="descripcion">
                <label><br>Duración (Semanas)</br></label> <input type="text" name="duracion">
                <label><br>Cantidad de horas (Semestral)</br></label> <input type="text" name="cantHoras">
                <label><br>Créditos</br></label> <input type="text" name="cantCred">
                <label><br>URL</br></label> <input type="text" name="url">
                <label for="previas"><br>Previas</br></label>
                <select id="previas" name="previas" multiple>
                    <option value="">previassssss</option>
                    <c:forEach var="curso" items="${cursos}">
                        <option value="${curso.nombreCurso()}">
                            ${curso.nombreCurso()}
                        </option>
                    </c:forEach>
                </select>
                
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
