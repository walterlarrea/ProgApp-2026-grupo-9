<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataUsuarioDTO" %>
<%@ page import="com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataEstudianteDTO" %>
<%@ page import="com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataDocenteDTO" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Mi Perfil</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .seguidos-container {
            margin-top: 2rem;
            padding-top: 1.5rem;
            border-top: 1px solid var(--border-color);
        }
        .seguidos-scroll {
            display: flex;
            gap: 1rem;
            overflow-x: auto;
            padding: 0.5rem 0 1rem 0;
        }
        .seguido-card {
            min-width: 160px;
            background: #ffffff;
            border: 1px solid var(--border-color);
            border-radius: 8px;
            padding: 1rem;
            display: flex;
            flex-direction: column;
            align-items: center;
            text-align: center;
            box-shadow: 0 1px 3px rgba(0,0,0,0.05);
        }
        .seguido-avatar {
            width: 56px;
            height: 56px;
            border-radius: 50%;
            object-fit: cover;
            border: 2px solid var(--primary);
            margin-bottom: 0.5rem;
        }
        .seguido-avatar-placeholder {
            width: 56px;
            height: 56px;
            border-radius: 50%;
            background-color: #f1f5f9;
            color: #64748b;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 0.75rem;
            border: 1px dashed #cbd5e1;
            margin-bottom: 0.5rem;
        }
        .seguido-name {
            font-size: 0.9rem;
            font-weight: 600;
            color: var(--text-primary);
            text-decoration: none;
            margin-bottom: 0.25rem;
            max-width: 140px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
        .seguido-name:hover {
            color: var(--primary);
        }
        .seguido-nick {
            font-size: 0.8rem;
            color: var(--text-secondary);
            margin-bottom: 0.75rem;
        }
    </style>
</head>
<body data-context-path="${pageContext.request.contextPath}">

    <!-- Cabezal Fijo -->
    <jsp:include page="/webMiscelanea/header.jsp" />

    <div class="layout-container">
        <!-- Sidebar -->
        <jsp:include page="/webMiscelanea/sidebar.jsp" />

        <!-- Contenido Principal -->
        <main class="main-content">
            <%
                DataUsuarioDTO userObj = (DataUsuarioDTO) request.getAttribute("usuario");
            %>
            <div class="profile-container">
                <c:if test="${param.mensaje != null}">
                    <div style="background-color: #dcfce7; color: #166534; padding: 1rem; border-radius: 6px; margin-bottom: 1.5rem; border: 1px solid #bbf7d0;">
                        <c:out value="${param.mensaje}"/>
                    </div>
                </c:if>

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

                    <div>
                        <a href="${pageContext.request.contextPath}/modificarUsuario" class="btn btn-primary">
                            Modificar Mis Datos
                        </a>
                    </div>
                </div>

                <!-- Datos de Usuario -->
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
                            <% 
                                if (userObj != null && userObj.getFechaNac() != null) {
                                    try {
                                        java.time.LocalDate fn = java.time.LocalDate.parse(userObj.getFechaNac().trim());
                                        out.print(fn.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                                    } catch (Exception e) {
                                        out.print(userObj.getFechaNac());
                                    }
                                } else {
                                    out.print("-");
                                }
                            %>
                        </div>
                    </div>
                    <c:if test="${usuario.tipo == 'Docente'}">
                        <div class="info-item">
                            <div class="info-label">Instituto</div>
                            <div class="info-value"><c:out value="${usuario.nombreInst != null ? usuario.nombreInst : '-'}"/></div>
                        </div>
                    </c:if>
                </div>

                <c:if test="${usuario.tipo == 'Estudiante'}">
                    <section class="ediciones-section">
                        <h3>Programas de formación</h3>
                        <c:choose>
                            <c:when test="${not empty inscripcionesProgramas}">
                                <ul class="ediciones-list">
                                    <c:forEach var="inscripcion" items="${inscripcionesProgramas}">
                                        <li><a class="edicion-card" href="${pageContext.request.contextPath}/programa?nombre=${inscripcion.programaNombre}">
                                            <span class="edicion-title"><c:out value="${inscripcion.programaNombre}"/></span>
                                            <span><c:out value="${inscripcion.fechaInscP}"/></span>
                                        </a></li>
                                    </c:forEach>
                                </ul>
                            </c:when>
                            <c:otherwise><p class="text-muted">No estás inscrito en programas de formación.</p></c:otherwise>
                        </c:choose>
                    </section>
                </c:if>

                <!-- Ediciones de Cursos -->
                <div class="ediciones-section">
                    <h3>
                        <c:choose>
                            <c:when test="${usuario.tipo == 'Docente'}">Ediciones dictadas</c:when>
                            <c:otherwise>Ediciones de curso inscriptas</c:otherwise>
                        </c:choose>
                    </h3>

                    <%
                        java.util.List<String> edicionesSet = new java.util.ArrayList<>();
                        if (userObj instanceof DataEstudianteDTO) {
                            edicionesSet = ((DataEstudianteDTO) userObj).getEdicionesInscriptas();
                        } else if (userObj instanceof DataDocenteDTO) {
                            edicionesSet = ((DataDocenteDTO) userObj).getEdiciones();
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
                                <p>No tienes ediciones de curso registradas por el momento.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Barra de Usuarios que sigo -->
                <div class="seguidos-container">
                    <h3>Usuarios que sigo (<c:out value="${empty usuariosSeguidos ? 0 : usuariosSeguidos.size()}"/>)</h3>
                    <c:choose>
                        <c:when test="${not empty usuariosSeguidos}">
                            <div class="seguidos-scroll">
                                <c:forEach var="seguido" items="${usuariosSeguidos}">
                                    <div class="seguido-card">
                                        <c:choose>
                                            <c:when test="${not empty seguido.imagen}">
                                                <c:choose>
                                                    <c:when test="${seguido.imagen.startsWith('http://') || seguido.imagen.startsWith('https://')}">
                                                        <c:set var="sImg" value="${seguido.imagen}" />
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:url var="sImg" value="/imagen">
                                                            <c:param name="path" value="${seguido.imagen}" />
                                                        </c:url>
                                                    </c:otherwise>
                                                </c:choose>
                                                <img src="${sImg}" alt="${seguido.nickname}" class="seguido-avatar" onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                                                <div class="seguido-avatar-placeholder" style="display:none;">
                                                    <span>Sin foto</span>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="seguido-avatar-placeholder">
                                                    <span>Sin foto</span>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>

                                        <a href="${pageContext.request.contextPath}/usuario?nickname=${seguido.nickname}" class="seguido-name" title="${seguido.nombre} ${seguido.apellido}">
                                            <c:out value="${not empty seguido.nombre ? seguido.nombre : seguido.nickname}"/>
                                        </a>
                                        <span class="seguido-nick">@<c:out value="${seguido.nickname}"/></span>
                                        
                                        <form action="${pageContext.request.contextPath}/seguir-usuario" method="post" style="margin-top: auto; width: 100%;">
                                            <input type="hidden" name="nickname" value="${seguido.nickname}">
                                            <input type="hidden" name="accion" value="dejar">
                                            <button type="submit" class="btn btn-outline btn-sm" style="width: 100%; color: #dc2626; border-color: #fca5a5;">
                                                Dejar de seguir
                                            </button>
                                        </form>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div style="padding: 1rem 0; color: #64748b;">
                                <p>No estás siguiendo a ningún usuario actualmente.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

            </div>
        </main>
    </div>

</body>
</html>
