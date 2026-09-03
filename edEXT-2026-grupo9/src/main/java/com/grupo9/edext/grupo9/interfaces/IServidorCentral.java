package com.grupo9.edext.grupo9.interfaces;

import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import java.util.HashSet;

public interface IServidorCentral {
    public void logStatus();
    
    // Programas de formación
    public DataProgramaFormacion guardarProgramaDeFormacion(DataProgramaFormacion nuevoPrograma);
    public HashSet<DataProgramaFormacion> consultarTodosLosProgramas();
    
    // Ediciones de cursos
    public DataEdicionCurso guardarEdicionCurso(DataEdicionCurso nuevaEdicion);
    
    //Cursos
    public DataCurso guardarCurso(DataCurso nuevoCurso);
    public HashSet<DataCurso> consultarTodosLosCursos();
}
