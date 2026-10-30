package com.grupo9.edext.grupo9.web_service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.servidor_central.controller.ServidorCentralFactory;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.web_service.dto.DataCursoDTO;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(serviceName = "CursoWebService")
public class CursoWebService {

    @WebMethod(operationName = "buscarCurso")
    public DataCursoDTO buscarCurso(@WebParam(name = "nombre") String nombre) {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        DataCurso curso = servidorCentral.buscarCurso(nombre);
        return DtoRecordMapper.toDTO(curso);
    }

    @WebMethod(operationName = "consultarTodosLosCursos")
    public List<DataCursoDTO> consultarTodosLosCursos() {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        HashSet<DataCurso> cursos = servidorCentral.consultarTodosLosCursos();
        return DtoRecordMapper.toDTOList(cursos, DataCursoDTO.class);
    }

    @WebMethod(operationName = "cursosPorInstituto")
    public List<DataCursoDTO> cursosPorInstituto(@WebParam(name = "nombreInstituto") String nombreInstituto) {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        HashSet<DataCurso> cursos = servidorCentral.cursosPorInstituto(nombreInstituto);
        return DtoRecordMapper.toDTOList(cursos, DataCursoDTO.class);
    }

    @WebMethod(operationName = "cursosNoRelacionadosConUnProgDeFormacion")
    public List<DataCursoDTO> cursosNoRelacionadosConUnProgDeFormacion(
            @WebParam(name = "idProgramaDeFormacion") String idProgramaDeFormacion) {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        HashSet<DataCurso> cursos = servidorCentral.cursosNoRelacionadosConUnProgDeFormacion(idProgramaDeFormacion);
        return DtoRecordMapper.toDTOList(cursos, DataCursoDTO.class);
    }

    @WebMethod(operationName = "existeCurso")
    public boolean existeCurso(@WebParam(name = "nombre") String nombre) {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        Boolean existe = servidorCentral.existeCurso(nombre);
        return Boolean.TRUE.equals(existe);
    }

    @WebMethod(operationName = "guardarCurso")
    public DataCursoDTO guardarCurso(
            @WebParam(name = "nombreInst") String nombreInst,
            @WebParam(name = "nombre") String nombre,
            @WebParam(name = "desc") String desc,
            @WebParam(name = "duracion") int duracion,
            @WebParam(name = "cantHoras") int cantHoras,
            @WebParam(name = "cantCred") int cantCred,
            @WebParam(name = "fechaReg") String fechaReg,
            @WebParam(name = "url") String url,
            @WebParam(name = "previas") List<String> previas,
            @WebParam(name = "imagen") String imagen) throws ErrorRepetidos {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        DataInstituto instituto = servidorCentral.buscarInstituto(nombreInst);

        Set<DataCurso> previasSeleccionadas = new HashSet<>();
        if (previas != null) {
            for (String nombrePrevia : previas) {
                if (nombrePrevia != null && !nombrePrevia.isBlank()) {
                    DataCurso previa = servidorCentral.buscarCurso(nombrePrevia);
                    if (previa != null) {
                        previasSeleccionadas.add(previa);
                    }
                }
            }
        }

        LocalDate fReg = DtoRecordMapper.toDate(fechaReg);
        if (fReg == null) {
            fReg = LocalDate.now();
        }

        DataCurso nuevoCurso = new DataCurso(
                instituto,
                nombre,
                desc,
                duracion,
                cantHoras,
                cantCred,
                fReg,
                url,
                previasSeleccionadas,
                new HashSet<>(),
                new HashSet<>(),
                imagen);

        DataCurso guardado = servidorCentral.guardarCurso(nuevoCurso);
        return DtoRecordMapper.toDTO(guardado);
    }
}
