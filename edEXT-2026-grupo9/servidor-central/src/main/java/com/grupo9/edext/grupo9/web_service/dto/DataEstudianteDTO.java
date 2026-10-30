package com.grupo9.edext.grupo9.web_service.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataEstudianteDTO")
public class DataEstudianteDTO extends DataUsuarioDTO {

    private List<String> edicionesInscriptas = new ArrayList<>();
    private List<DataInscProgramaDTO> inscripcionesProgramas = new ArrayList<>();

    public DataEstudianteDTO() {
        setTipo("Estudiante");
    }

    public List<String> getEdicionesInscriptas() {
        return edicionesInscriptas;
    }

    public void setEdicionesInscriptas(List<String> edicionesInscriptas) {
        this.edicionesInscriptas = edicionesInscriptas;
    }

    public List<DataInscProgramaDTO> getInscripcionesProgramas() {
        return inscripcionesProgramas;
    }

    public void setInscripcionesProgramas(List<DataInscProgramaDTO> inscripcionesProgramas) {
        this.inscripcionesProgramas = inscripcionesProgramas;
    }
}
