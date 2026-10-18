<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${programa.nombre()}"/> - edEXT</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <jsp:include page="/webMiscelanea/header.jsp" />
    <div class="layout-container">
        <jsp:include page="/webMiscelanea/sidebar.jsp" />
        <main class="main-content">
            <c:if test="${not empty param.mensaje}"><p class="success"><c:out value="${param.mensaje}"/></p></c:if>
            <c:if test="${not empty param.error}"><p class="error"><c:out value="${param.error}"/></p></c:if>
            <section class="hero-banner programa-hero">
                <div class="programa-imagen-container">
                    <c:choose>
                        <c:when test="${not empty programa.imagen()}">
                            <c:url var="programaImagenUrl" value="/imagen">
                                <c:param name="path" value="${programa.imagen()}"/>
                            </c:url>
                            <img class="programa-imagen" src="${programaImagenUrl}" alt="Imagen del programa"
                                 onerror="this.hidden = true; this.nextElementSibling.hidden = false;">
                            <div class="programa-imagen-fallback" hidden role="img" aria-label="Imagen del programa no disponible">
                                <span>Imagen no disponible</span>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="programa-imagen-fallback" role="img" aria-label="El programa no tiene imagen">
                                <span>Imagen no disponible</span>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="programa-hero-contenido">
                    <h1><c:out value="${programa.nombre()}"/></h1>
                    <p><c:out value="${programa.descripcion()}"/></p>
                </div>
            </section>
            <section class="programa-datos" aria-label="Información del programa">
                <div class="programa-dato">
                    <h2>Inicio</h2>
                    <p><c:out value="${programa.fechaInicio()}"/></p>
                </div>
                <div class="programa-dato">
                    <h2>Finalización</h2>
                    <p><c:out value="${programa.fechaFin()}"/></p>
                </div>
                <div class="programa-dato">
                    <h2>Creado</h2>
                    <p><c:out value="${programa.fechaDeCreacion()}"/></p>
                </div>
            </section>
            <section class="ediciones-section">
                <h2>Cursos incluidos</h2>
                <c:choose>
                    <c:when test="${not empty programa.cursos()}">
                        <ul class="ediciones-list">
                            <c:forEach var="curso" items="${programa.cursos()}">
                                <c:url var="cursoUrl" value="/curso">
                                    <c:param name="nombre" value="${curso.nombreCurso()}"/>
                                </c:url>
                                <li class="programa-curso-card">
                                    <a class="programa-curso-titulo" href="${cursoUrl}">
                                        <c:out value="${curso.nombreCurso()}"/>
                                    </a>
                                    <!-- <c:if test="${not empty curso.descCurso()}">
                                        <p class="programa-curso-descripcion"><c:out value="${curso.descCurso()}"/></p>
                                    </c:if> -->
                                    <dl class="programa-curso-datos">
                                        <c:if test="${not empty curso.instituto()}">
                                            <div><dt>Instituto</dt><dd><c:out value="${curso.instituto().nombreI()}"/></dd></div>
                                        </c:if>
                                        <div><dt>Duración</dt><dd><c:out value="${curso.duracion()}"/> semanas</dd></div>
                                        <div><dt>Horas</dt><dd><c:out value="${curso.cantHoras()}"/></dd></div>
                                        <div><dt>Créditos</dt><dd><c:out value="${curso.cantCred()}"/></dd></div>
                                        <c:if test="${not empty curso.categorias()}">
                                            <div>
                                                <dt>Categorías</dt>
                                                <dd>
                                                    <c:forEach var="categoria" items="${curso.categorias()}" varStatus="estado">
                                                        <c:if test="${estado.index gt 0}">, </c:if>
                                                        <c:out value="${categoria}"/>
                                                    </c:forEach>
                                                </dd>
                                            </div>
                                        </c:if>
                                    </dl>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:when>
                    <c:otherwise><p>Este programa aún no tiene cursos asociados.</p></c:otherwise>
                </c:choose>
            </section>
            <!-- <c:if test="${not empty sessionScope.usuarioLogueado}">
                <c:choose>
                    <c:when test="${sessionScope.usuarioLogueado.tipo == 'Estudiante' && not inscripto}">
                        <form action="${pageContext.request.contextPath}/programa" method="post">
                            <input type="hidden" name="nombre" value="${programa.nombre()}">
                            <input type="hidden" name="accion" value="inscribir">
                            <button type="submit" class="btn btn-primary">Inscribirme</button>
                        </form>
                    </c:when>
                    <c:when test="${inscripto}"><p>Ya estás inscrito en este programa.</p></c:when>
                </c:choose>
            </c:if>
            <c:if test="${puedeGestionar and not empty cursosDisponibles}">
                <form action="${pageContext.request.contextPath}/programa" method="post">
                    <input type="hidden" name="nombre" value="${programa.nombre()}">
                    <input type="hidden" name="accion" value="agregarCurso">
                    <label for="curso">Agregar curso</label>
                    <select id="curso" name="curso" required>
                        <c:forEach var="curso" items="${cursosDisponibles}">
                            <option value="${curso.nombreCurso()}"><c:out value="${curso.nombreCurso()}"/></option>
                        </c:forEach>
                    </select>
                    <button type="submit" class="btn btn-primary">Asociar</button>
                </form>
            </c:if> -->
        </main>
    </div>
</body>
</html>