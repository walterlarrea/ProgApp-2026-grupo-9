package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;

import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.controller.docente.Docente;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.ManejadorEdiciones;
import java.time.LocalDate;
import java.util.Set;

public class EdicionCursoController implements IEdicionCurso {
    
    public EdicionCursoController() {
        //EntityManagerFactory emf = Persistence.createEntityManagerFactory("SwingDemoPU");
        //em = emf.createEntityManager();
    }
    
    @Override
    public void guardarNuevaEdicionCurso(){
        System.out.println("nueva edicion de curso...");
    }
    
    //Cuando se implemente el GUI, ahí se agrega el modificar o cancelar altaEdicionCurso
    @Override
    public void altaEdicionCurso(String nEdi, Curso cur, LocalDate fInicio, LocalDate fFin, int c, Set<Docente> d) throws ErrorRepetidos{
        ManejadorEdiciones me = ManejadorEdiciones.getInstance();
        EdicionCurso ed = me.obtenerEdicion(nEdi);
        if(ed == null){
            ed = new EdicionCurso(nEdi, cur, fInicio,fFin, c, d, LocalDate.now());
            me.addEdicion(ed);
        }else{
            throw new ErrorRepetidos("La Edición " + nEdi + " ya ha sido registrada. \n ¿Desea modificar los datos?");
        }
    }
    
    /*
    
    @Override
    public EdicionCurso consultarEdicionCurso(String nInst) throws ErrorNoExiste{
        System.out.println("asdaad");
    };
    
    @Override
    public EdicionCurso inscripcionEdicionCurso() throws ErrorNoExiste{
        
    };*/
}
