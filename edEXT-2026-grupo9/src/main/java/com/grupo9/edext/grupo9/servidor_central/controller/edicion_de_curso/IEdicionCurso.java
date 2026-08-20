package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import java.time.LocalDate;
import java.util.Set;

public interface IEdicionCurso {
    public void guardarNuevaEdicionCurso();
    public abstract void crearEdicionCurso(String nEdi, LocalDate fInicio, LocalDate fFin, int c, Set<DataDocente> d) throws ErrorRepetidos;
    /*public abstract DataEdicionCurso consultarEdicionCurso(String nInst) throws ErrorNoExiste;
    public abstract DataEdicionCurso inscripcionEdicionCurso() throws ErrorNoExiste;*/
}
