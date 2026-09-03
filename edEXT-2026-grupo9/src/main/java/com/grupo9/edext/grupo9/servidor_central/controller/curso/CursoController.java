package com.grupo9.edext.grupo9.servidor_central.controller.curso;

import com.grupo9.edext.grupo9.servidor_central.controller.DtoMapper;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import java.time.LocalDate;
import java.util.HashSet;


public class CursoController implements ICurso{
    ManejadorCurso manejadorCurso = null;
    
    public CursoController(){
        this.manejadorCurso = ManejadorCurso.getInstance();
    }
    
    @Override
    public DataCurso guardarNuevoCurso(DataCurso curso){
        System.out.println("[SERVIDOR] Persistencia de un nuevo Curso: " + curso.nombreCurso());
        LocalDate fecha = LocalDate.now();
        
        DataInstituto dataInstituto = curso.instituto();
        
        Curso nuevoCurso = new Curso(DtoMapper.toEntity(dataInstituto), curso.nombreCurso(), curso.descCurso(), curso.duracion(), curso.cantHoras(), curso.cantCred(), fecha, curso.url());

        try{
            this.manejadorCurso.guardarNuevo(nuevoCurso);
            
            // TODO: devolver un nuevo DTO creado a partir del Curso ya guardado
            return curso;
        }catch(Exception e){
            System.out.println("[SERVIDOR] Persistencia FALLÓ al crear un nuevo Curso: " + nuevoCurso.getNombreCurso());
            System.out.println(e);
        }
        return null;
    }
    
    @Override
    public HashSet<DataCurso> todosLosCursos(){
        System.out.println("[SERVIDOR] Consulta todos los Cursos a persistencia");
        try {
            HashSet<DataCurso> todosLosCursos = this.manejadorCurso.traerTodos();
            
            return todosLosCursos;
        } catch (Exception e) {
            System.out.println("[SERVIDOR] Persistencia FALLÓ al intentar traer todos los Cursos");
            System.out.println(e);
        }
        return null;
    }
}
