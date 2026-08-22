package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;

import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EdicionCurso;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityManagerFactory;
//import jakarta.persistence.EntityTransaction;
//import jakarta.persistence.Persistence;

public class ManejadorEdiciones {
    private Map<String, EdicionCurso> edCurso;
    private static ManejadorEdiciones instance = null;
    
    private ManejadorEdiciones(){
        edCurso = new HashMap<String, EdicionCurso>();
    }
    
    public static ManejadorEdiciones getInstance() {
        if (instance == null)
            instance = new ManejadorEdiciones();
        return instance;
    }
    
    public void addEdicion(EdicionCurso ed) {
        String nombreEC = ed.getNombreEdi();
        edCurso.put(nombreEC, ed);
        
        /* Esto seria cuando usemos JPA
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SwingDemoPU");
        em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        try{
            t.begin();
            em.persist(ed);
            t.commit();
            
        }
        catch(Exception e){
            t.rollback();    
        }
        em.close();*/
    }
    
    public EdicionCurso obtenerEdicion(String nombreEC){
        return ((EdicionCurso)edCurso.get(nombreEC));
    }
    
    public EdicionCurso[] getEdiciones(){
        if(edCurso.isEmpty()){
            return null;
        }
        else{
            Collection<EdicionCurso> edc = edCurso.values();
            Object[] obj = edc.toArray();
            EdicionCurso[] ediciones = new EdicionCurso[obj.length];
            for (int i = 0; i < obj.length; i++) {
                ediciones[i] = (EdicionCurso) obj[i];
            }
            return ediciones;
        }
    }
}
