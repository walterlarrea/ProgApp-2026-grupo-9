
package com.grupo9.edext.grupo9.servidor_central.controller.curso;

import com.grupo9.edext.grupo9.dtos.DTOCurso;
import com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA;
import static com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA.getEntityManagerFactory;
import com.grupo9.edext.grupo9.servidor_central.controller.DtoMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.HashSet;


public class ManejadorCurso {
    private static ManejadorCurso instancia = null;
    
    private EntityManager em;
    
    private ManejadorCurso() {
        EntityManagerFactory emf = UtensiliosJPA.getEntityManagerFactory();
        em = emf.createEntityManager();
    }
    
    public static ManejadorCurso getInstance() {
        if (instancia == null){
            instancia = new ManejadorCurso();
        }
        return instancia;
    }
    
    public void guardarNuevo(Curso curso) {
        try {
            EntityTransaction et = em.getTransaction();
            try{
                et.begin();
                em.persist(curso);
                et.commit();
            }catch(Exception e){
                et.rollback();
            }
        }catch(Exception e){
            throw e;
        }
    }
    
    public HashSet<DTOCurso> traerTodos(){
        try {
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Curso> cQuery = cBuilder.createQuery(Curso.class);

            Root<Curso> rootEntry = cQuery.from(Curso.class);

            CriteriaQuery<Curso> todo = cQuery.select(rootEntry);

            TypedQuery<Curso> queryTodo = em.createQuery(todo);
            
            HashSet<DTOCurso> cursos = new HashSet();
            for(Curso curso: queryTodo.getResultList()){
                cursos.add(DtoMapper.CursoToDTOCurso(curso));
            }
            return cursos;
        }catch(Exception e){
            throw e;
        }
    }
}
