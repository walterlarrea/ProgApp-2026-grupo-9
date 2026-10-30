package com.grupo9.edext.grupo9.web_service.dto;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataInscProgramaDTO")
public class DataInscProgramaDTO implements Serializable {

    private String estudianteNickname;
    private String programaNombre;
    private String fechaInscP;

    public DataInscProgramaDTO() {
    }

    public DataInscProgramaDTO(String estudianteNickname, String programaNombre, String fechaInscP) {
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

    public String getFechaInscP() {
        return fechaInscP;
    }

    public void setFechaInscP(String fechaInscP) {
        this.fechaInscP = fechaInscP;
    }
}
