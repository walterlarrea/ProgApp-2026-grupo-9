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

    
    public DataProgramaFormacion guardarNuevoProgramaDeFormacion(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin){
        System.out.println("[CLIENTE] Llamada al servidor central: " + nombre);
        DataProgramaFormacion nuevoPrograma = new DataProgramaFormacion(nombre, descripcion, new HashSet<>(), fechaInicio, fechaFin, null);
        
        DataProgramaFormacion programaCreado = servidorCentral.guardarProgramaDeFormacion(nuevoPrograma);
        if(programaCreado != null){
            System.out.println("[CLIENTE] Programa creado con exito!");
        } else {
            System.out.println("[CLIENTE] Algo salio mal creando el nuevo Programa");
        }
        
        return programaCreado;
    }
    
    public HashSet<DataProgramaFormacion> cargarProgramas(){
        System.out.println("[CLIENTE] Consultar todos los Programas");
        HashSet<DataProgramaFormacion> programas = servidorCentral.consultarTodosLosProgramas();
        
        return programas;
    }

    public Boolean existeProgramaDeFormacion(String nombre){
        System.out.println("[CLIENTE] Consultar si existe el Programa: " + nombre);
        return servidorCentral.existeProgramaDeFormacion(nombre);
    }
    
    public DataProgramaFormacion buscarPorNombreId(String nombreId){
        System.out.println("[CLIENTE] Consultar todos los Programas");
        DataProgramaFormacion programa = servidorCentral.traerPorNombreId(nombreId);
        
        return programa;
    }

    public Boolean agregarCursoAProgramaDeFormacion(String nombreIdPrograma, String nombreIdCurso){
        System.out.println("[CLIENTE] Agregar curso " + nombreIdCurso + " al programa " + nombreIdPrograma);
        Boolean resultado = servidorCentral.agregarCursoAProgramaDeFormacion(nombreIdPrograma, nombreIdCurso);
        
        return resultado;
    }
}
