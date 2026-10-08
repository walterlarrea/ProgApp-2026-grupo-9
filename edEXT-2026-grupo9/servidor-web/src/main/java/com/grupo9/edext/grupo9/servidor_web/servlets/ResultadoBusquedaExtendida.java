package com.grupo9.edext.grupo9.servidor_web.servlets;

import com.grupo9.edext.grupo9.servidor_central.controller.busqueda.TipoBusqueda;

public record ResultadoBusquedaExtendida(
        TipoBusqueda tipo,
        Object data,
        String tipoVisible,
        String nombreVisible,
        String nombreVisibleHighlighted,
        String href,
        String tipoCss
) {
}