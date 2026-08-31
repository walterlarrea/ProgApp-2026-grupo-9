package com.grupo9.edext.grupo9.servidor_central.controller.usuario;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import jakarta.persistence.*;
import com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA;

import javax.print.Doc;

public class ManejadorDocente {
    private Map<String, Docente> Docente;
    private static ManejadorDocente instance = null;

    private ManejadorDocente(){
        Docente = new HashMap<String, Docente>();
    }

    public static ManejadorDocente getInstance(){
        if(instance == null)
            instance = new ManejadorDocente();
        return instance;
    }

    public void addDocente(Docente doc){
        String nickEst = doc.getNickname();
        Docente.put(nickEst, doc);
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
        return ((Docente) Docente.get(nickEst));
    }

    public Docente[] getDocente(){
        if(Docente.isEmpty()){
            return null;
        }
        else{
            Collection<Docente> est = Docente.values();
            Object[] obj = est.toArray();
            Docente[] docentes = new Docente[obj.length];
            for (int i = 0; i < obj.length; i++) {
                docentes[i] = (Docente) obj[i];
            }
            return docentes;
        }
    }
}
