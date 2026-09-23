package com.grupo9.edext.grupo9.interfaces;

import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEstudiante;
import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import java.util.HashSet;
import java.time.LocalDate;


public interface IServidorCentral {
    public void logStatus();
    
    // Programas de formación
    public DataProgramaFormacion guardarProgramaDeFormacion(DataProgramaFormacion nuevoPrograma);
    public HashSet<DataProgramaFormacion> consultarTodosLosProgramas();
    public Boolean existeProgramaDeFormacion(String nombre);
    public DataProgramaFormacion traerPorNombreId(String nombreId);
    public HashSet<DataProgramaFormacion> programasPorCurso(String nombreCurso);
    public Boolean agregarCursoAProgramaDeFormacion(String nombreIdPrograma, String nombreIdCurso);
    
    // Ediciones de cursos
    public DataEdicionCurso guardarEdicionCurso(DataEdicionCurso nuevaEdicion);
    public DataDocente[] traerDocentes(DataInstituto instituto);
    public DataEdicionCurso consultarUnaEdicionCurso(String nEdi) throws ErrorNoExiste;
    public DataEdicionCurso[] traerEdiciones(DataCurso curso);
    public HashSet<DataEdicionCurso> traerEdiciones(DataCurso curso, boolean asSet);
    public DataEstudiante[] traerEstudiantes();
    public void inscribirEstudiante(LocalDate fechaInsc, String nickname, String nombreEdi)throws ErrorRepetidos, ErrorNoExiste ;
    
    // Cursos
    public DataCurso guardarCurso(DataCurso nuevoCurso);
    public HashSet<DataCurso> consultarTodosLosCursos();
    public HashSet<DataCurso> cursosPorInstituto(String nombreInstituto);
    public Boolean existeCurso(String nombre);
    public HashSet<DataCurso> cursosNoRelacionadosConUnProgDeFormacion(String idProgramaDeFormacion);
    
    // Institutos
    public DataInstituto guardarInstituto(DataInstituto nuevoInstituto);
    public HashSet<DataInstituto> consultarTodosLosInstitutos();
    public Boolean existeInstituto(String nombre);
    
    public String[] listarUsuarios();
    public DataUsuario consultarUsuario(String nickname) throws ErrorNoExiste;
    public void modificarUsuario(String nick, String nom, String ape, LocalDate fechaNac, String rutaImg);
    public void eliminarUsuario(String nick) throws ErrorNoExiste;
    public void registrarEstudiante(String nickname, String nombre, String apellido, String email, LocalDate fechaNac, String rutaImagen) throws ErrorRepetidos;
    public void registrarDocente(String nickname, String nombre, String apellido, String email, LocalDate fechaNac, String rutaImagen, String nombreInst) throws ErrorRepetidos;
}
