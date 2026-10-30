package com.grupo9.edext.grupo9.web_service.dto;

import java.io.Serializable;

import com.grupo9.edext.grupo9.servidor_central.controller.busqueda.TipoBusqueda;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Resultado de búsqueda para SOAP. Según el {@link TipoBusqueda} se completa
 * uno solo de los campos curso / programa / usuario.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultadoBusquedaDTO")
public class ResultadoBusquedaDTO implements Serializable {

    private TipoBusqueda tipo;
    private DataCursoDTO curso;
    private DataProgramaFormacionDTO programa;
    private DataUsuarioDTO usuario;

    public ResultadoBusquedaDTO() {
    }

    public TipoBusqueda getTipo() {
        return tipo;
    }

    public void setTipo(TipoBusqueda tipo) {
        this.tipo = tipo;
    }

    public DataCursoDTO getCurso() {
        return curso;
    }

    public void setCurso(DataCursoDTO curso) {
        this.curso = curso;
    }

    public DataProgramaFormacionDTO getPrograma() {
        return programa;
    }

    public void setPrograma(DataProgramaFormacionDTO programa) {
        this.programa = programa;
    }

    public DataUsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(DataUsuarioDTO usuario) {
        this.usuario = usuario;
    }
}
