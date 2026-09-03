package com.grupo9.edext.grupo9.servidor_central.controller.instituto;

import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import java.util.HashSet;


public class InstitutoController implements IInstituto{
    ManejadorInstituto manejadorInstituto = null;
    
    public InstitutoController(){
        this.manejadorInstituto = ManejadorInstituto.getInstance();
    }
    
    @Override
    public DataInstituto guardarNuevoInstituto(DataInstituto instituto){
        System.out.println("[SERVIDOR] Persistencia de un nuevo Instituto: " + instituto.nombreI());
        
        Instituto nuevoInstituto = new Instituto(instituto.nombreI());

        try{
            this.manejadorInstituto.guardarNuevo(nuevoInstituto);
            
            // TODO: devolver un nuevo DTO creado a partir del Instituto ya guardado
            return instituto;
        }catch(Exception e){
            System.out.println("[SERVIDOR] Persistencia FALLÓ al crear un nuevo Instituto: " + nuevoInstituto.getNombreI());
            System.out.println(e);
        }
        return null;
    }
    
    @Override
    public HashSet<DataInstituto> todosLosInstitutos(){
        System.out.println("[SERVIDOR] Consulta todos los Institutos a persistencia");
        try {
            HashSet<DataInstituto> todosLosInstitutos = this.manejadorInstituto.traerTodos();
            
            return todosLosInstitutos;
        } catch (Exception e) {
            System.out.println("[SERVIDOR] Persistencia FALLÓ al intentar traer todos los Institutos");
            System.out.println(e);
        }
        return null;
    }
}
