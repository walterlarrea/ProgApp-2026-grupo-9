package com.grupo9.edext.grupo9.servidor_central.controller.usuario;

import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.Instituto;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;
import jakarta.persistence.Entity;
import jakarta.persistence.*;


@Entity
public class Docente extends Usuario implements Serializable {
    private String nombreInst;
    @ManyToMany
    @JoinTable(
        name = "docente_instituto",
        joinColumns = @JoinColumn(name = "docente_nickname"),
        inverseJoinColumns = @JoinColumn(name = "instituto_nombreI")
    )
    private Set<Instituto> institutos = new HashSet<>();

    @OneToMany(mappedBy = "docente")
    private Set<EdicionCurso> ediciones = new HashSet<>();

    public Docente() {
        super();
    }
    public Docente(String nickname, String nombreUs, String apellidoUs, String email, LocalDate fechaNac, String rutaImagen, String nombreInst) {
        super(nickname, nombreUs, apellidoUs, email, fechaNac, rutaImagen);
        this.nombreInst = nombreInst;
    }

    public String getNombreInst() {
        return nombreInst;
    }

    public void setNombreInst(String nombreInst) {
        this.nombreInst = nombreInst;
    }

    public Set<Instituto> getInstitutos() {
        return institutos;
    }
    
   public Set<EdicionCurso> getEdiciones(){
       return ediciones;
   }
   
   @Override
    public String toString() {
        return getNombre() + " " + getApellido();
}
}
