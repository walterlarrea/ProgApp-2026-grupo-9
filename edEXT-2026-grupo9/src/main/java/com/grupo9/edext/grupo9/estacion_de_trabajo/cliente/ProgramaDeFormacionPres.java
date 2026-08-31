package com.grupo9.edext.grupo9.estacion_de_trabajo.cliente;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.servidor_central.controller.ServidorCentralController;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import java.time.LocalDate;
import java.util.HashSet;

public class ProgramaDeFormacionPres {
    IServidorCentral servidorCentral = ServidorCentralController.getInstance();
  
    public ProgramaDeFormacionPres(){
//        servidorCentral.logStatus();
    }

    
    public void guardarNuevoProgramaDeFormacion(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin){
        System.out.println("[CLIENTE] Llamada al servidor central: " + nombre);
        DataProgramaFormacion nuevoPrograma = new DataProgramaFormacion(nombre, descripcion, new HashSet<>(), fechaInicio, fechaFin);
        
        DataProgramaFormacion programaCreado = servidorCentral.guardarProgramaDeFormacion(nuevoPrograma);
        if(programaCreado != null){
            System.out.println("[CLIENTE] Programa creado con exito!");
        } else {
            System.out.println("[CLIENTE] Algo salio mal creando el nuevo Programa");
        }
    }
    
    public HashSet<DataProgramaFormacion> cargarProgramas(){
        System.out.println("[CLIENTE] Consultar todos los Programas");
        HashSet<DataProgramaFormacion> cursos = servidorCentral.consultarTodosLosProgramas();
        
        return cursos;
    }
}
