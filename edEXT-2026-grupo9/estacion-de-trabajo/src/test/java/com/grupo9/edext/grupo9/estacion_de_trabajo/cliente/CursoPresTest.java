package com.grupo9.edext.grupo9.estacion_de_trabajo.cliente;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.miscelanea.Fabrica;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CursoPresTest {

    private CursoPres cursoPres;
    private IServidorCentral servidorCentral;

    @BeforeEach
    public void setUp() {
        cursoPres = new CursoPres();
        servidorCentral = Fabrica.getInstance().getIServidorCentral();
    }

    @Test
    public void testFlujoCursoCompletoEnEstacion() {
        String sufijo = Long.toString(System.currentTimeMillis());
        String instNombre = "InstPres_" + sufijo;
        DataInstituto inst = servidorCentral.guardarInstituto(new DataInstituto(instNombre));

        String cursoNombre = "CursoPres_" + sufijo;
        assertDoesNotThrow(() -> {
            DataCurso creado = cursoPres.guardarNuevoCurso(
                    inst, cursoNombre, "Curso desde estacion", 8, 40, 4,
                    "http://estacion.com", new HashSet<>(), "img/cursoPres.png");
            assertNotNull(creado);
            assertEquals(cursoNombre, creado.nombreCurso());

            assertTrue(cursoPres.existeCurso(cursoNombre));

            HashSet<DataCurso> todos = cursoPres.cargarCursos();
            assertNotNull(todos);
            assertTrue(todos.stream().anyMatch(c -> cursoNombre.equals(c.nombreCurso())));

            HashSet<DataCurso> porInst = cursoPres.cargarCursosPorInstituto(inst);
            assertNotNull(porInst);
            assertTrue(porInst.stream().anyMatch(c -> cursoNombre.equals(c.nombreCurso())));

            HashSet<DataCurso> noRel = cursoPres.cursosNoRelacionadosConUnProgDeFormacion("ProgInexistente_" + sufijo);
            assertNotNull(noRel);
        });
    }

    @Test
    public void testCargarCursosPorInstitutoNull() {
        HashSet<DataCurso> resultado = cursoPres.cargarCursosPorInstituto(null);
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}
