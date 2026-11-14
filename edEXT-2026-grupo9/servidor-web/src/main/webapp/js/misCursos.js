/**
 * Gestión de Valoraciones de Cursos por Estudiante (AJAX e interactividad de estrellas)
 */

function inicializarStarRatings() {
	const widgets = document.querySelectorAll('.rating-stars-selector');
	widgets.forEach(function (widget) {
		const input = widget.querySelector('input.select-valoracion');
		const group = widget.querySelector('.rating-stars-group');
		if (!group) return;
		const buttons = group.querySelectorAll('.star-btn');

		buttons.forEach(function (btn) {
			const val = parseInt(btn.getAttribute('data-value'), 10);

			// Previsualización al pasar el mouse (hover): colorea la estrella y todas las anteriores
			btn.addEventListener('mouseenter', function () {
				buttons.forEach(function (b) {
					const bVal = parseInt(b.getAttribute('data-value'), 10);
					if (bVal <= val) {
						b.classList.add('hover');
					} else {
						b.classList.remove('hover');
					}
				});
			});

			// Selección de la estrella al hacer clic: guarda el valor y colorea con relleno azul claro
			btn.addEventListener('click', function () {
				if (input) {
					input.value = val;
				}
				buttons.forEach(function (b) {
					const bVal = parseInt(b.getAttribute('data-value'), 10);
					if (bVal <= val) {
						b.classList.add('active');
					} else {
						b.classList.remove('active');
					}
				});
			});
		});

		// Eventos en el grupo para previsualización
		group.addEventListener('mouseenter', function () {
			group.classList.add('is-hovering');
		});

		group.addEventListener('mouseleave', function () {
			group.classList.remove('is-hovering');
			buttons.forEach(function (b) {
				b.classList.remove('hover');
			});
		});
	});
}

if (document.readyState === 'loading') {
	document.addEventListener('DOMContentLoaded', inicializarStarRatings);
} else {
	inicializarStarRatings();
}

