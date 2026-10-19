package com.grupo9.edext.grupo9.servidor_central.dominio;

import java.time.LocalDate;

public class DataInscPrograma {
    private String estudianteNickname;
    private String programaNombre;
    private LocalDate fechaInscP;

    public DataInscPrograma(LocalDate fechaInscP) {
        this.fechaInscP = fechaInscP;
    }

    public DataInscPrograma(String estudianteNickname, String programaNombre, LocalDate fechaInscP) {
        this.estudianteNickname = estudianteNickname;
        this.programaNombre = programaNombre;
        this.fechaInscP = fechaInscP;
    }

    public String getEstudianteNickname() {
        return estudianteNickname;
    }

    public void setEstudianteNickname(String estudianteNickname) {
        this.estudianteNickname = estudianteNickname;
    }

    public String getProgramaNombre() {
        return programaNombre;
    }

    public void setProgramaNombre(String programaNombre) {
        this.programaNombre = programaNombre;
    }

    public LocalDate getFechaInscP() {
        return fechaInscP;
    }

    public void setFechaInscP(LocalDate fechaInscP) {
        this.fechaInscP = fechaInscP;
    }
}
