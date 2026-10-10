<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Map, java.util.HashMap, java.util.ArrayList, com.grupo9.edext.grupo9.servidor_web.servlets.ResultadoBusquedaExtendida"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Cursos</title>
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
                                for (Map.Entry<String, String> filtro : optionesFiltro.entrySet()) {
                            %>
                                    <button class="menu-item" type="button" data-bs-value="<%= filtro.getKey() %>">
                                        <%= filtro.getValue() %>
                                        <svg class="menu-item-check" xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" viewBox="0 0 16 16"><path d="m2 7 4 5 8-8"/></svg>
                                    </button>
                            <%
                                }
                            %>
                        </div>
                    </div>
                    <div class="d-flex flex-row gap-3 align-items-center">
<!--                        <label for="filtros-busqueda">Ordenar por: </label>
                        <select id="filtros-busqueda" name="filtros-busqueda">
                            <option value="0">Selecciona una opción</option>
                            <option value="alfabetico">Alfabéticamente (A-Z a-z)</option>
                            <option value="anio-publicacion">Año (descendente)</option>
                        </select>-->
                        
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
                        for (ResultadoBusquedaExtendida resultado : resultados) {
                    %>
                            <li class="resultado-busqueda" data-tipo="<%= resultado.tipoCss() %>" data-fecha-creacion="<%= resultado.fechaCreacion() %>" data-nombre-visible="<%= resultado.nombreVisible() %>">
                                <a href="<%= resultado.href() %>" class="w-100">
                                    <div class="d-flex flex-column w-100">
                                        <div class="d-inline-flex">
                                            <span class="resultado-tipo flex-shrink-0" data-tipo="<%= resultado.tipoCss() %>"><%= resultado.tipoVisible() %></span>
                                            <span class="flex-grow-1 resultado-titulo overflow-hidden text-truncate"><%= resultado.nombreVisibleHighlighted() %></span>
                                            <%
                                                if (resultado.fechaCreacion() != null){
                                            %>
                                                <span class="fs-sm"><%= resultado.fechaCreacion() %></span>
                                            <%
                                                }
                                            %>
                                        </div>
                                        <p>
                                            <%= resultado.descVisibleHighlighted() %>
                                        </p>
                                    </div>
                                </a>
                            </li>
                    <%
                        }
                    %>
                </ol>
            </section>
        </main>
    </div>
</body>
</html>
