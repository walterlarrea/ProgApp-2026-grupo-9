package com.grupo9.edext.grupo9.servidor_central.dominio;

import java.time.LocalDate;

public class DataEstudiante extends DataUsuario {

    public DataEstudiante(String nickname, String nombreUs, String apellidoUs, String email, LocalDate fechaNac) {
        super(nickname, nombreUs, apellidoUs, email, fechaNac);
    }
}
