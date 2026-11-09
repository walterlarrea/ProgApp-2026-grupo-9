// Carga la lista de institutos de la sidebar desde /institutosSidebar,
// así ningún servlet necesita setear "institutos" solo para la barra lateral.
document.addEventListener("DOMContentLoaded", function () {
    const lista = document.getElementById("sidebar-institutos");
    if (!lista) {
        return;
    }
    const contextPath = lista.dataset.contextPath || "";

    function mostrarVacio() {
        lista.innerHTML = "";
        const li = document.createElement("li");
        li.className = "empty-hint";
        li.textContent = "Sin institutos disponibles";
        lista.appendChild(li);
    }

    fetch(contextPath + "/institutosSidebar")
        .then(function (r) { return r.ok ? r.json() : []; })
        .then(function (nombres) {
            if (!nombres || nombres.length === 0) {
                mostrarVacio();
                return;
            }
            lista.innerHTML = "";
            nombres.forEach(function (nombre) {
                const li = document.createElement("li");
                const a = document.createElement("a");
                a.className = "corte-texto";
                a.href = contextPath + "/instituto?nombre=" + encodeURIComponent(nombre);
                a.textContent = nombre;
                li.appendChild(a);
                lista.appendChild(li);
            });
        })
        .catch(mostrarVacio);
});
