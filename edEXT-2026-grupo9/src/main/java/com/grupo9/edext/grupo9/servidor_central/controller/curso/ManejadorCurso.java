
package com.grupo9.edext.grupo9.servidor_central.controller.curso;

import com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA;
import com.grupo9.edext.grupo9.servidor_central.controller.DtoMapper;
import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.ProgramaDeFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.util.HashSet;
import java.util.Set;


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
                if (et.isActive()) {
                    et.rollback();
                }
                throw e;
            }
        }catch(Exception e){
            throw e;
        }
    }
    
    public HashSet<DataCurso> traerTodos(){
        try {
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Curso> cQuery = cBuilder.createQuery(Curso.class);

            Root<Curso> rootEntry = cQuery.from(Curso.class);

            CriteriaQuery<Curso> todo = cQuery.select(rootEntry);

            TypedQuery<Curso> queryTodo = em.createQuery(todo);
                        
            HashSet<DataCurso> cursos = DtoMapper.toDataList(new HashSet<>(queryTodo.getResultList()), DataCurso.class);
            
            return cursos;
        }catch(Exception e){
            throw e;
        }
    }

    public Boolean existeCurso(String nombre){
        try {
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Curso> cQuery = cBuilder.createQuery(Curso.class);
            Root<Curso> rootEntry = cQuery.from(Curso.class);

            cQuery.select(rootEntry)
                .where(cBuilder.equal(rootEntry.get("nombreCurso"), nombre));

            TypedQuery<Curso> query = em.createQuery(cQuery);
            query.setMaxResults(1);
            return !query.getResultList().isEmpty();
        } catch (Exception e) {
            throw e;
        }
    }

    public HashSet<DataCurso> traerCursosNoRelacionadosConUnProgDeFormacion(String idProgramaDeFormacion){
        try {
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Curso> cQuery = cBuilder.createQuery(Curso.class);

            Root<Curso> rootEntry = cQuery.from(Curso.class);

            Subquery<Curso> cursosRelacionados = cQuery.subquery(Curso.class);
            Root<ProgramaDeFormacion> programa = cursosRelacionados.from(ProgramaDeFormacion.class);
            Join<ProgramaDeFormacion, Curso> cursoRelacionado = programa.join("cursos");
            cursosRelacionados
                .select(cursoRelacionado)
                .where(cBuilder.equal(programa.get("nombre"), idProgramaDeFormacion));

            CriteriaQuery<Curso> todo = cQuery
                .select(rootEntry)
                .where(cBuilder.not(rootEntry.in(cursosRelacionados)));

            TypedQuery<Curso> queryTodo = em.createQuery(todo);
                        
            HashSet<DataCurso> cursos = DtoMapper.toDataList(new HashSet<>(queryTodo.getResultList()), DataCurso.class);
            
            return cursos;
        }catch(Exception e){
            throw e;
        }
    }
    
    public HashSet<Curso> traerTodosEntidades() {
        try {
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Curso> cQuery = cBuilder.createQuery(Curso.class);
            Root<Curso> rootEntry = cQuery.from(Curso.class);
            cQuery.select(rootEntry);
            TypedQuery<Curso> queryTodo = em.createQuery(cQuery);
            HashSet<Curso> cursos = new HashSet<>();
        
            for (Curso curso : queryTodo.getResultList()) {
                cursos.add(curso);
            }
            return cursos;
        } catch (Exception e) {
        throw e;
        }
    }
}
