package com.grupo9.edext.grupo9.servidor_central.dominio;

import java.time.LocalDate;
import java.util.Set;

public class DataProgramaFormacion {
    private String nombreForm;
    private String descForm;
    private Set<DataCurso> cursos;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    
    public DataProgramaFormacion (String nombreForm, String descForm, Set<DataCurso> cursos, LocalDate fechaInicio, LocalDate fechaFin){
        this.nombreForm = nombreForm;
        this.descForm = descForm;
        this.cursos = cursos;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
    
    public String getNombreFrom() {
        return nombreForm;
    }
    
    public void setNombreFrom(String nombreForm) {
        this.nombreForm = nombreForm;
    }
    
    public String getDescFrom() {
        return descForm;
    }
    
    public void setDescFrom(String descForm) {
        this.descForm = descForm;
    }
    
    public Set<DataCurso> getCursos() {
        return cursos;
    }
    
    public void setCursos(Set<DataCurso> cursos) {
        this.cursos = cursos;
    }
    
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    public LocalDate getFechaFin() {
        return fechaFin;
    }
    
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
}
