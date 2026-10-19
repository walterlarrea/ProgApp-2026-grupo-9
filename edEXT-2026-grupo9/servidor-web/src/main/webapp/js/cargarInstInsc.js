document.addEventListener("DOMContentLoaded", function () { // para que se cargue el HTML
    const selectInstituto = document.getElementById("instituto");
    const contextPath = document.body.dataset.contextPath;

    if (selectInstituto) {
        selectInstituto.addEventListener("change", function () {
            const instituto = this.value;

            if (instituto === "") {
                window.location.href = contextPath + "/inscripcion?accion=insc";
            } else {
                window.location.href = contextPath + "/inscripcion?accion=insc&instituto=" + encodeURIComponent(instituto);
            }
        });
    }
});