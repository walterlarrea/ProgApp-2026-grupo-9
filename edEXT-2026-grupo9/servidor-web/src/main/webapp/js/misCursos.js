/**
 * Gestión de Valoraciones de Cursos por Estudiante (AJAX)
 */

function guardarValoracionesAjax() {
    const form = document.getElementById('formValoraciones');
    if (!form) return;

    const selects = form.querySelectorAll('.select-valoracion');
    if (!selects || selects.length === 0) {
        mostrarPopup('Ya has valorado todos tus cursos participantes.', 'Aviso', '!');
        return;
    }

    let cantidadSeleccionadas = 0;
    const valoracionesMap = {};

    for (let i = 0; i < selects.length; i++) {
        const val = selects[i].value.trim();
        const cursoNom = selects[i].getAttribute('data-curso');
        if (val !== '') {
            const num = parseInt(val, 10);
            if (isNaN(num) || num < 1 || num > 5) {
                mostrarPopup('El puntaje debe ser un valor entero entre 1 y 5.', 'Error de Validación', '✕');
                selects[i].focus();
                return;
            }
            cantidadSeleccionadas++;
            valoracionesMap[cursoNom] = num;
        }
    }

    if (cantidadSeleccionadas === 0) {
        mostrarPopup('Debe asignar un puntaje (de 1 a 5) a al menos un curso antes de guardar.', 'Aviso', '!');
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
            'Accept': 'application/json'
        }
    })
    .then(function(res) {
        return res.json().then(function(data) {
            return { status: res.status, ok: res.ok, data: data };
        });
    })
    .then(function(result) {
        const data = result.data;
        if (result.ok && data.success) {
            mostrarPopup(data.mensaje || 'Valoración registrada con éxito.', '¡Valoración Guardada!', '✓');

            if (data.actualizados && Array.isArray(data.actualizados)) {
                data.actualizados.forEach(function(nombreCurso) {
                    const celda = document.getElementById('col-valoracion-' + nombreCurso);
                    const puntaje = valoracionesMap[nombreCurso];
                    if (celda && puntaje) {
                        celda.innerHTML = '<span class="badge badge-success" style="font-size: 0.95rem; padding: 6px 12px; font-weight: 600;">★ ' +
                            puntaje + ' / 5</span><br><small style="color: #64748b; font-style: italic;">(Valoración enviada)</small>';
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
            mostrarPopup(data.mensaje || 'Ocurrió un error al guardar la valoración.', 'Error', '✕');
            if (btnGuardar) {
                btnGuardar.disabled = false;
            }
        }
    })
    .catch(function(err) {
        mostrarPopup('No se pudo conectar con el servidor: ' + err.message, 'Error de Conexión', '✕');
        if (btnGuardar) {
            btnGuardar.disabled = false;
        }
    });
}
