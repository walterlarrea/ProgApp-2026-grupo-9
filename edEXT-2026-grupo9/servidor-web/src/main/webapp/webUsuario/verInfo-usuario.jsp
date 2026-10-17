<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario" %>
<%@ page import="com.grupo9.edext.grupo9.servidor_central.dominio.DataEstudiante" %>
<%@ page import="com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfil de Usuario - edEXT</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body data-context-path="${pageContext.request.contextPath}">

    <!-- Cabezal Fijo -->
    <jsp:include page="/webMiscelanea/header.jsp" />

    <div class="layout-container">
        <!-- Sidebar -->
        <jsp:include page="/webMiscelanea/sidebar.jsp" />

        <!-- Contenido Principal -->
        <main class="main-content">
            <c:if test="${not empty error}">
                <p class="error"><c:out value="${error}"/></p>
            </c:if>

            <c:if test="${not empty usuario}">
                <div class="profile-container">
                    <div class="profile-header">
                        <div class="profile-user-details">
                            <c:choose>
                                <c:when test="${not empty usuario.imagen}">
                                    <c:choose>
                                        <c:when test="${usuario.imagen.startsWith('http://') || usuario.imagen.startsWith('https://')}">
                                            <c:set var="imgSrc" value="${usuario.imagen}" />
                                        </c:when>
                                        <c:otherwise>
                                            <c:url var="imgSrc" value="/imagen">
                                                <c:param name="path" value="${usuario.imagen}" />
                                            </c:url>
                                        </c:otherwise>
                                    </c:choose>
                                    <img src="${imgSrc}" alt="${usuario.nickname}" class="profile-avatar" onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                                    <div class="profile-avatar-placeholder" style="display:none;">
                                        <span>Sin foto</span>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="profile-avatar-placeholder">
                                        <span>Sin foto</span>
                                    </div>
                                </c:otherwise>
                            </c:choose>

                            <div>
                                <h2><c:out value="${usuario.nombre != null ? usuario.nombre : usuario.nickname}"/> <c:out value="${usuario.apellido != null ? usuario.apellido : ''}"/></h2>
                                <p class="text-muted">@<c:out value="${usuario.nickname}"/></p>
                                <span class="badge badge-success" style="margin-top: 0.25rem;"><c:out value="${usuario.tipo != null ? usuario.tipo : 'Usuario'}"/></span>
                            </div>
                        </div>

                        <!-- Botón Seguir / Dejar de seguir si hay usuario logueado y no es el propio perfil -->
                        <c:if test="${not empty sessionScope.usuarioLogueado && sessionScope.usuarioLogueado.nickname != usuario.nickname}">
                            <div>
                                <form action="${pageContext.request.contextPath}/seguir-usuario" method="post" style="display: inline;">
                                    <input type="hidden" name="nickname" value="${usuario.nickname}">
                                    <c:choose>
                                        <c:when test="${loSigue}">
                                            <input type="hidden" name="accion" value="dejar">
                                            <button type="submit" class="btn btn-outline" style="color: #dc2626; border-color: #fca5a5;">
                                                Dejar de seguir
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="hidden" name="accion" value="seguir">
                                            <button type="submit" class="btn btn-primary">
                                                + Seguir usuario
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                </form>
                            </div>
                        </c:if>
                    </div>

                    <!-- Datos del Usuario -->
                    <div class="profile-info-grid">
                        <div class="info-item">
                            <div class="info-label">Nickname</div>
                            <div class="info-value"><c:out value="${usuario.nickname}"/></div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Nombre</div>
                            <div class="info-value"><c:out value="${usuario.nombre != null ? usuario.nombre : '-'}"/></div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Apellido</div>
                            <div class="info-value"><c:out value="${usuario.apellido != null ? usuario.apellido : '-'}"/></div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Correo Electrónico</div>
                            <div class="info-value"><c:out value="${usuario.email}"/></div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Fecha de Nacimiento</div>
                            <div class="info-value">
                                <c:choose>
                                    <c:when test="${usuario.fechaNac != null}">
                                        <c:out value="${usuario.fechaNac.format(java.time.format.DateTimeFormatter.ofPattern('dd/MM/yyyy'))}"/>
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <c:if test="${usuario.tipo == 'Docente'}">
                            <div class="info-item">
                                <div class="info-label">Instituto</div>
                                <div class="info-value"><c:out value="${usuario.nombreInst != null ? usuario.nombreInst : '-'}"/></div>
                            </div>
                        </c:if>
                    </div>

                    <!-- Ediciones de Cursos -->
                    <div class="ediciones-section">
                        <h3>
                            <c:choose>
                                <c:when test="${usuario.tipo == 'Docente'}">Ediciones dictadas</c:when>
                                <c:otherwise>Ediciones inscriptas</c:otherwise>
                            </c:choose>
                        </h3>

                        <%
                            DataUsuario uObj = (DataUsuario) request.getAttribute("usuario");
                            java.util.Set<String> edicionesSet = new java.util.HashSet<>();
                            if (uObj instanceof DataEstudiante) {
                                edicionesSet = ((DataEstudiante) uObj).getEdicionesInscriptas();
                            } else if (uObj instanceof DataDocente) {
                                edicionesSet = ((DataDocente) uObj).getEdiciones();
                            }
                            pageContext.setAttribute("edicionesSet", edicionesSet);
                        %>

                        <c:choose>
                            <c:when test="${not empty edicionesSet}">
                                <ul class="ediciones-list">
                                    <c:forEach var="edi" items="${edicionesSet}">
                                        <li>
                                            <a href="${pageContext.request.contextPath}/edicionCurso?nombre=${edi}" class="edicion-card">
                                                <span class="edicion-title"><c:out value="${edi}"/></span>
                                                <span>→</span>
                                            </a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </c:when>
                            <c:otherwise>
                                <div style="padding: 1rem; color: #64748b;">
                                    <p>No tiene ediciones registradas.</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:if>
        </main>
    </div>

</body>
</html>
