package com.grupo9.edext.grupo9.web_service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.servidor_central.controller.ServidorCentralFactory;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.web_service.dto.DataProgramaFormacionDTO;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(serviceName = "ProgramaDeFormacionWebService")
public class ProgramaDeFormacionWebService {

    @WebMethod(operationName = "consultarTodosLosProgramas")
    public List<DataProgramaFormacionDTO> consultarTodosLosProgramas() {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        HashSet<DataProgramaFormacion> programas = servidorCentral.consultarTodosLosProgramas();
        return DtoRecordMapper.toDTOList(programas, DataProgramaFormacionDTO.class);
    }

    @WebMethod(operationName = "traerPorNombreId")
    public DataProgramaFormacionDTO traerPorNombreId(@WebParam(name = "nombreId") String nombreId) {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        DataProgramaFormacion programa = servidorCentral.traerPorNombreId(nombreId);
        return DtoRecordMapper.toDTO(programa);
    }

    @WebMethod(operationName = "existeProgramaDeFormacion")
    public boolean existeProgramaDeFormacion(@WebParam(name = "nombre") String nombre) {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        Boolean existe = servidorCentral.existeProgramaDeFormacion(nombre);
        return Boolean.TRUE.equals(existe);
    }

    @WebMethod(operationName = "guardarProgramaDeFormacion")
    public DataProgramaFormacionDTO guardarProgramaDeFormacion(
            @WebParam(name = "nombre") String nombre,
            @WebParam(name = "descripcion") String descripcion,
            @WebParam(name = "fechaInicio") String fechaInicio,
            @WebParam(name = "fechaFin") String fechaFin,
            @WebParam(name = "imagen") String imagen) {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        LocalDate fInicio = DtoRecordMapper.toDate(fechaInicio);
        LocalDate fFin = DtoRecordMapper.toDate(fechaFin);
        DataProgramaFormacion nuevo = new DataProgramaFormacion(
                nombre, descripcion, new HashSet<>(), fInicio, fFin, LocalDate.now(), imagen);
        DataProgramaFormacion guardado = servidorCentral.guardarProgramaDeFormacion(nuevo);
        return DtoRecordMapper.toDTO(guardado);
    }

    @WebMethod(operationName = "agregarCursoAProgramaDeFormacion")
    public boolean agregarCursoAProgramaDeFormacion(
            @WebParam(name = "nombreIdPrograma") String nombreIdPrograma,
            @WebParam(name = "nombreIdCurso") String nombreIdCurso) {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        Boolean resultado = servidorCentral.agregarCursoAProgramaDeFormacion(nombreIdPrograma, nombreIdCurso);
        return Boolean.TRUE.equals(resultado);
    }

    @WebMethod(operationName = "inscribirEstudianteEnPrograma")
    public void inscribirEstudianteEnPrograma(
            @WebParam(name = "fechaInsc") String fechaInsc,
            @WebParam(name = "nickname") String nickname,
            @WebParam(name = "nombrePrograma") String nombrePrograma) throws ErrorRepetidos, ErrorNoExiste {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        LocalDate fInsc = DtoRecordMapper.toDate(fechaInsc);
        if (fInsc == null) {
            fInsc = LocalDate.now();
        }
        servidorCentral.inscribirEstudianteEnPrograma(fInsc, nickname, nombrePrograma);
    }
}
