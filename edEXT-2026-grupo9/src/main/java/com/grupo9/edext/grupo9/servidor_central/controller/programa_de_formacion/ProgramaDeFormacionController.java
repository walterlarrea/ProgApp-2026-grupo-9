package com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion;

import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import java.util.HashSet;

public class ProgramaDeFormacionController implements IProgramaDeFormacion {
    ManejadorProgDeFormacion manejadorProgDeFormacion = null;
    
    public ProgramaDeFormacionController(){
        this.manejadorProgDeFormacion = ManejadorProgDeFormacion.getInstance();
    }
    
    @Override
    public DataProgramaFormacion guardarNuevoProgramaDeFormacion(DataProgramaFormacion programa){
        System.out.println("[SERVIDOR] Persistencia de un nuevo programa de formación: " + programa.nombre());
        
        ProgramaDeFormacion nuevoProgramaDeFormacion = new ProgramaDeFormacion(programa.nombre(), programa.descripcion(), new HashSet<Curso>(), programa.fechaInicio(), programa.fechaFin());

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
    public HashSet<DataProgramaFormacion> todosLosProgramas(){
        System.out.println("[SERVIDOR] Consulta todos los Programas a persistencia");
        try {
            HashSet<DataProgramaFormacion> todosLosProgramas = this.manejadorProgDeFormacion.traerTodos();
            
            return todosLosProgramas;
        } catch (Exception e) {
            System.out.println("[SERVIDOR] Persistencia FALLÓ al intentar traer todos los Programas");
            System.out.println(e);
        }
        return null;
    }
}
