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
    <title>Mi Perfil - edEXT</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .profile-container {
            background: #ffffff;
            border: 1px solid #e2e8f0;
            border-radius: 10px;
            padding: 2rem;
            margin-bottom: 2rem;
            box-shadow: 0 1px 3px rgba(0,0,0,0.05);
        }
        .profile-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding-bottom: 1.5rem;
            border-bottom: 1px solid #e2e8f0;
            margin-bottom: 1.5rem;
            flex-wrap: wrap;
            gap: 1.5rem;
        }
        .profile-user-details {
            display: flex;
            align-items: center;
            gap: 1.5rem;
        }
        .profile-avatar {
            width: 96px;
            height: 96px;
            border-radius: 50%;
            object-fit: cover;
            border: 3px solid #2563eb;
        }
        .profile-avatar-placeholder {
            width: 96px;
            height: 96px;
            border-radius: 50%;
            background-color: #f1f5f9;
            color: #64748b;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            border: 2px dashed #cbd5e1;
            font-size: 0.75rem;
            font-weight: 600;
            text-align: center;
            padding: 4px;
        }
        .profile-info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 1.25rem;
            margin-bottom: 2rem;
        }
        .info-item {
            background: #f8fafc;
            padding: 1rem;
            border-radius: 8px;
            border: 1px solid #f1f5f9;
        }
        .info-label {
            font-size: 0.8rem;
            text-transform: uppercase;
            color: #64748b;
            font-weight: 600;
            margin-bottom: 0.25rem;
        }
        .info-value {
            font-size: 1rem;
            font-weight: 500;
            color: #1e293b;
        }
        .ediciones-section h3 {
            font-size: 1.2rem;
            margin-bottom: 1rem;
            color: #1e293b;
        }
        .ediciones-list {
            list-style: none;
            padding: 0;
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 1rem;
        }
        .edicion-card {
            background: #ffffff;
            border: 1px solid #cbd5e1;
            border-radius: 8px;
            padding: 1rem;
            display: flex;
            align-items: center;
            justify-content: space-between;
            transition: transform 0.2s, box-shadow 0.2s;
            text-decoration: none;
            color: inherit;
        }
        .edicion-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 10px rgba(37, 99, 235, 0.1);
            border-color: #2563eb;
        }
        .edicion-title {
            font-weight: 600;
            color: #2563eb;
        }
    </style>
</head>
<body>

    <!-- Cabezal Fijo -->
    <jsp:include page="/webMiscelanea/header.jsp" />

    <div class="layout-container">
        <!-- Sidebar -->
        <jsp:include page="/webMiscelanea/sidebar.jsp" />

        <!-- Contenido Principal -->
        <main class="main-content">
            <div class="profile-container">
                <c:if test="${param.mensaje != null}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert" style="background-color: #dcfce7; color: #166534; padding: 1rem; border-radius: 6px; margin-bottom: 1.5rem;">
                        <i class="fas fa-check-circle me-2"></i><c:out value="${param.mensaje}"/>
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
                                        <c:set var="imgSrc" value="${pageContext.request.contextPath}/imagen?path=${usuario.imagen}" />
                                    </c:otherwise>
                                </c:choose>
                                <img src="${imgSrc}" alt="${usuario.nickname}" class="profile-avatar" onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                                <div class="profile-avatar-placeholder" style="display:none;">
                                    <i class="fas fa-user-slash fa-2x mb-1"></i>
                                    <span>Sin foto de perfil</span>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="profile-avatar-placeholder">
                                    <i class="fas fa-user-slash fa-2x mb-1"></i>
                                    <span>Sin foto de perfil</span>
                                </div>
                            </c:otherwise>
                        </c:choose>

                        <div>
                            <h2><c:out value="${usuario.nombre != null ? usuario.nombre : usuario.nickname}"/> <c:out value="${usuario.apellido != null ? usuario.apellido : ''}"/></h2>
                            <p class="text-muted">@<c:out value="${usuario.nickname}"/></p>
                            <span class="badge badge-success mt-1"><c:out value="${usuario.tipo != null ? usuario.tipo : 'Usuario'}"/></span>
                        </div>
                    </div>

                    <div>
                        <a href="${pageContext.request.contextPath}/modificarUsuario" class="btn btn-primary" style="display: inline-flex; align-items: center; gap: 8px;">
                            <i class="fas fa-user-edit"></i> Modificar Mis Datos
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
                        <div class="info-value"><c:out value="${usuario.fechaNac != null ? usuario.fechaNac : '-'}"/></div>
                    </div>
                </div>

                <!-- Ediciones de Cursos -->
                <div class="ediciones-section">
                    <h3>
                        <i class="fas fa-bookmark text-primary me-2"></i>
                        <c:choose>
                            <c:when test="${usuario.tipo == 'Docente'}">Ediciones dictadas</c:when>
                            <c:otherwise>Ediciones de curso inscriptas</c:otherwise>
                        </c:choose>
                    </h3>

                    <%
                        DataUsuario userObj = (DataUsuario) request.getAttribute("usuario");
                        java.util.Set<String> edicionesSet = new java.util.HashSet<>();
                        if (userObj instanceof DataEstudiante) {
                            edicionesSet = ((DataEstudiante) userObj).getEdicionesInscriptas();
                        } else if (userObj instanceof DataDocente) {
                            edicionesSet = ((DataDocente) userObj).getEdiciones();
                        }
                        pageContext.setAttribute("edicionesSet", edicionesSet);
                    %>

                    <c:choose>
                        <c:when test="${not empty edicionesSet}">
                            <ul class="ediciones-list">
                                <c:forEach var="edi" items="${edicionesSet}">
                                    <li>
                                        <a href="${pageContext.request.contextPath}/edicionCurso?nombre=${edi}" class="edicion-card">
                                            <span class="edicion-title"><i class="fas fa-graduation-cap me-2"></i><c:out value="${edi}"/></span>
                                            <i class="fas fa-chevron-right text-muted"></i>
                                        </a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state" style="padding: 1.5rem;">
                                <p class="text-muted">No tienes ediciones de curso registradas por el momento.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </main>
    </div>

</body>
</html>
