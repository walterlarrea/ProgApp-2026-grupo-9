package com.grupo9.edext.grupo9.web_service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataProgramaFormacionDTO")
public class DataProgramaFormacionDTO implements Serializable {

    private String nombre;
    private String descripcion;
    private List<DataCursoDTO> cursos;
    private String fechaInicio;
    private String fechaFin;
    private String fechaDeCreacion;
    private String imagen;

    public DataProgramaFormacionDTO() {
        this.cursos = new ArrayList<>();
    }

    public DataProgramaFormacionDTO(String nombre, String descripcion, List<DataCursoDTO> cursos,
                                   String fechaInicio, String fechaFin, String fechaDeCreacion, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cursos = cursos != null ? cursos : new ArrayList<>();
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaDeCreacion = fechaDeCreacion;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<DataCursoDTO> getCursos() {
        return cursos;
    }

    public void setCursos(List<DataCursoDTO> cursos) {
        this.cursos = cursos;
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

    public String getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public void setFechaDeCreacion(String fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    // Alias methods matching record accessor naming so both ${p.nombre()} and ${p.nombre} or ${p.getNombre()} work
    public String nombre() {
        return nombre;
    }

    public String descripcion() {
        return descripcion;
    }

    public String fechaInicio() {
        return fechaInicio;
    }

    public String fechaFin() {
        return fechaFin;
    }

    public String fechaDeCreacion() {
        return fechaDeCreacion;
    }

    public String imagen() {
        return imagen;
    }
}
