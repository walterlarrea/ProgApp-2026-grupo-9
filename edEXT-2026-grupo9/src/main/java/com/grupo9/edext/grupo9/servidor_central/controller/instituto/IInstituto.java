package com.grupo9.edext.grupo9.servidor_central.controller.instituto;

import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;

public interface IInstituto {
    public void altaInstituto(String nombre) throws ErrorRepetidos;
}