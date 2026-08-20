/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo9.edext.grupo9.estacion_de_trabajo.cliente;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.servidor_central.controller.ServidorCentralController;
/**
 *
 * @author ivomaciel
 */
public class EdicionCursoPres {
    IServidorCentral servidorCentral = ServidorCentralController.getInstance();
    
     public EdicionCursoPres(){
        servidorCentral.logStatus();
    }

    
    public void guardarNuevaEdicionCurso(){
        System.out.println("[CLIENTE] Mock de ENVÍO AL SERVIDOR de una nueva edición de curso");
        servidorCentral.guardarProgramaDeFormacion();
    }
}
