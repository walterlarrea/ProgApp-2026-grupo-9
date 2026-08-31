package com.grupo9.edext.grupo9.estacion_de_trabajo.cliente;

import com.grupo9.edext.grupo9.dtos.DTOCurso;

import com.grupo9.edext.grupo9.dtos.DTOProgramaDeFormacion;
import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.servidor_central.controller.ServidorCentralController;
import java.util.Date;
import java.util.HashSet;

public class ProgramaDeFormacionPres {
    IServidorCentral servidorCentral = ServidorCentralController.getInstance();
  
    public ProgramaDeFormacionPres(){
//        servidorCentral.logStatus();
    }

    
    public void guardarNuevoProgramaDeFormacion(String nombre, String descripcion, Date fechaInicio, Date fechaFin){
        System.out.println("[CLIENTE] Llamada al servidor central: " + nombre);
        DTOProgramaDeFormacion nuevoPrograma = new DTOProgramaDeFormacion(nombre, descripcion, new HashSet<>(), fechaInicio, fechaFin);
        
        DTOProgramaDeFormacion programaCreado = servidorCentral.guardarProgramaDeFormacion(nuevoPrograma);
        if(programaCreado != null){
            System.out.println("[CLIENTE] Programa creado con exito!");
        } else {
            System.out.println("[CLIENTE] Algo salio mal creando el nuevo Programa");
        }
    }
    
    public HashSet<DTOProgramaDeFormacion> cargarProgramas(){
        System.out.println("[CLIENTE] Consultar todos los Programas");
        HashSet<DTOProgramaDeFormacion> cursos = servidorCentral.consultarTodosLosProgramas();
        
        return cursos;
    }
}
