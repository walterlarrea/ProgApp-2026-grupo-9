package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;

import com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Docente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInscEdicion;
import jakarta.persistence.criteria.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import jakarta.persistence.*;
import java.util.Set;
import org.hibernate.Hibernate;


public class ManejadorEdiciones {
    private Map<String, EdicionCurso> edCurso;
    private static ManejadorEdiciones instance = null;
    private EntityManager em;
    
    private ManejadorEdiciones(){
        edCurso = new HashMap<String, EdicionCurso>();
        cargarEdicionesDesdeBD();
    }
    
    private void cargarEdicionesDesdeBD() {
    em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
    try {
        List<EdicionCurso> lista = em.createQuery("SELECT edc FROM EdicionCurso edc", EdicionCurso.class).getResultList();
        for (EdicionCurso edC : lista) {
            inicializarRelaciones(edC);
            edCurso.put(edC.getNombreEdi(), edC);
        }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    void inicializarRelaciones(EdicionCurso edicion) {
        if (edicion.getInscripciones() != null) {
            for (InscEdicion inscripcion : edicion.getInscripciones()) {
                if (inscripcion.getEstudiante() != null) {
                    Hibernate.initialize(inscripcion.getEstudiante());
                }
            }
        }

        if (edicion.getDocentes() != null) {
            for (Docente docente : edicion.getDocentes()) {
                if (docente != null) {
                    Hibernate.initialize(docente);
                }
            }
        }

        if (edicion.getCursoAsoc() != null) {
            inicializarCurso(edicion.getCursoAsoc(), new HashSet<>());
        }
    }

    private void inicializarCurso(Curso curso, Set<String> cursosVisitados) {
        if (curso == null || !cursosVisitados.add(curso.getNombreCurso())) {
            return;
        }

        if (curso.getInstituto() != null) {
            Hibernate.initialize(curso.getInstituto());
        }
        if (curso.getCategorias() != null) {
            Hibernate.initialize(curso.getCategorias());
        }
        if (curso.getPrevias() != null) {
            Hibernate.initialize(curso.getPrevias());
            for (Curso previa : curso.getPrevias()) {
                inicializarCurso(previa, cursosVisitados);
            }
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
        em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
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
        em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
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

    public void actualizarInscripcion(InscEdicion ie){
        em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            em.merge(ie);
            et.commit();  
        }catch(Exception e){
            if (et.isActive()) et.rollback();
            e.printStackTrace();
            throw e;
        }finally{
            em.close();
        }
    }

    public void removerInscripcionesDeEstudiante(String nickname) {
        if (nickname == null || edCurso == null) return;
        for (EdicionCurso ed : edCurso.values()) {
            if (ed.getInscripciones() != null) {
                ed.getInscripciones().removeIf(insc -> 
                    insc.getEstudiante() != null && nickname.equalsIgnoreCase(insc.getEstudiante().getNickname())
                );
            }
        }
    }

    public void removerDocenteDeEdiciones(String nickname) {
        if (nickname == null || edCurso == null) return;
        for (EdicionCurso ed : edCurso.values()) {
            if (ed.getDocentes() != null) {
                ed.getDocentes().removeIf(doc -> 
                    doc != null && nickname.equalsIgnoreCase(doc.getNickname())
                );
            }
        }
    }
    
    public DataDocente[] traerDocenteDeSuEdi(DataEdicionCurso edicion){
        try {
            Set<DataDocente> docentes = edicion.getDocentes();
            if (docentes == null || docentes.isEmpty()) {
                return new DataDocente[0];
            }
            return docentes.toArray(new DataDocente[0]);
        } catch (Exception e) {
            throw e;
        }
    }
    
    public Set<DataInscEdicion> traerInscripcionesDeEdicion(DataEdicionCurso edicion) {
        if (edicion == null || edicion.getInscripciones() == null) {
            return Collections.emptySet();
        }
        return edicion.getInscripciones();
    }
    
    public Boolean existeEdicion(String nombre) {
        CriteriaBuilder cBuilder = em.getCriteriaBuilder();
        CriteriaQuery<EdicionCurso> cQuery = cBuilder.createQuery(EdicionCurso.class);
        Root<EdicionCurso> rootEntry = cQuery.from(EdicionCurso.class);

        cQuery.select(rootEntry).where(cBuilder.equal(rootEntry.get("nombreEdi"), nombre));

        TypedQuery<EdicionCurso> query = em.createQuery(cQuery);
        query.setMaxResults(1);

        return !query.getResultList().isEmpty();
    }
}
