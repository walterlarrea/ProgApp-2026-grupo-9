package com.grupo9.edext.grupo9.dtos;

import java.time.LocalDate;
import java.util.Date;


public class DTOCurso {
    private String nombre;
    private String nombreInst;
    private String descripcion;
    private int duracion;
    private int cantHoras;
    private int cantCred;
    private LocalDate fechaReg;
    private String url;
    
    public DTOCurso(String nombreInst, String nombre, String descripcion, int duracion, int cantHoras, int cantCred, LocalDate fechaReg, String url) {
        this.nombreInst = nombreInst;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.cantHoras = cantHoras;
        this.cantCred = cantCred;
        this.fechaReg = fechaReg;
        this.url = url;
    }
    
    // Crea un curso sin fecha de creación.
    // Se usa al momento de crear un curso nuevo, mientras es creado no tiene fecha de creación.
    public DTOCurso(String nombreInst, String nombre, String descripcion, int duracion, int cantHoras, int cantCred, String url) {
        this.nombreInst = nombreInst;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.cantHoras = cantHoras;
        this.cantCred = cantCred;
        this.fechaReg = null;
        this.url = url;
    }
    
    public String getNombre(){
        return this.nombre;
    }
    
    public String getNombreInstituto(){
        return this.nombreInst;
    }
    
    public String getDescripcion(){
        return this.descripcion;
    }

    public int getDuracion(){
        return this.duracion;
    }

    public int getCantHoras(){
        return this.cantHoras;
    }

    public int getCantCred(){
        return this.cantCred;
    }

    public LocalDate getFechaReg(){
        return this.fechaReg;
    }

    public String getUrl(){
        return this.url;
    }
}
