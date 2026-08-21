package com.grupo9.edext.grupo9;

import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.IProgramaDeFormacion;
import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.ProgramaDeFormacionController;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.IEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EdicionCursoController;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.IUsuario;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.UsuarioController;
        
public class Fabrica {
    private static Fabrica instance;
    
    private Fabrica(){};
    
    public static Fabrica getInstance(){
        if(instance == null){
            instance = new Fabrica();
        }
        return instance;
    }
        
    public IEdicionCurso getIEdicionCurso() {
        return new EdicionCursoController();
    }
    
    public IProgramaDeFormacion getIProgDeFormacion() {
        return new ProgramaDeFormacionController();
    }
    
    public IUsuario getIUsuario() {
     return new UsuarioController();
    }
}
