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
            <a href="${resultado.href}" title="${resultado.nombreVisible}">
              <span class="resultado-tipo" data-tipo="${resultado.tipoCss}">${resultado.tipoVisible}</span>
              <span class="resultado-nombre">${resultado.nombreVisibleHighlighted}</span>
            </a>
          </li>
        `,
			)
			.join('');
	} catch (error) {
		console.error('Error fetching search results:', error);
	}
}

document.addEventListener('DOMContentLoaded', () => {
	const inputBuscar = document.getElementById('campo-buscar');
	const resultadosContainer = document.getElementById(
		'resultados-busqueda-typeahead',
	);
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
