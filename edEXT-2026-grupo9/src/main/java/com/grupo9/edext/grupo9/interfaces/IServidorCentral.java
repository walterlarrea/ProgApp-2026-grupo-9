package com.grupo9.edext.grupo9.interfaces;

import com.grupo9.edext.grupo9.dtos.DTOCurso;
import com.grupo9.edext.grupo9.dtos.DTOProgramaDeFormacion;
import java.util.HashSet;

public interface IServidorCentral {
    public void logStatus();
    
    // Programas de formación
    public DTOProgramaDeFormacion guardarProgramaDeFormacion(DTOProgramaDeFormacion nuevoPrograma);
    public HashSet<DTOProgramaDeFormacion> consultarTodosLosProgramas();
    
    // Ediciones de cursos
    public void guardarEdicionCurso();
    
    //Cursos
    public DTOCurso guardarCurso(DTOCurso nuevoCurso);
    public HashSet<DTOCurso> consultarTodosLosCursos();
}
