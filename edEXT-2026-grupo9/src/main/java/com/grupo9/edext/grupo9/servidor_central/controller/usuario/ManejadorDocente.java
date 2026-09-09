package com.grupo9.edext.grupo9.servidor_central.controller.usuario;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import jakarta.persistence.*;
import com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA;
import java.util.List;

public class ManejadorDocente {
    private Map<String, Docente> Docente;
    private static ManejadorDocente instance = null;

    private ManejadorDocente(){
        Docente = new HashMap<>();
        cargarDocentesDesdeBD();
    }
    
    private void cargarDocentesDesdeBD() {
        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        try {
            List<Docente> lista = em.createQuery("SELECT d FROM Docente d", Docente.class).getResultList();
            for (Docente doc : lista) {
                // Opcional: inicializa colecciones si las vas a necesitar en memoria
                if(doc.getEdiciones() != null) {
                    doc.getEdiciones().size();
                }
                Docente.put(doc.getNickname(), doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
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
        System.out.println("Cantidad de docentes en el Map: " + Docente.size());
        Collection<Docente> doc = Docente.values();
        return doc.toArray(new Docente[0]);
    }
}
