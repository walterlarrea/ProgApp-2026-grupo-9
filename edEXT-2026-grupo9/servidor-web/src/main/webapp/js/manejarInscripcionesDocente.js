// Requiere js/cerrarPopup.js (mostrarConfirmacion / mostrarPopup)
document
	.getElementById('btnGuardarSelecciones')
	?.addEventListener('click', function () {
		mostrarConfirmacion(
			'Se aplicarán los estados elegidos a los estudiantes inscriptos.',
			'¿Guardar los cambios?',
			guardarSelecciones,
		);
	});

function guardarSelecciones() {
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

	// Al cerrar el aviso se recarga para mostrar el estado real de cada inscripción
	const recargar = () => window.location.reload();

	const xhr = new XMLHttpRequest();
	xhr.open('POST', '/servidor-web/inscripcionesDocente?format=json');
	xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xhr.onload = function () {
		if (xhr.status >= 200 && xhr.status < 300) {
			let avisos = [];
			try {
				avisos = JSON.parse(xhr.responseText).avisos || [];
			} catch (e) {
				avisos = [];
			}
			if (avisos.length > 0) {
				// Algunas aceptaciones no se aplicaron (por ejemplo, cupo completo)
				mostrarPopup('Se guardaron los cambios, salvo:\n\n- ' + avisos.join('\n- '),
					'Cambios guardados con avisos', '!', recargar);
			} else {
				mostrarPopup('Las selecciones se guardaron correctamente.', '¡Cambios guardados!', '✓', recargar);
			}
		} else {
			mostrarPopup('Hubo un error al guardar las selecciones (' + xhr.status + ').', 'Error', '✕');
		}
	};
	xhr.onerror = function () {
		mostrarPopup('Error de red al intentar guardar las selecciones.', 'Error de conexión', '✕');
	};
	xhr.send(params.toString());
}
