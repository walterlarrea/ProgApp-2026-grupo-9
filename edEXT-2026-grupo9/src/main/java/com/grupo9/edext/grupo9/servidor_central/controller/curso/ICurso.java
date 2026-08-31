package com.grupo9.edext.grupo9.servidor_central.controller.curso;

import com.grupo9.edext.grupo9.dtos.DTOCurso;
import java.util.HashSet;

public interface ICurso {
    public DTOCurso guardarNuevoCurso(DTOCurso nuevoCurso);
    public HashSet<DTOCurso> todosLosCursos();
}
