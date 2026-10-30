package com.grupo9.edext.grupo9.web_service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

/**
 * DTO de Curso para SOAP. Las previas y los programas se envían solo por nombre
 * para evitar estructuras recursivas.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataCursoDTO")
public class DataCursoDTO implements Serializable {

    private DataInstitutoDTO instituto;
    private String nombreCurso;
    private String descCurso;
    private int duracion;
    private int cantHoras;
    private int cantCred;
    private String fechaReg;
    private String url;
    private List<String> previas = new ArrayList<>();
    private List<String> programas = new ArrayList<>();
    private List<String> categorias = new ArrayList<>();
    private String imagen;

    public DataCursoDTO() {
    }

    public DataInstitutoDTO getInstituto() {
        return instituto;
    }

    public void setInstituto(DataInstitutoDTO instituto) {
        this.instituto = instituto;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getDescCurso() {
        return descCurso;
    }

    public void setDescCurso(String descCurso) {
        this.descCurso = descCurso;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getCantHoras() {
        return cantHoras;
    }

    public void setCantHoras(int cantHoras) {
        this.cantHoras = cantHoras;
    }

    public int getCantCred() {
        return cantCred;
    }

    public void setCantCred(int cantCred) {
        this.cantCred = cantCred;
    }

    public String getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(String fechaReg) {
        this.fechaReg = fechaReg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getPrevias() {
        return previas;
    }

    public void setPrevias(List<String> previas) {
        this.previas = previas;
    }

    public List<String> getProgramas() {
        return programas;
    }

    public void setProgramas(List<String> programas) {
        this.programas = programas;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
