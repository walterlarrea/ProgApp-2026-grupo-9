package com.grupo9.edext.grupo9.servidor_central.dominio;

import java.time.LocalDate;

public class InscEdicion {
    private LocalDate fechaInscE;

    public InscEdicion(LocalDate fechaInscE) {
        this.fechaInscE = fechaInscE;
    }

    public LocalDate getFechaInscE() {
        return fechaInscE;
    }

    public void setFechaInscE(LocalDate fechaInscE) {
        this.fechaInscE = fechaInscE;
    }
}
