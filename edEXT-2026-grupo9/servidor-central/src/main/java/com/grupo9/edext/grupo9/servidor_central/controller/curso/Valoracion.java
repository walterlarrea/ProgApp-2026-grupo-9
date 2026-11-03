package com.grupo9.edext.grupo9.servidor_central.controller.curso;

import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Estudiante;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "valoraciones", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"curso_nombreCurso", "estudiante_nickname"})
})
public class Valoracion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int puntaje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_nombreCurso", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_nickname", nullable = false)
    private Estudiante estudiante;

    public Valoracion() {
    }

    public Valoracion(int puntaje, Curso curso, Estudiante estudiante) {
        if (puntaje < 1 || puntaje > 5) {
            throw new IllegalArgumentException("El puntaje debe estar comprendido entre 1 y 5.");
        }
        this.puntaje = puntaje;
        this.curso = curso;
        this.estudiante = estudiante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        if (puntaje < 1 || puntaje > 5) {
            throw new IllegalArgumentException("El puntaje debe estar comprendido entre 1 y 5.");
        }
        this.puntaje = puntaje;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Valoracion that = (Valoracion) o;
        if (id != null && that.id != null) {
            return Objects.equals(id, that.id);
        }
        return puntaje == that.puntaje &&
                Objects.equals(curso != null ? curso.getNombreCurso() : null, that.curso != null ? that.curso.getNombreCurso() : null) &&
                Objects.equals(estudiante != null ? estudiante.getNickname() : null, that.estudiante != null ? that.estudiante.getNickname() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, puntaje, curso != null ? curso.getNombreCurso() : null, estudiante != null ? estudiante.getNickname() : null);
    }
}
