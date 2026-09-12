package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;

import com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import jakarta.persistence.*;
import java.util.HashSet;


public class ManejadorEdiciones {
    private Map<String, EdicionCurso> edCurso;
    private static ManejadorEdiciones instance = null;
    
    private ManejadorEdiciones(){
        edCurso = new HashMap<String, EdicionCurso>();
        cargarEdicionesDesdeBD();
    }
    
    private void cargarEdicionesDesdeBD() {
    EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
    try {
        List<EdicionCurso> lista = em.createQuery("SELECT edc FROM EdicionCurso edc", EdicionCurso.class).getResultList();
        for (EdicionCurso edC : lista) {
            //si necesitas asegurar que las inscripciones, previas y el docente vengan cargados en memoria:
            if (edC.getInscripciones() != null) {
                edC.getInscripciones().size();
            }
            if (edC.getDocentes() != null) {
                edC.getDocentes().size(); //fuerza la carga si es un proxy de Hibernate
            }
            if (edC.getCursoAsoc() != null) {
                edC.getCursoAsoc().getPrevias().size();
            }
            edCurso.put(edC.getNombreEdi(), edC);
        }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    
    public static ManejadorEdiciones getInstance(){
        if(instance == null)
            instance = new ManejadorEdiciones();
        return instance;
    }
    
    public void addEdicion(EdicionCurso ed){
        String nombreEC = ed.getNombreEdi();
        edCurso.put(nombreEC, ed);
        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            em.persist(ed);
            et.commit();  
        }catch(Exception e){
            if (et.isActive()) et.rollback();
            e.printStackTrace();    
        }
        em.close();
    }
    
    public EdicionCurso obtenerEdicion(String nombreEC){
        return ((EdicionCurso)edCurso.get(nombreEC));
    }
    
    public EdicionCurso[] getEdiciones(Curso curso){
        System.out.println("Cantidad de ediciones en el Map: " + edCurso.size());
        Collection<EdicionCurso> todas = edCurso.values();
        Collection<EdicionCurso> filtradas = new java.util.ArrayList<>();

        for (EdicionCurso edicion : todas) {
            if (edicion.getCursoAsoc() != null && edicion.getCursoAsoc().getNombreCurso().equals(curso.getNombreCurso())) {
                filtradas.add(edicion);
            }
        }
        return filtradas.toArray(new EdicionCurso[0]);
    }
    
    public void addInscripcion(InscEdicion ie){
//        String nombreEC = ed.getNombreEdi();
//        edCurso.put(nombreEC, ed);
        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            em.persist(ie);
            et.commit();  
        }catch(Exception e){
            if (et.isActive()) et.rollback();
            e.printStackTrace();    
        }
        em.close();
    }
}
