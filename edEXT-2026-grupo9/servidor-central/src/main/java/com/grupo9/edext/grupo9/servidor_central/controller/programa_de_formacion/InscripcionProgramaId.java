package com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion;

import java.io.Serializable;
import java.util.Objects;

public class InscripcionProgramaId implements Serializable {
  private String estudiante;
  private String programa;

  public InscripcionProgramaId() {
  }

  public InscripcionProgramaId(String estudiante, String programa) {
    this.estudiante = estudiante;
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