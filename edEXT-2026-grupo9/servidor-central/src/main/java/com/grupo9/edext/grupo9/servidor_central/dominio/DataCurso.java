package com.grupo9.edext.grupo9.servidor_central.dominio;

import java.time.LocalDate;
import java.util.Set;

public record DataCurso(
        DataInstituto instituto,
        String nombreCurso,
        String descCurso,
        int duracion,
        int cantHoras,
        int cantCred,
        LocalDate fechaReg,
        String url,
        Set<DataCurso> previas,
        Set<DataProgramaFormacion> programas,
        Set<String> categorias,
        String imagen,
        Double puntajePromedio,
        int cantValoraciones,
        Integer valoracionEstudiante,
        int cantVal1,
        int cantVal2,
        int cantVal3,
        int cantVal4,
        int cantVal5,
        int cantidadVisitas) {

    public DataCurso(
            DataInstituto instituto,
            String nombreCurso,
            String descCurso,
            int duracion,
            int cantHoras,
            int cantCred,
            LocalDate fechaReg,
            String url,
            Set<DataCurso> previas,
            Set<DataProgramaFormacion> programas,
            Set<String> categorias,
            String imagen,
            Double puntajePromedio,
            int cantValoraciones,
            Integer valoracionEstudiante,
            int cantVal1,
            int cantVal2,
            int cantVal3,
            int cantVal4,
            int cantVal5) {
        this(instituto, nombreCurso, descCurso, duracion, cantHoras, cantCred, fechaReg, url, previas, programas, categorias, imagen, puntajePromedio, cantValoraciones, valoracionEstudiante, cantVal1, cantVal2, cantVal3, cantVal4, cantVal5, 0);
    }

    public DataCurso(
            DataInstituto instituto,
            String nombreCurso,
            String descCurso,
            int duracion,
            int cantHoras,
            int cantCred,
            LocalDate fechaReg,
            String url,
            Set<DataCurso> previas,
            Set<DataProgramaFormacion> programas,
            Set<String> categorias,
            String imagen,
            Double puntajePromedio,
            int cantValoraciones,
            Integer valoracionEstudiante) {
        this(instituto, nombreCurso, descCurso, duracion, cantHoras, cantCred, fechaReg, url, previas, programas, categorias, imagen, puntajePromedio, cantValoraciones, valoracionEstudiante, 0, 0, 0, 0, 0, 0);
    }

    public DataCurso(
            DataInstituto instituto,
            String nombreCurso,
            String descCurso,
            int duracion,
            int cantHoras,
            int cantCred,
            LocalDate fechaReg,
            String url,
            Set<DataCurso> previas,
            Set<DataProgramaFormacion> programas,
            Set<String> categorias,
            String imagen) {
        this(instituto, nombreCurso, descCurso, duracion, cantHoras, cantCred, fechaReg, url, previas, programas, categorias, imagen, 0.0, 0, null, 0, 0, 0, 0, 0, 0);
    }
}
