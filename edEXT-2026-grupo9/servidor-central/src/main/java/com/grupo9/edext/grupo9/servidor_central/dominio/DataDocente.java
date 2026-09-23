package com.grupo9.edext.grupo9.servidor_central.dominio;

import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;

public class DataDocente extends DataUsuario {
    private String nombreInst;
    private Set<String> ediciones = new HashSet<>();
    private Set<String> cursos = new HashSet<>();

    public DataDocente(String nickname, String nombreUs, String apellidoUs, String email, LocalDate fechaNac, String imagen) {
        super(nickname, nombreUs, apellidoUs, email, fechaNac, imagen);
        this.nombreInst = nombreInst;
        if (ediciones != null) this.ediciones = ediciones;
        if (cursos != null) this.cursos = cursos;
    }

    public String getNombreInst() {
        return nombreInst;
    }

    public void setNombreInst(String nombreInst) {
        this.nombreInst = nombreInst;
    }
    
    public Set<String> getEdiciones(){
        return ediciones;
    }

    public Set<String> getCursos() {
        return cursos;
    }
    
    @Override
    public String getTipo() {
        return "Docente";
    }
    
}
