<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Estudiantes inscriptos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>

<body>
    <%@ include file="../webMiscelanea/header.jsp" %>
    <div class="layout-container">
        <%@ include file="../webMiscelanea/sidebar.jsp" %>
        <main class="main-content">
            <section class="hero-banner">
                <div class="hero-contenido">                
                    <h1>Inscripciones ${manejarInscripciones ? '' : 'aceptadas'}</h1>
                    <p>Consulta las inscripciones ${manejarInscripciones ? '' : 'aceptadas'} en las ediciones de tus cursos</p>
                </div>
            </section>
            
            <c:if test="${not empty errorCarga}">
                <p class="error" role="alert"><c:out value="${errorCarga}"/></p>
            </c:if>

            <form action="${pageContext.request.contextPath}${manejarInscripciones ? '/inscripcionesDocente' : '/inscriptosDocente'}" method="get">
                <div class="dato-space">
                    <label for="instituto">Instituto</label>
                    <br>
                    <select id="instituto" name="instituto" style="width: 30%"
                        onchange="var curso = this.form.elements.namedItem('curso'); var edicion = this.form.elements.namedItem('edicion'); if (curso) curso.value = ''; if (edicion) edicion.value = ''; this.form.submit()">
                        <option value="">Seleccione un instituto</option>
                        <c:forEach var="instituto" items="${institutosDoc}">
                            <option value="${fn:escapeXml(instituto.nombreI())}"
                                ${param.instituto eq instituto.nombreI() ? 'selected' : ''}>
                                <c:out value="${instituto.nombreI()}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <c:if test="${not empty param.instituto}">
                    <div class="dato-space">
                        <label for="curso">Curso</label>
                        <br>
                        <select id="curso" name="curso" style="width: 30%"
                            onchange="var edicion = this.form.elements.namedItem('edicion'); if (edicion) edicion.value = ''; this.form.submit()">
                            <option value="">Seleccione un curso</option>
                            <c:forEach var="curso" items="${cursosDoc}">
                                <option value="${fn:escapeXml(curso.nombreCurso())}"
                                    ${param.curso eq curso.nombreCurso() ? 'selected' : ''}>
                                    <c:out value="${curso.nombreCurso()}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </c:if>

                <c:if test="${not empty param.curso}">
                    <div class="dato-space">
                        <label for="edicion">Edición del curso</label>
                        <br>
                        <select id="edicion" name="edicion" onchange="this.form.submit()"  style="width: 30%">
                            <option value="">Seleccione una edición</option>
                            <c:forEach var="edicion" items="${edicionesDoc}">
                                <option value="${fn:escapeXml(edicion.nombreEdi)}"
                                    ${param.edicion eq edicion.nombreEdi ? 'selected' : ''}>
                                    <c:out value="${edicion.nombreEdi}"/> -
                                    <c:out value="${edicion.fechaInicio}"/> a
                                    <c:out value="${edicion.fechaFin}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </c:if>
            </form>

            <c:if test="${not empty edicionSeleccionadaDoc}">
                <section class="dato-space">
                    <h2 style="margin-bottom: 1rem">Edición seleccionada: <c:out value="${edicionSeleccionadaDoc.nombreEdi}"/></h2>
                    <p><b>Curso:</b> <c:out value="${edicionSeleccionadaDoc.cursoAsoc.nombreCurso()}"/></p>
                    <p><b>Fecha de inicio:</b> <c:out value="${edicionSeleccionadaDoc.fechaInicio}"/></p>
                    <p><b>Fecha de finalización:</b> <c:out value="${edicionSeleccionadaDoc.fechaFin}"/></p>
                    <p><b>Cupo:</b> <c:out value="${edicionSeleccionadaDoc.cupo}"/></p>
                </section>

                <div class="table-container">
                    <table class="inscriptos-table">
                        <thead>
                            <tr>
                                <th>Estudiante</th>
                                <th>Fecha de inscripción</th>
                                <th>Estado</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty inscriptosDoc}">
                                    <c:forEach var="insc" items="${inscriptosDoc}">
                                        <tr>
                                            <td class="nav-list">
                                                <a href="${pageContext.request.contextPath}/usuario?nickname=${insc.estudiante.nickname}">
                                                    <c:out value="${insc.estudiante.nombre}"/>
                                                    <c:out value="${insc.estudiante.apellido}"/>
                                                </a>
                                            </td>
                                            <td><c:out value="${insc.fechaInscE}"/></td>
                                            <c:if test="${!manejarInscripciones}">
                                                <td>
                                                    <span class="estado-badge"><c:out value="${insc.estado}"/></span>
                                                </td>
                                            </c:if>
                                            <c:if test="${manejarInscripciones}">
                                                <td>
<!--                                                    <input type="radio" id="${insc.estudiante.nombre}-estado-inscripto" name="estado" value="inscripto" />
                                                    <label for="${insc.estudiante.nombre}-estado-inscripto">Inscripto</label>-->
                                                    
                                                    <input type="radio" id="estado-aceptado-${insc.estudiante.nickname}" name="estado-${insc.estudiante.nickname}" value="aceptado" data-nickname="${insc.estudiante.nickname}" ${insc.estado == 'ACEPTADA' ? 'checked' : ''} />
                                                    <label for="estado-aceptado-${insc.estudiante.nickname}">Aceptar</label>
                                                    
                                                    <input type="radio" id="estado-rechazado-${insc.estudiante.nickname}" name="estado-${insc.estudiante.nickname}" value="rechazado" data-nickname="${insc.estudiante.nickname}" ${insc.estado == 'RECHAZADA' ? 'checked' : ''} />
                                                    <label for="estado-rechazado-${insc.estudiante.nickname}">Rechazar</label>
                                                </td>
                                            </c:if>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="3" class="empty-table">
                                            <c:choose>
                                                <c:when test="${manejarInscripciones}">
                                                    No hay inscripciones para esta edición. 
                                                </c:when>    
                                                <c:otherwise>
                                                    No hay inscripciones aceptadas para esta edición.
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                    <c:if test="${manejarInscripciones}">
                        <div class="dato-space btn-cambios-inscripcion">
                            <button type="button" id="btnGuardarSelecciones" class="btn btn-primary">Guardar selecciones</button>
                        </div>
                    </c:if>
                </div>
            </c:if>
        </main>
    </div>
    <script
        type="text/javascript"
        src="${pageContext.request.contextPath}/js/manejarInscripcionesDocente.js"></script>
</body>
</html>
