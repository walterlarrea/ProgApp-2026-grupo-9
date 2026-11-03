package com.grupo9.edext.grupo9.servidor_central.controller.curso;

import com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA;
import com.grupo9.edext.grupo9.servidor_central.controller.DataRecordMapper;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EstadoInscripcion;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.Instituto;
import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.ProgramaDeFormacion;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Estudiante;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.mensajes.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManejadorCurso {
    private static ManejadorCurso instancia = null;
    
    private EntityManager em;
    
    private ManejadorCurso() {
        EntityManagerFactory emf = UtensiliosJPA.getEntityManagerFactory();
        em = emf.createEntityManager();
    }
    
    public static ManejadorCurso getInstance() {
        if (instancia == null){
            instancia = new ManejadorCurso();
        }
        return instancia;
    }
    
    public void guardarNuevo(Curso curso) throws ErrorRepetidos {
        if (existeCurso(curso.getNombreCurso())) {
            throw new ErrorRepetidos("Ya existe un curso con ese nombre.");
        }

        EntityTransaction et = em.getTransaction();

        try {
            et.begin();
            if (curso.getCategorias() != null && !curso.getCategorias().isEmpty()) {
                Set<Categoria> managedCategorias = new HashSet<>();
                for (Categoria cat : curso.getCategorias()) {
                    Categoria existente = em.find(Categoria.class, cat.getNombreCategoria());
                    if (existente == null) {
                        em.persist(cat);
                        managedCategorias.add(cat);
                    } else {
                        managedCategorias.add(existente);
                    }
                }
                curso.setCategorias(managedCategorias);
            }
            em.persist(curso);
            et.commit();
        } catch (Exception e) {
            if (et.isActive()) {
                et.rollback();
            }
            throw e;
        }
    }
    
    public HashSet<DataCurso> traerTodos(){
        try {
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Curso> cQuery = cBuilder.createQuery(Curso.class);

            Root<Curso> rootEntry = cQuery.from(Curso.class);

            CriteriaQuery<Curso> todo = cQuery.select(rootEntry);

            TypedQuery<Curso> queryTodo = em.createQuery(todo);
                        
            HashSet<DataCurso> cursos = DataRecordMapper.toDataList(new HashSet<>(queryTodo.getResultList()), DataCurso.class);
            
            return cursos;
        }catch(Exception e){
            throw e;
        }
    }

    public Boolean existeCurso(String nombre){
        try {
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Curso> cQuery = cBuilder.createQuery(Curso.class);
            Root<Curso> rootEntry = cQuery.from(Curso.class);

            cQuery.select(rootEntry).where(cBuilder.equal(rootEntry.get("nombreCurso"), nombre));

            TypedQuery<Curso> query = em.createQuery(cQuery);
            query.setMaxResults(1);
            return !query.getResultList().isEmpty();
        } catch (Exception e) {
            throw e;
        }
    }

    public HashSet<DataCurso> traerCursosPorInstituto(String nombreInstituto){
        try {
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Curso> cQuery = cBuilder.createQuery(Curso.class);
            Root<Curso> rootEntry = cQuery.from(Curso.class);
            Join<Curso, Instituto> instituto = rootEntry.join("instituto");

            cQuery.select(rootEntry).where(cBuilder.equal(instituto.get("nombreI"), nombreInstituto));

            TypedQuery<Curso> query = em.createQuery(cQuery);
            return DataRecordMapper.toDataList(new HashSet<>(query.getResultList()), DataCurso.class);
        } catch (Exception e) {
            throw e;
        }
    }

    public HashSet<DataCurso> traerCursosNoRelacionadosConUnProgDeFormacion(String idProgramaDeFormacion){
        try {
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Curso> cQuery = cBuilder.createQuery(Curso.class);

            Root<Curso> rootEntry = cQuery.from(Curso.class);

            Subquery<Curso> cursosRelacionados = cQuery.subquery(Curso.class);
            Root<ProgramaDeFormacion> programa = cursosRelacionados.from(ProgramaDeFormacion.class);
            Join<ProgramaDeFormacion, Curso> cursoRelacionado = programa.join("cursos");
            cursosRelacionados.select(cursoRelacionado).where(cBuilder.equal(programa.get("nombre"), idProgramaDeFormacion));

            CriteriaQuery<Curso> todo = cQuery.select(rootEntry).where(cBuilder.not(rootEntry.in(cursosRelacionados)));

            TypedQuery<Curso> queryTodo = em.createQuery(todo);
                        
            HashSet<DataCurso> cursos = DataRecordMapper.toDataList(new HashSet<>(queryTodo.getResultList()), DataCurso.class);
            
            return cursos;
        }catch(Exception e){
            throw e;
        }
    }
    
    public HashSet<Curso> traerTodosEntidades() {
        try {
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Curso> cQuery = cBuilder.createQuery(Curso.class);
            Root<Curso> rootEntry = cQuery.from(Curso.class);
            cQuery.select(rootEntry);
            TypedQuery<Curso> queryTodo = em.createQuery(cQuery);
            HashSet<Curso> cursos = new HashSet<>();
        
            for (Curso curso : queryTodo.getResultList()) {
                cursos.add(curso);
            }
            return cursos;
        } catch (Exception e) {
            throw e;
        }
    }
    
    public DataCurso traerCursoPorNombre(String nombre){
        try {
            CriteriaBuilder cBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Curso> cQuery = cBuilder.createQuery(Curso.class);
            Root<Curso> rootEntry = cQuery.from(Curso.class);
            cQuery.select(rootEntry).where(cBuilder.equal(rootEntry.get("nombreCurso"), nombre));
            TypedQuery<Curso> query = em.createQuery(cQuery);
            query.setMaxResults(1);

            if (query.getResultList().isEmpty()) {
                return null;
            }
            Curso curso = query.getSingleResult();
            return DataRecordMapper.toDataList(new HashSet<>(Set.of(curso)), DataCurso.class).iterator().next();
        } catch (Exception e) {
            throw e;
        }
    }

    public List<DataCurso> cursosParticipaEstudiante(String nicknameEstudiante) {
        if (nicknameEstudiante == null || nicknameEstudiante.isBlank()) {
            return Collections.emptyList();
        }
        EntityManager emLocal = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT DISTINCT c FROM Curso c " +
                          "JOIN c.ediciones e " +
                          "JOIN e.inscripciones i " +
                          "WHERE LOWER(i.estudiante.nickname) = LOWER(:nickname) " +
                          "  AND i.estado = :estado";
            List<Curso> cursos = emLocal.createQuery(jpql, Curso.class)
                    .setParameter("nickname", nicknameEstudiante)
                    .setParameter("estado", EstadoInscripcion.ACEPTADA)
                    .getResultList();

            List<DataCurso> resultado = new ArrayList<>();
            for (Curso c : cursos) {
                if (c.getValoraciones() != null) {
                    c.getValoraciones().size();
                }
                resultado.add(DataRecordMapper.toData(c, nicknameEstudiante));
            }
            return resultado;
        } finally {
            emLocal.close();
        }
    }

    public void valorarCurso(String nombreCurso, String nicknameEstudiante, int puntaje) throws ErrorNoExiste, ErrorRepetidos {
        if (puntaje < 1 || puntaje > 5) {
            throw new IllegalArgumentException("El puntaje debe estar comprendido entre 1 y 5.");
        }
        if (nicknameEstudiante == null || nicknameEstudiante.isBlank()) {
            throw new ErrorNoExiste("Nickname de estudiante inválido.");
        }
        if (nombreCurso == null || nombreCurso.isBlank()) {
            throw new ErrorNoExiste("Nombre de curso inválido.");
        }

        EntityManager emLocal = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = emLocal.getTransaction();
        try {
            et.begin();
            Estudiante estudiante = emLocal.find(Estudiante.class, nicknameEstudiante);
            if (estudiante == null) {
                throw new ErrorNoExiste("No existe el estudiante con nickname: " + nicknameEstudiante);
            }
            Curso curso = emLocal.find(Curso.class, nombreCurso);
            if (curso == null) {
                throw new ErrorNoExiste("No existe el curso con nombre: " + nombreCurso);
            }

            // Verificar que el estudiante participe en el curso (inscripción ACEPTADA en alguna edición)
            String jpqlParticipa = "SELECT COUNT(i) FROM InscEdicion i " +
                    "WHERE LOWER(i.estudiante.nickname) = LOWER(:nickname) " +
                    "  AND i.edicion.cursoAsoc.nombreCurso = :nombreCurso " +
                    "  AND i.estado = :estado";
            Long cantParticipaciones = emLocal.createQuery(jpqlParticipa, Long.class)
                    .setParameter("nickname", nicknameEstudiante)
                    .setParameter("nombreCurso", nombreCurso)
                    .setParameter("estado", EstadoInscripcion.ACEPTADA)
                    .getSingleResult();
            if (cantParticipaciones == null || cantParticipaciones == 0) {
                throw new IllegalStateException("El estudiante no participa en el curso " + nombreCurso + " con inscripción aceptada.");
            }

            // Verificar si el estudiante ya valoró este curso
            String jpqlYaValoro = "SELECT COUNT(v) FROM Valoracion v " +
                    "WHERE LOWER(v.estudiante.nickname) = LOWER(:nickname) " +
                    "  AND v.curso.nombreCurso = :nombreCurso";
            Long cantValoraciones = emLocal.createQuery(jpqlYaValoro, Long.class)
                    .setParameter("nickname", nicknameEstudiante)
                    .setParameter("nombreCurso", nombreCurso)
                    .getSingleResult();
            if (cantValoraciones != null && cantValoraciones > 0) {
                throw new ErrorRepetidos("El estudiante ya ha valorado este curso. No es posible modificar la valoración.");
            }

            Valoracion valoracion = new Valoracion(puntaje, curso, estudiante);
            emLocal.persist(valoracion);
            if (curso.getValoraciones() != null) {
                curso.getValoraciones().add(valoracion);
            }
            et.commit();
        } catch (Exception e) {
            if (et.isActive()) {
                et.rollback();
            }
            throw e;
        } finally {
            emLocal.close();
        }
    }
}
