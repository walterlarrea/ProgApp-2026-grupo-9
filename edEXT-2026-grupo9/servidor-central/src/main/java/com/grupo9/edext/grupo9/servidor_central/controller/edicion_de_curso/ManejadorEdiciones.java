package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;

import com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Docente;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Estudiante;
import com.grupo9.edext.grupo9.servidor_central.controller.DataRecordMapper;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEstudiante;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInscEdicion;
import jakarta.persistence.criteria.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import jakarta.persistence.*;
import java.util.Set;
import org.hibernate.Hibernate;

public class ManejadorEdiciones {
    private static ManejadorEdiciones instance = null;

    private ManejadorEdiciones() {
    }

    public static ManejadorEdiciones getInstance() {
        if (instance == null) {
            instance = new ManejadorEdiciones();
        }
        return instance;
    }

    public void inicializarRelaciones(EdicionCurso edicion) {
        if (edicion == null) {
            return;
        }

        if (edicion.getInscripciones() != null) {
            for (InscEdicion inscripcion : edicion.getInscripciones()) {
                if (inscripcion != null && inscripcion.getEstudiante() != null) {
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
        if (curso.getValoraciones() != null) {
            Hibernate.initialize(curso.getValoraciones());
        }
        if (curso.getPrevias() != null) {
            Hibernate.initialize(curso.getPrevias());
            for (Curso previa : curso.getPrevias()) {
                inicializarCurso(previa, cursosVisitados);
            }
        }
    }

    public void addEdicion(EdicionCurso ed) {
        if (ed == null || ed.getNombreEdi() == null) {
            return;
        }
        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            EdicionCurso existente = em.find(EdicionCurso.class, ed.getNombreEdi());
            if (existente == null) {
                if (ed.getCursoAsoc() != null && ed.getCursoAsoc().getNombreCurso() != null) {
                    Curso managedCurso = em.find(Curso.class, ed.getCursoAsoc().getNombreCurso());
                    if (managedCurso != null) {
                        ed.setCursoAsoc(managedCurso);
                    }
                }
                if (ed.getDocentes() != null && !ed.getDocentes().isEmpty()) {
                    Set<Docente> managedDocentes = new HashSet<>();
                    for (Docente doc : ed.getDocentes()) {
                        Docente managedDoc = em.find(Docente.class, doc.getNickname());
                        if (managedDoc != null) {
                            managedDocentes.add(managedDoc);
                        } else {
                            managedDocentes.add(doc);
                        }
                    }
                    ed.setDocentes(managedDocentes);
                }
                em.persist(ed);
            } else {
                em.merge(ed);
            }
            et.commit();
        } catch (Exception e) {
            if (et.isActive()) {
                et.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public EdicionCurso obtenerEdicion(String nombreEC) {
        if (nombreEC == null) {
            return null;
        }
        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        try {
            EdicionCurso ed = em.find(EdicionCurso.class, nombreEC);
            if (ed != null) {
                inicializarRelaciones(ed);
            }
            return ed;
        } finally {
            em.close();
        }
    }

    public Collection<EdicionCurso> getTodasLasEdiciones() {
        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        try {
            List<EdicionCurso> lista = em.createQuery("SELECT edc FROM EdicionCurso edc", EdicionCurso.class).getResultList();
            for (EdicionCurso ed : lista) {
                inicializarRelaciones(ed);
            }
            return lista;
        } finally {
            em.close();
        }
    }

    public EdicionCurso[] getEdiciones(Curso curso) {
        if (curso == null || curso.getNombreCurso() == null) {
            return new EdicionCurso[0];
        }
        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<EdicionCurso> query = em.createQuery(
                "SELECT edc FROM EdicionCurso edc WHERE edc.cursoAsoc.nombreCurso = :nombreCurso",
                EdicionCurso.class
            );
            query.setParameter("nombreCurso", curso.getNombreCurso());
            List<EdicionCurso> lista = query.getResultList();
            for (EdicionCurso ed : lista) {
                inicializarRelaciones(ed);
            }
            return lista.toArray(new EdicionCurso[0]);
        } finally {
            em.close();
        }
    }

    public void addInscripcion(InscEdicion ie) {
        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(ie);
            et.commit();
        } catch (Exception e) {
            if (et.isActive()) {
                et.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void actualizarInscripcion(InscEdicion ie) {
        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.merge(ie);
            et.commit();
        } catch (Exception e) {
            if (et.isActive()) {
                et.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    public void actualizarEdicion(EdicionCurso ed) {
        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.merge(ed);
            et.commit();
        } catch (Exception e) {
            if (et.isActive()) {
                et.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    public void removerInscripcionesDeEstudiante(String nickname) {
        // Las inscripciones se eliminan directamente en la base de datos (e.g. en UsuarioController.bajaUsuario)
    }

    public void removerDocenteDeEdiciones(String nickname) {
        // Las asignaciones de docentes se eliminan directamente en la base de datos (e.g. en UsuarioController.bajaUsuario)
    }

    public DataDocente[] traerDocenteDeSuEdi(DataEdicionCurso edicion) {
        if (edicion == null) {
            return new DataDocente[0];
        }
        if (edicion.getDocentes() != null && !edicion.getDocentes().isEmpty()) {
            return edicion.getDocentes().toArray(new DataDocente[0]);
        }
        EdicionCurso ed = obtenerEdicion(edicion.getNombreEdi());
        if (ed != null && ed.getDocentes() != null) {
            return DataRecordMapper.toData(ed.getDocentes()).toArray(new DataDocente[0]);
        }
        return new DataDocente[0];
    }

    public Set<DataInscEdicion> traerInscripcionesDeEdicion(DataEdicionCurso edicion) {
        if (edicion == null) {
            return Collections.emptySet();
        }
        if (edicion.getInscripciones() != null && !edicion.getInscripciones().isEmpty()) {
            return edicion.getInscripciones();
        }
        EdicionCurso ed = obtenerEdicion(edicion.getNombreEdi());
        if (ed != null && ed.getInscripciones() != null) {
            Set<DataInscEdicion> datosInscriptos = new HashSet<>();
            for (InscEdicion inscriptos : ed.getInscripciones()) {
                Estudiante estudiante = inscriptos.getEstudiante();
                DataEstudiante datosEst = estudiante != null
                        ? new DataEstudiante(estudiante.getNickname(), estudiante.getNombre(), estudiante.getApellido(), estudiante.getEmail(), estudiante.getFechaNac(), null)
                        : null;
                DataInscEdicion datosInsc = new DataInscEdicion(inscriptos.getFechaInscE(), datosEst, inscriptos.getEdicion().getNombreEdi(), inscriptos.getEstado(), inscriptos.getNota());
                datosInscriptos.add(datosInsc);
            }
            return datosInscriptos;
        }
        return Collections.emptySet();
    }

    public Boolean existeEdicion(String nombre) {
        if (nombre == null) {
            return false;
        }
        EntityManager emConsulta = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        try {
            CriteriaBuilder cBuilder = emConsulta.getCriteriaBuilder();
            CriteriaQuery<EdicionCurso> cQuery = cBuilder.createQuery(EdicionCurso.class);
            Root<EdicionCurso> rootEntry = cQuery.from(EdicionCurso.class);

            cQuery.select(rootEntry).where(cBuilder.equal(rootEntry.get("nombreEdi"), nombre));

            TypedQuery<EdicionCurso> query = emConsulta.createQuery(cQuery);
            query.setMaxResults(1);
            return !query.getResultList().isEmpty();
        } finally {
            emConsulta.close();
        }
    }
}
