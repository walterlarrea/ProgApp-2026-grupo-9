package com.grupo9.edext.grupo9.web_service.dto;

import java.io.Serializable;

import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EstadoInscripcion;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataInscEdicionDTO")
public class DataInscEdicionDTO implements Serializable {

    private String fechaInscE;
    private DataEstudianteDTO estudiante;
    private String edicion;
    private EstadoInscripcion estado;

    public DataInscEdicionDTO() {
    }

    public String getFechaInscE() {
        return fechaInscE;
    }

    public void setFechaInscE(String fechaInscE) {
        this.fechaInscE = fechaInscE;
    }

    public DataEstudianteDTO getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(DataEstudianteDTO estudiante) {
        this.estudiante = estudiante;
    }

    public String getEdicion() {
        return edicion;
    }

    public void setEdicion(String edicion) {
        this.edicion = edicion;
    }

    public EstadoInscripcion getEstado() {
        return estado;
    }

    public void setEstado(EstadoInscripcion estado) {
        this.estado = estado;
    }
}
