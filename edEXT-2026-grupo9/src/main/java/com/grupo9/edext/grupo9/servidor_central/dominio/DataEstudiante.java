package com.grupo9.edext.grupo9.servidor_central.dominio;

import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;

public class DataEstudiante extends DataUsuario {
    private Set<DataInscEdicion> inscripciones = new HashSet<>();
    private Set<String> edicionesInscriptas = new HashSet<>();

    public DataEstudiante(String nickname, String nombreUs, String apellidoUs, String email, LocalDate fechaNac, String imagen) {
        super(nickname, nombreUs, apellidoUs, email, fechaNac, imagen);
        if (edicionesInscriptas != null) this.edicionesInscriptas = edicionesInscriptas;
    }
    
    public Set<DataInscEdicion> getInscripciones(){
        return inscripciones;
    }

    public Set<String> getEdicionesInscriptas() {
        return edicionesInscriptas;
    }
    
    @Override
    public String getTipo() {
        return "Estudiante";
    }
}
