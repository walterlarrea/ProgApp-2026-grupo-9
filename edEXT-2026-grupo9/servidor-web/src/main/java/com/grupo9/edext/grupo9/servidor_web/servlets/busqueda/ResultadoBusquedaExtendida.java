package com.grupo9.edext.grupo9.servidor_web.servlets.busqueda;

import com.grupo9.edext.grupo9.servidor_web.web_service_client.busqueda.TipoBusqueda;

public record ResultadoBusquedaExtendida(
        TipoBusqueda tipo,
        Object data,
        String tipoVisible,
        String nombreVisible,
        String nombreVisibleHighlighted,
        String descVisibleHighlighted,
        String href,
        String tipoCss,
        String fechaCreacion
) {
}
