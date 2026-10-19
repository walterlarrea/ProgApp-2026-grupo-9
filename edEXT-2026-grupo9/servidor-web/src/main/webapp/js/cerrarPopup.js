function cerrarPopup() {
    const popup = document.getElementById("popupExito");

    if (popup) {
        popup.remove();
    }
}

    //para cerrar el popup con esc.
    document.addEventListener("keydown", function(event) {
        if (event.key === "Escape") {
            cerrarPopup();
        }
    });



