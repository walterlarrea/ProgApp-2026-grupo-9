<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Curso</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>

<body>
    <!-- cabezal fijo -->
    <%@ include file="../webMiscelanea/header.jsp" %>
    <!-- Layout Principal: Contenido Dinámico -->
    <div class="layout-container">
        <!-- Sidebar / Menú Lateral -->
        <%@ include file="../webMiscelanea/sidebar.jsp" %>
        <main class="main-content">
            <section class="hero-banner">
                <div class="hero-imagen">
                    <img src="${pageContext.request.contextPath}/imagenes/curso/${curso.imagen}" alt="Imagen del curso"
                        onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/imagenes/curso/curso-placeholder.jpg';">
                </div>

                <div class="hero-contenido">
                    <h1>${curso.nombreCurso}</h1>
                    <p>${curso.descCurso}</p>
                </div>
            </section>
                <div class="dato-space">
                    <h4>Instituto</h4> 
                    <div class="nav-list">
                        <a href="${pageContext.request.contextPath}/instituto?nombre=${curso.instituto.nombreI}">
                            <p>${curso.instituto.nombreI}</p>
                        </a>
                    </div>
                </div>
                <div class="dato-space">
                    <h4>Créditos</h4> <p class="detalle">${curso.cantCred}</p>
                </div>
                <div class="dato-space">
                    <h4>Cantidad de horas</h4> <p class="detalle">${curso.cantHoras}</p>
                </div>
                <div class="dato-space">
                    <h4>Duración</h4> <p class="detalle">${curso.duracion} semanas</p>
                </div>
                <div class="dato-space">
                    <h4>Visitas</h4> <p class="detalle">${curso.cantidadVisitas}</p>
                </div>
                <div class="dato-space">
                    <h4>Valoración del curso</h4>
                    <div id="stat-promedio-${fn:escapeXml(curso.nombreCurso)}"
                         class="rating-breakdown-container"
                         data-promedio="${curso.cantValoraciones > 0 && curso.puntajePromedio != null ? curso.puntajePromedio : 0}"
                         data-votos="${curso.cantValoraciones}"
                         data-val1="${curso.cantVal1}"
                         data-val2="${curso.cantVal2}"
                         data-val3="${curso.cantVal3}"
                         data-val4="${curso.cantVal4}"
                         data-val5="${curso.cantVal5}">
                        
                        <c:set var="maxVal" value="${curso.cantVal1}" />
                        <c:if test="${curso.cantVal2 > maxVal}"><c:set var="maxVal" value="${curso.cantVal2}" /></c:if>
                        <c:if test="${curso.cantVal3 > maxVal}"><c:set var="maxVal" value="${curso.cantVal3}" /></c:if>
                        <c:if test="${curso.cantVal4 > maxVal}"><c:set var="maxVal" value="${curso.cantVal4}" /></c:if>
                        <c:if test="${curso.cantVal5 > maxVal}"><c:set var="maxVal" value="${curso.cantVal5}" /></c:if>
                        <c:if test="${empty maxVal || maxVal == 0}"><c:set var="maxVal" value="1" /></c:if>

                        <!-- Barras de desglose por estrella (5 a 1) -->
                        <div class="rating-breakdown-bars">
                            <div class="rating-bar-row">
                                <span class="rating-bar-label">5 star</span>
                                <div class="rating-bar-track-wrap">
                                    <div class="rating-bar-fill bar-5" id="bar-fill-5" style="width: ${curso.cantValoraciones > 0 ? (curso.cantVal5 * 100.0 / maxVal) : 0}%;"></div>
                                </div>
                                <span class="rating-bar-count" id="bar-count-5"><c:out value="${curso.cantVal5}"/></span>
                            </div>

                            <div class="rating-bar-row">
                                <span class="rating-bar-label">4 star</span>
                                <div class="rating-bar-track-wrap">
                                    <div class="rating-bar-fill bar-4" id="bar-fill-4" style="width: ${curso.cantValoraciones > 0 ? (curso.cantVal4 * 100.0 / maxVal) : 0}%;"></div>
                                </div>
                                <span class="rating-bar-count" id="bar-count-4"><c:out value="${curso.cantVal4}"/></span>
                            </div>

                            <div class="rating-bar-row">
                                <span class="rating-bar-label">3 star</span>
                                <div class="rating-bar-track-wrap">
                                    <div class="rating-bar-fill bar-3" id="bar-fill-3" style="width: ${curso.cantValoraciones > 0 ? (curso.cantVal3 * 100.0 / maxVal) : 0}%;"></div>
                                </div>
                                <span class="rating-bar-count" id="bar-count-3"><c:out value="${curso.cantVal3}"/></span>
                            </div>

                            <div class="rating-bar-row">
                                <span class="rating-bar-label">2 star</span>
                                <div class="rating-bar-track-wrap">
                                    <div class="rating-bar-fill bar-2" id="bar-fill-2" style="width: ${curso.cantValoraciones > 0 ? (curso.cantVal2 * 100.0 / maxVal) : 0}%;"></div>
                                </div>
                                <span class="rating-bar-count" id="bar-count-2"><c:out value="${curso.cantVal2}"/></span>
                            </div>

                            <div class="rating-bar-row">
                                <span class="rating-bar-label">1 star</span>
                                <div class="rating-bar-track-wrap">
                                    <div class="rating-bar-fill bar-1" id="bar-fill-1" style="width: ${curso.cantValoraciones > 0 ? (curso.cantVal1 * 100.0 / maxVal) : 0}%;"></div>
                                </div>
                                <span class="rating-bar-count" id="bar-count-1"><c:out value="${curso.cantVal1}"/></span>
                            </div>
                        </div>

                        <!-- Card de Average rating -->
                        <div class="rating-summary-card">
                            <span class="rating-summary-title">Average rating:</span>
                            <span class="rating-summary-score" id="summary-score">
                                <c:choose>
                                    <c:when test="${curso.cantValoraciones > 0}">
                                        <c:out value="${curso.puntajePromedio}"/>
                                    </c:when>
                                    <c:otherwise>0.0</c:otherwise>
                                </c:choose>
                            </span>
                            <div class="rating-summary-stars" id="summary-stars">
                                <c:set var="promedioRedondeado" value="${curso.cantValoraciones > 0 ? curso.puntajePromedio : 0}" />
                                <c:forEach var="s" begin="1" end="5">
                                    <svg class="star-icon-small ${s <= promedioRedondeado ? 'active' : ''}" viewBox="0 0 24 24" aria-hidden="true">
                                        <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>
                                    </svg>
                                </c:forEach>
                            </div>
                            <span class="rating-summary-total" id="summary-total">
                                <c:out value="${curso.cantValoraciones}"/>
                            </span>
                        </div>
                    </div>
                </div>

                <c:if test="${esEstudiante}">
                    <div class="dato-space">
                        <h4>Tu Valoración</h4>
                        <div id="col-valoracion-${fn:escapeXml(curso.nombreCurso)}" class="stat-valoracion-contenedor">
                            <c:choose>
                                <c:when test="${valoracionEstudiante != null && valoracionEstudiante > 0}">
                                    <div class="rating-stars-readonly" aria-label="Tu valoración: ${valoracionEstudiante} de 5" style="margin-bottom: 4px;">
                                        <c:forEach var="s" begin="1" end="5">
                                            <svg class="star-icon ${s <= valoracionEstudiante ? 'active' : ''}" viewBox="0 0 24 24" aria-hidden="true">
                                                <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>
                                            </svg>
                                        </c:forEach>
                                    </div>
                                    <small class="stat-subtexto-enviado">(Valoración enviada)</small>
                                </c:when>
                                <c:when test="${puedeValorar}">
                                    <form id="formValoracionDetalle"
                                          action="${pageContext.request.contextPath}/misCursos"
                                          method="post"
                                          onsubmit="event.preventDefault(); guardarValoracionCursoDetalle('${fn:escapeXml(curso.nombreCurso)}');">
                                        <input type="hidden" name="redirect" value="${pageContext.request.contextPath}/curso?nombre=${fn:escapeXml(curso.nombreCurso)}">
                                        <div class="rating-stars-selector" data-curso="${fn:escapeXml(curso.nombreCurso)}">
                                            <input type="hidden"
                                                   name="valoracion-${fn:escapeXml(curso.nombreCurso)}"
                                                   class="select-valoracion stat-input-valoracion"
                                                   data-curso="${fn:escapeXml(curso.nombreCurso)}"
                                                   value="">
                                            <div class="rating-stars-group" role="radiogroup" aria-label="Valoración para ${fn:escapeXml(curso.nombreCurso)}">
                                                <c:forEach var="s" begin="1" end="5">
                                                    <button type="button"
                                                            class="star-btn"
                                                            data-value="${s}"
                                                            aria-label="${s} de 5 estrellas"
                                                            title="${s} de 5 estrellas">
                                                        <svg class="star-icon" viewBox="0 0 24 24" aria-hidden="true">
                                                            <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>
                                                        </svg>
                                                    </button>
                                                </c:forEach>
                                            </div>
                                        </div>
                                        <button type="button"
                                                id="btnGuardarValoracionDetalle"
                                                class="btn btn-primary"
                                                style="margin-left: 10px; padding: 6px 14px; font-size: 0.9rem;"
                                                onclick="guardarValoracionCursoDetalle('${fn:escapeXml(curso.nombreCurso)}')">
                                            Guardar valoración
                                        </button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <p class="detalle" style="color: var(--text-secondary); font-style: italic; font-size: 0.9rem;">
                                        Debes participar del curso con inscripción aceptada para poder valorarlo.
                                    </p>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:if>

                <h4>Previas del curso</h4> 
                <ol class="nav-list">
                <c:choose>
                    <c:when test="${not empty previas}">
                        <c:forEach var="prev" items="${previas}">
                            <li>
                                <a href="${pageContext.request.contextPath}/curso?nombre=${prev.nombreCurso}">
                                    <c:out value="${prev.nombreCurso}"/>
                                </a>
                            </li>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <li class="empty-hint">No contiene previas.</li>
                    </c:otherwise>
                </c:choose>
            </ol>
                <div class="dato-space">
                    <h4>URL</h4> <p class="detalle">${curso.url}</p>
                </div>
            <div class="dato-space">
            <h2>Ediciones</h2> 
            <ol class="nav-list">
                <c:choose>
                    <c:when test="${not empty ediciones}">
                        <c:forEach var="edi" items="${ediciones}">
                            <li>
                                <a href="${pageContext.request.contextPath}/edicionCurso?nombre=${edi.getNombreEdi()}">
                                    <c:out value="${edi.getNombreEdi()}"/>
                                </a>
                            </li>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <li class="empty-hint">Sin ediciones disponibles</li>
                    </c:otherwise>
                </c:choose>
            </ol>
            </div>
        </main>
    </div>

    <%@ include file="../webMiscelanea/popup-mensaje.jsp" %>
    <script src="${pageContext.request.contextPath}/js/misCursos.js"></script>
</body>
</html>
