package com.grupo9.edext.grupo9.miscelanea;

import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.IProgramaDeFormacion;
import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.ProgramaDeFormacionController;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.IEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EdicionCursoController;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.ICurso;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.CursoController;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.IUsuario;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.UsuarioController;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.IInstituto;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.InstitutoController;
import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.servidor_central.controller.ServidorCentralFactory;
        
public class Fabrica {
    private static Fabrica instance;
    
    private Fabrica(){};
    
    public static Fabrica getInstance(){
        if(instance == null){
            instance = new Fabrica();
        }
        return instance;
    }

    public IServidorCentral getIServidorCentral() {
        return ServidorCentralFactory.getServidorCentral();
    }
        
    public IEdicionCurso getIEdicionCurso() {
        return new EdicionCursoController();
    }
    
    public IProgramaDeFormacion getIProgDeFormacion() {
        return new ProgramaDeFormacionController();
    }
    

    public ICurso getICurso() {
        return new CursoController();
    }
    
    public IUsuario getIUsuario() {
        return new UsuarioController();
    }
    
    public IInstituto getIInstituto() {
        return new InstitutoController();
    }
}
