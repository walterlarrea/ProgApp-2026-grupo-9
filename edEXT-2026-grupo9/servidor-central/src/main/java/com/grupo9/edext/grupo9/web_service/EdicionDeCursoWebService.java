package com.grupo9.edext.grupo9.web_service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.mensajes.ErrorEstadoInvalido;
import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.servidor_central.controller.ServidorCentralFactory;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EstadoInscripcion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInscEdicion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import com.grupo9.edext.grupo9.web_service.dto.DataDocenteDTO;
import com.grupo9.edext.grupo9.web_service.dto.DataEdicionCursoDTO;
import com.grupo9.edext.grupo9.web_service.dto.DataInscEdicionDTO;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(serviceName = "EdicionDeCursoWebService")
public class EdicionDeCursoWebService {

    @WebMethod(operationName = "consultarUnaEdicionCurso")
    public DataEdicionCursoDTO consultarUnaEdicionCurso(@WebParam(name = "nombreEdi") String nombreEdi)
            throws ErrorNoExiste {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        DataEdicionCurso edicion = servidorCentral.consultarUnaEdicionCurso(nombreEdi);
        return DtoRecordMapper.toDTO(edicion);
    }

    @WebMethod(operationName = "traerEdicionesPorCurso")
    public List<DataEdicionCursoDTO> traerEdicionesPorCurso(@WebParam(name = "nombreCurso") String nombreCurso) {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        DataCurso curso = servidorCentral.buscarCurso(nombreCurso);
        if (curso == null) {
            return Collections.emptyList();
        }
        HashSet<DataEdicionCurso> ediciones = servidorCentral.traerEdiciones(curso, true);
        return DtoRecordMapper.toDTOList(ediciones, DataEdicionCursoDTO.class);
    }

    @WebMethod(operationName = "traerDocentesPorInstituto")
    public List<DataDocenteDTO> traerDocentesPorInstituto(@WebParam(name = "nombreInstituto") String nombreInstituto) {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        DataInstituto inst = servidorCentral.buscarInstituto(nombreInstituto);
        if (inst == null) {
            return Collections.emptyList();
        }
        DataDocente[] docentes = servidorCentral.traerDocentes(inst);
        if (docentes == null) {
            return Collections.emptyList();
        }
        List<DataDocenteDTO> res = new ArrayList<>();
        for (DataDocente d : docentes) {
            if (d != null) {
                res.add(DtoRecordMapper.toDTO(d));
            }
        }
        return res;
    }

    @WebMethod(operationName = "buscarDocentesEdicion")
    public List<DataDocenteDTO> buscarDocentesEdicion(@WebParam(name = "nombreEdicion") String nombreEdicion) {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        try {
            DataEdicionCurso edicion = servidorCentral.consultarUnaEdicionCurso(nombreEdicion);
            if (edicion == null) {
                return Collections.emptyList();
            }
            DataDocente[] docentes = servidorCentral.buscarDocentes(edicion);
            if (docentes == null) {
                return Collections.emptyList();
            }
            List<DataDocenteDTO> res = new ArrayList<>();
            for (DataDocente d : docentes) {
                if (d != null) {
                    res.add(DtoRecordMapper.toDTO(d));
                }
            }
            return res;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @WebMethod(operationName = "buscarInscriptosEdicion")
    public List<DataInscEdicionDTO> buscarInscriptosEdicion(@WebParam(name = "nombreEdicion") String nombreEdicion) {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        try {
            DataEdicionCurso edicion = servidorCentral.consultarUnaEdicionCurso(nombreEdicion);
            if (edicion == null) {
                return Collections.emptyList();
            }
            Set<DataInscEdicion> inscriptos = servidorCentral.buscarInscriptos(edicion);
            return DtoRecordMapper.toDTOList(inscriptos, DataInscEdicionDTO.class);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @WebMethod(operationName = "existeEdicionCurso")
    public boolean existeEdicionCurso(@WebParam(name = "nombre") String nombre) {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        Boolean existe = servidorCentral.existeEdicionCurso(nombre);
        return Boolean.TRUE.equals(existe);
    }

    @WebMethod(operationName = "guardarEdicionCurso")
    public DataEdicionCursoDTO guardarEdicionCurso(
            @WebParam(name = "nombreEdi") String nombreEdi,
            @WebParam(name = "nombreCurso") String nombreCurso,
            @WebParam(name = "fechaInicio") String fechaInicio,
            @WebParam(name = "fechaFin") String fechaFin,
            @WebParam(name = "cupo") Integer cupo,
            @WebParam(name = "docentesNicknames") List<String> docentesNicknames,
            @WebParam(name = "imagen") String imagen) throws ErrorRepetidos {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        DataCurso curso = servidorCentral.buscarCurso(nombreCurso);

        Set<DataDocente> docentesSeleccionados = new HashSet<>();
        if (docentesNicknames != null) {
            for (String nick : docentesNicknames) {
                try {
                    DataUsuario u = servidorCentral.consultarUsuario(nick);
                    if (u instanceof DataDocente d) {
                        docentesSeleccionados.add(d);
                    }
                } catch (Exception ignored) {
                }
            }
        }

        LocalDate fIni = DtoRecordMapper.toDate(fechaInicio);
        LocalDate fFin = DtoRecordMapper.toDate(fechaFin);

        DataEdicionCurso nuevaEdicion = new DataEdicionCurso(
                nombreEdi, curso, fIni, fFin, cupo, docentesSeleccionados, new HashSet<>(), LocalDate.now(), imagen);
        DataEdicionCurso guardada = servidorCentral.guardarEdicionCurso(nuevaEdicion);
        return DtoRecordMapper.toDTO(guardada);
    }

    @WebMethod(operationName = "inscribirEstudiante")
    public void inscribirEstudiante(
            @WebParam(name = "fechaInsc") String fechaInsc,
            @WebParam(name = "nickname") String nickname,
            @WebParam(name = "nombreEdi") String nombreEdi,
            @WebParam(name = "estado") String estado) throws ErrorRepetidos, ErrorNoExiste {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        LocalDate fInsc = DtoRecordMapper.toDate(fechaInsc);
        if (fInsc == null) {
            fInsc = LocalDate.now();
        }
        EstadoInscripcion estadoEnum = EstadoInscripcion.INSCRIPTO;
        if (estado != null) {
            try {
                estadoEnum = EstadoInscripcion.valueOf(estado.trim().toUpperCase());
            } catch (Exception ignored) {
            }
        }
        servidorCentral.inscribirEstudiante(fInsc, nickname, nombreEdi, estadoEnum);
    }

    @WebMethod(operationName = "cambiarEstadoInscripcion")
    public void cambiarEstadoInscripcion(
            @WebParam(name = "nombreEdicion") String nombreEdicion,
            @WebParam(name = "nicknameEstudiante") String nicknameEstudiante,
            @WebParam(name = "nuevoEstado") String nuevoEstado) throws ErrorNoExiste, ErrorEstadoInvalido {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        EstadoInscripcion estadoEnum = EstadoInscripcion.valueOf(nuevoEstado.trim().toUpperCase());
        servidorCentral.cambiarEstadoInscripcion(nombreEdicion, nicknameEstudiante, estadoEnum);
    }
}
