package com.grupo9.edext.grupo9.mensajes;

@SuppressWarnings("serial")
public class ErrorEstadoInvalido extends Exception {
    
    public ErrorEstadoInvalido (String mensaje) {
        super(mensaje);
    }
}
