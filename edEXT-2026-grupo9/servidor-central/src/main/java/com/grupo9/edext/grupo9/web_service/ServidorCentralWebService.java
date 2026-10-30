package com.grupo9.edext.grupo9.web_service;

import jakarta.jws.WebService;

import java.util.HashSet;
import java.util.List;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.servidor_central.controller.ServidorCentralFactory;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.web_service.dto.DataProgramaFormacionDTO;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;


@WebService(serviceName = "ServidorCentralWebService")
public class ServidorCentralWebService {

    /**
     * This is a sample web service operation
     * @param txt
     * @return 
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    /**
     * Devuelve la lista completa de Programas de Formación
     * @return 
     */
    @WebMethod(operationName = "consultarTodosLosProgramas")
    public List<DataProgramaFormacionDTO> consultarTodosLosProgramas() {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        HashSet<DataProgramaFormacion> programas = servidorCentral.consultarTodosLosProgramas();
        
        return DtoRecordMapper.toDTOList(programas, DataProgramaFormacionDTO.class);
    }
}
