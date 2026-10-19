<div class="search-box">
    <%
        String query = (String) request.getAttribute("query-param");
        String placeholder = "Buscar cursos, usuarios y programas...";
        if(query != null) {
            placeholder = query;
        }
    %>
	<form action="${pageContext.request.contextPath}/buscar" method="get">
		<input
			id="campo-buscar"
			type="text"
			name="q"
			placeholder="<%= placeholder %>" />
		<button type="submit">Buscar</button>
	</form>

	<div class="container-busqueda-typeahead">
		<ul id="resultados-busqueda-typeahead"></ul>
	</div>
	<script
		type="text/javascript"
		src="${pageContext.request.contextPath}/js/busqueda.js"></script>
</div>
