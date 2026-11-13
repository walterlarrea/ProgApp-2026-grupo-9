<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Map, java.util.HashMap, java.util.ArrayList, com.grupo9.edext.grupo9.servidor_web.servlets.busqueda.ResultadoBusquedaExtendida"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es" data-bs-theme="light">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Búsqueda</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@6.0.0-alpha.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-B/GM4XqrwHnWXNOWMbloTmrYXZg10cakYGmpfsR/bbzQ6JAJI4ihuyADKLnBgrCe" crossorigin="anonymous">
    <script type="module" src="https://cdn.jsdelivr.net/npm/bootstrap@6.0.0-alpha.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-1a/pXj49ZQ1aHEmrJ+gMw1otqoVsYwlEnlD8mIfY2TV03r20Y0CN7uqx1tQogjPL" crossorigin="anonymous"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/resultados-busqueda.js"></script>
</head>

<body>
    <!-- cabezal fijo -->
    <%@ include file="../webMiscelanea/header.jsp" %>
    <!-- Layout Principal: Contenido Dinámico -->
    <div class="layout-container">
        <!-- Sidebar / Menú Lateral -->
        <%@ include file="../webMiscelanea/sidebar.jsp" %>
        <main class="main-content">
            <section class="mb-5">
                <div class="d-flex flex-row justify-content-between">
                    <div>
                        <label class="text-nowrap" for="filtros-busqueda">Filtrar: </label>
                        <button class="form-control combobox-toggle w-12" type="button"
                            id="filtros-busqueda"
                            data-bs-toggle="combobox"
                            data-bs-name="tipo-resultado"
                            data-bs-placeholder="Seleccionar Tipo"
                            data-bs-multiple="true">
                            <span class="combobox-value">Tipos de resultados</span>
                            <svg class="combobox-caret" width="10" height="16" viewBox="0 0 10 16" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M0.46967 5.46967C0.762563 5.17678 1.23744 5.17678 1.53033 5.46967L5 8.93934L8.46967 5.46967C8.76256 5.17678 9.23744 5.17678 9.53033 5.46967C9.82322 5.76256 9.82322 6.23744 9.53033 6.53033L5.53033 10.5303C5.23744 10.8232 4.76256 10.8232 4.46967 10.5303L0.46967 6.53033C0.176777 6.23744 0.176777 5.76256 0.46967 5.46967Z" fill="currentcolor"/></svg>
                        </button>
                        <div class="menu" id="opciones-filtro-busqueda">
                            <%
                                HashMap<String, String> optionesFiltro = (HashMap<String, String>) request.getAttribute("filtros");
                                if (optionesFiltro != null) {
                                    for (Map.Entry<String, String> filtro : optionesFiltro.entrySet()) {
                            %>
                                    <button class="menu-item" type="button" data-bs-value="<%= filtro.getKey() %>">
                                        <%= filtro.getValue() %>
                                        <svg class="menu-item-check" xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" viewBox="0 0 16 16"><path d="m2 7 4 5 8-8"/></svg>
                                    </button>
                            <%
                                    }
                                }
                            %>
                        </div>
                    </div>
                    <div class="d-flex flex-row gap-3 align-items-center">
                        <label class="text-nowrap" for="orden-busqueda">Ordenar por: </label>
                        <button class="form-control combobox-toggle boton-ordenar-busqueda" type="button"
                            id="orden-busqueda"
                            data-bs-toggle="combobox"
                            data-bs-name="filtros-busqueda"
                            data-bs-placeholder="Elige una opción">
                            <span class="combobox-value">Elige una opción</span>
                            <svg class="combobox-caret" width="10" height="16" viewBox="0 0 10 16" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M0.46967 5.46967C0.762563 5.17678 1.23744 5.17678 1.53033 5.46967L5 8.93934L8.46967 5.46967C8.76256 5.17678 9.23744 5.17678 9.53033 5.46967C9.82322 5.76256 9.82322 6.23744 9.53033 6.53033L5.53033 10.5303C5.23744 10.8232 4.76256 10.8232 4.46967 10.5303L0.46967 6.53033C0.176777 6.23744 0.176777 5.76256 0.46967 5.46967Z" fill="currentcolor"/></svg>
                        </button>
                        <div class="menu">
                            <button class="menu-item" type="button" data-bs-value="alfabetico">Alfabéticamente (A-Z a-z)</button>
                            <button class="menu-item" type="button" data-bs-value="anio-publicado">Año (descendente)</button>
                        </div>
                    </div>
                </div>
            </section>

            <section>
                <ol class="lista-busqueda" id="resultados-busqueda">
                    <%
                        ArrayList<ResultadoBusquedaExtendida> resultados = (ArrayList<ResultadoBusquedaExtendida>) request.getAttribute("listaResultados");
                        if (resultados != null && !resultados.isEmpty()) {
                            for (ResultadoBusquedaExtendida resultado : resultados) {
                    %>
                            <li class="resultado-busqueda" data-tipo="<%= resultado.tipoCss() %>" data-fecha-creacion="<%= resultado.fechaCreacion() %>" data-nombre-visible="<%= resultado.nombreVisible() %>">
                                <a href="<%= resultado.href() %>" class="resultado-card-link">
                                    <div class="resultado-busqueda-media">
                                        <% if (resultado.tipo() == com.grupo9.edext.grupo9.servidor_web.web_service_client.busqueda.TipoBusqueda.USUARIO) { %>
                                            <% if (resultado.imagenUrl() != null && !resultado.imagenUrl().isBlank()) { %>
                                                <img src="<%= resultado.imagenUrl() %>"
                                                     alt="Foto de <%= resultado.nombreVisible() %>"
                                                     class="resultado-busqueda-img"
                                                     onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';" />
                                                <div class="profile-avatar-placeholder resultado-busqueda-placeholder" style="display:none;">
                                                    <span>Sin foto</span>
                                                </div>
                                            <% } else { %>
                                                <div class="profile-avatar-placeholder resultado-busqueda-placeholder">
                                                    <span>Sin foto</span>
                                                </div>
                                            <% } %>
                                        <% } else { %>
                                            <img src="<%= resultado.imagenUrl() != null && !resultado.imagenUrl().isBlank() ? resultado.imagenUrl() : request.getContextPath() + "/imagenes/curso/curso-placeholder.jpg" %>"
                                                 alt="Imagen de <%= resultado.nombreVisible() %>"
                                                 class="resultado-busqueda-img"
                                                 onerror="this.onerror=null; this.src='<%= request.getContextPath() %>/imagenes/curso/curso-placeholder.jpg';" />
                                        <% } %>
                                    </div>
                                    <div class="resultado-busqueda-info">
                                        <div class="resultado-busqueda-header">
                                            <div class="resultado-busqueda-rating">
                                                <span class="resultado-tipo flex-shrink-0" data-tipo="<%= resultado.tipoCss() %>"><%= resultado.tipoVisible() %></span>
                                                <% if (resultado.fechaCreacion() != null && !"-".equals(resultado.fechaCreacion())) { %>
                                                    <span class="resultado-busqueda-fecha"><%= resultado.fechaCreacion() %></span>
                                                <% } %>
                                            </div>
                                            <% if (resultado.tipo() == com.grupo9.edext.grupo9.servidor_web.web_service_client.busqueda.TipoBusqueda.CURSO) { %>
                                                <div class="resultado-busqueda-rating" aria-label="Valoración promedio: <%= resultado.puntajePromedio() != null && resultado.cantValoraciones() > 0 ? String.format(java.util.Locale.US, "%.1f", resultado.puntajePromedio()) : "0.0" %> de 5">
                                                    <span class="resultado-rating-score"><%= (resultado.puntajePromedio() != null && resultado.cantValoraciones() > 0) ? String.format(java.util.Locale.US, "%.1f", resultado.puntajePromedio()) : "0.0" %></span>
                                                    <% if (resultado.cantValoraciones() > 0) { %>
                                                        <span class="resultado-rating-total">(<%= resultado.cantValoraciones() %>)</span>
                                                    <% } %>
                                                    <div class="resultado-rating-stars">
                                                        <%
                                                            double prom = (resultado.puntajePromedio() != null && resultado.cantValoraciones() > 0) ? resultado.puntajePromedio() : 0.0;
                                                            long promRound = Math.round(prom);
                                                            for (int s = 1; s <= 5; s++) {
                                                        %>
                                                            <svg class="star-icon-search <%= s <= promRound ? "active" : "" %>" viewBox="0 0 24 24" aria-hidden="true">
                                                                <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>
                                                            </svg>
                                                        <% } %>
                                                    </div>
                                                </div>
                                            <% } %>
                                        </div>
                                        <h3 class="resultado-busqueda-titulo"><%= resultado.nombreVisibleHighlighted() %></h3>
                                        
                                        <% if (resultado.descVisibleHighlighted() != null && !resultado.descVisibleHighlighted().isBlank()) { %>
                                            <p class="resultado-busqueda-desc">
                                                <%= resultado.descVisibleHighlighted() %>
                                            </p>
                                        <% } %>
                                    </div>
                                </a>
                            </li>
                    <%
                            }
                        } else {
                    %>
                            <div class="empty-state">
                                <p>No se encontraron resultados para tu búsqueda.</p>
                            </div>
                    <%
                        }
                    %>
                </ol>
            </section>
        </main>
    </div>
</body>
</html>
