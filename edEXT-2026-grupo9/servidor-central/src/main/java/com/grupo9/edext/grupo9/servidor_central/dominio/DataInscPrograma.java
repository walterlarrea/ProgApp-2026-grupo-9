package com.grupo9.edext.grupo9.servidor_central.dominio;

import java.time.LocalDate;

public class DataInscPrograma {
    private LocalDate fechaInscP;

    public DataInscPrograma(LocalDate fechaInscP) {
        this.fechaInscP = fechaInscP;
    }

    public LocalDate getFechaInscP() {
        return fechaInscP;
    }

    public void setFechaInscP(LocalDate fechaInscP) {
        this.fechaInscP = fechaInscP;
    }
}
