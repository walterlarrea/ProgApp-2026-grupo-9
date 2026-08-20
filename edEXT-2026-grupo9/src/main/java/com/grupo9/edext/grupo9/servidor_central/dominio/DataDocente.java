package com.grupo9.edext.grupo9.servidor_central.dominio;

import java.time.LocalDate;

public class DataDocente extends DataUsuario {
    private String nombreInst;

    public DataDocente(String nickname, String nombreUs, String apellidoUs, String email, LocalDate fechaNac, String nombreInst) {
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
