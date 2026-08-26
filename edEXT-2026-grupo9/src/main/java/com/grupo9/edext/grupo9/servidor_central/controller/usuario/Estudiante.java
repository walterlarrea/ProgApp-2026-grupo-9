package com.grupo9.edext.grupo9.servidor_central.controller.usuario;

import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Usuario;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.InscEdicion;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Estudiante extends Usuario implements Serializable{
    @OneToMany(mappedBy = "estudiante")
    private Set<InscEdicion> inscripciones;
    
    public Estudiante(String nickname, String nombreUs, String apellidoUs, String email, LocalDate fechaNac, Set<InscEdicion> inscripciones) {
        super(nickname, nombreUs, apellidoUs, email, fechaNac);
    }
    
    public Set<InscEdicion> getInscripciones(){
        return inscripciones;
    }
}
