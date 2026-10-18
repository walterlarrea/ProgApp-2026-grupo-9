<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${programa.nombre}"/> - edEXT</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <jsp:include page="/webMiscelanea/header.jsp" />
    <div class="layout-container">
        <jsp:include page="/webMiscelanea/sidebar.jsp" />
        <main class="main-content">
            <c:if test="${not empty param.mensaje}"><p class="success"><c:out value="${param.mensaje}"/></p></c:if>
            <c:if test="${not empty param.error}"><p class="error"><c:out value="${param.error}"/></p></c:if>
            <section class="hero-banner">
                <c:if test="${not empty programa.imagen}"><img src="${pageContext.request.contextPath}/imagen?path=${programa.imagen}" alt="" style="max-width: 100%; max-height: 280px; object-fit: cover;"></c:if>
                <h1><c:out value="${programa.nombre}"/></h1>
                <p><c:out value="${programa.descripcion}"/></p>
                <p><c:out value="${programa.fechaInicio}"/> - <c:out value="${programa.fechaFin}"/></p>
            </section>
            <section class="ediciones-section">
                <h2>Cursos incluidos</h2>
                <c:choose>
                    <c:when test="${not empty programa.cursos}">
                        <ul class="ediciones-list">
                            <c:forEach var="curso" items="${programa.cursos}">
                                <li><a class="edicion-card" href="${pageContext.request.contextPath}/curso?nombre=${curso.nombreCurso()}"><c:out value="${curso.nombreCurso()}"/></a></li>
                            </c:forEach>
                        </ul>
                    </c:when>
                    <c:otherwise><p>Este programa aún no tiene cursos asociados.</p></c:otherwise>
                </c:choose>
            </section>
            <c:if test="${not empty sessionScope.usuarioLogueado}">
                <c:choose>
                    <c:when test="${sessionScope.usuarioLogueado.tipo == 'Estudiante' && not inscripto}">
                        <form action="${pageContext.request.contextPath}/programa" method="post">
                            <input type="hidden" name="nombre" value="${programa.nombre}">
                            <input type="hidden" name="accion" value="inscribir">
                            <button type="submit" class="btn btn-primary">Inscribirme</button>
                        </form>
                    </c:when>
                    <c:when test="${inscripto}"><p>Ya estás inscrito en este programa.</p></c:when>
                </c:choose>
            </c:if>
            <c:if test="${puedeGestionar and not empty cursosDisponibles}">
                <form action="${pageContext.request.contextPath}/programa" method="post">
                    <input type="hidden" name="nombre" value="${programa.nombre}">
                    <input type="hidden" name="accion" value="agregarCurso">
                    <label for="curso">Agregar curso</label>
                    <select id="curso" name="curso" required>
                        <c:forEach var="curso" items="${cursosDisponibles}">
                            <option value="${curso.nombreCurso()}"><c:out value="${curso.nombreCurso()}"/></option>
                        </c:forEach>
                    </select>
                    <button type="submit" class="btn btn-primary">Asociar</button>
                </form>
            </c:if>
        </main>
    </div>
</body>
</html>