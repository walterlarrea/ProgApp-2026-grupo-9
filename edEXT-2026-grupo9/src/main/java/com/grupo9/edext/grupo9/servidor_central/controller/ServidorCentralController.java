package com.grupo9.edext.grupo9.servidor_central.controller;

import com.grupo9.edext.grupo9.dtos.DTOProgramaDeFormacion;
import com.grupo9.edext.grupo9.interfaces.IServidorCentral;

import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.IProgramaDeFormacion;
import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.ProgramaDeFormacionController;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.IEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EdicionCursoController;

import com.grupo9.edext.grupo9.servidor_central.controller.instituto.IInstituto;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.InstitutoController;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;

public class ServidorCentralController implements IServidorCentral {
    private final IProgramaDeFormacion progDeFormacion = new ProgramaDeFormacionController();
    private final IEdicionCurso edicionCurso = new EdicionCursoController();
    private final IInstituto instituto = new InstitutoController();
    
    
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
    
    @Override
    public void guardarProgramaDeFormacion(DTOProgramaDeFormacion nuevoPrograma){
        this.progDeFormacion.guardarNuevoProgramaDeFormacion(nuevoPrograma);
    }
    
    @Override
    public void guardarEdicionCurso(){
        this.edicionCurso.guardarNuevaEdicionCurso();
    }    
    
    @Override
    public void altaInstituto(String nombre) throws ErrorRepetidos {
    this.instituto.altaInstituto(nombre);
}
}
