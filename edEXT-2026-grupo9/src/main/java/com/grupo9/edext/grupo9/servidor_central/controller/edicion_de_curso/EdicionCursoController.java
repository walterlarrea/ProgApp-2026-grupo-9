package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;

import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Docente;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.ManejadorEstudiantes;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Estudiante;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInscEdicion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEstudiante;
import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;

public class EdicionCursoController implements IEdicionCurso {
    
    public EdicionCursoController(){}
    
    @Override
    public void guardarNuevaEdicionCurso(){
        System.out.println("nueva edicion de curso...");
    }
    
    //Cuando se implemente el GUI, ahí se agrega el modificar o cancelar altaEdicionCurso
    @Override
    public void altaEdicionCurso(String nEdi, Curso cur, LocalDate fInicio, LocalDate fFin, int c, Set<InscEdicion> insc, Set<Docente> d) throws ErrorRepetidos{
        ManejadorEdiciones me = ManejadorEdiciones.getInstance();
        EdicionCurso ed = me.obtenerEdicion(nEdi);
        if(ed == null){
            ed = new EdicionCurso(nEdi, cur, fInicio,fFin, c, d, insc, LocalDate.now());
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

            //para obtener los inscriptos
            Set<DataInscEdicion> datosInscriptos = new HashSet<>();
            for(InscEdicion inscriptos : new HashSet<InscEdicion>()){
                Estudiante estudiante = inscriptos.getEstudiante();
                DataEstudiante datosEst = new DataEstudiante(estudiante.getNickname(),estudiante.getNombre(),estudiante.getApellido(),estudiante.getEmail(), estudiante.getFechaNac(), null);
                DataInscEdicion datosInsc = new DataInscEdicion(inscriptos.getFechaInscE(), datosEst, inscriptos.getEdicion());
                datosInscriptos.add(datosInsc);
            }
            //para que funcione DataDocente
            Set<DataDocente> datosDocentes = new HashSet<>();
            for(Docente docente : ed.getDocentes()) {//para obtener los datos de cada docente.
                DataDocente datosDoc = new DataDocente(docente.getNickname(),docente.getNombre(),docente.getApellido(),docente.getEmail(),docente.getFechaNac(),docente.getNombreInst());
                datosDocentes.add(datosDoc);
        }
            return new DataEdicionCurso(ed.getNombreEdi(), datosCurAsoc, ed.getFechaInicio(), ed.getFechaFin(), ed.getCupo(), datosDocentes, datosInscriptos, ed.getFechaPub()); 
        }else{
            throw new ErrorNoExiste("La Edición " + nEdi + " no está registrada.");
        }
    }

    @Override
    public void inscripcionEdicionCurso(LocalDate fInsc, String nickEstudiante, String nEdi) throws ErrorRepetidos, ErrorNoExiste{
        ManejadorEdiciones me = ManejadorEdiciones.getInstance();
        ManejadorEstudiantes mest = ManejadorEstudiantes.getInstance();
        Estudiante est = mest.obtenerEstudiante(nickEstudiante);
        EdicionCurso ed = me.obtenerEdicion(nEdi);
        
        if(est == null){
            throw new ErrorRepetidos("El estudiante " + nickEstudiante + " no existe.");
        }
        if(ed != null){
            for(InscEdicion insc : est.getInscripciones()){//checkeo si ya está inscripto.
                if(insc.getEdicion().equals(ed.getNombreEdi())){
                    throw new ErrorRepetidos("El estudiante " + nickEstudiante + " ya está inscripto.");
                }
            }
            InscEdicion inscripcion = new InscEdicion(fInsc, est, ed.getNombreEdi());
            est.getInscripciones().add(inscripcion);
//            por si consultar edición de curso te muestra las inscripciones. 
//            ed.getInscripciones().add(inscripcion);   
        }else{
            throw new ErrorNoExiste("La Edición " + nEdi + " no existe.");
        }
    };
}
