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
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import java.util.HashSet;
/**
 *
 * @author Walter
 */
public class ServidorCentralController implements IServidorCentral {
    private final IProgramaDeFormacion progDeFormacionCtrl = new ProgramaDeFormacionController();
    private final IEdicionCurso edicionCursoCtrl = new EdicionCursoController();
    private final ICurso cursoCtrl = new CursoController();
    private final IInstituto institutoCtrl = new InstitutoController();
    
    
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
    
    // Ediciones de Cursos
    @Override
    public DataEdicionCurso guardarEdicionCurso(DataEdicionCurso nuevaEdicion){
        return this.edicionCursoCtrl.guardarNuevaEdicionCurso(nuevaEdicion);
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

    // Institutos
    @Override
    public DataInstituto guardarInstituto(DataInstituto nuevoInstituto){
        return this.institutoCtrl.guardarNuevoInstituto(nuevoInstituto);
    }
    
    @Override
    public HashSet<DataInstituto> consultarTodosLosInstitutos(){
        return this.institutoCtrl.todosLosInstitutos();
    }
}
