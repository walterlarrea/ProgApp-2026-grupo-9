package com.grupo9.edext.grupo9.estacion_de_trabajo.cliente;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.servidor_central.controller.ServidorCentralController;

public class ProgramaDeFormacion {
    IServidorCentral servidorCentral = ServidorCentralController.getInstance();
  
    public ProgramaDeFormacion(){
        servidorCentral.logStatus();
    }

    
    public void guardarNuevoProgramaDeFormacion(){
        System.out.println("[CLIENTE] Mock de ENVÍO AL SERVIDOR de un nuevo programa de formación");
        servidorCentral.guardarProgramaDeFormacion();
    }
}
