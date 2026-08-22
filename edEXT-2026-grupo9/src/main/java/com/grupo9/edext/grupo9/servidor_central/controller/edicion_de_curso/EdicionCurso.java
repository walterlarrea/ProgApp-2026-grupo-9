package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;

import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.controller.docente.Docente;
import java.time.LocalDate;
import java.util.Set;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityManagerFactory;
//import jakarta.persistence.EntityTransaction;
//import jakarta.persistence.Persistence;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;

@Entity
public class EdicionCurso {
    @Id
    private String nombreEdi;
    @ManyToOne
    @JoinColumn(name = "curso_nombreCurso")
    private Curso cursoAsoc;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer cupo;
    @ManyToMany
    @JoinTable(name = "edicion_docente",
    joinColumns = @JoinColumn(name = "edicion_nombreEdi"),
    inverseJoinColumns = @JoinColumn(name = "docente_nickname"))
    private Set<Docente> docentes;
    private LocalDate fechaPub;
    
    public EdicionCurso(String nombreEdi, Curso cursoAsoc, LocalDate fechaInicio, LocalDate fechaFin, Integer cupo, Set<Docente> docentes, LocalDate fechaPub) {
        this.nombreEdi = nombreEdi;
        this.cursoAsoc = cursoAsoc;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cupo = cupo;
        this.docentes = docentes;
        this.fechaPub = fechaPub;
    }

    public String getNombreEdi() {
        return nombreEdi;
    }

    public void setNombreEdi(String nombreEdi) {
        this.nombreEdi = nombreEdi;
    }

    public Curso getCursoAsoc() {
        return cursoAsoc;
    }

    public void setCursoAsoc(Curso cursoAsoc) {
        this.cursoAsoc = cursoAsoc;
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

    public Integer getCupo() {
        return cupo;
    }

    public void setCupo(Integer cupo) {
        this.cupo = cupo;
    }

    public Set<Docente> getDocentes() {
        return docentes;
    }

    public void setDocentes(Set<Docente> docentes) {
        this.docentes = docentes;
    }

    public LocalDate getFechaPub() {
        return fechaPub;
    }

    public void setFechaPub(LocalDate fechaPub) {
        this.fechaPub = fechaPub;
    }
}
