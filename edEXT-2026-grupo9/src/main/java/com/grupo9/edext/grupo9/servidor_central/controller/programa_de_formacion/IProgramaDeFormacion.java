package com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion;

import com.grupo9.edext.grupo9.dtos.DTOProgramaDeFormacion;
import java.util.HashSet;

public interface IProgramaDeFormacion {
    public DTOProgramaDeFormacion guardarNuevoProgramaDeFormacion(DTOProgramaDeFormacion nuevoPrograma);
    public HashSet<DTOProgramaDeFormacion> todosLosProgramas();
}
