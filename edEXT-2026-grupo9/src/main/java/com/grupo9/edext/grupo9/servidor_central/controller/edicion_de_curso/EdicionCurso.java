package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;

import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Docente;
import java.time.LocalDate;
import java.util.Set;
import jakarta.persistence.*;

@Entity
public class EdicionCurso {
    @Id
    private String nombreEdi;
    @ManyToOne
    @JoinTable(
    name = "edicion_curso",
    joinColumns = @JoinColumn(name = "edicion_nombreEdi"),
    inverseJoinColumns = @JoinColumn(name = "curso_nombreCurso"))
    private Curso cursoAsoc;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer cupo;
    @ManyToOne
    @JoinColumn(name = "docente_nickname")
    private Docente docente;
    @OneToMany(mappedBy = "edicion")
    private Set<InscEdicion> inscripciones;
    private LocalDate fechaPub;
    
    public EdicionCurso(){}
    
    public EdicionCurso(String nombreEdi, Curso cursoAsoc, LocalDate fechaInicio, LocalDate fechaFin, Integer cupo, Docente docente, Set<InscEdicion> inscripciones, LocalDate fechaPub) {
        this.nombreEdi = nombreEdi;
       this.cursoAsoc = cursoAsoc;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cupo = cupo;
       this.docente = docente;
        this.inscripciones = inscripciones;
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

   public Docente getDocente() {
       return docente;
   }

   public void setDocente(Docente docente) {
       this.docente = docente;
   }

    public LocalDate getFechaPub() {
        return fechaPub;
    }

    public void setFechaPub(LocalDate fechaPub) {
        this.fechaPub = fechaPub;
    }
    
    public Set<InscEdicion> getInscripciones(){
       return inscripciones;
    }
}
