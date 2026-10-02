package com.grupo9.edext.grupo9.servidor_central.dominio;

import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EstadoInscripcion;
import java.time.LocalDate;

public class DataInscEdicion {
    private LocalDate fechaInscE;
    private DataEstudiante estudiante;
    private String nombreEdi;
    private EstadoInscripcion estado;
    
    public DataInscEdicion(LocalDate fechaInscE, DataEstudiante estudiante, String nombreEdi, EstadoInscripcion estado) {
        this.fechaInscE = fechaInscE;
        this.estudiante = estudiante;
        this.nombreEdi = nombreEdi;
        this.estado = estado;
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
}
