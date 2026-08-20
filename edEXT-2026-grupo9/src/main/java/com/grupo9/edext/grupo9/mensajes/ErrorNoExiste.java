package com.grupo9.edext.grupo9.mensajes;


@SuppressWarnings("serial")
public class ErrorNoExiste extends Exception {
    
    public ErrorNoExiste (String mensaje) {
        super(mensaje);
    }
}

