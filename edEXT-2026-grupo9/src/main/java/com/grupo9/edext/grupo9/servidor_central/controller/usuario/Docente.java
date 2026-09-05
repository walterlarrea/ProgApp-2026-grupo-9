package com.grupo9.edext.grupo9.servidor_central.controller.usuario;

import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EdicionCurso;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import jakarta.persistence.*;


@Entity
public class Docente extends Usuario implements Serializable {
    private String nombreInst;
    @OneToMany(mappedBy = "docente")
    private Set<EdicionCurso> ediciones;

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
    
   public Set<EdicionCurso> getEdiciones(){
       return ediciones;
   }
}
