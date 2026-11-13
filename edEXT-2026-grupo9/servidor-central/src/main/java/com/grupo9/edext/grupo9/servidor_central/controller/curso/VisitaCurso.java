package com.grupo9.edext.grupo9.servidor_central.controller.curso;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "visitas_cursos")
public class VisitaCurso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_nombreCurso", unique = true, nullable = false)
    private Curso curso;

    @Column(name = "cantidad_visitas", nullable = false)
    private int cantidadVisitas;

    public VisitaCurso() {
        this.cantidadVisitas = 0;
    }

    public VisitaCurso(Curso curso, int cantidadVisitas) {
        this.curso = curso;
        this.cantidadVisitas = cantidadVisitas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public int getCantidadVisitas() {
        return cantidadVisitas;
    }

    public void setCantidadVisitas(int cantidadVisitas) {
        this.cantidadVisitas = cantidadVisitas;
    }

    public void incrementarVisitas() {
        this.cantidadVisitas++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisitaCurso that = (VisitaCurso) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

