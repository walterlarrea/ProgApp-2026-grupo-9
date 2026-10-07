async function busquedaTypeahead(query, resultadosContainer) {
	if (!resultadosContainer) {
		console.error('Resultados container not found');
		return;
	}

	if (query === '') {
		resultadosContainer.innerHTML = '';
		return;
	}

	try {
		const response = await fetch(
			`/servidor-web/buscar?q=${encodeURIComponent(query)}&format=json`,
		);
		const resultados = await response.json();

		resultadosContainer.innerHTML = resultados
			.map((resultado) => {
				const nombreParaMostrar = nombrePorTipo(resultado.tipo, resultado.data)
					.replace(/<[^>]*>?/gm, '') // Eliminar etiquetas HTML
					.replace(new RegExp(query, 'gi'), (match) => `<b>${match}</b>`); // Resaltar coincidencias
				// .replace(query, (match) => `<span class="resaltado">${match}</span>`); // Resaltar coincidencias

				return `
          <li class="resultado-item">
            <a href="${hrefPorTipo(resultado.tipo, resultado.data)}" title="${nombreParaMostrar.replace(/<[^>]*>?/gm, '')}">
              <span class="resultado-tipo" data-tipo="${resultado.tipo.toLowerCase()}">${mappingTipo(resultado.tipo)}</span>
              <span class="resultado-nombre">${nombreParaMostrar}</span>
            </a>
          </li>
        `;
			})
			.join('');
	} catch (error) {
		console.error('Error fetching search results:', error);
	}
}

function hrefPorTipo(tipo, data) {
	if (!tipo || !data) return '/servidor-web/';

	switch (tipo.toLowerCase()) {
		case 'curso':
			return `/servidor-web/curso?nombre=${encodeURIComponent(data.nombreCurso)}`;
			break;
		case 'usuario':
			return `/servidor-web/usuario?nombre=${encodeURIComponent(data.nickname)}`;
			break;
		case 'programa_formacion':
			return `/servidor-web/programa?nombre=${encodeURIComponent(data.nombre)}`;
			break;
		default:
			console.error('Tipo de resultado desconocido:', tipo);
			return '/servidor-web/';
			break;
	}
}

function nombrePorTipo(tipo, data) {
	if (!tipo || !data) return '';
	switch (tipo.toLowerCase()) {
		case 'curso':
			return data.nombreCurso;
		case 'usuario':
			return data.nombre + ' ' + data.apellido;
		case 'programa_formacion':
			return data.nombre;
		default:
			console.error('Tipo de resultado desconocido:', tipo);
			return '';
	}
}

function mappingTipo(tipo) {
	if (!tipo) return '';
	switch (tipo.toLowerCase()) {
		case 'curso':
			return 'Curso';
		case 'usuario':
			return 'Usuario';
		case 'programa_formacion':
			return 'Programa';
		default:
			console.error('Tipo de resultado desconocido:', tipo);
			return '';
	}
}

document.addEventListener('DOMContentLoaded', () => {
	const inputBuscar = document.getElementById('campo-buscar');
	const resultadosContainer = document.getElementById('resultados-busqueda');
	const timerDelay = 500; // para que no haga llamadas al servidor cada vez que se escribe una letra, sino que espere un tiempo antes de hacer la llamada
	let timer;

	inputBuscar.addEventListener('input', (event) => {
		if (timer) {
			clearTimeout(timer);
			timer = null;
		}
		timer = setTimeout(function () {
			console.log(`Input event detected`);
			busquedaTypeahead(event.target.value, resultadosContainer || null);

			timer = null;
		}, timerDelay);

		// window.location.href = `/busqueda?query=${encodeURIComponent(event.target.value.trim())}`;
	});
});
