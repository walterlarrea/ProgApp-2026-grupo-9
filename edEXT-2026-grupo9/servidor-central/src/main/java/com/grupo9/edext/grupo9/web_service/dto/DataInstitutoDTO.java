package com.grupo9.edext.grupo9.web_service.dto;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataInstitutoDTO")
public class DataInstitutoDTO implements Serializable {

    private String nombreI;

    public DataInstitutoDTO() {
    }

    public DataInstitutoDTO(String nombreI) {
        this.nombreI = nombreI;
    }

    public String getNombreI() {
        return nombreI;
    }

    public void setNombreI(String nombreI) {
        this.nombreI = nombreI;
    }
}
