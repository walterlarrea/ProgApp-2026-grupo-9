package com.grupo9.edext.grupo9.estacion_de_trabajo.cliente;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.servidor_central.controller.ServidorCentralController;

public class InstitutoPres {
    IServidorCentral servidorCentral = ServidorCentralController.getInstance();

    public void altaInstituto(String nombre) throws ErrorRepetidos {
        servidorCentral.altaInstituto(nombre);
    }
}