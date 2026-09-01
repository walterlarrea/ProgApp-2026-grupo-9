package com.grupo9.edext.grupo9.servidor_central.controller.instituto;

import com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA;
import com.grupo9.edext.grupo9.servidor_central.controller.DtoMapper;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.HashSet;


public class ManejadorInstituto {
    private static ManejadorInstituto instancia = null;
    
    private EntityManager em;
    
    private ManejadorInstituto() {
        EntityManagerFactory emf = UtensiliosJPA.getEntityManagerFactory();
        em = emf.createEntityManager();
    }
    
    public static ManejadorInstituto getInstance() {
        if (instancia == null) {
            instancia = new ManejadorInstituto();
        }
        return instancia;
    }
    
    public void guardarNuevo(Instituto instituto) {
        try {
            EntityTransaction et = em.getTransaction();
            try{
                et.begin();
                em.persist(instituto);
                et.commit();
            }catch(Exception e){
                et.rollback();
            }
        }catch(Exception e){
            throw e;
        }
    }
    
    public HashSet<DataInstituto> traerTodos(){
        try {
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Instituto> cQuery = cBuilder.createQuery(Instituto.class);

            Root<Instituto> rootEntry = cQuery.from(Instituto.class);

            CriteriaQuery<Instituto> todo = cQuery.select(rootEntry);

            TypedQuery<Instituto> queryTodo = em.createQuery(todo);
            
            HashSet<DataInstituto> institutos = new HashSet();
            for(Instituto instituto: queryTodo.getResultList()){
                institutos.add(DtoMapper.toData(instituto));
            }
            return institutos;
        }catch(Exception e){
            throw e;
        }
    }
}
