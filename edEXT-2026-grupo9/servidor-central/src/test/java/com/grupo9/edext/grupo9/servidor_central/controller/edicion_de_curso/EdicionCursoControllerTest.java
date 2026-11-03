package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;

import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.CursoController;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.InstitutoController;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.UsuarioController;
import com.grupo9.edext.grupo9.servidor_central.dominio.*;
import com.grupo9.edext.grupo9.servidor_central.controller.DataRecordMapper;
import com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.Categoria;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.Instituto;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Estudiante;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class EdicionCursoControllerTest {

    private EdicionCursoController edicionController;
    private CursoController cursoController;
    private InstitutoController institutoController;
    private UsuarioController usuarioController;

    @BeforeEach
    public void setUp() {
        edicionController = new EdicionCursoController();
        cursoController = new CursoController();
        institutoController = new InstitutoController();
        usuarioController = new UsuarioController();
    }

    @Test
    public void testConsultarEdicionInexistente() {
        String edicionInexistente = "EdInexistente_" + System.currentTimeMillis();
        assertThrows(ErrorNoExiste.class, () -> {
            edicionController.consultarEdicionCurso(edicionInexistente);
        });
    }

    @Test
    public void testInicializarRelacionesAntesDeMapearEdicion() {
        String sufijo = String.valueOf(System.currentTimeMillis());
        String nombreEdicion = "EdicionLazy_" + sufijo;
        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        EdicionCurso edicion;

        try {
            em.getTransaction().begin();
            Instituto instituto = new Instituto("InstitutoLazy_" + sufijo);
            Categoria categoria = new Categoria("CategoriaLazy_" + sufijo);
            Curso curso = new Curso(instituto, "CursoLazy_" + sufijo, "Desc", 1, 1, 1,
                    LocalDate.now(), null, new HashSet<>(), new HashSet<>(), null);
            curso.getCategorias().add(categoria);
            Estudiante estudiante = new Estudiante("EstudianteLazy_" + sufijo, "Nombre", "Apellido",
                    "estudiante-lazy-" + sufijo + "@test.com", LocalDate.of(2000, 1, 1), null);
            HashSet<InscEdicion> inscripciones = new HashSet<>();
            edicion = new EdicionCurso(nombreEdicion, curso, LocalDate.now(), LocalDate.now().plusDays(1),
                    10, new HashSet<>(), inscripciones, LocalDate.now(), null);
            inscripciones.add(new InscEdicion(LocalDate.now(), estudiante, edicion));

            em.persist(instituto);
            em.persist(categoria);
            em.persist(curso);
            em.persist(estudiante);
            em.persist(edicion);
            for (InscEdicion inscripcion : inscripciones) {
                em.persist(inscripcion);
            }
            em.getTransaction().commit();
            em.clear();

            edicion = em.find(EdicionCurso.class, nombreEdicion);
            ManejadorEdiciones.getInstance().inicializarRelaciones(edicion);
        } finally {
            em.close();
        }

        DataEdicionCurso datos = DataRecordMapper.toData(edicion);
        assertEquals("InstitutoLazy_" + sufijo, datos.getCursoAsoc().instituto().nombreI());
        assertTrue(datos.getCursoAsoc().categorias().contains("CategoriaLazy_" + sufijo));
        assertEquals("Nombre", datos.getInscripciones().iterator().next().getEstudiante().getNombre());
    }

    @Test
    public void testAltaEdicionDuplicadaLanzaExcepcion() {
        String nombreEdi = "EdicionDup_" + System.currentTimeMillis();
        assertDoesNotThrow(() -> {
            edicionController.altaEdicionCurso(
                    nombreEdi, null, LocalDate.now(), LocalDate.now().plusMonths(3),
                    20, new HashSet<>(), new HashSet<>(), null
            );
        });

        assertThrows(ErrorRepetidos.class, () -> {
            edicionController.altaEdicionCurso(
                    nombreEdi, null, LocalDate.now(), LocalDate.now().plusMonths(3),
                    20, new HashSet<>(), new HashSet<>(), null
            );
        });
    }

    @Test
    public void testTraerEstudiantesYDocentes() {
        assertDoesNotThrow(() -> {
            assertNotNull(edicionController.traerEstudiantes());
            DataInstituto dataInst = institutoController.guardarNuevoInstituto(new DataInstituto("InstDocentes_" + System.currentTimeMillis()));
            assertNotNull(edicionController.traerDocentes(DataRecordMapper.toEntity(dataInst)));
        });
    }

    @Test
    public void testRegistrarCalificacionYCerrarEdicion() {
        String sufijo = String.valueOf(System.currentTimeMillis());
        String nombreEdicion = "EdicionEval_" + sufijo;
        String nombreCurso = "CursoEval_" + sufijo;
        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();
            Instituto instituto = new Instituto("InstEval_" + sufijo);
            Curso curso = new Curso(instituto, nombreCurso, "Desc", 1, 1, 1,
                    LocalDate.now(), null, new HashSet<>(), new HashSet<>(), null);
            Estudiante estudiante = new Estudiante("EstEval_" + sufijo, "Nombre", "Apellido",
                    "est-eval-" + sufijo + "@test.com", LocalDate.of(2000, 1, 1), null);
            HashSet<InscEdicion> inscripciones = new HashSet<>();
            EdicionCurso edicion = new EdicionCurso(nombreEdicion, curso, LocalDate.now().minusDays(5), LocalDate.now().plusDays(10),
                    10, new HashSet<>(), inscripciones, LocalDate.now(), null, false);
            InscEdicion insc = new InscEdicion(LocalDate.now(), estudiante, edicion, EstadoInscripcion.ACEPTADA, null);
            inscripciones.add(insc);

            em.persist(instituto);
            em.persist(curso);
            em.persist(estudiante);
            em.persist(edicion);
            em.persist(insc);
            em.getTransaction().commit();

            ManejadorEdiciones.getInstance().addEdicion(edicion);
        } finally {
            em.close();
        }

        // 1. Edición vigente
        DataEdicionCurso vigente = edicionController.obtenerEdicionVigente(nombreCurso);
        assertNotNull(vigente);
        assertEquals(nombreEdicion, vigente.getNombreEdi());
        assertFalse(vigente.isCerrada());

        // 2. Validación de rango de nota
        assertThrows(IllegalArgumentException.class, () -> {
            edicionController.registrarCalificacion(nombreEdicion, "EstEval_" + sufijo, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            edicionController.registrarCalificacion(nombreEdicion, "EstEval_" + sufijo, 13);
        });

        // 3. No permite cerrar con estudiantes sin nota
        assertThrows(Exception.class, () -> {
            edicionController.cerrarEdicionCurso(nombreEdicion);
        });

        // 4. Registro exitoso
        assertDoesNotThrow(() -> {
            edicionController.registrarCalificacion(nombreEdicion, "EstEval_" + sufijo, 10);
        });

        // 5. Cierre exitoso
        assertDoesNotThrow(() -> {
            edicionController.cerrarEdicionCurso(nombreEdicion);
        });

        // 6. Posterior al cierre, no se puede modificar notas
        assertThrows(Exception.class, () -> {
            edicionController.registrarCalificacion(nombreEdicion, "EstEval_" + sufijo, 12);
        });
    }

    @Test
    public void testEdicionesConsultadasDirectamenteDesdeBD() throws Exception {
        String sufijo = String.valueOf(System.currentTimeMillis());
        String nombreEdicion = "EdicionBD_" + sufijo;
        String nombreCurso = "CursoBD_" + sufijo;
        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();
            Instituto instituto = new Instituto("InstBD_" + sufijo);
            Curso curso = new Curso(instituto, nombreCurso, "Desc", 1, 1, 1,
                    LocalDate.now(), null, new HashSet<>(), new HashSet<>(), null);
            EdicionCurso edicion = new EdicionCurso(nombreEdicion, curso, LocalDate.now(), LocalDate.now().plusMonths(1),
                    15, new HashSet<>(), new HashSet<>(), LocalDate.now(), null, false);

            em.persist(instituto);
            em.persist(curso);
            em.persist(edicion);
            em.getTransaction().commit();
        } finally {
            em.close();
        }

        // Se debe consultar directamente la BD sin necesidad de registrar previamente en memoria
        DataEdicionCurso dataEd = edicionController.consultarEdicionCurso(nombreEdicion);
        assertNotNull(dataEd);
        assertEquals(nombreEdicion, dataEd.getNombreEdi());
        assertEquals(15, dataEd.getCupo());

        // Actualizamos directamente en la base de datos
        EntityManager emUpdate = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        try {
            emUpdate.getTransaction().begin();
            EdicionCurso edBD = emUpdate.find(EdicionCurso.class, nombreEdicion);
            assertNotNull(edBD);
            edBD.setCupo(30);
            emUpdate.getTransaction().commit();
        } finally {
            emUpdate.close();
        }

        // La consulta debe reflejar inmediatamente el valor actualizado de la BD
        DataEdicionCurso dataEdActualizada = edicionController.consultarEdicionCurso(nombreEdicion);
        assertNotNull(dataEdActualizada);
        assertEquals(30, dataEdActualizada.getCupo());
    }
}
