package com.grupo9.edext.grupo9.web_service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.servidor_central.controller.ServidorCentralFactory;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import com.grupo9.edext.grupo9.servidor_central.dominio.ResultadoBusqueda;
import com.grupo9.edext.grupo9.web_service.dto.DataCursoDTO;
import com.grupo9.edext.grupo9.web_service.dto.DataProgramaFormacionDTO;
import com.grupo9.edext.grupo9.web_service.dto.DataUsuarioDTO;
import com.grupo9.edext.grupo9.web_service.dto.ResultadoBusquedaDTO;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(serviceName = "BusquedaWebService")
public class BusquedaWebService {

    @WebMethod(operationName = "busquedaPrincipal")
    public List<ResultadoBusquedaDTO> busquedaPrincipal(@WebParam(name = "query") String query) {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        List<ResultadoBusqueda> resultados = servidorCentral.busquedaPrincipal(query);
        if (resultados == null) {
            return new ArrayList<>();
        }
        List<ResultadoBusquedaDTO> dtos = new ArrayList<>();
        for (ResultadoBusqueda res : resultados) {
            if (res != null) {
                dtos.add(DtoRecordMapper.toDTO(res));
            }
        }
        return dtos;
    }

    @WebMethod(operationName = "buscarCursos")
    public List<DataCursoDTO> buscarCursos(@WebParam(name = "query") String query) {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        HashSet<DataCurso> cursos = servidorCentral.buscarCursos(query);
        return DtoRecordMapper.toDTOList(cursos, DataCursoDTO.class);
    }

    @WebMethod(operationName = "buscarProgramas")
    public List<DataProgramaFormacionDTO> buscarProgramas(@WebParam(name = "query") String query) {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        HashSet<DataProgramaFormacion> programas = servidorCentral.buscarProgramas(query);
        return DtoRecordMapper.toDTOList(programas, DataProgramaFormacionDTO.class);
    }

    @WebMethod(operationName = "buscarUsuarios")
    public List<DataUsuarioDTO> buscarUsuarios(
            @WebParam(name = "nombre") String nombre,
            @WebParam(name = "apellido") String apellido) {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        HashSet<DataUsuario> usuarios = servidorCentral.buscarUsuarios(nombre, apellido);
        return DtoRecordMapper.toDTOList(usuarios, DataUsuarioDTO.class);
    }
}
