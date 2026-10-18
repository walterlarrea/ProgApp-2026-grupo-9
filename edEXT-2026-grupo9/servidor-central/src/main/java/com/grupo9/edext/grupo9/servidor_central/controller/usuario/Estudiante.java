package com.grupo9.edext.grupo9.servidor_central.controller.usuario;

import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.InscEdicion;
import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.InscripcionPrograma;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "estudiantes")
public class Estudiante extends Usuario implements Serializable {
    @OneToMany(mappedBy = "estudiante")
    private Set<InscEdicion> inscripciones = new HashSet<>();
    @OneToMany(mappedBy = "estudiante")
    private Set<InscripcionPrograma> inscripcionesProgramas = new HashSet<>();

    public Estudiante() {
        super();
    }

    public Estudiante(String nickname, String nombre, String apellido, String email, LocalDate fechaNac, String rutaImagen) {
        super(nickname, nombre, apellido, email, fechaNac, rutaImagen, null);
    }

    public Estudiante(String nickname, String nombre, String apellido, String email, LocalDate fechaNac, String rutaImagen, String password) {
        super(nickname, nombre, apellido, email, fechaNac, rutaImagen, password);
    }

    public Set<InscEdicion> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(Set<InscEdicion> inscripciones) {
        this.inscripciones = inscripciones;
    }

    public Set<InscripcionPrograma> getInscripcionesProgramas() {
        return inscripcionesProgramas;
    }

    public void setInscripcionesProgramas(Set<InscripcionPrograma> inscripcionesProgramas) {
        this.inscripcionesProgramas = inscripcionesProgramas;
    }
}
