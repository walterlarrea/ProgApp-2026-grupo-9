package com.grupo9.edext.grupo9.servidor_central.controller;

import java.util.HashSet;
import java.util.Set;

import com.grupo9.edext.grupo9.dtos.DTOCurso;
import com.grupo9.edext.grupo9.dtos.DTOProgramaDeFormacion;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.ProgramaDeFormacion;


public class DtoMapper {

    //Cursos
    public static Curso DTOCursoToCurso(DTOCurso dtoCurso){
        Curso curso = new Curso(
                dtoCurso.getNombreInstituto(),
                dtoCurso.getNombre(),
                dtoCurso.getDescripcion(),
                dtoCurso.getDuracion(),
                dtoCurso.getCantHoras(),
                dtoCurso.getCantCred(),
                dtoCurso.getFechaReg(),
                dtoCurso.getUrl()
        );
        
        return curso;
    }

    public static DTOCurso CursoToDTOCurso(Curso curso){
        DTOCurso dtoCurso = new DTOCurso(
                curso.getNombreInst(),
                curso.getNombreCurso(),
                curso.getDescCurso(),
                curso.getDuracion(),
                curso.getCantHoras(),
                curso.getCantCred(),
                curso.getFechaReg(),
                curso.getUrl()
        );
        
        return dtoCurso;
    }
    
    // Programas de Formación

    public static ProgramaDeFormacion DTOProgramadeFormToProgramadeForm(DTOProgramaDeFormacion dtoProgramaDeForm){
        Set<Curso> cursos = new HashSet<>();
        for (DTOCurso dtoCurso : dtoProgramaDeForm.getCursos()) {
            cursos.add(DTOCursoToCurso(dtoCurso));
        }

        ProgramaDeFormacion programa = new ProgramaDeFormacion(
                dtoProgramaDeForm.getNombre(),
                dtoProgramaDeForm.getDescripcion(),
                cursos,
                dtoProgramaDeForm.getFechaInicio(),
                dtoProgramaDeForm.getFechaFin()
        );
        
        return programa;
    }

    public static DTOProgramaDeFormacion ProgramadeFormToDTOProgramadeForm(ProgramaDeFormacion programaDeForm){
        Set<DTOCurso> cursos = new HashSet<>();
        for (Curso curso : programaDeForm.getCursos()) {
            cursos.add(CursoToDTOCurso(curso));
        }

        DTOProgramaDeFormacion dtoPrograma = new DTOProgramaDeFormacion(
                programaDeForm.getNombre(),
                programaDeForm.getDescripcion(),
                cursos,
                programaDeForm.getFechaInicio(),
                programaDeForm.getFechaFin()
        );
        
        return dtoPrograma;
    }
}
