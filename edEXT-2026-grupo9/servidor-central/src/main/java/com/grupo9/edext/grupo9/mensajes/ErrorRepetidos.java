package com.grupo9.edext.grupo9.mensajes;

@SuppressWarnings("serial")
public class ErrorRepetidos extends Exception {
    
    public ErrorRepetidos (String mensaje) {
        super(mensaje);
    }
}
