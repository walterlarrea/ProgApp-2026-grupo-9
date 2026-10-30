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

    /*@Test
    public void testAltaYConsultarEdicionCurso() {
        String nombreInst = "InstEdicion_" + System.currentTimeMillis();
        DataInstituto inst = institutoController.guardarNuevoInstituto(new DataInstituto(nombreInst));

        String nombreCurso = "CursoEdicion_" + System.currentTimeMillis();
        DataCurso curso = cursoController.guardarNuevoCurso(new DataCurso(
                inst, nombreCurso, "Desc", 6, 40, 4, LocalDate.now(), "http://test.com", new HashSet<>(), new HashSet<>(), null
        ));

        String nombreEdicion = "EdicionTest_" + System.currentTimeMillis();
        DataEdicionCurso nuevaEdicion = new DataEdicionCurso(
                nombreEdicion, curso, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 7, 1),
                25, new HashSet<>(), new HashSet<>(), LocalDate.now(), null
        );

        assertDoesNotThrow(() -> {
            DataEdicionCurso res = edicionController.guardarNuevaEdicionCurso(nuevaEdicion);
            assertNotNull(res);
            assertEquals(nombreEdicion, res.getNombreEdi());

            DataEdicionCurso consultada = edicionController.consultarEdicionCurso(nombreEdicion);
            assertNotNull(consultada);
            assertEquals(nombreEdicion, consultada.getNombreEdi());
            assertEquals(25, consultada.getCupo());
        });
    }*/

    /*@Test
    public void testInscripcionEstudianteExitosaYDuplicada() {
        String nombreInst = "InstInsc_" + System.currentTimeMillis();
        DataInstituto inst = institutoController.guardarNuevoInstituto(new DataInstituto(nombreInst));

        String nombreCurso = "CursoInsc_" + System.currentTimeMillis();
        DataCurso curso = cursoController.guardarNuevoCurso(new DataCurso(
                inst, nombreCurso, "Desc", 6, 40, 4, LocalDate.now(), "http://test.com", new HashSet<>(), new HashSet<>(), null
        ));

        String nombreEdicion = "EdInsc_" + System.currentTimeMillis();
        edicionController.guardarNuevaEdicionCurso(new DataEdicionCurso(
                nombreEdicion, curso, LocalDate.now(), LocalDate.now().plusMonths(3), 30, new HashSet<>(), new HashSet<>(), LocalDate.now(), null
        ));

        String nickEst = "estInsc_" + System.currentTimeMillis();
        assertDoesNotThrow(() -> {
            usuarioController.registrarEstudiante(nickEst, "Alumno", "Inscripto", "estinsc_" + System.currentTimeMillis() + "@test.com", LocalDate.of(2001, 1, 1), null, "1234");
            edicionController.inscribirNuevoEstudiante(LocalDate.now(), nickEst, nombreEdicion);
        });

        assertThrows(ErrorRepetidos.class, () -> {
            edicionController.inscribirNuevoEstudiante(LocalDate.now(), nickEst, nombreEdicion);
        });
    }*/

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

    /*@Test
    public void testInscripcionEstudianteInexistente() {
        String edicionInexistente = "EdInexistente_" + System.currentTimeMillis();
        String estudianteInexistente = "EstInexistente_" + System.currentTimeMillis();

        assertThrows(ErrorNoExiste.class, () -> {
            edicionController.inscribirNuevoEstudiante(LocalDate.now(), estudianteInexistente, edicionInexistente);
        });
    }*/

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
}
