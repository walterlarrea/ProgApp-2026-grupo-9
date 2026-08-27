package com.grupo9.edext.grupo9.servidor_central.dominio;

import java.time.LocalDate;

public class DataInscEdicion {
    private LocalDate fechaInscE;
    private DataEstudiante estudiante;
    private String nombreEdi;;
    
    public DataInscEdicion(LocalDate fechaInscE, DataEstudiante estudiante, String nombreEdi) {
        this.fechaInscE = fechaInscE;
        this.estudiante = estudiante;
        this.nombreEdi = nombreEdi;
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
}
