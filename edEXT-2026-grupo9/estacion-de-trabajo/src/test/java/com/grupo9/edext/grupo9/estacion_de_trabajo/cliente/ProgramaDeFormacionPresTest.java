package com.grupo9.edext.grupo9.estacion_de_trabajo.cliente;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.miscelanea.Fabrica;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class ProgramaDeFormacionPresTest {

    private ProgramaDeFormacionPres programaPres;
    private CursoPres cursoPres;
    private IServidorCentral servidorCentral;

    @BeforeEach
    public void setUp() {
        programaPres = new ProgramaDeFormacionPres();
        cursoPres = new CursoPres();
        servidorCentral = Fabrica.getInstance().getIServidorCentral();
    }

    @Test
    public void testFlujoProgramaCompletoEnEstacion() {
        String sufijo = Long.toString(System.currentTimeMillis());
        String progNombre = "ProgPres_" + sufijo;

        assertDoesNotThrow(() -> {
            DataProgramaFormacion creado = programaPres.guardarNuevoProgramaDeFormacion(
                    progNombre, "Descripcion programa estacion",
                    LocalDate.of(2026, 3, 1), LocalDate.of(2026, 11, 30));
            assertNotNull(creado);
            assertEquals(progNombre, creado.nombre());

            assertTrue(programaPres.existeProgramaDeFormacion(progNombre));

            DataProgramaFormacion porId = programaPres.buscarPorNombreId(progNombre);
            assertNotNull(porId);
            assertEquals(progNombre, porId.nombre());

            HashSet<DataProgramaFormacion> todos = programaPres.cargarProgramas();
            assertNotNull(todos);
            assertTrue(todos.stream().anyMatch(p -> progNombre.equals(p.nombre())));
        });
    }

    @Test
    public void testAsociarCursoAProgramaEnEstacion() {
        String sufijo = Long.toString(System.currentTimeMillis());
        String instNombre = "InstProgPres_" + sufijo;
        DataInstituto inst = servidorCentral.guardarInstituto(new DataInstituto(instNombre));

        String cursoNombre = "CursoParaProg_" + sufijo;
        String progNombre = "ProgConCursoPres_" + sufijo;

        assertDoesNotThrow(() -> {
            DataCurso curso = cursoPres.guardarNuevoCurso(
                    inst, cursoNombre, "Curso desc", 6, 30, 3,
                    "http://curso.com", new HashSet<>(), null);
            assertNotNull(curso);

            DataProgramaFormacion prog = programaPres.guardarNuevoProgramaDeFormacion(
                    progNombre, "Programa desc",
                    LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31));
            assertNotNull(prog);

            Boolean asociado = programaPres.agregarCursoAProgramaDeFormacion(progNombre, cursoNombre);
            assertTrue(asociado);

            HashSet<DataProgramaFormacion> progs = programaPres.cargarProgramasPorCurso(curso);
            assertNotNull(progs);
            assertTrue(progs.stream().anyMatch(p -> progNombre.equals(p.nombre())));
        });
    }

    @Test
    public void testCargarProgramasPorCursoNull() {
        HashSet<DataProgramaFormacion> progs = programaPres.cargarProgramasPorCurso(null);
        assertNotNull(progs);
        assertTrue(progs.isEmpty());
    }
}
