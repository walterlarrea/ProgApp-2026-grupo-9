package com.grupo9.edext.grupo9.servidor_central.controller.curso;

import com.grupo9.edext.grupo9.servidor_central.controller.instituto.InstitutoController;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;

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
                LocalDate.now(), "http://test.com", new HashSet<>(), new HashSet<>()
        );

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
                LocalDate.now(), "http://test.com", new HashSet<>(), new HashSet<>()
        );

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
            HashSet<DataCurso> sinProg = cursoController.cursosNoRelacionadosConUnProgDeFormacion("ProgramaInexistente_" + System.currentTimeMillis());
            assertNotNull(sinProg);
        });
    }

    @Test
    public void testExisteCursoInexistente() {
        Boolean existe = cursoController.existeCurso("CursoInexistente_" + System.currentTimeMillis());
        assertFalse(existe);
    }
}
