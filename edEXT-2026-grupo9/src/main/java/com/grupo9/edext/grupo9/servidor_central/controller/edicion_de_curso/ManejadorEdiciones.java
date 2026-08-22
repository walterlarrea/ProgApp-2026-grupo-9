package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ManejadorEdiciones {
    private Map<String, DataEdicionCurso> edCurso;
    private static ManejadorEdiciones instance = null;
    
    private ManejadorEdiciones(){
        edCurso = new HashMap<String, DataEdicionCurso>();
    }
    
    public static ManejadorEdiciones getInstance() {
        if (instance == null)
            instance = new ManejadorEdiciones();
        return instance;
    }
    
    public void addEdicion(DataEdicionCurso ed) {
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
    
    public DataEdicionCurso obtenerEdicion(String nombreEC){
        return ((DataEdicionCurso)edCurso.get(nombreEC));
    }
    
    public DataEdicionCurso[] getEdiciones(){
        if(edCurso.isEmpty()){
            return null;
        }
        else{
            Collection<DataEdicionCurso> edc = edCurso.values();
            Object[] obj = edc.toArray();
            DataEdicionCurso[] ediciones = new DataEdicionCurso[obj.length];
            for (int i = 0; i < obj.length; i++) {
                ediciones[i] = (DataEdicionCurso) obj[i];
            }
            return ediciones;
        }
    }
}
