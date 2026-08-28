package com.grupo9.edext.grupo9.servidor_central.dominio;

import java.time.LocalDate;
import java.util.Set;

public class DataEstudiante extends DataUsuario {
    private Set<DataInscEdicion> inscripciones;
    
    public DataEstudiante(String nickname, String nombreUs, String apellidoUs, String email, LocalDate fechaNac, Set<DataInscEdicion> inscripciones) {
        super(nickname, nombreUs, apellidoUs, email, fechaNac);
        this.inscripciones = inscripciones;
    }
    
    public Set<DataInscEdicion> getInscripciones(){
        return inscripciones;
    }
}
