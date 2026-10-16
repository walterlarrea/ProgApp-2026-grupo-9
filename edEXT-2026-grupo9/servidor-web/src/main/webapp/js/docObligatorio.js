const formulario = document.getElementById("formularioEdicion");

formulario.addEventListener("submit", function (event) {
    const docentes = document.querySelectorAll(
        'input[name="docentes"]:checked'
    );

    if (docentes.length === 0) {
        event.preventDefault();
        alert("Debe seleccionar al menos un docente.");
    }
});


