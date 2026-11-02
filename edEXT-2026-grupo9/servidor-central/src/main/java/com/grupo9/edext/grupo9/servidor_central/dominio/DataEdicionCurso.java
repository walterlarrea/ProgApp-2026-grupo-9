package com.grupo9.edext.grupo9.servidor_central.dominio;

import java.time.LocalDate;
import java.util.Set;

public class DataEdicionCurso {
    private String nombreEdi;
    private DataCurso cursoAsoc;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer cupo;
    private Set<DataDocente> docentes;
    private Set<DataInscEdicion> inscripciones;
    private LocalDate fechaPub;
    private String imagen;
    private boolean cerrada;
    
    public DataEdicionCurso(String nombreEdi, DataCurso cursoAsoc, LocalDate fechaInicio, LocalDate fechaFin, Integer cupo, Set<DataDocente> docentes, Set<DataInscEdicion> inscripciones, LocalDate fechaPub, String imagen) {
        this(nombreEdi, cursoAsoc, fechaInicio, fechaFin, cupo, docentes, inscripciones, fechaPub, imagen, false);
    }

    public DataEdicionCurso(String nombreEdi, DataCurso cursoAsoc, LocalDate fechaInicio, LocalDate fechaFin, Integer cupo, Set<DataDocente> docentes, Set<DataInscEdicion> inscripciones, LocalDate fechaPub, String imagen, boolean cerrada) {
        this.nombreEdi = nombreEdi;
        this.cursoAsoc = cursoAsoc;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cupo = cupo;
        this.docentes = docentes;
        this.inscripciones = inscripciones;
        this.fechaPub = fechaPub;
        this.imagen = imagen;
        this.cerrada = cerrada;
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
    
    public Set<DataInscEdicion> getInscripciones(){
        return inscripciones;
    }
    
    public String getImagen(){
        return imagen;
    }
    
    public void setImagen(String imagen){
        this.imagen = imagen;
    }

    public boolean isCerrada() {
        return cerrada;
    }

    public void setCerrada(boolean cerrada) {
        this.cerrada = cerrada;
    }
}
