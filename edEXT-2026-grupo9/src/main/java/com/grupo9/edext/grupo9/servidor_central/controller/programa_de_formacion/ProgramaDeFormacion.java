package com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion;

import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import java.io.Serializable;
import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class ProgramaDeFormacion implements Serializable{
    @Id
    private String nombre;
    private String descripcion;
    @OneToMany
    private Set<Curso> cursos;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    
    public ProgramaDeFormacion(){}
    
    public ProgramaDeFormacion (String nombre, String descripcion, Set<Curso> cursos, LocalDate fechaInicio, LocalDate fechaFin){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cursos = cursos;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombreForm) {
        this.nombre = nombreForm;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descForm) {
        this.descripcion = descForm;
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
