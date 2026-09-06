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
            if (et.isActive()) et.rollback();
            e.printStackTrace();
        }
        em.close();
    }

    public Docente obtenerDocente(String nickDoc){
    if (Docente.containsKey(nickDoc)) {
        return Docente.get(nickDoc);
    }
    EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
    try {
        Docente doc = em.find(Docente.class, nickDoc);
        if (doc != null) {
            // ESTA ES LA LÍNEA CLAVE: Obliga a cargar las inscripciones antes de cerrar la sesión
            if(doc.getEdiciones() != null) {
                doc.getEdiciones().size(); 
            }
            Docente.put(nickDoc, doc);
        }
        return doc;
    } catch (Exception e) {
        return null;
    } finally {
        em.close();
    }
}

    public Docente[] getDocente(){
        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        try {
            java.util.List<Docente> lista = em.createQuery("SELECT d FROM Docente d", Docente.class).getResultList();
            for (Docente d : lista) {
                // Fuerza la inicialización antes de guardarlo en el mapa
                if (d.getEdiciones() != null) {
                    d.getEdiciones().size();
                }
                Docente.put(d.getNickname(), d);
            }
        } catch (Exception e) {
            // Manejo de error silencioso
        } finally {
            em.close();
        }
        if(Docente.isEmpty()){
            return null;
        }
        else{
            Collection<Docente> est = Docente.values();
            return est.toArray(new Docente[0]);
        }
    }
}
