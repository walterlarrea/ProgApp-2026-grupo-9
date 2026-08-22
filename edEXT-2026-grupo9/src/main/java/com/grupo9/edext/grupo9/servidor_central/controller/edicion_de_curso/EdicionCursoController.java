package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;

import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.controller.docente.Docente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;

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
    
    @Override
    public DataEdicionCurso consultarEdicionCurso(String nEdi) throws ErrorNoExiste{
        ManejadorEdiciones me = ManejadorEdiciones.getInstance();
        EdicionCurso ed = me.obtenerEdicion(nEdi);
       
        if(ed != null){
            Curso curso = ed.getCursoAsoc();
            //para que funcione DataCurso
            DataCurso datosCurAsoc = new DataCurso(curso.getNombreInst(), curso.getNombreCurso(), curso.getDescCurso(), curso.getDuracion(), curso.getCantHoras(), curso.getCantCred(), curso.getFechaReg(), curso.getUrl());
            //para que funcione DataDocente
            Set<DataDocente> datosDocentes = new HashSet<>();
        
        for (Docente docente : ed.getDocentes()) {//para obtener los datos de cada docente.
            DataDocente datosDoc = new DataDocente(docente.getNickname(),docente.getNombre(),docente.getApellido(),docente.getEmail(),docente.getFechaNac(),docente.getNombreInst());
            datosDocentes.add(datosDoc);
        }
            return new DataEdicionCurso(ed.getNombreEdi(), datosCurAsoc, ed.getFechaInicio(), ed.getFechaFin(), ed.getCupo(), datosDocentes, ed.getFechaPub()); 
        }else{
            throw new ErrorNoExiste("La edición " + nEdi + " no está registrada.");
        }
    }

    /*
    @Override
    public EdicionCurso inscripcionEdicionCurso() throws ErrorNoExiste{
        
    };*/
}
