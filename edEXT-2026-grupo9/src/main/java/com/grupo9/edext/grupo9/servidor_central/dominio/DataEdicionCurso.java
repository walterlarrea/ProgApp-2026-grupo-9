package com.grupo9.edext.grupo9.servidor_central.dominio;

import java.time.LocalDate;
import java.util.Set;

public class DataEdicionCurso {
    private String nombreEdi;
    private DataCurso cursoAsoc;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer cupo;
    private DataDocente docente;
    private Set<DataInscEdicion> inscripciones;
    private LocalDate fechaPub;
    
    public DataEdicionCurso(String nombreEdi, DataCurso cursoAsoc, LocalDate fechaInicio, LocalDate fechaFin, Integer cupo, DataDocente docente, Set<DataInscEdicion> inscripciones, LocalDate fechaPub) {
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

    public DataDocente getDocente() {
        return docente;
    }

    public void setDocente(Set<DataDocente> docentes) {
        this.docente = docente;
    }

    public LocalDate getFechaPub() {
        return fechaPub;
    }

    public void setFechaPub(LocalDate fechaPub) {
        this.fechaPub = fechaPub;
    }
    
    public Set<DataInscEdicion> getInscripciones(){
        return inscripciones;
    }
}
