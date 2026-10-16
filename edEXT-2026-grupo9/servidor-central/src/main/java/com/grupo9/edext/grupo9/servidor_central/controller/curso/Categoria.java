package com.grupo9.edext.grupo9.servidor_central.controller.curso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categorias")
public class Categoria implements Serializable {
  @Id
  @Column(name = "nombreCategoria")
  private String nombreCategoria;

  @ManyToMany(mappedBy = "categorias")
  private Set<Curso> cursos = new HashSet<>();

  public Categoria() {
  }

  public Categoria(String nombreCategoria) {
    this.nombreCategoria = nombreCategoria;
  }

  public String getNombreCategoria() {
    return nombreCategoria;
  }

  public void setNombreCategoria(String nombreCategoria) {
    this.nombreCategoria = nombreCategoria;
  }

  public Set<Curso> getCursos() {
    return cursos;
  }
}