package com.grupo9.edext.grupo9.servidor_central.controller.curso;

import com.grupo9.edext.grupo9.servidor_central.controller.instituto.InstitutoController;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CursoControllerTest {

    private CursoController cursoController;
    private InstitutoController institutoController;

    @BeforeEach
    public void setUp() {
        cursoController = new CursoController();
        institutoController = new InstitutoController();
    }

    @Test
    public void testGuardarYExisteCurso() {
        String nombreInst = "InstCurso_" + System.currentTimeMillis();
        DataInstituto inst = institutoController.guardarNuevoInstituto(new DataInstituto(nombreInst));

        String nombreCurso = "CursoTest_" + System.currentTimeMillis();
        DataCurso nuevoCurso = new DataCurso(
                inst, nombreCurso, "Descripción de prueba", 12, 100, 10,
                LocalDate.now(), "http://test.com", new HashSet<>(), new HashSet<>(), new HashSet<>(), null);

        assertDoesNotThrow(() -> {
            DataCurso res = cursoController.guardarNuevoCurso(nuevoCurso);
            assertNotNull(res);
            assertEquals(nombreCurso, res.nombreCurso());

            Boolean existe = cursoController.existeCurso(nombreCurso);
            assertTrue(existe);
        });
    }

    @Test
    public void testTodosLosCursosYCursosPorInstituto() {
        String nombreInst = "InstCursoAll_" + System.currentTimeMillis();
        DataInstituto inst = institutoController.guardarNuevoInstituto(new DataInstituto(nombreInst));

        String nombreCurso = "CursoPorInst_" + System.currentTimeMillis();
        DataCurso nuevoCurso = new DataCurso(
                inst, nombreCurso, "Desc", 8, 40, 5,
                LocalDate.now(), "http://test.com", new HashSet<>(), new HashSet<>(), new HashSet<>(), null);

        assertDoesNotThrow(() -> {
            cursoController.guardarNuevoCurso(nuevoCurso);

            HashSet<DataCurso> todos = cursoController.todosLosCursos();
            assertNotNull(todos);
            assertTrue(todos.stream().anyMatch(c -> c.nombreCurso().equals(nombreCurso)));

            HashSet<DataCurso> porInstituto = cursoController.cursosPorInstituto(nombreInst);
            assertNotNull(porInstituto);
            assertTrue(porInstituto.stream().anyMatch(c -> c.nombreCurso().equals(nombreCurso)));
        });
    }

    @Test
    public void testCursosNoRelacionadosConUnProgDeFormacion() {
        assertDoesNotThrow(() -> {
            HashSet<DataCurso> sinProg = cursoController
                    .cursosNoRelacionadosConUnProgDeFormacion("ProgramaInexistente_" + System.currentTimeMillis());
            assertNotNull(sinProg);
        });
    }

    @Test
    public void testGuardarYConsultarCursoConCategorias() {
        String nombreInst = "InstCat_" + System.currentTimeMillis();
        DataInstituto inst = institutoController.guardarNuevoInstituto(new DataInstituto(nombreInst));

        String nombreCurso = "CursoCat_" + System.currentTimeMillis();
        HashSet<String> cats = new HashSet<>();
        cats.add("Social");
        cats.add("Educativo");

        DataCurso nuevoCurso = new DataCurso(
                inst, nombreCurso, "Curso con categorias", 10, 50, 4,
                LocalDate.now(), "http://cat.com", new HashSet<>(), new HashSet<>(), cats, "img/cat.png");

        assertDoesNotThrow(() -> {
            cursoController.guardarNuevoCurso(nuevoCurso);

            DataCurso cursoObtenido = cursoController.traerCurso(nombreCurso);
            assertNotNull(cursoObtenido);
            assertEquals(nombreCurso, cursoObtenido.nombreCurso());
            assertNotNull(cursoObtenido.categorias());
            assertTrue(cursoObtenido.categorias().contains("Social"));
            assertTrue(cursoObtenido.categorias().contains("Educativo"));
            assertEquals("img/cat.png", cursoObtenido.imagen());
        });
    }

    @Test
    public void testExisteCursoInexistente() {
        Boolean existe = cursoController.existeCurso("CursoInexistente_" + System.currentTimeMillis());
        assertFalse(existe);
    }

    @Test
    public void testValorarCursoYCursosParticipaEstudiante() {
        String sufijo = String.valueOf(System.currentTimeMillis());
        String nombreInst = "InstVal_" + sufijo;
        DataInstituto inst = institutoController.guardarNuevoInstituto(new DataInstituto(nombreInst));

        String nombreCurso = "CursoVal_" + sufijo;
        DataCurso nuevoCurso = new DataCurso(
                inst, nombreCurso, "Desc valoración", 10, 60, 5,
                LocalDate.now(), "http://val.com", new HashSet<>(), new HashSet<>(), new HashSet<>(), null);

        assertDoesNotThrow(() -> {
            cursoController.guardarNuevoCurso(nuevoCurso);
        });

        // Setup student and edition
        com.grupo9.edext.grupo9.servidor_central.controller.usuario.UsuarioController usrCtrl = new com.grupo9.edext.grupo9.servidor_central.controller.usuario.UsuarioController();
        String nickEst = "estVal_" + sufijo;
        assertDoesNotThrow(() -> {
            usrCtrl.registrarEstudiante(nickEst, "Nombre", "Apellido", "estVal_" + sufijo + "@test.com", LocalDate.of(2000, 1, 1), null, "1234");
        });

        com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EdicionCursoController edCtrl = new com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EdicionCursoController();
        String nombreEdi = "EdicionVal_" + sufijo;
        DataEdicionCurso nuevaEdi = new DataEdicionCurso(nombreEdi, nuevoCurso, LocalDate.now(), LocalDate.now().plusMonths(1), 20, new HashSet<>(), new HashSet<>(), LocalDate.now(), null);
        assertDoesNotThrow(() -> {
            edCtrl.guardarNuevaEdicionCurso(nuevaEdi);
        });

        // Before accepting inscription: student does NOT participate
        List<DataCurso> antesDeInsc = cursoController.cursosParticipaEstudiante(nickEst);
        assertTrue(antesDeInsc.stream().noneMatch(c -> c.nombreCurso().equals(nombreCurso)));

        // Attempting to rate before being accepted throws IllegalStateException
        assertThrows(IllegalStateException.class, () -> {
            cursoController.valorarCurso(nombreCurso, nickEst, 4);
        });

        // Inscribe student and accept
        assertDoesNotThrow(() -> {
            edCtrl.inscribirNuevoEstudiante(LocalDate.now(), nickEst, nombreEdi, com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EstadoInscripcion.ACEPTADA);
        });

        // Now student participates in course
        List<DataCurso> participa = cursoController.cursosParticipaEstudiante(nickEst);
        assertTrue(participa.stream().anyMatch(c -> c.nombreCurso().equals(nombreCurso)));
        DataCurso cursoEncontrado = participa.stream().filter(c -> c.nombreCurso().equals(nombreCurso)).findFirst().orElse(null);
        assertNotNull(cursoEncontrado);
        assertNull(cursoEncontrado.valoracionEstudiante());
        assertEquals(0, cursoEncontrado.cantValoraciones());

        // Invalid rating values
        assertThrows(IllegalArgumentException.class, () -> {
            cursoController.valorarCurso(nombreCurso, nickEst, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            cursoController.valorarCurso(nombreCurso, nickEst, 6);
        });

        // Valid rating
        assertDoesNotThrow(() -> {
            cursoController.valorarCurso(nombreCurso, nickEst, 5);
        });

        // Verify updated rating
        List<DataCurso> despuesDeValorar = cursoController.cursosParticipaEstudiante(nickEst);
        DataCurso cursoValorado = despuesDeValorar.stream().filter(c -> c.nombreCurso().equals(nombreCurso)).findFirst().orElse(null);
        assertNotNull(cursoValorado);
        assertEquals(5, cursoValorado.valoracionEstudiante());
        assertEquals(1, cursoValorado.cantValoraciones());
        assertEquals(5.0, cursoValorado.puntajePromedio());

        // Attempting to rate again throws ErrorRepetidos
        assertThrows(com.grupo9.edext.grupo9.mensajes.ErrorRepetidos.class, () -> {
            cursoController.valorarCurso(nombreCurso, nickEst, 3);
        });

        // Another student also accepted in course
        String nickEst2 = "estVal2_" + sufijo;
        assertDoesNotThrow(() -> {
            usrCtrl.registrarEstudiante(nickEst2, "Est2", "Ape2", "estVal2_" + sufijo + "@test.com", LocalDate.of(2001, 2, 2), null, "1234");
            edCtrl.inscribirNuevoEstudiante(LocalDate.now(), nickEst2, nombreEdi, com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EstadoInscripcion.ACEPTADA);
            cursoController.valorarCurso(nombreCurso, nickEst2, 3);
        });

        // Average is now (5 + 3) / 2 = 4.0
        List<DataCurso> listaEst2 = cursoController.cursosParticipaEstudiante(nickEst2);
        DataCurso cursoEst2 = listaEst2.stream().filter(c -> c.nombreCurso().equals(nombreCurso)).findFirst().orElse(null);
        assertNotNull(cursoEst2);
        assertEquals(3, cursoEst2.valoracionEstudiante());
        assertEquals(2, cursoEst2.cantValoraciones());
        assertEquals(4.0, cursoEst2.puntajePromedio());
    }
}
