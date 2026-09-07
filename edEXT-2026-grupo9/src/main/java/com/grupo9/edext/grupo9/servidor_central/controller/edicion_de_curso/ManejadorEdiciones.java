package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;

import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA;

public class ManejadorEdiciones {
    private Map<String, EdicionCurso> edCurso;
    private static ManejadorEdiciones instance = null;
    
    private ManejadorEdiciones(){
        edCurso = new HashMap<String, EdicionCurso>();
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
            et.rollback();    
        }
        em.close();
    }
    
    public EdicionCurso obtenerEdicion(String nombreEC){
        return ((EdicionCurso)edCurso.get(nombreEC));
    }
    
    public EdicionCurso[] getEdiciones(){
        System.out.println("Cantidad de ediciones en el Map: " + edCurso.size());
        Collection<EdicionCurso> ed = edCurso.values();
        return ed.toArray(new EdicionCurso[0]);
    }
}
