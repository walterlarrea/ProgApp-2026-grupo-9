package com.grupo9.edext.grupo9.estacion_de_trabajo.cliente;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.servidor_central.controller.ServidorCentralController;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import java.util.HashSet;


public class InstitutoPres {
    IServidorCentral servidorCentral = ServidorCentralController.getInstance();
    
    public InstitutoPres(){
//        servidorCentral.logStatus();
    }
    
    
    public void guardarNuevoInstituto(String nombre){
        System.out.println("[CLIENTE] Crear nuevo Instituto: " + nombre);
        DataInstituto nuevoInstituto = new DataInstituto(nombre);
        
        DataInstituto institutoCreado = servidorCentral.guardarInstituto(nuevoInstituto);
        if(institutoCreado != null){
            System.out.println("[CLIENTE] Instituto creado con exito!");
        } else {
            System.out.println("[CLIENTE] Algo salio mal creando el nuevo Instituto");
        }
    }
    
    public HashSet<DataInstituto> cargarInstitutos(){
        System.out.println("[CLIENTE] Consultar todos los Institutos");
        HashSet<DataInstituto> institutos = servidorCentral.consultarTodosLosInstitutos();
        
        return institutos;
    }
}
