package com.grupo9.edext.grupo9.servidor_central.controller.docente;

import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Usuario;
import java.io.Serializable;
import java.time.LocalDate;


public class Docente extends Usuario implements Serializable {
    private String nombreInst;
    //@ManyToMany(mappedBy = "docentes")
    //private Set<EdicionCurso> ediciones;

    public Docente(String nickname, String nombreUs, String apellidoUs, String email, LocalDate fechaNac, String nombreInst) {
        super(nickname, nombreUs, apellidoUs, email, fechaNac);
        this.nombreInst = nombreInst;
    }

    public String getNombreInst() {
        return nombreInst;
    }

    public void setNombreInst(String nombreInst) {
        this.nombreInst = nombreInst;
    }
}
