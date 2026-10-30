package com.grupo9.edext.grupo9.web_service.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataDocenteDTO")
public class DataDocenteDTO extends DataUsuarioDTO {

    private String nombreInst;
    private List<String> ediciones = new ArrayList<>();
    private List<String> cursos = new ArrayList<>();

    public DataDocenteDTO() {
        setTipo("Docente");
    }

    public String getNombreInst() {
        return nombreInst;
    }

    public void setNombreInst(String nombreInst) {
        this.nombreInst = nombreInst;
    }

    public List<String> getEdiciones() {
        return ediciones;
    }

    public void setEdiciones(List<String> ediciones) {
        this.ediciones = ediciones;
    }

    public List<String> getCursos() {
        return cursos;
    }

    public void setCursos(List<String> cursos) {
        this.cursos = cursos;
    }
}
