package com.grupo9.edext.grupo9.servidor_central.controller;

import java.util.HashSet;
import java.util.Set;

import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.Instituto;
import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.ProgramaDeFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;


public class DtoMapper {

    //Cursos
    
    public static Curso toEntity(DataCurso dataCurso){
        if(dataCurso == null){
            return null;
        }
        Curso curso = new Curso(
                toEntity(dataCurso.instituto()),
                dataCurso.nombreCurso(),
                dataCurso.descCurso(),
                dataCurso.duracion(),
                dataCurso.cantHoras(),
                dataCurso.cantCred(),
                dataCurso.fechaReg(),
                dataCurso.url()
        );
        
        return curso;
    }

    public static DataCurso toData(Curso curso){
        if(curso == null){
            return null;
        }
        DataCurso dataCurso = new DataCurso(
                toData(curso.getInstituto()),
                curso.getNombreCurso(),
                curso.getDescCurso(),
                curso.getDuracion(),
                curso.getCantHoras(),
                curso.getCantCred(),
                curso.getFechaReg(),
                curso.getUrl()
        );
        
        return dataCurso;
    }
    
    // Programas de Formación

    public static ProgramaDeFormacion toEntity(DataProgramaFormacion dataPrograma){
        if(dataPrograma == null){
            return null;
        }
        Set<Curso> cursos = new HashSet<>();
        for (DataCurso dataCurso : dataPrograma.cursos()) {
            cursos.add(toEntity(dataCurso));
        }

        ProgramaDeFormacion programa = new ProgramaDeFormacion(
                dataPrograma.nombre(),
                dataPrograma.descripcion(),
                cursos,
                dataPrograma.fechaInicio(),
                dataPrograma.fechaFin()
        );
        
        return programa;
    }

    public static DataProgramaFormacion toData(ProgramaDeFormacion programa){
        if(programa == null){
            return null;
        }
        
        Set<DataCurso> cursos = new HashSet<>();

        for (Curso curso : programa.getCursos()) {
            cursos.add(toData(curso));
        }

        DataProgramaFormacion dataPrograma = new DataProgramaFormacion(
                programa.getNombre(),
                programa.getDescripcion(),
                cursos,
                programa.getFechaInicio(),
                programa.getFechaFin()
        );
        
        return dataPrograma;
    }
    
    // Institutos
    
 
    public static Instituto toEntity(DataInstituto dataInstituto){
        if(dataInstituto == null){
            return null;
        }

        Instituto instituto = new Instituto(
                dataInstituto.nombreI()
        );
        
        return instituto;
    }

    public static DataInstituto toData(Instituto instituto){
        if(instituto == null){
            return null;
        }
        
        DataInstituto dataInstituto = new DataInstituto(
                instituto.getNombreI()
        );
        
        return dataInstituto;
    }
}
