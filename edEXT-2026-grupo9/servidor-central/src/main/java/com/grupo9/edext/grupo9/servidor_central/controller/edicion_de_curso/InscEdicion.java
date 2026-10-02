package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;

import com.grupo9.edext.grupo9.mensajes.ErrorEstadoInvalido;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Estudiante;
import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "inscripciones_a_ediciones")
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
    @Enumerated(EnumType.STRING)
    private EstadoInscripcion estado;
    
    public InscEdicion(){}

    public InscEdicion(LocalDate fechaInscE, Estudiante estudiante, EdicionCurso edicion) {
        this.fechaInscE = fechaInscE;
        this.estudiante = estudiante;
        this.edicion = edicion;
        this.estado = EstadoInscripcion.INSCRIPTO;
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
    
    public EstadoInscripcion getEstado() {
        return estado;
    }
    
    public void cambiarEstado(EstadoInscripcion nuevoEstado) throws ErrorEstadoInvalido {
        if (estado != EstadoInscripcion.INSCRIPTO) {
            throw new ErrorEstadoInvalido("La inscripción ya fue procesada.");
        }

        if (nuevoEstado != EstadoInscripcion.ACEPTADA && nuevoEstado != EstadoInscripcion.RECHAZADA) {
            throw new ErrorEstadoInvalido("Estado de destino no válido.");
        }

        this.estado = nuevoEstado;
    }
}
