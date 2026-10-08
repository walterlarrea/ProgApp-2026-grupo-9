
function ordenarResultadosPorFechaDePublicacion(elementos) {
    return Array.from(elementos).toSorted((a, b) => {
        // Lo que no tenga fecha va abajo de todo
        if (a.dataset.fechaCreacion === "-") return 1;
        if (b.dataset.fechaCreacion === "-") return -1;

        // Separo dia, mes y anio para asegurarme que tome bien dia y mes
        const fechaAPartes = a.dataset.fechaCreacion.split("/");
        const fechaBPartes = b.dataset.fechaCreacion.split("/");

        // Creo objeto Date y ordena Desc
        const fechaA = new Date(+fechaAPartes[2], fechaAPartes[1] - 1, +fechaAPartes[0]);
        const fechaB = new Date(+fechaBPartes[2], fechaBPartes[1] - 1, +fechaBPartes[0]);
        if (fechaA > fechaB) return -1;
        if (fechaA < fechaB) return 1;
        return 0;
    });
}

function ordenarResultadosPorAlfabeto(elementos) {
    return Array.from(elementos).toSorted((a, b) => {
        const nombreA = a.dataset.nombreVisible;
        const nombreB = b.dataset.nombreVisible;
        
        if (nombreA > nombreB) return 1;
        if (nombreA < nombreB) return -1;
        return 0;
    });
}

document.addEventListener('DOMContentLoaded', () => {
	const inputFiltros = document.getElementById('filtros-busqueda');
	const inputOpcioneOrden = document.getElementById('orden-busqueda');
        const contenedorResultados = document.getElementById('resultados-busqueda');

	inputFiltros.addEventListener('change.bs.combobox', (event) => {
            const resultados = document.querySelectorAll(".resultado-busqueda");
            
            const filtrosSeleccionados = event.value;
            
            console.log("[FILTROS] ", filtrosSeleccionados);

            resultados.forEach(resultado => {
                const tipoDelRecurso = resultado.dataset.tipo;

                if (
                    filtrosSeleccionados?.length === 0 ||
                    filtrosSeleccionados.includes(tipoDelRecurso)
                ) {
                    resultado.style.display = "";
                } else {
                    resultado.style.display = "none";
                }
            });
	});
        
        inputOpcioneOrden.addEventListener('change.bs.combobox', (event) => {
            const resultados = document.querySelectorAll(".resultado-busqueda");
            const ordenSeleccionado = event.value;
            
            console.log("[ORDEN] ", ordenSeleccionado);
            
            let resultadosOrdenados = [];
            
            if(ordenSeleccionado === "anio-publicado"){
                resultadosOrdenados = ordenarResultadosPorFechaDePublicacion(resultados);
            } else if (ordenSeleccionado === "alfabetico") {
                resultadosOrdenados = ordenarResultadosPorAlfabeto(resultados);
            }
            
            contenedorResultados.innerHTML = "";
            for(const nodo of resultadosOrdenados){
                contenedorResultados.appendChild(nodo);
            }
	});
});