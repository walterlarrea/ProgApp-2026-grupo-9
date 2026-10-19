document.addEventListener("DOMContentLoaded", function () {
    const selectEdicion = document.getElementById("edicion");
    const selectInstituto = document.getElementById("instituto");
    const selectCurso = document.getElementById("curso");
    const contextPath = document.body.dataset.contextPath;

    if (selectEdicion) {
        selectEdicion.addEventListener("change", function () {
            const instituto = selectInstituto.value;
            const curso = selectCurso.value;
            const edicion = this.value;
            const params = new URLSearchParams();
            params.set("accion", "insc");

            if (instituto) params.set("instituto", instituto);
            if (curso) params.set("curso", curso);
            if (edicion) params.set("edicion", edicion);

            window.location.href = contextPath + "/inscripcion?" + params.toString();
        });
    }
});
