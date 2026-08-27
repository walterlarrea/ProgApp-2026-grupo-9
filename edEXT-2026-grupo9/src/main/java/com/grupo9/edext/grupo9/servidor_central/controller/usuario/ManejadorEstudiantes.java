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
            et.rollback();    
        }
        em.close();
    }
    
    public Estudiante obtenerEstudiante(String nickEst){
        return ((Estudiante)estudiante.get(nickEst));
    }
    
    public Estudiante[] getEstudiante(){
        if(estudiante.isEmpty()){
            return null;
        }
        else{
            Collection<Estudiante> est = estudiante.values();
            Object[] obj = est.toArray();
            Estudiante[] estudiantes = new Estudiante[obj.length];
            for (int i = 0; i < obj.length; i++) {
                estudiantes[i] = (Estudiante) obj[i];
            }
            return estudiantes;
        }
    }
}
