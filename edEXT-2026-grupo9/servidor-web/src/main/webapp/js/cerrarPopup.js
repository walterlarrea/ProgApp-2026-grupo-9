// Función opcional a ejecutar cuando se cierra el popup de mensaje (ver mostrarPopup)
let alCerrarPopup = null;

function cerrarPopup() {
    const popup = document.getElementById("popupExito");
    if (popup) {
        popup.style.display = "none";
    }
    if (typeof alCerrarPopup === "function") {
        const callback = alCerrarPopup;
        alCerrarPopup = null;
        callback();
    }
}

/**
 * Muestra el popup de mensaje con un único botón "Aceptar".
 * alCerrar (opcional): función que se ejecuta al cerrarlo.
 */
function mostrarPopup(mensaje, titulo, icono, alCerrar) {
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
                <h2 id="tituloPopup">¡Registrado!</h2>
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
        elemMensaje.style.whiteSpace = "pre-line"; // respeta los saltos de línea del mensaje
    }

    const elemIcono = document.getElementById("popupIcono");
    if (elemIcono && icono !== undefined && icono !== null) {
        elemIcono.textContent = icono;
    }

    alCerrarPopup = typeof alCerrar === "function" ? alCerrar : null;
    popup.style.display = "flex";
}

/**
 * Popup de confirmación con el mismo estilo que el de mensajes:
 * "Aceptar" (verde) ejecuta alAceptar; "Cancelar" (rojo) o Esc lo cierran sin hacer nada.
 */
function mostrarConfirmacion(mensaje, titulo, alAceptar, icono) {
    let popup = document.getElementById("popupConfirmacion");
    if (!popup) {
        popup = document.createElement("div");
        popup.id = "popupConfirmacion";
        popup.className = "popup-overlay";
        popup.setAttribute("role", "dialog");
        popup.setAttribute("aria-modal", "true");
        popup.innerHTML = `
            <div class="popup-contenido">
                <div class="popup-icono" id="confirmacionIcono">?</div>
                <h2 id="confirmacionTitulo"></h2>
                <p id="confirmacionMensaje" style="white-space: pre-line;"></p>
                <div class="popup-botones">
                    <button type="button" id="btnConfirmarAceptar" class="btn btn-success">Aceptar</button>
                    <button type="button" id="btnConfirmarCancelar" class="btn btn-danger">Cancelar</button>
                </div>
            </div>
        `;
        document.body.appendChild(popup);
        document.getElementById("btnConfirmarCancelar").addEventListener("click", cerrarConfirmacion);
    }

    document.getElementById("confirmacionTitulo").textContent = titulo || "¿Confirmar?";
    document.getElementById("confirmacionMensaje").textContent = mensaje || "";
    document.getElementById("confirmacionIcono").textContent = icono || "?";
    document.getElementById("btnConfirmarAceptar").onclick = function () {
        cerrarConfirmacion();
        if (typeof alAceptar === "function") {
            alAceptar();
        }
    };

    popup.style.display = "flex";
}

function cerrarConfirmacion() {
    const popup = document.getElementById("popupConfirmacion");
    if (popup) {
        popup.style.display = "none";
    }
}

// Para cerrar los popups con Esc
document.addEventListener("keydown", function(event) {
    if (event.key === "Escape") {
        cerrarConfirmacion();
        cerrarPopup();
    }
});
