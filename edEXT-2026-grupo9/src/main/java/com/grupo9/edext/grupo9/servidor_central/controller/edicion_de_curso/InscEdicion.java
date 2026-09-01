package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;

import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Estudiante;
import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
public class InscEdicion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fechaInscE;
    @ManyToOne
    @JoinColumn(name = "estudiane_nickname")
    private Estudiante estudiante;
    @ManyToOne
    @JoinColumn(name = "edicion_nombreEdi")
    private EdicionCurso edicion;
    
    public InscEdicion(){}

    public InscEdicion(LocalDate fechaInscE, Estudiante estudiante, EdicionCurso edicion) {
        this.fechaInscE = fechaInscE;
        this.estudiante = estudiante;
        this.edicion = edicion;
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
