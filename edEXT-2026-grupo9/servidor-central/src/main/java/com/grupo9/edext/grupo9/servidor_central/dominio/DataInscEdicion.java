package com.grupo9.edext.grupo9.servidor_central.dominio;

import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EstadoInscripcion;
import java.time.LocalDate;

public class DataInscEdicion {
    private LocalDate fechaInscE;
    private DataEstudiante estudiante;
    private String nombreEdi;
    private EstadoInscripcion estado;
    private Integer nota;
    
    public DataInscEdicion(LocalDate fechaInscE, DataEstudiante estudiante, String nombreEdi, EstadoInscripcion estado) {
        this(fechaInscE, estudiante, nombreEdi, estado, null);
    }

    public DataInscEdicion(LocalDate fechaInscE, DataEstudiante estudiante, String nombreEdi, EstadoInscripcion estado, Integer nota) {
        this.fechaInscE = fechaInscE;
        this.estudiante = estudiante;
        this.nombreEdi = nombreEdi;
        this.estado = estado;
        this.nota = nota;
    }

    public LocalDate getFechaInscE() {
        return fechaInscE;
    }

    public void setFechaInscE(LocalDate fechaInscE) {
        this.fechaInscE = fechaInscE;
    }
    
    public DataEstudiante getEstudiante(){
        return estudiante;
    }
    
    public String getEdicion(){
        return nombreEdi;
    }
    
    public EstadoInscripcion getEstado(){
        return estado;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }
}
