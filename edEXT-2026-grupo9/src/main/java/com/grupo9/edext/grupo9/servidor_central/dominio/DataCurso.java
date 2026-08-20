package com.grupo9.edext.grupo9.servidor_central.dominio;

import java.time.LocalDate;

public class DataCurso {
    private String nombreInst;
    private String nombreCurso;
    private String descCurso;
    private int duracion;
    private int cantHoras;
    private int cantCred;
    private LocalDate fechaReg;
    private String url;

    public DataCurso(String nombreInst, String nombreCurso, String descCurso, int duracion, int cantHoras, int cantCred, LocalDate fechaReg, String url) {
        this.nombreInst = nombreInst;
        this.nombreCurso = nombreCurso;
        this.descCurso = descCurso;
        this.duracion = duracion;
        this.cantHoras = cantHoras;
        this.cantCred = cantCred;
        this.fechaReg = fechaReg;
        this.url = url;
    }

    public String getNombreInst() {
        return nombreInst;
    }

    public void setNombreInst(String nombreInst) {
        this.nombreInst = nombreInst;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getDescCurso() {
        return descCurso;
    }

    public void setDescCurso(String descCurso) {
        this.descCurso = descCurso;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getCantHoras() {
        return cantHoras;
    }

    public void setCantHoras(int cantHoras) {
        this.cantHoras = cantHoras;
    }

    public int getCantCred() {
        return cantCred;
    }

    public void setCantCred(int cantCred) {
        this.cantCred = cantCred;
    }

    public LocalDate getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(LocalDate fechaReg) {
        this.fechaReg = fechaReg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
