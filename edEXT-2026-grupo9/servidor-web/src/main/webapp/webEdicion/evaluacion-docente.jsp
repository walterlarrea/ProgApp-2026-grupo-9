<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Ingresar Evaluaciones</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>

<body>
    <%@ include file="../webMiscelanea/header.jsp" %>
    <div class="layout-container">
        <%@ include file="../webMiscelanea/sidebar.jsp" %>
        <main class="main-content">
            <section class="hero-banner">
                <div class="hero-contenido">
                    <h1>Ingresar Resultados de Evaluaciones</h1>
                    <p>Califique a los estudiantes aceptados en la edición vigente de sus cursos</p>
                </div>
            </section>

            <c:if test="${not empty mensajeExito}">
                <div class="mensaje-exito" role="status">
                    <c:out value="${mensajeExito}" escapeXml="false"/>
                </div>
            </c:if>

            <c:if test="${not empty mensajeError}">
                <div class="error" role="alert">
                    <c:out value="${mensajeError}" escapeXml="false"/>
                </div>
            </c:if>

            <!-- Formulario de Selección: Instituto, Curso y Edición -->
            <form action="${pageContext.request.contextPath}/evaluacionDocente" method="get">
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
                                    ${(param.edicion eq ed.nombreEdi or param.nombreEdi eq ed.nombreEdi) ? 'selected' : ''}>
                                    <c:out value="${ed.nombreEdi}"/> -
                                    <c:out value="${ed.fechaInicio}"/> a
                                    <c:out value="${ed.fechaFin}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </c:if>
            </form>

            <!-- Datos de la Edición Seleccionada -->
            <c:if test="${not empty edicionSeleccionada}">
                <section class="dato-space" style="background: #f8fafc; padding: 16px; border-radius: 8px; border: 1px solid #e2e8f0;">
                    <h2 style="margin-bottom: 0.5rem; color: #1e293b;">
                        Edición: <c:out value="${edicionSeleccionada.nombreEdi}"/>
                    </h2>
                    <p><strong>Curso:</strong> <c:out value="${edicionSeleccionada.cursoAsoc.nombreCurso}"/></p>
                    <p><strong>Período:</strong> <c:out value="${edicionSeleccionada.fechaInicio}"/> al <c:out value="${edicionSeleccionada.fechaFin}"/></p>
                    <p><strong>Cupo:</strong> <c:out value="${edicionSeleccionada.cupo != null ? edicionSeleccionada.cupo : 'Libre'}"/></p>
                    <p><strong>Estado del curso:</strong>
                        <c:choose>
                            <c:when test="${edicionCerrada}">
                                <span id="badgeEstadoCurso" class="badge" style="background-color: #ef4444; color: white; padding: 3px 8px; border-radius: 4px;">CERRADA</span>
                            </c:when>
                            <c:otherwise>
                                <span id="badgeEstadoCurso" class="badge" style="background-color: #10b981; color: white; padding: 3px 8px; border-radius: 4px;">ABIERTA</span>
                            </c:otherwise>
                        </c:choose>
                    </p>
                </section>

                <c:choose>
                    <c:when test="${!docenteParticipa}">
                        <div class="alerta-info dato-space" style="background: #fef2f2; border-color: #fecaca; color: #991b1b;">
                            <p>Usted no figura como docente asignado a esta edición de curso. Solo los docentes participantes pueden registrar evaluaciones.</p>
                        </div>
                    </c:when>

                    <c:otherwise>
                        <!-- Tabla de Estudiantes Aceptados y Formulario AJAX -->
                        <form id="formEvaluaciones" action="${pageContext.request.contextPath}/evaluacionDocente" method="post">
                            <input type="hidden" name="instituto" value="${fn:escapeXml(param.instituto)}" />
                            <input type="hidden" name="curso" value="${fn:escapeXml(param.curso)}" />
                            <input type="hidden" name="edicion" value="${fn:escapeXml(edicionSeleccionada.nombreEdi)}" />
                            <input type="hidden" name="nombreEdi" value="${fn:escapeXml(edicionSeleccionada.nombreEdi)}" />

                                    <div class="dato-space">
                                        <h3 style="margin-bottom: 0.5rem; color: #334155;">Estudiantes Aceptados en la Edición</h3>
                                        <table class="tabla-gestion" style="width: 100%; border-collapse: collapse; margin-top: 8px;">
                                            <thead>
                                                <tr style="background: #f1f5f9; text-align: left;">
                                                    <th style="padding: 10px; border-bottom: 2px solid #cbd5e1;">Nickname</th>
                                                    <th style="padding: 10px; border-bottom: 2px solid #cbd5e1;">Nombre</th>
                                                    <th style="padding: 10px; border-bottom: 2px solid #cbd5e1;">Correo</th>
                                                    <th style="padding: 10px; border-bottom: 2px solid #cbd5e1; text-align: center;">Calificación (1 - 12)</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${not empty inscriptosAceptados}">
                                                        <c:forEach var="insc" items="${inscriptosAceptados}">
                                                            <tr style="border-bottom: 1px solid #e2e8f0;">
                                                                <td style="padding: 10px;">
                                                                    <strong><c:out value="${insc.estudiante.nickname}"/></strong>
                                                                </td>
                                                                <td style="padding: 10px;">
                                                                    <c:out value="${insc.estudiante.nombre}"/> <c:out value="${insc.estudiante.apellido}"/>
                                                                </td>
                                                                <td style="padding: 10px;">
                                                                    <c:out value="${insc.estudiante.email}"/>
                                                                </td>
                                                                <td style="padding: 10px; text-align: center;">
                                                                    <c:choose>
                                                                        <c:when test="${edicionCerrada}">
                                                                            <span style="font-weight: bold; font-size: 1.1rem; color: #1e293b;">
                                                                                <c:out value="${insc.nota != null ? insc.nota : '-'}"/>
                                                                            </span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <input type="number"
                                                                                   name="nota-${fn:escapeXml(insc.estudiante.nickname)}"
                                                                                   class="input-nota"
                                                                                   min="1"
                                                                                   max="12"
                                                                                   step="1"
                                                                                   value="${insc.nota != null ? insc.nota : ''}"
                                                                                   placeholder="1 - 12"
                                                                                   style="width: 80px; text-align: center; padding: 4px; border: 1px solid #cbd5e1; border-radius: 4px;" />
                                                                        </c:otherwise>
                                                                    </c:choose>
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

                                    <c:if test="${not empty inscriptosAceptados}">
                                        <div id="contenedorAcciones" class="dato-space" style="margin-top: 1.5rem; display: flex; gap: 12px; align-items: center;">
                                            <c:if test="${!edicionCerrada}">
                                                <button type="button" id="btnGuardarEvaluaciones" class="btn btn-primary" onclick="ejecutarAccionAjax('guardar')">
                                                    Guardar Evaluaciones
                                                </button>
                                                <button type="button" id="btnCerrarCurso" class="btn btn-outline" onclick="ejecutarAccionAjax('cerrar')">
                                                    Cerrar Curso
                                                </button>
                                            </c:if>
                                            <c:if test="${edicionCerrada}">
                                                <p style="color: #64748b; font-style: italic;">
                                                    * Esta edición de curso ya ha sido cerrada. No es posible modificar las notas ingresadas.
                                                </p>
                                            </c:if>
                                        </div>
                                    </c:if>
                                </form>
                            </c:otherwise>
                        </c:choose>
            </c:if>
        </main>
    </div>

    <%-- Componente parametrizable popup-mensaje.jsp para avisos y notificaciones en pantalla --%>
    <%@ include file="../webMiscelanea/popup-mensaje.jsp" %>

    <script src="${pageContext.request.contextPath}/js/evaluacionDocente.js"></script>
</body>
</html>

