function busquedaTypeahead(query, resultadosContainer) {
	if (!resultadosContainer) {
		console.error('Resultados container not found');
		return;
	}

	if (query === '') {
		resultadosContainer.innerHTML = '';
		return;
	}

	// (5) Envía el texto como parámetro de búsqueda; la respuesta se procesa al llegar.
	const request = new XMLHttpRequest();
	request.open(
		'GET',
		`/servidor-web/buscar?q=${encodeURIComponent(query)}&format=json`,
	);
	request.onload = () => {
		if (request.status < 200 || request.status >= 300) {
			console.error(`Error en la solicitud de búsqueda: ${request.status}`);
			return;
		}

		try {
			// (6) Convierte el cuerpo JSON de la respuesta en los resultados recibidos.
			const resultados = JSON.parse(request.responseText);

			// (7) Cada resultado aporta el enlace, tipo y nombre que se renderizan en la lista.
			resultadosContainer.innerHTML = resultados
				.map(
					(resultado) => `
          <li class="resultado-item">
            <a href="${resultado.href}" title="${resultado.nombreVisible}">
              <span class="resultado-tipo" data-tipo="${resultado.tipoCss}">${resultado.tipoVisible}</span>
              <span class="resultado-nombre">${resultado.nombreVisibleHighlighted}</span>
            </a>
          </li>
        `,
				)
				.join('');
		} catch (error) {
			console.error('Error obteniendo los resultados de busqueda:', error);
		}
	};
	request.onerror = () => {
		console.error('Error en la solicitud de búsqueda');
	};
	request.send();
}

document.addEventListener('DOMContentLoaded', () => {
	// (1) Conecta el campo de búsqueda con el contenedor donde aparecerán las sugerencias.
	const inputBuscar = document.getElementById('campo-buscar');
	const resultadosContainer = document.getElementById(
		'resultados-busqueda-typeahead',
	);
	// para que no haga llamadas al servidor cada vez que se escribe una letra, sino que espere un tiempo antes de hacer la llamada
	const timerDelay = 500; 
	let timer;

	// (2) Se suscribe al evento de entrada del campo de búsqueda para detectar cuando el usuario escribe algo.
	inputBuscar.addEventListener('input', (event) => {
		// (3) Reinicia el temporizador para consultar solo después de una pausa al escribir.
		if (timer) {
			clearTimeout(timer);
			timer = null;
		}
		timer = setTimeout(function () {
			console.log(`Input event detected`);
			// (4) Pasa el texto (escrito por el usuario) más reciente y el destino de las sugerencias a la búsqueda (resultadosContainer).
			busquedaTypeahead(event.target.value, resultadosContainer || null);

			timer = null;
		}, timerDelay);

		// (!) Redirect, no se usa porque se hace la búsqueda en el mismo sitio, no se redirige a otra página
		// window.location.href = `/busqueda?query=${encodeURIComponent(event.target.value.trim())}`;
	});
});
