package com.grupo9.edext.grupo9.servidor_central.controller.curso;

import com.grupo9.edext.grupo9.servidor_central.controller.DtoMapper;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.Instituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


public class CursoController implements ICurso{
    ManejadorCurso manejadorCurso = null;
    
    public CursoController(){
        this.manejadorCurso = ManejadorCurso.getInstance();
    }
    
    @Override
    public DataCurso guardarNuevoCurso(DataCurso curso){
        System.out.println("[SERVIDOR] Persistencia de un nuevo Curso: " + curso.nombreCurso());
        LocalDate fechaDeCreacion = LocalDate.now();
        
        Instituto instituto = DtoMapper.toEntity(curso.instituto());
        Set<Curso> previas = DtoMapper.toEntityList(curso.previas(), Curso.class);
        
        
        Curso nuevoCurso = new Curso(instituto, curso.nombreCurso(), curso.descCurso(), curso.duracion(), curso.cantHoras(), curso.cantCred(), fechaDeCreacion, curso.url(), previas);

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
    
    @Override
    public HashSet<DataCurso> cursosNoRelacionadosConUnProgDeFormacion(String idProgramaDeFormacion){
        System.out.println("[SERVIDOR] Consulta en persistencia los Cursos que no están relacionados a un Prog de Formación específico");
        try {
            HashSet<DataCurso> cursosNoRelacionados = this.manejadorCurso.traerCursosNoRelacionadosConUnProgDeFormacion(idProgramaDeFormacion);
            
            return cursosNoRelacionados;
        } catch (Exception e) {
            System.out.println("[SERVIDOR] Persistencia FALLÓ al intentar traer los Cursos no relacionados a este Prog de Formación");
            System.out.println(e);
        }
        return null;
    }
}
