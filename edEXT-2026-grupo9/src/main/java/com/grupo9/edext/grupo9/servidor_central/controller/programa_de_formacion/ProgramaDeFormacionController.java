package com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion;

import com.grupo9.edext.grupo9.dtos.DTOProgramaDeFormacion;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import java.util.HashSet;

public class ProgramaDeFormacionController implements IProgramaDeFormacion {
    ManejadorProgDeFormacion manejadorProgDeFormacion = null;
    
    public ProgramaDeFormacionController(){
        this.manejadorProgDeFormacion = ManejadorProgDeFormacion.getInstance();
    }
    
    @Override
    public DTOProgramaDeFormacion guardarNuevoProgramaDeFormacion(DTOProgramaDeFormacion programa){
        System.out.println("[SERVIDOR] Persistencia de un nuevo programa de formación: " + programa.getNombre());
        
        ProgramaDeFormacion nuevoProgramaDeFormacion = new ProgramaDeFormacion(programa.getNombre(), programa.getDescripcion(), new HashSet<Curso>(), programa.getFechaInicio(), programa.getFechaFin());

        try{
//            UtensiliosJPA.save(nuevoProgramaDeFormacion);
            this.manejadorProgDeFormacion.guardarNuevo(nuevoProgramaDeFormacion);
            
            // TODO: devolver un nuevo DTO creado a partir del Programa ya guardado
            return programa;
        }catch(Exception e){
            System.out.println("[SERVIDOR] Persistencia FALLÓ al crear un nuevo programa de formación: " + nuevoProgramaDeFormacion.getNombre());
            System.out.println(e);
        }
        return null;
    }
    
    @Override
    public HashSet<DTOProgramaDeFormacion> todosLosProgramas(){
        System.out.println("[SERVIDOR] Consulta todos los Programas a persistencia");
        try {
            HashSet<DTOProgramaDeFormacion> todosLosProgramas = this.manejadorProgDeFormacion.traerTodos();
            
            return todosLosProgramas;
        } catch (Exception e) {
            System.out.println("[SERVIDOR] Persistencia FALLÓ al intentar traer todos los Programas");
            System.out.println(e);
        }
        return null;
    }
}
