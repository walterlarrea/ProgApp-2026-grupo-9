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
        String fechaCreacion,
        String imagenUrl,
        Double puntajePromedio,
        int cantValoraciones
) {
    public ResultadoBusquedaExtendida(
            TipoBusqueda tipo,
            Object data,
            String tipoVisible,
            String nombreVisible,
            String nombreVisibleHighlighted,
            String descVisibleHighlighted,
            String href,
            String tipoCss,
            String fechaCreacion,
            String imagenUrl) {
        this(tipo, data, tipoVisible, nombreVisible, nombreVisibleHighlighted, descVisibleHighlighted, href, tipoCss, fechaCreacion, imagenUrl, null, 0);
    }
}