function guardarValoracionesAjax() {
	const form = document.getElementById('formValoraciones');
	if (!form) return;

	const selects = form.querySelectorAll('.select-valoracion');
	if (!selects || selects.length === 0) {
		mostrarPopup(
			'Ya has valorado todos tus cursos participantes.',
			'Aviso',
			'!',
		);
		return;
	}

	let cantidadSeleccionadas = 0;
	const valoracionesMap = {};

	for (let i = 0; i < selects.length; i++) {
		const val = selects[i].value ? selects[i].value.trim() : '';
		const cursoNom = selects[i].getAttribute('data-curso');
		if (val !== '') {
			const num = parseInt(val, 10);
			if (isNaN(num) || num < 1 || num > 5) {
				mostrarPopup(
					'El puntaje debe ser un valor entero entre 1 y 5.',
					'Error de Validación',
					'✕',
				);
				const widget = selects[i].closest('.rating-stars-selector');
				if (widget) {
					const firstBtn = widget.querySelector('.star-btn');
					if (firstBtn) firstBtn.focus();
				}
				return;
			}
			cantidadSeleccionadas++;
			valoracionesMap[cursoNom] = num;
		}
	}

	if (cantidadSeleccionadas === 0) {
		mostrarPopup(
			'Debe asignar un puntaje (de 1 a 5) a al menos un curso antes de guardar.',
			'Aviso',
			'!',
		);
		return;
	}

	const btnGuardar = document.getElementById('btnGuardarValoraciones');
	if (btnGuardar) {
		btnGuardar.disabled = true;
	}

	const formData = new FormData(form);
	formData.append('ajax', 'true');

	fetch(form.action, {
		method: 'POST',
		body: new URLSearchParams(formData),
		headers: {
			'X-Requested-With': 'XMLHttpRequest',
			Accept: 'application/json',
		},
	})
		.then(function (res) {
			return res.json().then(function (data) {
				return { status: res.status, ok: res.ok, data: data };
			});
		})
		.then(function (result) {
			const data = result.data;
			if (result.ok && data.success) {
				mostrarPopup(
					data.mensaje || 'Valoración registrada con éxito.',
					'¡Valoración Guardada!',
					'✓',
				);

				if (data.actualizados && Array.isArray(data.actualizados)) {
					data.actualizados.forEach(function (nombreCurso) {
						const celda = document.getElementById(
							'col-valoracion-' + nombreCurso,
						);
						const puntaje = valoracionesMap[nombreCurso];
						if (celda && puntaje) {
							let starsHtml =
								'<div class="rating-stars-readonly" aria-label="Valoración enviada: ' +
								puntaje +
								' de 5" style="margin-bottom: 4px;">';
							for (let s = 1; s <= 5; s++) {
								const isFilled = s <= puntaje ? 'active' : '';
								starsHtml +=
									'<svg class="star-icon ' +
									isFilled +
									'" viewBox="0 0 24 24" aria-hidden="true">' +
									'<polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>' +
									'</svg>';
							}
							starsHtml += '</div>';

							celda.innerHTML =
								starsHtml +
								'<small class="stat-subtexto-enviado">(Valoración enviada)</small>';
						}

						// Actualizar el Promedio en la UI enseguida de guardar los cambios sin recargar la página
						const celdaPromedio = document.getElementById(
							'stat-promedio-' + nombreCurso,
						);
						if (celdaPromedio) {
							let nuevoPromedio = null;
							let nuevosVotos = null;

							if (data.promedios && data.promedios[nombreCurso]) {
								const info = data.promedios[nombreCurso];
								if (
									info.puntajePromedio !== undefined &&
									info.puntajePromedio !== null
								) {
									nuevoPromedio = parseFloat(info.puntajePromedio);
								}
								if (
									info.cantValoraciones !== undefined &&
									info.cantValoraciones !== null
								) {
									nuevosVotos = parseInt(info.cantValoraciones, 10);
								}
							}

							// Fallback de cálculo aritmético si no viniera en el JSON
							if (
								nuevoPromedio === null ||
								isNaN(nuevoPromedio) ||
								nuevosVotos === null ||
								isNaN(nuevosVotos)
							) {
								const prevPromedio =
									parseFloat(celdaPromedio.getAttribute('data-promedio')) || 0;
								const prevVotos =
									parseInt(celdaPromedio.getAttribute('data-votos'), 10) || 0;
								nuevosVotos = prevVotos + 1;
								const sumaTotal = prevPromedio * prevVotos + puntaje;
								nuevoPromedio = Math.round((sumaTotal / nuevosVotos) * 10) / 10;
							}

							celdaPromedio.setAttribute('data-promedio', nuevoPromedio);
							celdaPromedio.setAttribute('data-votos', nuevosVotos);

							const textoVotos =
								nuevosVotos === 1 ? '1 voto' : nuevosVotos + ' votos';
							const promedioFormateado =
								nuevoPromedio % 1 === 0
									? nuevoPromedio.toFixed(1)
									: nuevoPromedio;

							celdaPromedio.innerHTML =
								'<span class="stat-estrellas">★ ' +
								promedioFormateado +
								'</span> ' +
								'<span class="stat-votos">(' +
								textoVotos +
								')</span>';
						}
					});
				}

				const restantes = form.querySelectorAll('.select-valoracion');
				if (!restantes || restantes.length === 0) {
					if (btnGuardar) {
						btnGuardar.disabled = true;
						btnGuardar.textContent = 'Todas las valoraciones enviadas';
					}
				} else if (btnGuardar) {
					btnGuardar.disabled = false;
				}
			} else {
				mostrarPopup(
					data.mensaje || 'Ocurrió un error al guardar la valoración.',
					'Error',
					'✕',
				);
				if (btnGuardar) {
					btnGuardar.disabled = false;
				}
			}
		})
		.catch(function (err) {
			mostrarPopup(
				'No se pudo conectar con el servidor: ' + err.message,
				'Error de Conexión',
				'✕',
			);
			if (btnGuardar) {
				btnGuardar.disabled = false;
			}
		});
}

