package com.grupo9.edext.grupo9.servidor_central.controller;

import java.util.HashSet;
import java.util.Set;

import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.Instituto;
import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.ProgramaDeFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
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

    public static DataProgramaFormacion toData(ProgramaDeFormacion programa){
        if(programa == null){
            return null;
        }
        
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
            null,
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
