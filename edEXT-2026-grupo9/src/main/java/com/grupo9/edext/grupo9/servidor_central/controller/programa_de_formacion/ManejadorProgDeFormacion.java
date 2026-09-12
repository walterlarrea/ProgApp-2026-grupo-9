package com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion;

import com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA;
import com.grupo9.edext.grupo9.servidor_central.controller.DtoMapper;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import java.util.HashSet;


public class ManejadorProgDeFormacion {
    private static ManejadorProgDeFormacion instancia = null;
    
    private EntityManager em;
    
    private ManejadorProgDeFormacion() {
        EntityManagerFactory emf = UtensiliosJPA.getEntityManagerFactory();
        em = emf.createEntityManager();
    }
    
    public static ManejadorProgDeFormacion getInstance() {
        if (instancia == null){
            instancia = new ManejadorProgDeFormacion();
        }
        return instancia;
    }
    
    public void guardarNuevo(ProgramaDeFormacion programa) {
        try {
            EntityTransaction et = em.getTransaction();
            try{
                et.begin();
                em.persist(programa);
                et.commit();
            }catch(Exception e){
                et.rollback();
            }
        }catch(Exception e){
            throw e;
        }
    }
    
    public HashSet<DataProgramaFormacion> traerTodos(){
        try {
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<ProgramaDeFormacion> cQuery = cBuilder.createQuery(ProgramaDeFormacion.class);

            Root<ProgramaDeFormacion> rootEntry = cQuery.from(ProgramaDeFormacion.class);

            CriteriaQuery<ProgramaDeFormacion> todo = cQuery.select(rootEntry);

            TypedQuery<ProgramaDeFormacion> queryTodo = em.createQuery(todo);
            
            HashSet<DataProgramaFormacion> programas = DtoMapper.toDataList(new HashSet<>(queryTodo.getResultList()), DataProgramaFormacion.class);
            
            return programas;
        }catch(Exception e){
            throw e;
        }
    }
    
    public DataProgramaFormacion traerPorNombreId(String nombreId){
        try {
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<ProgramaDeFormacion> cQuery = cBuilder.createQuery(ProgramaDeFormacion.class);

            Root<ProgramaDeFormacion> rootEntry = cQuery.from(ProgramaDeFormacion.class);

            CriteriaQuery<ProgramaDeFormacion> todo = cQuery
                .select(rootEntry)
                .where(cBuilder.equal(rootEntry.get("nombre"), nombreId));

            TypedQuery<ProgramaDeFormacion> queryTodo = em.createQuery(todo);
            
            ProgramaDeFormacion programa = queryTodo.getResultList().getFirst();
            return DtoMapper.toData(programa);
        }catch(Exception e){
            throw e;
        }
    }

    public HashSet<DataProgramaFormacion> traerProgramasPorCurso(String nombreCurso){
        try {
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<ProgramaDeFormacion> cQuery = cBuilder.createQuery(ProgramaDeFormacion.class);
            Root<ProgramaDeFormacion> programa = cQuery.from(ProgramaDeFormacion.class);
            Join<ProgramaDeFormacion, Curso> curso = programa.join("cursos");

            cQuery.select(programa)
                    .where(cBuilder.equal(curso.get("nombreCurso"), nombreCurso))
                    .distinct(true);

            TypedQuery<ProgramaDeFormacion> query = em.createQuery(cQuery);
            return DtoMapper.toDataList(new HashSet<>(query.getResultList()), DataProgramaFormacion.class);
        } catch (Exception e) {
            throw e;
        }
    }

    public Boolean existeProgramaDeFormacion(String nombreId){
        try {
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<ProgramaDeFormacion> cQuery = cBuilder.createQuery(ProgramaDeFormacion.class);

            Root<ProgramaDeFormacion> rootEntry = cQuery.from(ProgramaDeFormacion.class);

            CriteriaQuery<ProgramaDeFormacion> todo = cQuery
                .select(rootEntry)
                .where(cBuilder.equal(rootEntry.get("nombre"), nombreId));

            TypedQuery<ProgramaDeFormacion> queryTodo = em.createQuery(todo);
            queryTodo.setMaxResults(1);
            return !queryTodo.getResultList().isEmpty();
        }catch(Exception e){
            throw e;
        }
    }

    public Boolean agregarCursoAProgramaDeFormacion(String nombreIdPrograma, String nombreIdCurso){
        try {
            ProgramaDeFormacion programa = em.find(ProgramaDeFormacion.class, nombreIdPrograma);
            Curso curso = em.find(Curso.class, nombreIdCurso);
            if(programa != null && curso != null){
                EntityTransaction et = em.getTransaction();
                try{
                    et.begin();
                    programa.agregarCurso(curso);
                    et.commit();
                    return true;
                }catch(Exception e){
                    et.rollback();
                    return false;
                }
            }else{
                return false;
            }
        }catch(Exception e){
            throw e;
        }
    }
}
