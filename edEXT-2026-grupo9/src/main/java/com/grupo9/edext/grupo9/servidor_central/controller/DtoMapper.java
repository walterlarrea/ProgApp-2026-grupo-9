package com.grupo9.edext.grupo9.servidor_central.controller;

import java.util.HashSet;
import java.util.Set;

import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.ProgramaDeFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInscEdicion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.InscEdicion;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Docente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEstudiante;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Estudiante;

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
        for(DataCurso dataCurso : dataPrograma.cursos()) {
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
        for(Curso curso : programa.getCursos()) {
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
    
    // Ediciones de cursos
    public static EdicionCurso toEntity(DataEdicionCurso dataEdicion) {
        Set<Curso> cursos = new HashSet<>();
        for(DataCurso dataCurso : dataEdicion.getCursoAsoc()) {
            cursos.add(toEntity(dataCurso));
        }
        // Docentes
        Set<Docente> docentes = new HashSet<>();
        for(DataDocente dataDocente : dataEdicion.getDocentes()) {
            docentes.add(toEntity(dataDocente));
        }
        // Inscripciones, empieza vacía
        Set<InscEdicion> inscripciones = new HashSet<>();
        
        return new EdicionCurso(
            dataEdicion.getNombreEdi(),
            cursos,
            dataEdicion.getFechaInicio(),
            dataEdicion.getFechaFin(),
            dataEdicion.getCupo(),
            docentes,
            inscripciones,
            dataEdicion.getFechaPub());
    }
    
    public static DataEdicionCurso toData(EdicionCurso edicion) {
        // Cursos asociados
        Set<DataCurso> cursos = new HashSet<>();
        for(Curso curso : edicion.getCursoAsoc()) {
            cursos.add(toData(curso));
        }
        // Docentes
        Set<DataDocente> docentes = new HashSet<>();
        for(Docente docente : edicion.getDocentes()) {
            docentes.add(toData(docente));
        }
        // Inscripciones
        Set<DataInscEdicion> inscripciones = new HashSet<>();
        for(InscEdicion insc : edicion.getInscripciones()) {
            Estudiante estudiante = insc.getEstudiante();
            DataEstudiante dataEstudiante = new DataEstudiante(
                estudiante.getNickname(),
                estudiante.getNombre(),
                estudiante.getApellido(),
                estudiante.getEmail(),
                estudiante.getFechaNac(),
                null);
            DataInscEdicion dataInsc = new DataInscEdicion(insc.getFechaInscE(), dataEstudiante, insc.getEdicion().getNombreEdi());
            inscripciones.add(dataInsc);
        }

        return new DataEdicionCurso(
            edicion.getNombreEdi(),
            cursos,
            edicion.getFechaInicio(),
            edicion.getFechaFin(),
            edicion.getCupo(),
            docentes,
            inscripciones,
            edicion.getFechaPub());
    }
    
    public static Docente toEntity(DataDocente dataDocente) {
        return new Docente(
            dataDocente.getNickname(),
            dataDocente.getNombre(),
            dataDocente.getApellido(),
            dataDocente.getEmail(),
            dataDocente.getFechaNac(),
            dataDocente.getNombreInst());
    }
    
    public static DataDocente toData(Docente docente) {
        return new DataDocente(
            docente.getNickname(),
            docente.getNombre(),
            docente.getApellido(),
            docente.getEmail(),
            docente.getFechaNac(),
            docente.getNombreInst());
    }
}
