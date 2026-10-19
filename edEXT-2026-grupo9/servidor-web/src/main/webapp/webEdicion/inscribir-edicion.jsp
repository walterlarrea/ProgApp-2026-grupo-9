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

<body data-context-path="${pageContext.request.contextPath}">
    <!-- cabezal fijo -->
    <%@ include file="../webMiscelanea/header.jsp" %>
    <!-- Layout Principal: Contenido Dinámico -->
    <div class="layout-container">
        <!-- Sidebar / Menú Lateral -->
        <%@ include file="../webMiscelanea/sidebar.jsp" %>
        <main class="main-content">
            <section class="hero-banner">
                <div class="hero-contenido">
                    <h1>Inscripción</h1>
                    <p>Regístrate a una edición de curso</p>
                </div>
            </section>
            
            <c:if test="${not empty error}">
                <p class="error">
                    <c:out value="${error}"/>
                </p>
            </c:if>
                
            <form action="${pageContext.request.contextPath}/inscripcion" method="post">
                <h2>Datos</h2>
                
                <label for="instituto"><br>Instituto</br></label>
                <select id="instituto" name="instituto" style="width: 30%" required>
                <option value="" ${empty institutoSeleccionado ? 'selected' : ''}>Seleccione un instituto</option>
                    <c:forEach var="instituto" items="${institutos}">
                        <option value="${instituto.nombreI()}" ${institutoSeleccionado eq instituto.nombreI() ? 'selected' : ''}>
                            <c:out value="${instituto.nombreI()}"/>
                        </option>
                    </c:forEach>
                </select>
                <script src="${pageContext.request.contextPath}/js/cargarInstInsc.js"></script>
                
                <br>
                <label for="curso"><br>Curso perteneciente</br></label>
                <select id="curso" name="curso" style="width: 30%" required>
                    <option value="">Seleccione un curso</option>
                    <c:forEach var="curso" items="${cursos}">
                        <option value="${curso.nombreCurso()}" ${cursoSeleccionado eq curso.nombreCurso() ? 'selected' : ''}>
                            <c:out value="${curso.nombreCurso()}"/>
                        </option>
                    </c:forEach>
                </select>
                </br>
                <script src="${pageContext.request.contextPath}/js/cargarCursosInsc.js"></script>
                
                <label for="edicion"><br>Edición a inscribirse</br></label>
                <select id="edicion" name="edicion" style="width: 30%" required>
                    <option value="">Seleccione una edición</option>
                        <c:forEach var="edi" items="${ediciones}">
                            <option value="${edi.nombreEdi}" ${edicionSeleccionada eq edi.nombreEdi ? 'selected' : ''}>
                                <c:out value="${edi.nombreEdi}"/>
                            </option>
                        </c:forEach>
                </select>
                <script src="${pageContext.request.contextPath}/js/cargarEdicionInsc.js"></script>
                
                <br> </br>
                <c:if test="${not empty edicion}">
                <div class="dato-space">
                    <h2>Detalles de la edición</h2>
                </div>
                <div class="dato-space">
                <h3>Docentes</h3>
                <ol class="nav-list">
                    <c:choose>
                        <c:when test="${not empty docentes}">
                            <c:forEach var="doc" items="${docentes}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/usuario?nickname=${doc.nickname}">
                                        <c:out value="${doc.nombre}"/> 
                                        <c:out value="${doc.apellido}"/>
                                    </a>
                                </li>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <li class="empty-hint">Sin docentes disponibles</li>
                        </c:otherwise>
                    </c:choose>
                </ol>
                </div>
                
                
                <div class="dato-space">
                    <h4>Fecha de inicio</h4> <p class="detalle">${edicion.fechaInicio}</p>
                </div>      
                <div class="dato-space">
                    <h4>Fecha de finalización</h4> <p class="detalle">${edicion.fechaFin}</p>
                </div>
                <div class="dato-space">
                    <h4>Cupo</h4>
                    <p class="detalle">
                        <c:choose>
                            <c:when test="${edicion.cupo == null}">
                                Libre
                            </c:when>
                            <c:otherwise>
                                <c:out value="${edicion.cupo}"/>
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
                </c:if>
                
                <div class="btn-der">
                    <button type="submit" class="btn btn-primary">Inscribirse</button>
                </div>
            </form>   
        </main>
    </div>
</body>
</html>
