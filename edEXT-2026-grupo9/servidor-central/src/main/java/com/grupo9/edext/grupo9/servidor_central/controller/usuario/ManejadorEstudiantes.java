package com.grupo9.edext.grupo9.servidor_central.controller.usuario;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import jakarta.persistence.*;
import com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA;

public class ManejadorEstudiantes {
    private Map<String, Estudiante> estudiante;
    private static ManejadorEstudiantes instance = null;
    
    private ManejadorEstudiantes(){
        estudiante = new HashMap<String, Estudiante>();
    }
    
    public static ManejadorEstudiantes getInstance(){
        if(instance == null)
            instance = new ManejadorEstudiantes();
        return instance;
    }
    
    public void addEstudiante(Estudiante est){
        String nickEst = est.getNickname();
        estudiante.put(nickEst, est);
        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();//
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            em.persist(est);
            et.commit();
        }
        catch(Exception e){
            if (et.isActive()) et.rollback();
            e.printStackTrace();
        }
        em.close();
    }
    
    public Estudiante obtenerEstudiante(String nickEst){
    if (estudiante.containsKey(nickEst)) {
        return estudiante.get(nickEst);
    }
    EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
    try {
        Estudiante est = em.find(Estudiante.class, nickEst);
        if (est != null) {
            // ESTA ES LA LÍNEA CLAVE: Obliga a cargar las inscripciones antes de cerrar la sesión
            if(est.getInscripciones() != null) {
                est.getInscripciones().size(); 
            }
            estudiante.put(nickEst, est);   
        }
        return est;
    } catch (Exception e) {
        return null;
    } finally {
        em.close();
    }
}
    
    public Estudiante[] getEstudiante(){
        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        try {
            java.util.List<Estudiante> lista = em.createQuery("SELECT e FROM Estudiante e", Estudiante.class).getResultList();
            for (Estudiante e : lista) {
                // Fuerza la inicialización antes de guardarlo en el mapa
                if (e.getInscripciones() != null) {
                    e.getInscripciones().size();
                }
                estudiante.put(e.getNickname(), e);
            }
        } catch (Exception e) {
            // Manejo de error silencioso
        } finally {
            em.close();
        }

        if(estudiante.isEmpty()){
            return null;
        } else {
            Collection<Estudiante> est = estudiante.values();
            return est.toArray(new Estudiante[0]);
        }
    }

    public void removerEstudiante(String nickEst) {
        if (nickEst != null) {
            estudiante.remove(nickEst);
        }
    }

    public void addEstudianteDirect(Estudiante est) {
        if (est != null) {
            estudiante.put(est.getNickname(), est);
        }
    }
}
