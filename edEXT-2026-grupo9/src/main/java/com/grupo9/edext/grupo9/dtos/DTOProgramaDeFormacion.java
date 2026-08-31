package com.grupo9.edext.grupo9.dtos;

import java.util.Date;
import java.util.Set;


public class DTOProgramaDeFormacion {
    private final String nombre;
    private final String descripcion;
    private final Set<DTOCurso> cursos;
    private final Date fechaInicio;
    private final Date fechaFin;
    
    public DTOProgramaDeFormacion (String nombre, String descripcion, Set<DTOCurso> cursos, Date fechaInicio, Date fechaFin){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cursos = cursos;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public Set<DTOCurso> getCursos() {
        return cursos;
    }
    
    public Date getFechaInicio() {
        return fechaInicio;
    }
    
    public Date getFechaFin() {
        return fechaFin;
    }
    
    @Override
    public String toString() {
        return "DTOProgramaDeFormacion{" +
                "nombre='" + nombre + '\'' + "," +
                "descripcion='" + descripcion + '\'' + "," +
                "cursos=" + cursos + "," +
                "fechaInicio=" + fechaInicio + "," +
                "fechaFin=" + fechaFin +
                '}';
    }
}
