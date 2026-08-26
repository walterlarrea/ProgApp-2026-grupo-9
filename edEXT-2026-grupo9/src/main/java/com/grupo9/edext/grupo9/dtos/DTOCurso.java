package com.grupo9.edext.grupo9.dtos;


import java.time.LocalDate;


public class DTOCurso {
    private final String nombreCurso;
    private final String descripcion;
    private final int duracion;
    private final int cantHoras;
    private final int cantCred;
    private final LocalDate fechaReg;
    private final String url;

    public DTOCurso(String nombreCurso, String descripcion, int duracion, int cantHoras, int cantCred, LocalDate fechaReg, String url) {
        this.nombreCurso = nombreCurso;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.cantHoras = cantHoras;
        this.cantCred = cantCred;
        this.fechaReg = fechaReg;
        this.url = url;
    }

    public String getNombre() {
        return nombreCurso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getDuracion() {
        return duracion;
    }

    public int getCantHoras() {
        return cantHoras;
    }

    public int getCantCred() {
        return cantCred;
    }

    public LocalDate getFechaRegistro() {
        return fechaReg;
    }

    public String getUrl() {
        return url;
    }
}
