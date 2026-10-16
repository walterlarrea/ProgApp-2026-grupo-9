document.addEventListener("DOMContentLoaded", function () {
    const selectCurso = document.getElementById("curso");
    const contextPath = document.body.dataset.contextPath;

    if (selectCurso) {
        selectCurso.addEventListener("change", function () {
            const curso = this.value;
            const instituto = document.getElementById("instituto").value;

            if (curso === ""){
                window.location.href = contextPath + "/inscripcion?accion=insc&instituto=" + encodeURIComponent(instituto);
            }else{    
                window.location.href = contextPath + "/inscripcion?accion=insc&instituto=" + encodeURIComponent(instituto) + "&curso=" + encodeURIComponent(curso);
            }
        });
    }
});