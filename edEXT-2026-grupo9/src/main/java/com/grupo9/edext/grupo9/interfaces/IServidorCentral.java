package com.grupo9.edext.grupo9.interfaces;

import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Docente;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EdicionCurso;
import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import java.util.HashSet;

public interface IServidorCentral {
    public void logStatus();
    
    // Programas de formación
    public DataProgramaFormacion guardarProgramaDeFormacion(DataProgramaFormacion nuevoPrograma);
    public HashSet<DataProgramaFormacion> consultarTodosLosProgramas();
    public DataProgramaFormacion traerPorNombreId(String nombreId);
    public Boolean agregarCursoAProgramaDeFormacion(String nombreIdPrograma, String nombreIdCurso);
    
    // Ediciones de cursos
    public DataEdicionCurso guardarEdicionCurso(DataEdicionCurso nuevaEdicion);
    public Docente[] traerDocentes();
    public DataEdicionCurso consultarUnaEdicionCurso(String nEdi) throws ErrorNoExiste;
    public EdicionCurso[] traerEdiciones(Curso curso);
    
    // Cursos
    public DataCurso guardarCurso(DataCurso nuevoCurso);
    public HashSet<DataCurso> consultarTodosLosCursos();
    public HashSet<DataCurso> cursosNoRelacionadosConUnProgDeFormacion(String idProgramaDeFormacion);
    
    // Institutos
    public DataInstituto guardarInstituto(DataInstituto nuevoInstituto);
    public HashSet<DataInstituto> consultarTodosLosInstitutos();
    
    public String[] listarUsuarios();
    public DataUsuario consultarUsuario(String nickname) throws ErrorNoExiste;
}
