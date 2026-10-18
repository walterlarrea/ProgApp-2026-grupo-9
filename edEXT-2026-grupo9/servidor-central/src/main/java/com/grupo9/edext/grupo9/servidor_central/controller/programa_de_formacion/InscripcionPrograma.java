package com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion;

import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Estudiante;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "inscripciones_a_programas")
public class InscripcionPrograma {
  @EmbeddedId
  private InscripcionProgramaId id = new InscripcionProgramaId();

  @ManyToOne
  @MapsId("estudiante")
  @JoinColumn(name = "estudiante_nickname")
  private Estudiante estudiante;

  @ManyToOne
  @MapsId("programa")
  @JoinColumn(name = "programa_nombre")
  private ProgramaDeFormacion programa;

  @Column(name = "fechaInscripcion", nullable = false)
  private LocalDate fechaInscripcion;

  public InscripcionPrograma() {
  }

  public InscripcionPrograma(Estudiante estudiante, ProgramaDeFormacion programa, LocalDate fechaInscripcion) {
    this.estudiante = estudiante;
    this.programa = programa;
    this.fechaInscripcion = fechaInscripcion;
    if (estudiante != null && programa != null) {
      this.id = new InscripcionProgramaId(estudiante.getNickname(), programa.getNombre());
    }
  }

  public InscripcionProgramaId getId() {
    return id;
  }

  public void setId(InscripcionProgramaId id) {
    this.id = id;
  }

  public Estudiante getEstudiante() {
    return estudiante;
  }

  public void setEstudiante(Estudiante estudiante) {
    this.estudiante = estudiante;
  }

  public ProgramaDeFormacion getPrograma() {
    return programa;
  }

  public void setPrograma(ProgramaDeFormacion programa) {
    this.programa = programa;
  }

  public LocalDate getFechaInscripcion() {
    return fechaInscripcion;
  }

  public void setFechaInscripcion(LocalDate fechaInscripcion) {
    this.fechaInscripcion = fechaInscripcion;
  }
}