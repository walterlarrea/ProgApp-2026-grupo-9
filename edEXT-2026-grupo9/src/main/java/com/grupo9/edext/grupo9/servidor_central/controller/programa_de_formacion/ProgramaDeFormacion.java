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
    private LocalDate fechaDeCreacion;
    
    public ProgramaDeFormacion(){}
    
    public ProgramaDeFormacion (String nombre, String descripcion, Set<Curso> cursos, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaDeCreacion){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cursos = cursos;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaDeCreacion = fechaDeCreacion;
    }
    
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombreForm) {
        this.nombre = nombreForm;
    }
    
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descForm) {
        this.descripcion = descForm;
    }
    
    public Set<Curso> getCursos() {
        return this.cursos;
    }
    
    public void setCursos(Set<Curso> cursos) {
        this.cursos = cursos;
    }
    
    public void agregarCurso(Curso curso) {
        this.cursos.add(curso);
    }

    public LocalDate getFechaInicio() {
        return this.fechaInicio;
    }
    
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    public LocalDate getFechaFin() {
        return this.fechaFin;
    }
    
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public LocalDate getFechaDeCreacion() {
        return this.fechaDeCreacion;
    }
    
    public void setFechaDeCreacion(LocalDate fechaDeCreacion){
        this.fechaDeCreacion = fechaDeCreacion;
    }
}
