package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;

import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import java.time.LocalDate;
import java.util.Set;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

//DONDE VAN LAS ENTIDADES
public class EdicionCurso {
    
    private String nombreEdi;
    private DataCurso cursoAsoc;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer cupo;
    private Set<DataDocente> docentes;
    private LocalDate fechaPub;
    
    public EdicionCurso(String nombreEdi, DataCurso cursoAsoc, LocalDate fechaInicio, LocalDate fechaFin, Integer cupo, Set<DataDocente> docentes, LocalDate fechaPub) {
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

    public DataCurso getCursoAsoc() {
        return cursoAsoc;
    }

    public void setCursoAsoc(DataCurso cursoAsoc) {
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

    public Set<DataDocente> getDocentes() {
        return docentes;
    }

    public void setDocentes(Set<DataDocente> docentes) {
        this.docentes = docentes;
    }

    public LocalDate getFechaPub() {
        return fechaPub;
    }

    public void setFechaPub(LocalDate fechaPub) {
        this.fechaPub = fechaPub;
    }
}
