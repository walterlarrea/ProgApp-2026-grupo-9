/**
 * Gestión de Evaluaciones de Estudiantes por Docente (AJAX)
 */

function ejecutarAccionAjax(accion) {
    const form = document.getElementById('formEvaluaciones');
    if (!form) return;

    const inputs = form.querySelectorAll('.input-nota');

    // Validación de rango 1 - 12
    for (let i = 0; i < inputs.length; i++) {
        const val = inputs[i].value.trim();
        if (val !== '') {
            const num = parseInt(val, 10);
            if (isNaN(num) || num < 1 || num > 12) {
                mostrarPopup('Todas las notas deben ser valores numéricos comprendidos entre 1 y 12.', 'Error de Validación', '✕');
                inputs[i].focus();
                return;
            }
        }
    }

    // Validaciones específicas para el cierre del curso
    if (accion === 'cerrar') {
        if (inputs.length === 0) {
            mostrarPopup('No hay estudiantes aceptados para cerrar el curso.', 'Aviso', '!');
            return;
        }

        let faltanNotas = false;
        for (let i = 0; i < inputs.length; i++) {
            if (inputs[i].value.trim() === '') {
                faltanNotas = true;
                break;
            }
        }

        if (faltanNotas) {
            mostrarPopup('Para indicar el cierre del curso, debe haber ingresado las evaluaciones de todos los estudiantes aceptados.', 'Evaluaciones pendientes', '!');
            return;
        }

        if (!confirm('¿Está seguro de que desea indicar el cierre del curso? Esta acción registrará las notas y no se podrán modificar posteriormente.')) {
            return;
        }
    }

    // Deshabilitar botones temporalmente durante el envío AJAX
    const btnGuardar = document.getElementById('btnGuardarEvaluaciones');
    const btnCerrar = document.getElementById('btnCerrarCurso');
    if (btnGuardar) btnGuardar.disabled = true;
    if (btnCerrar) btnCerrar.disabled = true;

    const formData = new FormData(form);
    formData.append('accion', accion);
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
            if (accion === 'cerrar' || data.cerrada) {
                mostrarPopup(data.mensaje || 'Curso cerrado exitosamente.', 'Curso Cerrado', '✓');

                // Deshabilitar todos los inputs de nota
                inputs.forEach(function(inp) {
                    inp.disabled = true;
                });

                // Reemplazar botones de acción por mensaje informativo
                const contenedor = document.getElementById('contenedorAcciones');
                if (contenedor) {
                    contenedor.innerHTML = '<p style="color: #64748b; font-style: italic;">* Esta edición de curso ha sido cerrada. No es posible modificar las notas ingresadas.</p>';
                }

                // Actualizar badge de estado
                const badge = document.getElementById('badgeEstadoCurso');
                if (badge) {
                    badge.textContent = 'CERRADA';
                    badge.style.backgroundColor = '#ef4444';
                }
            } else {
                mostrarPopup(data.mensaje || 'Evaluaciones guardadas correctamente.', '¡Éxito!', '✓');
            }
        } else {
            mostrarPopup(data.mensaje || 'Ocurrió un error al procesar la solicitud.', 'Error', '✕');
        }
    })
    .catch(function(err) {
        mostrarPopup('Error de comunicación con el servidor: ' + err.message, 'Error de Conexión', '✕');
    })
    .finally(function() {
        if (btnGuardar) btnGuardar.disabled = false;
        if (btnCerrar) btnCerrar.disabled = false;
    });
}

document.addEventListener('DOMContentLoaded', function() {
    const btnGuardar = document.getElementById('btnGuardarEvaluaciones');
    if (btnGuardar && !btnGuardar.onclick) {
        btnGuardar.addEventListener('click', function() {
            ejecutarAccionAjax('guardar');
        });
    }

    const btnCerrar = document.getElementById('btnCerrarCurso');
    if (btnCerrar && !btnCerrar.onclick) {
        btnCerrar.addEventListener('click', function() {
            ejecutarAccionAjax('cerrar');
        });
    }
});
