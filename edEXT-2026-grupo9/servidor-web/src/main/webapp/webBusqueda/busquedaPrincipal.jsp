<div class="search-box">
    <form action="${pageContext.request.contextPath}/buscar" method="get">
        <input id="campo-buscar" type="text" name="q" placeholder="Buscar cursos, usuarios y programas...">
        <button type="submit">Buscar</button>
    </form>
    
    <div class="container-busqueda-typeahead">
        <ul id="resultados-busqueda">
            
        </ul>
    </div>
</div>