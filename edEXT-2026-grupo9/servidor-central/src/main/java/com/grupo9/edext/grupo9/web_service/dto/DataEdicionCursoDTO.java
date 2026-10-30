package com.grupo9.edext.grupo9.web_service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

/**
 * DTO de Edición de Curso para SOAP. Los docentes viajan solo por nickname;
 * para el detalle de docentes e inscriptos existen operaciones específicas.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataEdicionCursoDTO")
public class DataEdicionCursoDTO implements Serializable {

    private String nombreEdi;
    private DataCursoDTO cursoAsoc;
    private String fechaInicio;
    private String fechaFin;
    private Integer cupo;
    private List<String> docentes = new ArrayList<>();
    private String fechaPub;
    private String imagen;

    public DataEdicionCursoDTO() {
    }

    public String getNombreEdi() {
        return nombreEdi;
    }

    public void setNombreEdi(String nombreEdi) {
        this.nombreEdi = nombreEdi;
    }

    public DataCursoDTO getCursoAsoc() {
        return cursoAsoc;
    }

    public void setCursoAsoc(DataCursoDTO cursoAsoc) {
        this.cursoAsoc = cursoAsoc;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getCupo() {
        return cupo;
    }

    public void setCupo(Integer cupo) {
        this.cupo = cupo;
    }

    public List<String> getDocentes() {
        return docentes;
    }

    public void setDocentes(List<String> docentes) {
        this.docentes = docentes;
    }

    public String getFechaPub() {
        return fechaPub;
    }

    public void setFechaPub(String fechaPub) {
        this.fechaPub = fechaPub;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
