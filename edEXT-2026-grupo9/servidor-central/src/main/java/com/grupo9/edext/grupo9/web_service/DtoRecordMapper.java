package com.grupo9.edext.grupo9.web_service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.web_service.dto.DataProgramaFormacionDTO;

public class DtoRecordMapper {

    // Programas de Formación
    public static DataProgramaFormacionDTO toDTO(DataProgramaFormacion dataPrograma) {
        if (dataPrograma == null) {
            return null;
        }

        List<String> nombresCursos = new ArrayList<>();
        if (dataPrograma.cursos() != null) {
            for (DataCurso curso : dataPrograma.cursos()) {
                if (curso != null && curso.nombreCurso() != null) {
                    nombresCursos.add(curso.nombreCurso());
                }
            }
        }

        return new DataProgramaFormacionDTO(
                dataPrograma.nombre(),
                dataPrograma.descripcion(),
                nombresCursos,
                dataPrograma.fechaInicio() != null ? dataPrograma.fechaInicio().toString() : null,
                dataPrograma.fechaFin() != null ? dataPrograma.fechaFin().toString() : null,
                dataPrograma.fechaDeCreacion() != null ? dataPrograma.fechaDeCreacion().toString() : null,
                dataPrograma.imagen());
    }

    public static DataProgramaFormacion toData(DataProgramaFormacionDTO dto) {
        if (dto == null) {
            return null;
        }

        HashSet<DataCurso> cursos = new HashSet<>();
        // Los cursos en el DTO son nombres, se instancian o se dejan vacíos según necesidad
        return new DataProgramaFormacion(
                dto.getNombre(),
                dto.getDescripcion(),
                cursos,
                dto.getFechaInicio() != null ? LocalDate.parse(dto.getFechaInicio()) : null,
                dto.getFechaFin() != null ? LocalDate.parse(dto.getFechaFin()) : null,
                dto.getFechaDeCreacion() != null ? LocalDate.parse(dto.getFechaDeCreacion()) : null,
                dto.getImagen());
    }

    // Colecciones / Listas
    private static <Source, Target> List<Target> convertList(
            Collection<Source> sourceList, Class<Target> targetType) {
        if (sourceList == null) {
            return new ArrayList<>();
        }
        if (targetType == null) {
            throw new NullPointerException("The target type cannot be null");
        }

        List<Target> targetList = new ArrayList<>();
        for (Source source : sourceList) {
            targetList.add(targetType.cast(convertItem(source, targetType)));
        }
        return targetList;
    }

    public static <Source, Target> List<Target> toDTOList(
            Collection<Source> sourceList, Class<Target> targetType) {
        return convertList(sourceList, targetType);
    }

    public static <Source, Target> List<Target> toDataList(
            Collection<Source> sourceList, Class<Target> targetType) {
        return convertList(sourceList, targetType);
    }

    private static Object convertItem(Object source, Class<?> targetType) {
        if (source == null) {
            return null;
        }
        if (targetType == DataProgramaFormacionDTO.class && source instanceof DataProgramaFormacion dataPrograma) {
            return toDTO(dataPrograma);
        }
        if (targetType == DataProgramaFormacion.class && source instanceof DataProgramaFormacionDTO dto) {
            return toData(dto);
        }

        throw new IllegalArgumentException(
                "Unsupported conversion from " + source.getClass().getName()
                        + " to " + targetType.getName());
    }
}
