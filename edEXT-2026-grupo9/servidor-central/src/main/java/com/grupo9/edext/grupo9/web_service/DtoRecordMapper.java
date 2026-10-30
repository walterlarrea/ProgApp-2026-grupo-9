package com.grupo9.edext.grupo9.web_service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEstudiante;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInscEdicion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInscPrograma;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import com.grupo9.edext.grupo9.servidor_central.dominio.ResultadoBusqueda;
import com.grupo9.edext.grupo9.web_service.dto.DataCursoDTO;
import com.grupo9.edext.grupo9.web_service.dto.DataDocenteDTO;
import com.grupo9.edext.grupo9.web_service.dto.DataEdicionCursoDTO;
import com.grupo9.edext.grupo9.web_service.dto.DataEstudianteDTO;
import com.grupo9.edext.grupo9.web_service.dto.DataInscEdicionDTO;
import com.grupo9.edext.grupo9.web_service.dto.DataInscProgramaDTO;
import com.grupo9.edext.grupo9.web_service.dto.DataInstitutoDTO;
import com.grupo9.edext.grupo9.web_service.dto.DataProgramaFormacionDTO;
import com.grupo9.edext.grupo9.web_service.dto.DataUsuarioDTO;
import com.grupo9.edext.grupo9.web_service.dto.ResultadoBusquedaDTO;

/**
 * Conversión entre los Data* del dominio del Servidor Central y los DTOs que
 * viajan por SOAP. Las fechas se envían como texto ISO-8601 (yyyy-MM-dd).
 */
public class DtoRecordMapper {

    private DtoRecordMapper() {
    }

    // Fechas
    public static String toText(LocalDate fecha) {
        return fecha != null ? fecha.toString() : null;
    }

    public static LocalDate toDate(String texto) {
        return texto != null && !texto.isBlank() ? LocalDate.parse(texto.trim()) : null;
    }

    // Programas de Formación
    public static DataProgramaFormacionDTO toDTO(DataProgramaFormacion dataPrograma) {
        if (dataPrograma == null) {
            return null;
        }

        List<DataCursoDTO> cursos = new ArrayList<>();
        if (dataPrograma.cursos() != null) {
            for (DataCurso curso : dataPrograma.cursos()) {
                if (curso != null) {
                    cursos.add(toDTO(curso));
                }
            }
        }

        return new DataProgramaFormacionDTO(
                dataPrograma.nombre(),
                dataPrograma.descripcion(),
                cursos,
                toText(dataPrograma.fechaInicio()),
                toText(dataPrograma.fechaFin()),
                toText(dataPrograma.fechaDeCreacion()),
                dataPrograma.imagen());
    }

    public static DataProgramaFormacion toData(DataProgramaFormacionDTO dto) {
        if (dto == null) {
            return null;
        }

        HashSet<DataCurso> cursos = new HashSet<>();
        // Los cursos se asocian luego con agregarCursoAProgramaDeFormacion
        return new DataProgramaFormacion(
                dto.getNombre(),
                dto.getDescripcion(),
                cursos,
                toDate(dto.getFechaInicio()),
                toDate(dto.getFechaFin()),
                toDate(dto.getFechaDeCreacion()),
                dto.getImagen());
    }

    // Institutos
    public static DataInstitutoDTO toDTO(DataInstituto instituto) {
        return instituto == null ? null : new DataInstitutoDTO(instituto.nombreI());
    }

    // Cursos
    public static DataCursoDTO toDTO(DataCurso curso) {
        if (curso == null) {
            return null;
        }
        DataCursoDTO dto = new DataCursoDTO();
        dto.setInstituto(toDTO(curso.instituto()));
        dto.setNombreCurso(curso.nombreCurso());
        dto.setDescCurso(curso.descCurso());
        dto.setDuracion(curso.duracion());
        dto.setCantHoras(curso.cantHoras());
        dto.setCantCred(curso.cantCred());
        dto.setFechaReg(toText(curso.fechaReg()));
        dto.setUrl(curso.url());
        dto.setImagen(curso.imagen());
        if (curso.previas() != null) {
            for (DataCurso previa : curso.previas()) {
                if (previa != null && previa.nombreCurso() != null) {
                    dto.getPrevias().add(previa.nombreCurso());
                }
            }
        }
        if (curso.programas() != null) {
            for (DataProgramaFormacion programa : curso.programas()) {
                if (programa != null && programa.nombre() != null) {
                    dto.getProgramas().add(programa.nombre());
                }
            }
        }
        if (curso.categorias() != null) {
            dto.getCategorias().addAll(curso.categorias());
        }
        return dto;
    }

