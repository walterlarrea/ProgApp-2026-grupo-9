package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;

import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
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
    
    @Override
    public void altaEdicionCurso(String nEdi, LocalDate fInicio, LocalDate fFin, int c, Set<DataDocente> d) throws ErrorRepetidos{
        ManejadorEdiciones me = ManejadorEdiciones.getInstance();
        DataEdicionCurso ed = me.obtenerEdicion(nEdi);
        /*if(ed != null){
            try{
                EdicionCursoController.altaEdicionCurso(nEdi, fInicio,fFin, c, d);
            }catch(ErrorRepetidos e){
            System.out.println("La edición del curso " + nEdi + " ya está registrada.");
            }
        //ed = new DataEdicionCurso(nEdi, cur, fInicio,fFin, c, d, fPub);
        me.addEdicion(ed);
        }*/
    }
    
    
   /*@Override
    public DataEdicionCurso consultarEdicionCurso(String nInst) throws ErrorNoExiste{
        System.out.println("asdaad");
    };
    
    @Override
    public DataEdicionCurso inscripcionEdicionCurso() throws ErrorNoExiste{
        
    };*/
}
