package com.grupo9.edext.grupo9.servidor_central.dominio;

import com.grupo9.edext.grupo9.servidor_central.controller.busqueda.TipoBusqueda;

public record ResultadoBusqueda(TipoBusqueda tipo, Object data) {
}