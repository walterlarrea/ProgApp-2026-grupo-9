package com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion;

import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import java.util.HashSet;

public interface IProgramaDeFormacion {
    public DataProgramaFormacion guardarNuevoProgramaDeFormacion(DataProgramaFormacion nuevoPrograma);
    public HashSet<DataProgramaFormacion> todosLosProgramas();
    public DataProgramaFormacion traerPorNombreId(String nombreId);
    public Boolean agregarCursoAProgramaDeFormacion(String nombreIdPrograma, String nombreIdCurso);
}
