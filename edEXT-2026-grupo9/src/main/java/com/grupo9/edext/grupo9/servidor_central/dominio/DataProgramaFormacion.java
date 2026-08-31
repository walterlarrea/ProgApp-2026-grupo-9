package com.grupo9.edext.grupo9.servidor_central.dominio;

import java.time.LocalDate;
import java.util.Set;


public record DataProgramaFormacion(
        String nombre,
        String descripcion,
        Set<DataCurso> cursos,
        LocalDate fechaInicio,
        LocalDate fechaFin
    ) {
    
    @Override
    public String toString() {
        return "DTOProgramaDeFormacion{" +
                "nombre='" + nombre + '\'' + "," +
                "descripcion='" + descripcion + '\'' + "," +
                "cursos=" + cursos + "," +
                "fechaInicio=" + fechaInicio + "," +
                "fechaFin=" + fechaFin +
                '}';
    }
}
