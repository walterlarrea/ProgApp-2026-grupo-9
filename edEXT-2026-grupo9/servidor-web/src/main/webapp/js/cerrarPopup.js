function cerrarPopup() {
    const popup = document.getElementById("popupExito");
    if (popup) {
        popup.style.display = "none";
    }
}

function mostrarPopup(mensaje, titulo, icono) {
    let popup = document.getElementById("popupExito");
    if (!popup) {
        popup = document.createElement("div");
        popup.id = "popupExito";
        popup.className = "popup-overlay";
        popup.setAttribute("role", "dialog");
        popup.setAttribute("aria-modal", "true");
        popup.innerHTML = `
            <div class="popup-contenido">
                <div class="popup-icono" id="popupIcono">✓</div>
                <h2 id="tituloPopup">¡Aviso!</h2>
                <p id="mensajePopup"></p>
                <button type="button" id="btnCerrarPopup" class="btn btn-primary" onclick="cerrarPopup()">Aceptar</button>
            </div>
        `;
        document.body.appendChild(popup);
    }

    const elemTitulo = document.getElementById("tituloPopup");
    if (elemTitulo && titulo !== undefined && titulo !== null) {
        elemTitulo.textContent = titulo;
    }

    const elemMensaje = document.getElementById("mensajePopup");
    if (elemMensaje && mensaje !== undefined && mensaje !== null) {
        elemMensaje.textContent = mensaje;
    }

    const elemIcono = document.getElementById("popupIcono");
    if (elemIcono && icono !== undefined && icono !== null) {
        elemIcono.textContent = icono;
    }

    popup.style.display = "flex";
}

// Para cerrar el popup con Esc
document.addEventListener("keydown", function(event) {
    if (event.key === "Escape") {
        cerrarPopup();
    }
});
