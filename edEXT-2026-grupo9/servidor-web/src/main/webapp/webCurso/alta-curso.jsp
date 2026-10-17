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
            
            <c:if test="${not empty error}">
                <p class="error">
                    <c:out value="${error}"/>
                </p>
            </c:if>
            
            <form action="${pageContext.request.contextPath}/curso" method="post" enctype="multipart/form-data">
                <h2>Datos</h2>

                <label for="instituto"><br>Instituto</br></label>
                <select id="instituto" name="instituto" style="width: 30%" required>
                <option value="" ${empty param.instituto ? 'selected' : ''}>Seleccione un instituto</option>
                    <c:forEach var="instituto" items="${institutos}">
                        <option value="${instituto.nombreI()}" ${param.instituto eq instituto.nombreI() ? 'selected' : ''}>
                            <c:out value="${instituto.nombreI()}"/>
                        </option>
                    </c:forEach>
                </select>
                <script src="${pageContext.request.contextPath}/js/cargarInstCursos.js"></script>
                <div class="dato-space2">
                    <label><br>Nombre</br></label> <input type="text" name="nombre" style="width: 30%" required>
                </div>
                <div class="dato-space2">
                    <label><br>Descripción</br></label> <textarea id="desc" name="desc" rows="5" required></textarea>
                </div>
                <div class="dato-space2">
                    <label><br>Duración (Semanas)</br></label> <input type="number" name="duracion" step="1" min="1" max="52" style="width: 10%" required>
                </div>
                <div class="dato-space2">
                    <label><br>Cantidad de horas (Semestral)</br></label> <input type="number" step="1" min="1" max="8760" name="cantHoras" style="width: 10%" required>
                </div>
                <div class="dato-space2">
                    <label><br>Créditos</br></label> <input type="number" name="cantCred" step="1" min="0" style="width: 10%" required>
                </div>
                
                <label><br>URL</br></label> <input type="text" name="url" style="width: 30%" required>
                
                <div class="dato-space2">
                    <label><br>Imagen representativa</br></label> 
                    <img src="${pageContext.request.contextPath}/uploads/curso/${curso.imagen()}">
                    <input type="file" name="imagen" accept="image/*">
                </div>
                
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
                    
                <div class="btn-der">
                    <button type="submit" class="btn btn-primary">Guardar curso</button>
                </div>
            </form>
        </main>
    </div>
</body>
</html>
