package com.grupo9.edext.grupo9.servidor_central.controller.curso;

import com.grupo9.edext.grupo9.dtos.DTOCurso;
import java.time.LocalDate;
import java.util.HashSet;


public class CursoController implements ICurso{
    ManejadorCurso manejadorCurso = null;
    
    public CursoController(){
        this.manejadorCurso = ManejadorCurso.getInstance();
    }
    
    @Override
    public DTOCurso guardarNuevoCurso(DTOCurso curso){
        System.out.println("[SERVIDOR] Persistencia de un nuevo curso: " + curso.getNombre());
//        Date fecha = Date.from(ZonedDateTime.now().toInstant());
        LocalDate fecha = LocalDate.now();
        
        Curso nuevoCurso = new Curso(curso.getNombreInstituto(), curso.getNombre(), curso.getDescripcion(), curso.getDuracion(), curso.getCantHoras(), curso.getCantCred(), fecha, curso.getUrl());

        try{
            this.manejadorCurso.guardarNuevo(nuevoCurso);
            
            // TODO: devolver un nuevo DTO creado a partir del Curso ya guardado
            return curso;
        }catch(Exception e){
            System.out.println("[SERVIDOR] Persistencia FALLÓ al crear un nuevo curso: " + nuevoCurso.getNombreCurso());
            System.out.println(e);
        }
        return null;
    }
    
    @Override
    public HashSet<DTOCurso> todosLosCursos(){
        System.out.println("[SERVIDOR] Consulta todos los Cursos a persistencia");
        try {
            HashSet<DTOCurso> todosLosCursos = this.manejadorCurso.traerTodos();
            
            return todosLosCursos;
        } catch (Exception e) {
            System.out.println("[SERVIDOR] Persistencia FALLÓ al intentar traer todos los Cursos");
            System.out.println(e);
        }
        return null;
    }
}
