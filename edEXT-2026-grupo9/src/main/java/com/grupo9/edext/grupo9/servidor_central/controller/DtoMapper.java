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
        
        Set<Curso> previas = toEntityList(dataCurso.previas(), Curso.class);
        Curso curso = new Curso(
                toEntity(dataCurso.instituto()),
                dataCurso.nombreCurso(),
                dataCurso.descCurso(),
                dataCurso.duracion(),
                dataCurso.cantHoras(),
                dataCurso.cantCred(),
                dataCurso.fechaReg(),
                dataCurso.url(),
                previas
        );
        
        return curso;
    }

    public static DataCurso toData(Curso curso){
        if(curso == null){
            return null;
        }
        
        Set<DataCurso> dataPrevias = toDataList(curso.getPrevias(), DataCurso.class);
        DataCurso dataCurso = new DataCurso(
                toData(curso.getInstituto()),
                curso.getNombreCurso(),
                curso.getDescCurso(),
                curso.getDuracion(),
                curso.getCantHoras(),
                curso.getCantCred(),
                curso.getFechaReg(),
                curso.getUrl(),
                dataPrevias
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
        // Inscripciones, empieza vacía
        Set<InscEdicion> inscripciones = new HashSet<>();
        
        return new EdicionCurso(
            dataEdicion.getNombreEdi(),
            toEntity(dataEdicion.getCursoAsoc()),
            dataEdicion.getFechaInicio(),
            dataEdicion.getFechaFin(),
            dataEdicion.getCupo(),
            toEntity(dataEdicion.getDocente()),
            inscripciones,
            dataEdicion.getFechaPub());
    }
    
    public static DataEdicionCurso toData(EdicionCurso edicion) {
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
            toData(edicion.getCursoAsoc()),
            edicion.getFechaInicio(),
            edicion.getFechaFin(),
            edicion.getCupo(),
            toData(edicion.getDocente()),
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

    private static <Source, Target> Set<Target> convertList(
            Set<Source> sourceList, Class<Target> targetType) {
        if (sourceList == null) {
            throw new NullPointerException("The source list cannot be null");
        }
        if (targetType == null) {
            throw new NullPointerException("The target type cannot be null");
        }

        Set<Target> targetList = new HashSet<>();
        for (Source source : sourceList) {
            targetList.add(targetType.cast(convertItem(source, targetType)));
        }
        return targetList;
    }

    public static <Source, Target> Set<Target> toEntityList(
            Set<Source> sourceList, Class<Target> targetType) {
        return convertList(sourceList, targetType);
    }

    public static <Source, Target> Set<Target> toDataList(
            Set<Source> sourceList, Class<Target> targetType) {
        return convertList(sourceList, targetType);
    }

    private static Object convertItem(Object source, Class<?> targetType) {
        if (source == null) {
            return null;
        }
        if (targetType == Curso.class && source instanceof DataCurso dataCurso) {
            return toEntity(dataCurso);
        }
        if (targetType == DataCurso.class && source instanceof Curso curso) {
            return toData(curso);
        }
        if (targetType == ProgramaDeFormacion.class && source instanceof DataProgramaFormacion dataPrograma) {
            return toEntity(dataPrograma);
        }
        if (targetType == DataProgramaFormacion.class && source instanceof ProgramaDeFormacion programa) {
            return toData(programa);
        }
        if (targetType == Instituto.class && source instanceof DataInstituto dataInstituto) {
            return toEntity(dataInstituto);
        }
        if (targetType == DataInstituto.class && source instanceof Instituto instituto) {
            return toData(instituto);
        }
        if (targetType == EdicionCurso.class && source instanceof DataEdicionCurso dataEdicion) {
            return toEntity(dataEdicion);
        }
        if (targetType == DataEdicionCurso.class && source instanceof EdicionCurso edicion) {
            return toData(edicion);
        }
        if (targetType == Docente.class && source instanceof DataDocente dataDocente) {
            return toEntity(dataDocente);
        }
        if (targetType == DataDocente.class && source instanceof Docente docente) {
            return toData(docente);
        }

        throw new IllegalArgumentException(
            "Unsupported conversion from " + source.getClass().getName()
                + " to " + targetType.getName());
    }
}
