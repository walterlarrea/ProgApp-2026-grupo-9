package com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion;

import com.grupo9.edext.grupo9.dtos.DTOProgramaDeFormacion;
import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.ProgramaDeFormacion;
import com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import java.util.HashSet;

public class ProgramaDeFormacionController implements IProgramaDeFormacion {
    
    @Override
    public void guardarNuevoProgramaDeFormacion(DTOProgramaDeFormacion nuevoPrograma){
        System.out.println("[SERVIDOR] Persistencia de un nuevo programa de formación: " + nuevoPrograma.getNombre());
        
        ProgramaDeFormacion nuevoProgramaDeFormacion = new ProgramaDeFormacion(nuevoPrograma.getNombre(), nuevoPrograma.getDescripcion(), new HashSet<Curso>(), nuevoPrograma.getFechaInicio(), nuevoPrograma.getFechaFin());

        try{
            UtensiliosJPA.save(nuevoProgramaDeFormacion);
        }catch(Exception e){
            System.out.println("[SERVIDOR] Persistencia FALLÓ al crear un nuevo programa de formación: " + nuevoPrograma.getNombre());
            System.out.println(e);
        }
    }
}
