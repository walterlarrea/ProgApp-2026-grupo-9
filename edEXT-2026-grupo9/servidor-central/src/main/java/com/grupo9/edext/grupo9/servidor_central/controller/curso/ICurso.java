package com.grupo9.edext.grupo9.servidor_central.controller.curso;

import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import java.util.HashSet;

public interface ICurso {
    public DataCurso guardarNuevoCurso(DataCurso nuevoCurso);
    public HashSet<DataCurso> todosLosCursos();
    public HashSet<DataCurso> cursosPorInstituto(String nombreInstituto);
    public Boolean existeCurso(String nombre);
    public HashSet<DataCurso> cursosNoRelacionadosConUnProgDeFormacion(String idProgramaDeFormacion);
}
