package com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion;

import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import java.io.Serializable;
import java.time.LocalDate;
import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Set;

@Entity
public class ProgramaDeFormacion implements Serializable{
    @Id
    private String nombreForm;
    private String descForm;
    private Set<Curso> cursos;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    
    public ProgramaDeFormacion (String nombreForm, String descForm, Set<Curso> cursos, LocalDate fechaInicio, LocalDate fechaFin){
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
    
    public Set<Curso> getCursos() {
        return cursos;
    }
    
    public void setCursos(Set<Curso> cursos) {
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
