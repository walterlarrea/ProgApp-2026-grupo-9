<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Ver Evaluaciones</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>

<body>
    <%@ include file="../webMiscelanea/header.jsp" %>
    <div class="layout-container">
        <%@ include file="../webMiscelanea/sidebar.jsp" %>
        <main class="main-content">
            <section class="hero-banner">
                <div class="hero-contenido">
                    <h1>Ver Resultados de Evaluaciones</h1>
                    <p>Consulte el acta de las ediciones cerradas de los cursos en los que participa</p>
                </div>
            </section>

            <c:if test="${not empty mensajeError}">
                <div class="error" role="alert">
                    <c:out value="${mensajeError}" escapeXml="false"/>
                </div>
            </c:if>

            <!-- Selección: Instituto, Curso y Edición (solo ediciones cerradas) -->
            <form action="${pageContext.request.contextPath}/verEvaluacionesDocente" method="get">
                <div class="dato-space">
                    <label for="instituto"><strong>Instituto</strong></label>
                    <br>
                    <select id="instituto" name="instituto" style="width: 30%; padding: 6px; margin-top: 4px;"
                            onchange="var curso = this.form.elements.namedItem('curso'); var edicion = this.form.elements.namedItem('edicion'); if (curso) curso.value = ''; if (edicion) edicion.value = ''; this.form.submit();">
                        <option value="">-- Seleccione un instituto --</option>
                        <c:forEach var="inst" items="${institutos}">
                            <option value="${fn:escapeXml(inst.nombreI)}"
                                ${param.instituto eq inst.nombreI ? 'selected' : ''}>
                                <c:out value="${inst.nombreI}"/>
                            </option>
                        </c:forEach>
                    </select>
                    <c:if test="${empty institutos}">
                        <p class="empty-hint">No hay ediciones cerradas en las que usted participe. Una edición se cierra desde "Ingresar evaluaciones" con el botón "Cerrar Curso".</p>
                    </c:if>
                </div>

                <c:if test="${not empty param.instituto}">
                    <div class="dato-space">
                        <label for="curso"><strong>Curso</strong></label>
                        <br>
                        <select id="curso" name="curso" style="width: 30%; padding: 6px; margin-top: 4px;"
                                onchange="var edicion = this.form.elements.namedItem('edicion'); if (edicion) edicion.value = ''; this.form.submit();">
                            <option value="">-- Seleccione un curso --</option>
                            <c:forEach var="c" items="${cursos}">
                                <option value="${fn:escapeXml(c.nombreCurso)}"
                                    ${param.curso eq c.nombreCurso ? 'selected' : ''}>
                                    <c:out value="${c.nombreCurso}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </c:if>

                <c:if test="${not empty param.curso}">
                    <div class="dato-space">
                        <label for="edicion"><strong>Edición del curso</strong></label>
                        <br>
                        <select id="edicion" name="edicion" style="width: 30%; padding: 6px; margin-top: 4px;" onchange="this.form.submit();">
                            <option value="">-- Seleccione una edición --</option>
                            <c:forEach var="ed" items="${ediciones}">
                                <option value="${fn:escapeXml(ed.nombreEdi)}"
                                    ${param.edicion eq ed.nombreEdi ? 'selected' : ''}>
                                    <c:out value="${ed.nombreEdi}"/> -
                                    <c:out value="${ed.fechaInicio}"/> a
                                    <c:out value="${ed.fechaFin}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </c:if>
            </form>

            <!-- Datos básicos de la edición y acta -->
            <c:if test="${not empty edicionSeleccionada}">
                <section class="dato-space" style="background: #f8fafc; padding: 16px; border-radius: 8px; border: 1px solid #e2e8f0;">
                    <h2 style="margin-bottom: 0.5rem; color: #1e293b;">
                        Edición: <c:out value="${edicionSeleccionada.nombreEdi}"/>
                    </h2>
                    <p><strong>Curso:</strong> <c:out value="${edicionSeleccionada.cursoAsoc.nombreCurso}"/></p>
                    <p><strong>Período:</strong> <c:out value="${edicionSeleccionada.fechaInicio}"/> al <c:out value="${edicionSeleccionada.fechaFin}"/></p>
                    <p><strong>Cupo:</strong> <c:out value="${edicionSeleccionada.cupo != null ? edicionSeleccionada.cupo : 'Sin límite'}"/></p>
                    <p><strong>Estado del curso:</strong>
                        <span class="badge" style="background-color: #ef4444; color: white; padding: 3px 8px; border-radius: 4px;">CERRADA</span>
                    </p>
                </section>

                <div class="dato-space">
                    <h3 style="margin-bottom: 0.5rem; color: #334155;">Acta de evaluaciones</h3>
                    <table class="tabla-gestion" style="width: 100%; border-collapse: collapse; margin-top: 8px;">
                        <thead>
                            <tr style="background: #f1f5f9; text-align: left;">
                                <th style="padding: 10px; border-bottom: 2px solid #cbd5e1;">Nickname</th>
                                <th style="padding: 10px; border-bottom: 2px solid #cbd5e1;">Nombre</th>
                                <th style="padding: 10px; border-bottom: 2px solid #cbd5e1;">Correo</th>
                                <th style="padding: 10px; border-bottom: 2px solid #cbd5e1; text-align: center;">Calificación</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty inscriptosAceptados}">
                                    <c:forEach var="insc" items="${inscriptosAceptados}">
                                        <tr style="border-bottom: 1px solid #e2e8f0;">
                                            <td style="padding: 10px;"><strong><c:out value="${insc.estudiante.nickname}"/></strong></td>
                                            <td style="padding: 10px;"><c:out value="${insc.estudiante.nombre}"/> <c:out value="${insc.estudiante.apellido}"/></td>
                                            <td style="padding: 10px;"><c:out value="${insc.estudiante.email}"/></td>
                                            <td style="padding: 10px; text-align: center;">
                                                <span style="font-weight: bold; font-size: 1.1rem; color: #1e293b;">
                                                    <c:out value="${insc.nota != null ? insc.nota : '-'}"/>
                                                </span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="4" class="empty-table" style="text-align: center; padding: 1.5rem;">
                                            No hay estudiantes aceptados en esta edición de curso.
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </c:if>
        </main>
    </div>
</body>
</html>
