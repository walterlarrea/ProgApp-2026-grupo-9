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
    private Double puntajePromedio;
    private int cantValoraciones;
    private Integer valoracionEstudiante;
    private int cantVal1;
    private int cantVal2;
    private int cantVal3;
    private int cantVal4;
    private int cantVal5;
    private int cantidadVisitas;

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

    public Double getPuntajePromedio() {
        return puntajePromedio;
    }

    public void setPuntajePromedio(Double puntajePromedio) {
        this.puntajePromedio = puntajePromedio;
    }

    public int getCantValoraciones() {
        return cantValoraciones;
    }

    public void setCantValoraciones(int cantValoraciones) {
        this.cantValoraciones = cantValoraciones;
    }

    public Integer getValoracionEstudiante() {
        return valoracionEstudiante;
    }

    public void setValoracionEstudiante(Integer valoracionEstudiante) {
        this.valoracionEstudiante = valoracionEstudiante;
    }

    public int getCantVal1() {
        return cantVal1;
    }

    public void setCantVal1(int cantVal1) {
        this.cantVal1 = cantVal1;
    }

    public int getCantVal2() {
        return cantVal2;
    }

    public void setCantVal2(int cantVal2) {
        this.cantVal2 = cantVal2;
    }

    public int getCantVal3() {
        return cantVal3;
    }

    public void setCantVal3(int cantVal3) {
        this.cantVal3 = cantVal3;
    }

    public int getCantVal4() {
        return cantVal4;
    }

    public void setCantVal4(int cantVal4) {
        this.cantVal4 = cantVal4;
    }

    public int getCantVal5() {
        return cantVal5;
    }

    public void setCantVal5(int cantVal5) {
        this.cantVal5 = cantVal5;
    }

    public int getCantidadVisitas() {
        return cantidadVisitas;
    }

    public void setCantidadVisitas(int cantidadVisitas) {
        this.cantidadVisitas = cantidadVisitas;
    }
}
