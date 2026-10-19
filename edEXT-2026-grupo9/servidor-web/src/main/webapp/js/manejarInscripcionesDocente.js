document
	.getElementById('btnGuardarSelecciones')
	?.addEventListener('click', function () {
		const radios = document.querySelectorAll(
			'input[type="radio"][data-nickname]:checked',
		);
		const selectEdicion = document.getElementById('edicion');
		const urlParams = new URLSearchParams(window.location.search);
		const edicionVal = (selectEdicion && selectEdicion.value) || urlParams.get('edicion') || '';

		const params = new URLSearchParams();
		params.append('edicion', edicionVal);

		radios.forEach((radio) => {
			params.append(radio.name, radio.value);
		});

		const xhr = new XMLHttpRequest();
		xhr.open('POST', '/servidor-web/inscripcionesDocente?format=json');
		xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		xhr.onload = function () {
			if (xhr.status >= 200 && xhr.status < 300) {
				window.alert('¡Selecciones guardadas con éxito!');
			} else {
				window.alert(
					'Hubo un error al guardar las selecciones (' + xhr.status + ').',
				);
			}
		};
		xhr.onerror = function () {
			window.alert('Error de red al intentar guardar las selecciones.');
		};
		xhr.send(params.toString());
	});
