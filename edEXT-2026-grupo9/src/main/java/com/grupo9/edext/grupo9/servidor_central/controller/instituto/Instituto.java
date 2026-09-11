package com.grupo9.edext.grupo9.servidor_central.controller.instituto;

import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Docente;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;

@Entity
@Table(name = "institutos")
public class Instituto implements Serializable{
    @Id
    private String nombreI;
    @ManyToMany(mappedBy = "institutos")
    private Set<Docente> docentes = new HashSet<>();
    
    public Instituto(){}
    
    public Instituto(String nombre){
        this.nombreI = nombre;
    }
    
    public String getNombreI() {
        return nombreI;
    }

    public void setNombreI(String nombreI) {
        this.nombreI = nombreI;
    }

    public Set<Docente> getDocentes() {
        return docentes;
    }
}
