<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataUsuarioDTO" %>
<%@ page import="com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataEstudianteDTO" %>
<%@ page import="com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataDocenteDTO" %>
<%@ page import="com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataEdicionCursoDTO" %>
<%@ page import="com.grupo9.edext.grupo9.servidor_web.web_service_client.curso.DataCursoDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Mi Perfil</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .tab-pane {
            display: none;
        }
        .tab-pane.active {
            display: block !important;
            animation: tabFadeIn 0.25s ease-in-out;
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
                List<DataUsuarioDTO> seguidosList = (List<DataUsuarioDTO>) request.getAttribute("usuariosSeguidos");
                List<DataUsuarioDTO> seguidoresList = (List<DataUsuarioDTO>) request.getAttribute("usuariosSeguidores");
                Set<String> seguidosNicks = (Set<String>) request.getAttribute("seguidosNicks");
                int cantSeguidos = (seguidosList != null) ? seguidosList.size() : 0;
                int cantSeguidores = (seguidoresList != null) ? seguidoresList.size() : 0;
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

                <!-- Pestañas de Navegación del Perfil -->
                <nav class="profile-tabs-header" role="tablist">
                    <button type="button" class="profile-tab-btn active" data-tab="tab-general" onclick="openTab('tab-general', this)" role="tab" aria-selected="true">
                        Información General
                    </button>
                    <button type="button" class="profile-tab-btn" data-tab="tab-cursos" onclick="openTab('tab-cursos', this)" role="tab" aria-selected="false">
                        <c:choose>
                            <c:when test="${usuario.tipo == 'Docente'}">Mis Cursos y Ediciones</c:when>
                            <c:otherwise>Cursos inscriptos</c:otherwise>
                        </c:choose>
                    </button>
                    <button type="button" class="profile-tab-btn" data-tab="tab-seguidores" onclick="openTab('tab-seguidores', this)" role="tab" aria-selected="false">
                        Seguidores <span class="profile-tab-badge" id="badge-seguidores"><%= cantSeguidores %></span>
                    </button>
                    <button type="button" class="profile-tab-btn" data-tab="tab-seguidos" onclick="openTab('tab-seguidos', this)" role="tab" aria-selected="false">
                        Seguidos <span class="profile-tab-badge count-seguidos" id="badge-seguidos"><%= cantSeguidos %></span>
                    </button>
                </nav>

                <!-- TAB 1: INFORMACIÓN GENERAL -->
                <section id="tab-general" class="tab-pane active" role="tabpanel">
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
                        <c:if test="${usuario.tipo == 'Docente'}">
                            <div class="info-item">
                                <div class="info-label">Instituto</div>
                                <div class="info-value"><c:out value="${usuario.nombreInst != null ? usuario.nombreInst : '-'}"/></div>
                            </div>
                        </c:if>
                    </div>
                </section>

                <!-- TAB 2: CURSOS -->
                <section id="tab-cursos" class="tab-pane" role="tabpanel">
                    <c:choose>
                        <c:when test="${usuario.tipo == 'Docente'}">
                            <!-- Vista para Docente -->
                            <c:if test="${not empty listaCursosDocente}">
                                <h3 style="margin-bottom: 0.75rem; color: var(--text-primary);">Cursos Dictados</h3>
                                <div class="cursos-perfil-grid" style="margin-bottom: 2rem;">
                                    <c:forEach var="cur" items="${listaCursosDocente}">
                                        <div class="curso-perfil-card">
                                            <div class="curso-perfil-header">
                                                <img src="${pageContext.request.contextPath}/imagen?path=${cur.imagen}" alt="${cur.nombreCurso}" class="curso-perfil-img" onerror="this.src='${pageContext.request.contextPath}/imagenes/curso/curso-placeholder.jpg';">
                                                <div>
                                                    <a href="${pageContext.request.contextPath}/curso?nombre=${cur.nombreCurso}" class="curso-perfil-title">
                                                        <c:out value="${cur.nombreCurso}"/>
                                                    </a>
                                                    <span class="text-muted" style="font-size: 0.8rem;"><c:out value="${cur.instituto != null ? cur.instituto.nombreI : ''}"/></span>
                                                </div>
                                            </div>
                                            <p style="font-size: 0.85rem; color: #475569; margin-bottom: 1rem;">
                                                <c:out value="${cur.descCurso}"/>
                                            </p>
                                            <div class="curso-perfil-links">
                                                <a href="${pageContext.request.contextPath}/curso?nombre=${cur.nombreCurso}" class="btn btn-outline btn-sm" style="width: 100%; text-align: center;">
                                                    Ver Curso
                                                </a>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:if>

                            <h3 style="margin-bottom: 0.75rem; color: var(--text-primary);">Ediciones de Curso Asignadas</h3>
                        </c:when>
                        <c:otherwise>
                            <!-- Vista para Estudiante -->
                            <h3 style="margin-bottom: 0.75rem; color: var(--text-primary);">Cursos y Ediciones en los que estás inscripto</h3>
                        </c:otherwise>
                    </c:choose>

                    <c:choose>
                        <c:when test="${not empty listaEdiciones}">
                            <div class="cursos-perfil-grid">
                                <c:forEach var="edicion" items="${listaEdiciones}">
                                    <div class="curso-perfil-card">
                                        <div>
                                            <div class="curso-perfil-header">
                                                <c:choose>
                                                    <c:when test="${not empty edicion.imagen}">
                                                        <img src="${pageContext.request.contextPath}/imagen?path=${edicion.imagen}" alt="${edicion.nombreEdi}" class="curso-perfil-img" onerror="this.src='${pageContext.request.contextPath}/imagenes/edicionCurso/edicion-placeholder.jpg';">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="${pageContext.request.contextPath}/imagenes/edicionCurso/edicion-placeholder.jpg" alt="${edicion.nombreEdi}" class="curso-perfil-img">
                                                    </c:otherwise>
                                                </c:choose>
                                                <div>
                                                    <c:if test="${not empty edicion.cursoAsoc}">
                                                        <a href="${pageContext.request.contextPath}/curso?nombre=${edicion.cursoAsoc.nombreCurso}" class="curso-perfil-title" title="Ver Curso">
                                                            <c:out value="${edicion.cursoAsoc.nombreCurso}"/>
                                                        </a>
                                                    </c:if>
                                                    <div class="curso-perfil-edicion">
                                                        Edición: <strong><c:out value="${edicion.nombreEdi}"/></strong>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="curso-perfil-fechas">
                                                <span>🗓 Vigencia: <c:out value="${edicion.fechaInicio != null ? edicion.fechaInicio : '-'}"/> al <c:out value="${edicion.fechaFin != null ? edicion.fechaFin : '-'}"/></span>
                                                <c:if test="${edicion.cupo != null}">
                                                    <br><span>👥 Cupo: <c:out value="${edicion.cupo}"/></span>
                                                </c:if>
                                            </div>
                                        </div>

                                        <div class="curso-perfil-links">
                                            <c:if test="${not empty edicion.cursoAsoc}">
                                                <a href="${pageContext.request.contextPath}/curso?nombre=${edicion.cursoAsoc.nombreCurso}" class="btn btn-outline btn-sm" style="flex: 1; text-align: center;">
                                                    Curso
                                                </a>
                                            </c:if>
                                            <a href="${pageContext.request.contextPath}/edicionCurso?nombre=${edicion.nombreEdi}" class="btn btn-primary btn-sm" style="flex: 1; text-align: center;">
                                                Ver Edición
                                            </a>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div style="padding: 2rem; background: #f8fafc; border-radius: 8px; border: 1px dashed var(--border-color); text-align: center;">
                                <p style="color: #64748b; margin-bottom: 1rem;">No tienes cursos o ediciones registrados en este momento.</p>
                                <a href="${pageContext.request.contextPath}/busqueda" class="btn btn-primary btn-sm">Explorar Cursos</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </section>

                <!-- TAB 3: SEGUIDORES (Los que me siguen) -->
                <section id="tab-seguidores" class="tab-pane" role="tabpanel">
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                        <h3 style="color: var(--text-primary); margin: 0;">
                            Usuarios que te siguen (<span id="count-seguidores"><%= cantSeguidores %></span>)
                        </h3>
                    </div>

                    <c:choose>
                        <c:when test="${not empty usuariosSeguidores}">
                            <div class="usuarios-tab-grid">
                                <c:forEach var="seguidor" items="${usuariosSeguidores}">
                                    <div class="usuario-card-tab" id="card-seguidor-${seguidor.nickname}">
                                        <c:choose>
                                            <c:when test="${not empty seguidor.imagen}">
                                                <c:choose>
                                                    <c:when test="${seguidor.imagen.startsWith('http://') || seguidor.imagen.startsWith('https://')}">
                                                        <c:set var="segImg" value="${seguidor.imagen}" />
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:url var="segImg" value="/imagen">
                                                            <c:param name="path" value="${seguidor.imagen}" />
                                                        </c:url>
                                                    </c:otherwise>
                                                </c:choose>
                                                <img src="${segImg}" alt="${seguidor.nickname}" class="usuario-tab-avatar" onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                                                <div class="usuario-tab-placeholder" style="display:none;">
                                                    <span>Sin foto</span>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="usuario-tab-placeholder">
                                                    <span>Sin foto</span>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>

                                        <a href="${pageContext.request.contextPath}/usuario?nickname=${seguidor.nickname}" class="usuario-tab-name" title="${seguidor.nombre} ${seguidor.apellido}">
                                            <c:out value="${not empty seguidor.nombre ? seguidor.nombre : seguidor.nickname}"/>
                                            <c:if test="${not empty seguidor.apellido}"> <c:out value="${seguidor.apellido}"/></c:if>
                                        </a>
                                        <span class="usuario-tab-nick">@<c:out value="${seguidor.nickname}"/></span>
                                        <span class="usuario-tab-badge-follow">Te sigue</span>

                                        <div class="usuario-tab-actions">
                                            <%
                                                DataUsuarioDTO itemSeg = (DataUsuarioDTO) pageContext.getAttribute("seguidor");
                                                boolean yoLoSigo = seguidosNicks != null && itemSeg != null && seguidosNicks.contains(itemSeg.getNickname());
                                            %>
                                            <form action="${pageContext.request.contextPath}/seguir-usuario" method="post" class="form-seguir-ajax" style="width: 100%;">
                                                <input type="hidden" name="nickname" value="${seguidor.nickname}">
                                                <% if (yoLoSigo) { %>
                                                    <input type="hidden" name="accion" value="dejar">
                                                    <button type="submit" class="btn btn-outline btn-sm" style="width: 100%; color: #dc2626; border-color: #fca5a5;">
                                                        Dejar de seguir
                                                    </button>
                                                <% } else { %>
                                                    <input type="hidden" name="accion" value="seguir">
                                                    <button type="submit" class="btn btn-primary btn-sm" style="width: 100%;">
                                                        + Seguir
                                                    </button>
                                                <% } %>
                                            </form>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div style="padding: 2rem; background: #f8fafc; border-radius: 8px; border: 1px dashed var(--border-color); text-align: center;">
                                <p style="color: #64748b;">Aún no tienes seguidores registrados.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </section>

                <!-- TAB 4: SEGUIDOS (Los que yo sigo) -->
                <section id="tab-seguidos" class="tab-pane" role="tabpanel">
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                        <h3 style="color: var(--text-primary); margin: 0;">
                            Usuarios que sigo (<span class="count-seguidos" id="count-seguidos"><%= cantSeguidos %></span>)
                        </h3>
                    </div>

                    <c:choose>
                        <c:when test="${not empty usuariosSeguidos}">
                            <div class="usuarios-tab-grid">
                                <c:forEach var="seguido" items="${usuariosSeguidos}">
                                    <div class="usuario-card-tab seguido-card-seguidos" id="card-seguido-${seguido.nickname}">
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
                                                <img src="${sImg}" alt="${seguido.nickname}" class="usuario-tab-avatar" onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                                                <div class="usuario-tab-placeholder" style="display:none;">
                                                    <span>Sin foto</span>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="usuario-tab-placeholder">
                                                    <span>Sin foto</span>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>

                                        <a href="${pageContext.request.contextPath}/usuario?nickname=${seguido.nickname}" class="usuario-tab-name" title="${seguido.nombre} ${seguido.apellido}">
                                            <c:out value="${not empty seguido.nombre ? seguido.nombre : seguido.nickname}"/>
                                            <c:if test="${not empty seguido.apellido}"> <c:out value="${seguido.apellido}"/></c:if>
                                        </a>
                                        <span class="usuario-tab-nick">@<c:out value="${seguido.nickname}"/></span>

                                        <div class="usuario-tab-actions">
                                            <form action="${pageContext.request.contextPath}/seguir-usuario" method="post" class="form-seguir-ajax" style="width: 100%;">
                                                <input type="hidden" name="nickname" value="${seguido.nickname}">
                                                <input type="hidden" name="accion" value="dejar">
                                                <button type="submit" class="btn btn-outline btn-sm" style="width: 100%; color: #dc2626; border-color: #fca5a5;">
                                                    Dejar de seguir
                                                </button>
                                            </form>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div style="padding: 2rem; background: #f8fafc; border-radius: 8px; border: 1px dashed var(--border-color); text-align: center;">
                                <p style="color: #64748b;">No estás siguiendo a ningún usuario actualmente.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </section>

            </div>
        </main>
    </div>

    <!-- Script para cambiar de pestañas -->
    <script>
        function openTab(targetId, btnElement) {
            const tabButtons = document.querySelectorAll('.profile-tab-btn');
            const tabPanes = document.querySelectorAll('.tab-pane');

            tabButtons.forEach(b => {
                b.classList.remove('active');
                b.setAttribute('aria-selected', 'false');
            });
            tabPanes.forEach(p => p.classList.remove('active'));

            if (btnElement) {
                btnElement.classList.add('active');
                btnElement.setAttribute('aria-selected', 'true');
            }

            const targetPane = document.getElementById(targetId);
            if (targetPane) {
                targetPane.classList.add('active');
            }
        }

        document.addEventListener('DOMContentLoaded', function () {
            const tabButtons = document.querySelectorAll('.profile-tab-btn');
            tabButtons.forEach(btn => {
                btn.addEventListener('click', function () {
                    const targetId = this.getAttribute('data-tab');
                    openTab(targetId, this);
                });
            });
        });
    </script>

    <!-- Script para seguir / dejar de seguir mediante AJAX sin refrescar -->
    <script src="${pageContext.request.contextPath}/js/seguimientoAjax.js"></script>
</body>
</html>
