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
			.map(
				(resultado) => `
        <li class="resultado-item">
          <a href="${hrefPorTipo(resultado.tipo, resultado.data)}">
            <span class="resultado-tipo">${resultado.tipo}</span>:
            <span class="resultado-nombre">${nombrePorTipo(resultado.tipo, resultado.data)}</span>
          </a>
        </li>
      `,
			)
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
			return `/servidor-web/usuario?nombre=${encodeURIComponent(data.nombre)}`;
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
			return data.nombre;
		case 'programa_formacion':
			return data.nombre;
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
