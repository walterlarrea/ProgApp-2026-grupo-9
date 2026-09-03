package com.grupo9.edext.grupo9.servidor_central.dominio;

import java.time.LocalDate;

public record DataCurso (
    DataInstituto instituto,
    String nombreCurso,
    String descCurso,
    int duracion,
    int cantHoras,
    int cantCred,
    LocalDate fechaReg,
    String url
){}
