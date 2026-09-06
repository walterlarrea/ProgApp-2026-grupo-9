package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Docente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import java.time.LocalDate;
import java.util.Set;

public interface IEdicionCurso {
    public DataEdicionCurso guardarNuevaEdicionCurso(DataEdicionCurso nuevaEdicion);
    public abstract void altaEdicionCurso(String nEdi, Curso cur, LocalDate fInicio, LocalDate fFin, int c, Set<InscEdicion> insc, Docente d) throws ErrorRepetidos;
    public abstract DataEdicionCurso consultarEdicionCurso(String nInst) throws ErrorNoExiste;
    public abstract void inscripcionEdicionCurso(LocalDate fInsc, String nickEstudiante, String nEdi) throws ErrorRepetidos, ErrorNoExiste;
}
