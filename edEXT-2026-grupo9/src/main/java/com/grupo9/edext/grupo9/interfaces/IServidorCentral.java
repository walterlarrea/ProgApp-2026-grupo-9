package com.grupo9.edext.grupo9.interfaces;

import com.grupo9.edext.grupo9.dtos.DTOProgramaDeFormacion;

public interface IServidorCentral {
    public void logStatus();
    public void guardarProgramaDeFormacion(DTOProgramaDeFormacion nuevoPrograma);
    public void guardarEdicionCurso();
}
