package com.grupo9.edext.grupo9.interfaces;

import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEstudiante;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInscEdicion;
import com.grupo9.edext.grupo9.servidor_central.dominio.ResultadoBusqueda;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EstadoInscripcion;
import com.grupo9.edext.grupo9.mensajes.ErrorEstadoInvalido;
import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
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

    public void inscribirEstudianteEnPrograma(LocalDate fechaInsc, String nickname, String nombrePrograma)
            throws ErrorRepetidos, ErrorNoExiste;

    public void inscribirseAPrograma(LocalDate fechaInsc, String nickname, String nombrePrograma)
            throws ErrorRepetidos, ErrorNoExiste;

    // Ediciones de cursos
    public DataEdicionCurso guardarEdicionCurso(DataEdicionCurso nuevaEdicion) throws ErrorRepetidos;

    public DataDocente[] traerDocentes(DataInstituto instituto);

    public DataEdicionCurso consultarUnaEdicionCurso(String nEdi) throws ErrorNoExiste;

    public DataEdicionCurso[] traerEdiciones(DataCurso curso);

    public HashSet<DataEdicionCurso> traerEdiciones(DataCurso curso, boolean asSet);

    public DataEstudiante[] traerEstudiantes();

    public void inscribirEstudiante(LocalDate fechaInsc, String nickname, String nombreEdi, EstadoInscripcion estado)
            throws ErrorRepetidos, ErrorNoExiste;

    public DataDocente[] buscarDocentes(DataEdicionCurso edicion);

    public Set<DataInscEdicion> buscarInscriptos(DataEdicionCurso edicion);
    
    public void cambiarEstadoInscripcion(String nombreEdicion, String nicknameEstudiante, EstadoInscripcion nuevoEstado)
            throws ErrorNoExiste, ErrorEstadoInvalido;

    // Cursos
    public DataCurso guardarCurso(DataCurso nuevoCurso) throws ErrorRepetidos;

    public HashSet<DataCurso> consultarTodosLosCursos();

    public HashSet<DataCurso> cursosPorInstituto(String nombreInstituto);

    public Boolean existeCurso(String nombre);

    public HashSet<DataCurso> cursosNoRelacionadosConUnProgDeFormacion(String idProgramaDeFormacion);

    public DataCurso buscarCurso(String nombre);

    // Busqueda
    public HashSet<DataCurso> buscarCursos(String query);

    public HashSet<DataProgramaFormacion> buscarProgramas(String query);

    public HashSet<DataUsuario> buscarUsuarios(String nombre, String apellido);

    public List<ResultadoBusqueda> busquedaPrincipal(String query);

    // Institutos
    public DataInstituto guardarInstituto(DataInstituto nuevoInstituto);

    public HashSet<DataInstituto> consultarTodosLosInstitutos();

    public Boolean existeInstituto(String nombre);

    public DataInstituto buscarInstituto(String nombre);

    // Usuarios
    public String[] listarUsuarios();

    public DataUsuario consultarUsuario(String nickname) throws ErrorNoExiste;

    public void modificarUsuario(String nick, String nom, String ape, LocalDate fechaNac, String rutaImg);

    public void registrarEstudiante(String nickname, String nombre, String apellido, String email, LocalDate fechaNac,
            String rutaImagen) throws ErrorRepetidos;

    public void registrarDocente(String nickname, String nombre, String apellido, String email, LocalDate fechaNac,
            String rutaImagen, String nombreInst) throws ErrorRepetidos;

    public boolean estaSiguiendoUsuario(String seguidorNick, String seguidoNick) throws ErrorNoExiste;

    public void modificarUsuario(String nick, String nom, String ape, LocalDate fechaNac, String rutaImg, String passActual, String passNueva) throws ErrorNoExiste;
    public void eliminarUsuario(String nick) throws ErrorNoExiste;
    public void registrarEstudiante(String nickname, String nombre, String apellido, String email, LocalDate fechaNac, String rutaImagen, String password) throws ErrorRepetidos;
    public void registrarDocente(String nickname, String nombre, String apellido, String email, LocalDate fechaNac, String rutaImagen, String nombreInst, String password) throws ErrorRepetidos;

    public DataUsuario iniciarSesion(String nicknameOEmail, String password) throws ErrorNoExiste;
    
    // Seguir usuarios
    public void seguirUsuario(String seguidorNick, String seguidoNick) throws ErrorNoExiste, ErrorRepetidos;
    public void dejarDeSeguirUsuario(String seguidorNick, String seguidoNick) throws ErrorNoExiste;
    public Set<String> obtenerSeguidos(String nickname) throws ErrorNoExiste;
    public Set<String> obtenerSeguidores(String nickname) throws ErrorNoExiste;
    public boolean esSeguidor(String seguidorNick, String seguidoNick);
}
