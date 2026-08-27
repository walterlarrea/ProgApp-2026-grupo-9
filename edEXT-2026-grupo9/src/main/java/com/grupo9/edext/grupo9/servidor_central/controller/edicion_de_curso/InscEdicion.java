package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;

import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Estudiante;
import java.time.LocalDate;
import jakarta.persistence.Entity;

@Entity
public class InscEdicion {
    private Estudiante estudiante;
    private EdicionCurso edicion;
    private LocalDate fechaInscE;

    public InscEdicion(Estudiante estudiante, EdicionCurso edicion, LocalDate fechaInscE) {
        this.estudiante = estudiante;
        this.edicion = edicion;
        this.fechaInscE = fechaInscE;
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
    
    public EdicionCurso getEdicion(){
        return edicion;
    }
}
