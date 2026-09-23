package com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion;

import com.grupo9.edext.grupo9.servidor_central.controller.curso.CursoController;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.InstitutoController;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class ProgramaDeFormacionControllerTest {

    private ProgramaDeFormacionController programaController;
    private CursoController cursoController;
    private InstitutoController institutoController;

    @BeforeEach
    public void setUp() {
        programaController = new ProgramaDeFormacionController();
        cursoController = new CursoController();
        institutoController = new InstitutoController();
    }

    @Test
    public void testGuardarYExisteProgramaDeFormacion() {
        String nombreProg = "ProgramaTest_" + System.currentTimeMillis();
        DataProgramaFormacion nuevoProg = new DataProgramaFormacion(
                nombreProg, "Descripción programa test", new HashSet<>(),
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), LocalDate.now()
        );

        assertDoesNotThrow(() -> {
            DataProgramaFormacion res = programaController.guardarNuevoProgramaDeFormacion(nuevoProg);
            assertNotNull(res);
            assertEquals(nombreProg, res.nombre());

            Boolean existe = programaController.existeProgramaDeFormacion(nombreProg);
            assertTrue(existe);
        });
    }

    @Test
    public void testTodosLosProgramasYTraerPorNombreId() {
        String nombreProg = "ProgTraer_" + System.currentTimeMillis();
        DataProgramaFormacion nuevoProg = new DataProgramaFormacion(
                nombreProg, "Desc", new HashSet<>(),
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), LocalDate.now()
        );

        assertDoesNotThrow(() -> {
            programaController.guardarNuevoProgramaDeFormacion(nuevoProg);

            HashSet<DataProgramaFormacion> todos = programaController.todosLosProgramas();
            assertNotNull(todos);
            assertTrue(todos.stream().anyMatch(p -> p.nombre().equals(nombreProg)));

            DataProgramaFormacion obtenido = programaController.traerPorNombreId(nombreProg);
            assertNotNull(obtenido);
            assertEquals(nombreProg, obtenido.nombre());
        });
    }

    @Test
    public void testAgregarCursoAProgramaDeFormacionYProgramasPorCurso() {
        String nombreInst = "InstProg_" + System.currentTimeMillis();
        DataInstituto inst = institutoController.guardarNuevoInstituto(new DataInstituto(nombreInst));

        String nombreCurso = "CursoProg_" + System.currentTimeMillis();
        DataCurso curso = cursoController.guardarNuevoCurso(new DataCurso(
                inst, nombreCurso, "Desc", 4, 20, 2, LocalDate.now(), "http://test.com", new HashSet<>(), new HashSet<>()
        ));

        String nombreProg = "ProgConCurso_" + System.currentTimeMillis();
        programaController.guardarNuevoProgramaDeFormacion(new DataProgramaFormacion(
                nombreProg, "Desc", new HashSet<>(),
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), LocalDate.now()
        ));

        assertDoesNotThrow(() -> {
            Boolean agregado = programaController.agregarCursoAProgramaDeFormacion(nombreProg, nombreCurso);
            assertTrue(agregado);

            HashSet<DataProgramaFormacion> progsPorCurso = programaController.programasPorCurso(nombreCurso);
            assertNotNull(progsPorCurso);
            assertTrue(progsPorCurso.stream().anyMatch(p -> p.nombre().equals(nombreProg)));
        });
    }

    @Test
    public void testExisteProgramaInexistente() {
        Boolean existe = programaController.existeProgramaDeFormacion("ProgInexistente_" + System.currentTimeMillis());
        assertFalse(existe);
    }
}