/**
 * Guarda la valoración de un curso individual desde la vista detallada (verInfo-curso.jsp)
 */
function guardarValoracionCursoDetalle(nombreCurso) {
	const selector = document.querySelector(
		'.rating-stars-selector[data-curso="' + nombreCurso + '"]',
	);
	if (!selector) return;
	const input = selector.querySelector(
		'input.stat-input-valoracion, input.select-valoracion',
	);
	const val = input ? input.value.trim() : '';

	if (val === '') {
		mostrarPopup(
			'Debe seleccionar una estrella (de 1 a 5) antes de guardar.',
			'Aviso',
			'!',
		);
		return;
	}

	const puntaje = parseInt(val, 10);
	if (isNaN(puntaje) || puntaje < 1 || puntaje > 5) {
		mostrarPopup(
			'El puntaje debe ser un valor entero entre 1 y 5.',
			'Error de Validación',
			'✕',
		);
		return;
	}

	const btnGuardar = document.getElementById('btnGuardarValoracionDetalle');
	if (btnGuardar) {
		btnGuardar.disabled = true;
	}

	const form = document.getElementById('formValoracionDetalle');
	let endpoint = form && form.action ? form.action : '';
	if (!endpoint) {
		const base = window.location.pathname.substring(
			0,
			window.location.pathname.indexOf('/', 1),
		);
		endpoint = base + '/misCursos';
	}

	const params = new URLSearchParams();
	params.append('ajax', 'true');
	params.append('nombreCurso', nombreCurso);
	params.append('puntaje', puntaje.toString());
	params.append('valoracion-' + nombreCurso, puntaje.toString());

	fetch(endpoint, {
		method: 'POST',
		body: params,
		headers: {
			'X-Requested-With': 'XMLHttpRequest',
			Accept: 'application/json',
		},
	})
		.then(function (res) {
			return res.json().then(function (data) {
				return { status: res.status, ok: res.ok, data: data };
			});
		})
		.then(function (result) {
			const data = result.data;
			if (result.ok && data.success) {
				mostrarPopup(
					data.mensaje || 'Valoración registrada con éxito.',
					'¡Valoración Guardada!',
					'✓',
				);

				// Actualizar la sección de Tu Valoración en la vista detallada
				const celda = document.getElementById('col-valoracion-' + nombreCurso);
				if (celda) {
					let starsHtml =
						'<div class="rating-stars-readonly" aria-label="Valoración enviada: ' +
						puntaje +
						' de 5" style="margin-bottom: 4px;">';
					for (let s = 1; s <= 5; s++) {
						const isFilled = s <= puntaje ? 'active' : '';
						starsHtml +=
							'<svg class="star-icon ' +
							isFilled +
							'" viewBox="0 0 24 24" aria-hidden="true">' +
							'<polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>' +
							'</svg>';
					}
					starsHtml += '</div>';

					celda.innerHTML =
						starsHtml +
						'<small class="stat-subtexto-enviado">(Valoración enviada)</small>';
				}

				// Actualizar el Desglose y Promedio en la vista detallada sin recargar la página
				const celdaPromedio = document.getElementById(
					'stat-promedio-' + nombreCurso,
				);
				if (celdaPromedio) {
					let nuevoPromedio = null;
					let nuevosVotos = null;
					let c1 = null,
						c2 = null,
						c3 = null,
						c4 = null,
						c5 = null;

					if (data.promedios && data.promedios[nombreCurso]) {
						const info = data.promedios[nombreCurso];
						if (
							info.puntajePromedio !== undefined &&
							info.puntajePromedio !== null
						) {
							nuevoPromedio = parseFloat(info.puntajePromedio);
						}
						if (
							info.cantValoraciones !== undefined &&
							info.cantValoraciones !== null
						) {
							nuevosVotos = parseInt(info.cantValoraciones, 10);
						}
						if (info.cantVal1 !== undefined) c1 = parseInt(info.cantVal1, 10);
						if (info.cantVal2 !== undefined) c2 = parseInt(info.cantVal2, 10);
						if (info.cantVal3 !== undefined) c3 = parseInt(info.cantVal3, 10);
						if (info.cantVal4 !== undefined) c4 = parseInt(info.cantVal4, 10);
						if (info.cantVal5 !== undefined) c5 = parseInt(info.cantVal5, 10);
					}

					if (c1 === null || isNaN(c1)) {
						c1 = parseInt(celdaPromedio.getAttribute('data-val1'), 10) || 0;
						c2 = parseInt(celdaPromedio.getAttribute('data-val2'), 10) || 0;
						c3 = parseInt(celdaPromedio.getAttribute('data-val3'), 10) || 0;
						c4 = parseInt(celdaPromedio.getAttribute('data-val4'), 10) || 0;
						c5 = parseInt(celdaPromedio.getAttribute('data-val5'), 10) || 0;

						if (puntaje === 1) c1++;
						else if (puntaje === 2) c2++;
						else if (puntaje === 3) c3++;
						else if (puntaje === 4) c4++;
						else if (puntaje === 5) c5++;
					}

					if (nuevosVotos === null || isNaN(nuevosVotos)) {
						nuevosVotos = c1 + c2 + c3 + c4 + c5;
					}

					if (nuevoPromedio === null || isNaN(nuevoPromedio)) {
						const sumaTotal = c1 * 1 + c2 * 2 + c3 * 3 + c4 * 4 + c5 * 5;
						nuevoPromedio =
							nuevosVotos > 0
								? Math.round((sumaTotal / nuevosVotos) * 10) / 10
								: 0.0;
					}

					celdaPromedio.setAttribute('data-promedio', nuevoPromedio);
					celdaPromedio.setAttribute('data-votos', nuevosVotos);
					celdaPromedio.setAttribute('data-val1', c1);
					celdaPromedio.setAttribute('data-val2', c2);
					celdaPromedio.setAttribute('data-val3', c3);
					celdaPromedio.setAttribute('data-val4', c4);
					celdaPromedio.setAttribute('data-val5', c5);

					// Actualizar barras y contadores individuales
					const counts = { 1: c1, 2: c2, 3: c3, 4: c4, 5: c5 };
					const maxVal = Math.max(c1, c2, c3, c4, c5, 1);
					for (let star = 1; star <= 5; star++) {
						const fill = document.getElementById('bar-fill-' + star);
						const countElem = document.getElementById('bar-count-' + star);
						const cnt = counts[star];
						const pct = nuevosVotos > 0 ? (cnt * 100.0) / maxVal : 0;
						if (fill) fill.style.width = pct + '%';
						if (countElem) countElem.textContent = cnt.toLocaleString();
					}

					// Actualizar tarjeta resumen (Average rating)
					const scoreElem = document.getElementById('summary-score');
					if (scoreElem) {
						scoreElem.textContent =
							nuevoPromedio % 1 === 0
								? nuevoPromedio.toFixed(1)
								: nuevoPromedio;
					}

					const totalElem = document.getElementById('summary-total');
					if (totalElem) {
						totalElem.textContent = nuevosVotos.toLocaleString();
					}

					const starsWrap = document.getElementById('summary-stars');
					if (starsWrap) {
						let smallStars = '';
						for (let s = 1; s <= 5; s++) {
							const activeCls = s <= Math.round(nuevoPromedio) ? 'active' : '';
							smallStars +=
								'<svg class="star-icon-small ' +
								activeCls +
								'" viewBox="0 0 24 24" aria-hidden="true">' +
								'<polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>' +
								'</svg>';
						}
						starsWrap.innerHTML = smallStars;
					}
				}
			} else {
				mostrarPopup(
					data.mensaje || 'Ocurrió un error al guardar la valoración.',
					'Error',
					'✕',
				);
				if (btnGuardar) {
					btnGuardar.disabled = false;
				}
			}
		})
		.catch(function (err) {
			mostrarPopup(
				'No se pudo conectar con el servidor: ' + err.message,
				'Error de Conexión',
				'✕',
			);
			if (btnGuardar) {
				btnGuardar.disabled = false;
			}
		});
}
