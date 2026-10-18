package com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class InscripcionProgramaId implements Serializable {
  @Column(name = "estudiante_nickname")
  private String estudiante;

  @Column(name = "programa_nombre")
  private String programa;

  public InscripcionProgramaId() {
  }

  public InscripcionProgramaId(String estudiante, String programa) {
    this.estudiante = estudiante;
    this.programa = programa;
  }

  public String getEstudiante() {
    return estudiante;
  }

  public void setEstudiante(String estudiante) {
    this.estudiante = estudiante;
  }

  public String getPrograma() {
    return programa;
  }

  public void setPrograma(String programa) {
    this.programa = programa;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof InscripcionProgramaId that)) {
      return false;
    }
    return Objects.equals(estudiante, that.estudiante)
        && Objects.equals(programa, that.programa);
  }

  @Override
  public int hashCode() {
    return Objects.hash(estudiante, programa);
  }
}