package com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion;

import com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA;
import com.grupo9.edext.grupo9.servidor_central.controller.DtoMapper;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
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
            
            HashSet<DataProgramaFormacion> programas = new HashSet();
            for(ProgramaDeFormacion programa: queryTodo.getResultList()){
                programas.add(DtoMapper.ProgramadeFormToDTOProgramadeForm(programa));
            }
            return programas;
        }catch(Exception e){
            throw e;
        }
    }    
}
