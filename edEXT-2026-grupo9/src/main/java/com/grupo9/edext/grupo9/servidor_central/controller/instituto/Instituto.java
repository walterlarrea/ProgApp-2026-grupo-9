package com.grupo9.edext.grupo9.servidor_central.controller.instituto;

import java.io.Serializable;
import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Instituto implements Serializable{
    @Id
    private String nombreI;
    
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
}
