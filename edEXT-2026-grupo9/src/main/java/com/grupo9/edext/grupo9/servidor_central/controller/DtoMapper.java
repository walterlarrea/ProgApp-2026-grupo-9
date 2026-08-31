package com.grupo9.edext.grupo9.servidor_central.controller;

import java.util.HashSet;
import java.util.Set;

import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.ProgramaDeFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;


public class DtoMapper {

    //Cursos
    
    public static Curso toEntity(DataCurso dataCurso){
        Curso curso = new Curso(
                dataCurso.nombreInst(),
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
        DataCurso dataCurso = new DataCurso(
                curso.getNombreInst(),
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

    public static ProgramaDeFormacion toData(DataProgramaFormacion dataPrograma){
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

    public static DataProgramaFormacion ProgramadeFormToDTOProgramadeForm(ProgramaDeFormacion programa){
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
}
