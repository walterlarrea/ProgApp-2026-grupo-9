package com.grupo9.edext.grupo9.miscelanea;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class UtensiliosJPA {
    private static EntityManagerFactory emf;
    //para no crear un EntityManagerFactory nuevo cada vez que lo usemos
    public static EntityManagerFactory getEntityManagerFactory() {
        if(emf == null) {
            emf = Persistence.createEntityManagerFactory("edEXT_PU");
        }
        return emf;
    }
    
    public static <T> void save(T record) {
        try {
            EntityManager em = getEntityManagerFactory().createEntityManager();
            EntityTransaction et = em.getTransaction();
            try{
                et.begin();
                em.persist(record);
                et.commit();
            }catch(Exception e){
                et.rollback();
            }
            
            em.close();
        }catch(Exception e){
            throw e;
        }
    }
}
