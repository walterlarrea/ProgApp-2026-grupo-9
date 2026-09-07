package com.grupo9.edext.grupo9.servidor_central.controller.usuario;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import jakarta.persistence.*;
import com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA;

public class ManejadorDocente {
    private Map<String, Docente> Docente;
    private static ManejadorDocente instance = null;

    private ManejadorDocente(){
        Docente = new HashMap<>();
    }

    public static ManejadorDocente getInstance(){
        if(instance == null)
            instance = new ManejadorDocente();
        return instance;
    }

    public void addDocente(Docente doc){
        String nickDoc = doc.getNickname();
        Docente.put(nickDoc, doc);
        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();//
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            em.persist(doc);
            et.commit();
        }
        catch(Exception e){
            et.rollback();
        }
        em.close();
    }

    public Docente obtenerDocente(String nickEst){
        return Docente.get(nickEst);
    }

    public Docente[] getDocente(){
        System.out.println("Cantidad de docentes en el Map: " + Docente.size());
        Collection<Docente> doc = Docente.values();
        return doc.toArray(new Docente[0]);
    }
}
