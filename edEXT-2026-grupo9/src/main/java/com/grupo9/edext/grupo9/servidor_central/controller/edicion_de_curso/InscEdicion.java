package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;

import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Estudiante;
import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class InscEdicion {
    @Id
    private String nombreEdi;
//    private EdicionCurso edicion;
    private LocalDate fechaInscE;
    private Estudiante estudiante;

    public InscEdicion(LocalDate fechaInscE, Estudiante estudiante, String nombreEdi) {
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
    
    public Estudiante getEstudiante(){
        return estudiante;
    }
    
    public String getEdicion(){
        return nombreEdi;
    }
}
