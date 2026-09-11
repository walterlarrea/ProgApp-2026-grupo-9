package com.grupo9.edext.grupo9.servidor_central.controller;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.IProgramaDeFormacion;
import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.ProgramaDeFormacionController;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.IEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EdicionCursoController;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.ICurso;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.CursoController;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.IInstituto;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.InstitutoController;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.IUsuario;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.UsuarioController;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.Instituto;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Docente;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EdicionCurso;
import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Estudiante;
import java.time.LocalDate;
import java.util.HashSet;

public class ServidorCentralController implements IServidorCentral {
    
    private final IProgramaDeFormacion progDeFormacionCtrl = new ProgramaDeFormacionController();
    private final IEdicionCurso edicionCursoCtrl = new EdicionCursoController();
    private final ICurso cursoCtrl = new CursoController();
    private final IInstituto institutoCtrl = new InstitutoController();
    private final IUsuario usuarioCtrl = new UsuarioController();
    
    
    // Step 1: Private constructor prevents instantiation from other classes
    private ServidorCentralController() {
        // Optional: Protect against reflection attacks
        if (ServidorCentral.INSTANCE != null) {
            throw new IllegalStateException("Instance already exists!");
        }
    }
    // Step 2: Inner static helper class responsible for holding the singleton instance
    // It is loaded into memory only when getInstance() is called (Lazy Loading)
    private static class ServidorCentral {
        private static final ServidorCentralController INSTANCE = new ServidorCentralController();
    }

    // Step 3: Public static method provides global access to the instance
    public static ServidorCentralController getInstance() {
        return ServidorCentral.INSTANCE;
    }
    
    @Override
    public void logStatus(){
        System.out.println("Server ACTIVE");
    }
    
    // Programas de Formación
    @Override
    public DataProgramaFormacion guardarProgramaDeFormacion(DataProgramaFormacion nuevoPrograma){
        return this.progDeFormacionCtrl.guardarNuevoProgramaDeFormacion(nuevoPrograma);
    }
    
    @Override
    public HashSet<DataProgramaFormacion> consultarTodosLosProgramas(){
        return this.progDeFormacionCtrl.todosLosProgramas();
    }

    @Override
    public Boolean existeProgramaDeFormacion(String nombre){
        return this.progDeFormacionCtrl.existeProgramaDeFormacion(nombre);
    }
    
    @Override
    public DataProgramaFormacion traerPorNombreId(String nombreId){
        return this.progDeFormacionCtrl.traerPorNombreId(nombreId);
    }

    @Override
    public Boolean agregarCursoAProgramaDeFormacion(String nombreIdPrograma, String nombreIdCurso){
        return this.progDeFormacionCtrl.agregarCursoAProgramaDeFormacion(nombreIdPrograma, nombreIdCurso);
    }
    
    // Ediciones de Cursos
    @Override
    public DataEdicionCurso guardarEdicionCurso(DataEdicionCurso nuevaEdicion){
        return this.edicionCursoCtrl.guardarNuevaEdicionCurso(nuevaEdicion);
    }
    
    @Override
    public Docente[] traerDocentes(Instituto instituto) {
        return this.edicionCursoCtrl.traerDocentes(instituto);
    }
    
    @Override
    public Estudiante[] traerEstudiantes(){
        return this.edicionCursoCtrl.traerEstudiantes();
    }
    
    @Override
    public void inscribirEstudiante(LocalDate fechaInsc, String nickname, String nombreEdi)throws ErrorRepetidos, ErrorNoExiste {
        this.edicionCursoCtrl.inscribirNuevoEstudiante(fechaInsc, nickname, nombreEdi);
    }
    
    @Override
    public DataEdicionCurso consultarUnaEdicionCurso(String nEdi) throws ErrorNoExiste{
        return this.edicionCursoCtrl.consultarEdicionCurso(nEdi);
    }
    
    @Override
    public EdicionCurso[] traerEdiciones(Curso curso){
        return this.edicionCursoCtrl.traerEdiciones(curso);
    }
    // Cursos
    @Override
    public DataCurso guardarCurso(DataCurso nuevoCurso){
        return this.cursoCtrl.guardarNuevoCurso(nuevoCurso);
    }
    
    @Override
    public HashSet<DataCurso> consultarTodosLosCursos(){
        return this.cursoCtrl.todosLosCursos();
    }

    @Override
    public Boolean existeCurso(String nombre){
        return this.cursoCtrl.existeCurso(nombre);
    }
    
    @Override
    public HashSet<DataCurso> cursosNoRelacionadosConUnProgDeFormacion(String idProgramaDeFormacion){
        return this.cursoCtrl.cursosNoRelacionadosConUnProgDeFormacion(idProgramaDeFormacion);
    }

    // Institutos
    @Override
    public DataInstituto guardarInstituto(DataInstituto nuevoInstituto){
        return this.institutoCtrl.guardarNuevoInstituto(nuevoInstituto);
    }
    
    @Override
    public HashSet<DataInstituto> consultarTodosLosInstitutos(){
        return this.institutoCtrl.todosLosInstitutos();
    }

    @Override
    public Boolean existeInstituto(String nombre){
        return this.institutoCtrl.existeInstituto(nombre);
    }
    
    // Usuarios
    @Override
    public String[] listarUsuarios() {
        return this.usuarioCtrl.listarUsuarios();
    }

    @Override
    public DataUsuario consultarUsuario(String nickname) throws ErrorNoExiste {
        return this.usuarioCtrl.consultarUsuario(nickname);
    }
    
}
