package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;

import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import java.time.LocalDate;
import java.util.Set;

public class EdicionCursoController implements IEdicionCurso {
    
    @Override
    public void guardarNuevaEdicionCurso(){
        System.out.println("nueva edicion de curso...");
    }
    
    @Override
    public void crearEdicionCurso(String nEdi, LocalDate fInicio, LocalDate fFin, int c, Set<DataDocente> d) throws ErrorRepetidos{
        
    };
    
    /*
    @Override
    public DataEdicionCurso consultarEdicionCurso(String nInst) throws ErrorNoExiste{
        System.out.println("asdaad");
    };
    
    @Override
    public DataEdicionCurso inscripcionEdicionCurso() throws ErrorNoExiste{
        
    };*/
}
