package com.grupo9.edext.grupo9.servidor_central.controller.usuario;

import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Usuario;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.InscEdicion;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "estudiantes")
public class Estudiante extends Usuario implements Serializable{
    @OneToMany(mappedBy = "estudiante")
    private Set<InscEdicion> inscripciones = new HashSet<>();

    public Estudiante() {
        super();
    }
    public Estudiante(String nickname, String nombre, String apellido, String email, LocalDate fechaNac, String rutaImagen) {
        super(nickname, nombre, apellido, email, fechaNac, rutaImagen);
    }

    public Set<InscEdicion> getInscripciones(){
        return inscripciones;
    }
}
