package com.grupo9.edext.grupo9.web_service;

import java.util.HashSet;
import java.util.List;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.servidor_central.controller.ServidorCentralFactory;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.web_service.dto.DataInstitutoDTO;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(serviceName = "InstitutoWebService")
public class InstitutoWebService {

    @WebMethod(operationName = "consultarTodosLosInstitutos")
    public List<DataInstitutoDTO> consultarTodosLosInstitutos() {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        HashSet<DataInstituto> institutos = servidorCentral.consultarTodosLosInstitutos();
        return DtoRecordMapper.toDTOList(institutos, DataInstitutoDTO.class);
    }

    @WebMethod(operationName = "buscarInstituto")
    public DataInstitutoDTO buscarInstituto(@WebParam(name = "nombre") String nombre) {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        DataInstituto inst = servidorCentral.buscarInstituto(nombre);
        return DtoRecordMapper.toDTO(inst);
    }

    @WebMethod(operationName = "existeInstituto")
    public boolean existeInstituto(@WebParam(name = "nombre") String nombre) {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        Boolean existe = servidorCentral.existeInstituto(nombre);
        return Boolean.TRUE.equals(existe);
    }

    @WebMethod(operationName = "guardarInstituto")
    public DataInstitutoDTO guardarInstituto(@WebParam(name = "nombre") String nombre) {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        DataInstituto inst = new DataInstituto(nombre);
        DataInstituto guardado = servidorCentral.guardarInstituto(inst);
        return DtoRecordMapper.toDTO(guardado);
    }
}
