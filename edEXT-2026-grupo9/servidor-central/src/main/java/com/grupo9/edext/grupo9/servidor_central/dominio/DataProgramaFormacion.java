package com.grupo9.edext.grupo9.servidor_central.dominio;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public record DataProgramaFormacion(
        String nombre,
        String descripcion,
        HashSet<DataCurso> cursos,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        LocalDate fechaDeCreacion,
        String imagen) {
    @Override
    public String toString() {
        return "DTOProgramaDeFormacion{" +
                "nombre='" + nombre + '\'' + "," +
                "descripcion='" + descripcion + '\'' + "," +
                "cursos=" + cursos + "," +
                "fechaInicio=" + fechaInicio + "," +
                "fechaFin=" + fechaFin +
                "fechaDeCreacion=" + fechaDeCreacion +
                "imagen=" + imagen +
                '}';
    }
}
