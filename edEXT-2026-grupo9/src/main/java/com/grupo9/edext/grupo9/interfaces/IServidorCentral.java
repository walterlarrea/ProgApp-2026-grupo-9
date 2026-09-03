package com.grupo9.edext.grupo9.interfaces;

import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import java.util.HashSet;

public interface IServidorCentral {
    public void logStatus();
    
    // Programas de formación
    public DataProgramaFormacion guardarProgramaDeFormacion(DataProgramaFormacion nuevoPrograma);
    public HashSet<DataProgramaFormacion> consultarTodosLosProgramas();
    
    // Ediciones de cursos
    public void guardarEdicionCurso();
    
    // Cursos
    public DataCurso guardarCurso(DataCurso nuevoCurso);
    public HashSet<DataCurso> consultarTodosLosCursos();
    
    // Institutos
    public DataInstituto guardarInstituto(DataInstituto nuevoInstituto);
    public HashSet<DataInstituto> consultarTodosLosInstitutos();
}