    // Usuarios
    public static DataUsuarioDTO toDTO(DataUsuario usuario) {
        if (usuario == null) {
            return null;
        }
        DataUsuarioDTO dto;
        if (usuario instanceof DataEstudiante estudiante) {
            DataEstudianteDTO estudianteDTO = new DataEstudianteDTO();
            if (estudiante.getEdicionesInscriptas() != null) {
                estudianteDTO.getEdicionesInscriptas().addAll(estudiante.getEdicionesInscriptas());
            }
            if (estudiante.getInscripcionesProgramas() != null) {
                for (DataInscPrograma insc : estudiante.getInscripcionesProgramas()) {
                    if (insc != null) {
                        estudianteDTO.getInscripcionesProgramas().add(new DataInscProgramaDTO(
                                insc.getEstudianteNickname(), insc.getProgramaNombre(), toText(insc.getFechaInscP())));
                    }
                }
            }
            dto = estudianteDTO;
        } else if (usuario instanceof DataDocente docente) {
            DataDocenteDTO docenteDTO = new DataDocenteDTO();
            docenteDTO.setNombreInst(docente.getNombreInst());
            if (docente.getEdiciones() != null) {
                docenteDTO.getEdiciones().addAll(docente.getEdiciones());
            }
            if (docente.getCursos() != null) {
                docenteDTO.getCursos().addAll(docente.getCursos());
            }
            dto = docenteDTO;
        } else {
            dto = new DataUsuarioDTO();
            dto.setTipo(usuario.getTipo());
        }
        dto.setNickname(usuario.getNickname());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setFechaNac(toText(usuario.getFechaNac()));
        dto.setImagen(usuario.getImagen());
        dto.getSeguidos().addAll(usuario.getSeguidos());
        dto.getSeguidores().addAll(usuario.getSeguidores());
        return dto;
    }

    public static DataDocenteDTO toDTO(DataDocente docente) {
        return (DataDocenteDTO) toDTO((DataUsuario) docente);
    }

    public static DataEstudianteDTO toDTO(DataEstudiante estudiante) {
        return (DataEstudianteDTO) toDTO((DataUsuario) estudiante);
    }

    // Ediciones de curso
    public static DataEdicionCursoDTO toDTO(DataEdicionCurso edicion) {
        if (edicion == null) {
            return null;
        }
        DataEdicionCursoDTO dto = new DataEdicionCursoDTO();
        dto.setNombreEdi(edicion.getNombreEdi());
        dto.setCursoAsoc(toDTO(edicion.getCursoAsoc()));
        dto.setFechaInicio(toText(edicion.getFechaInicio()));
        dto.setFechaFin(toText(edicion.getFechaFin()));
        dto.setCupo(edicion.getCupo());
        dto.setFechaPub(toText(edicion.getFechaPub()));
        dto.setImagen(edicion.getImagen());
        if (edicion.getDocentes() != null) {
            for (DataDocente docente : edicion.getDocentes()) {
                if (docente != null && docente.getNickname() != null) {
                    dto.getDocentes().add(docente.getNickname());
                }
            }
        }
        return dto;
    }

    public static DataInscEdicionDTO toDTO(DataInscEdicion inscripcion) {
        if (inscripcion == null) {
            return null;
        }
        DataInscEdicionDTO dto = new DataInscEdicionDTO();
        dto.setFechaInscE(toText(inscripcion.getFechaInscE()));
        dto.setEstudiante(toDTO(inscripcion.getEstudiante()));
        dto.setEdicion(inscripcion.getEdicion());
        dto.setEstado(inscripcion.getEstado());
        return dto;
    }

    // Búsqueda
    public static ResultadoBusquedaDTO toDTO(ResultadoBusqueda resultado) {
        if (resultado == null) {
            return null;
        }
        ResultadoBusquedaDTO dto = new ResultadoBusquedaDTO();
        dto.setTipo(resultado.tipo());
        Object data = resultado.data();
        if (data instanceof DataCurso curso) {
            dto.setCurso(toDTO(curso));
        } else if (data instanceof DataProgramaFormacion programa) {
            dto.setPrograma(toDTO(programa));
        } else if (data instanceof DataUsuario usuario) {
            dto.setUsuario(toDTO(usuario));
        }
        return dto;
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
        if (targetType == DataCursoDTO.class && source instanceof DataCurso curso) {
            return toDTO(curso);
        }
        if (targetType == DataInstitutoDTO.class && source instanceof DataInstituto instituto) {
            return toDTO(instituto);
        }
        if (DataUsuarioDTO.class.isAssignableFrom(targetType) && source instanceof DataUsuario usuario) {
            return toDTO(usuario);
        }
        if (targetType == DataEdicionCursoDTO.class && source instanceof DataEdicionCurso edicion) {
            return toDTO(edicion);
        }
        if (targetType == DataInscEdicionDTO.class && source instanceof DataInscEdicion inscripcion) {
            return toDTO(inscripcion);
        }
        if (targetType == ResultadoBusquedaDTO.class && source instanceof ResultadoBusqueda resultado) {
            return toDTO(resultado);
        }

        throw new IllegalArgumentException(
                "Unsupported conversion from " + source.getClass().getName()
                        + " to " + targetType.getName());
    }
}
