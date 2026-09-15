package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;

import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.CursoController;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.InstitutoController;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.UsuarioController;
import com.grupo9.edext.grupo9.servidor_central.dominio.*;
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
    public void testAltaYConsultarEdicionCurso() {
        String nombreInst = "InstEdicion_" + System.currentTimeMillis();
        DataInstituto inst = institutoController.guardarNuevoInstituto(new DataInstituto(nombreInst));

        String nombreCurso = "CursoEdicion_" + System.currentTimeMillis();
        DataCurso curso = cursoController.guardarNuevoCurso(new DataCurso(
                inst, nombreCurso, "Desc", 6, 40, 4, LocalDate.now(), "http://test.com", new HashSet<>(), new HashSet<>()
        ));

        String nombreEdicion = "EdicionTest_" + System.currentTimeMillis();
        DataEdicionCurso nuevaEdicion = new DataEdicionCurso(
                nombreEdicion, curso, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 7, 1),
                25, new HashSet<>(), new HashSet<>(), LocalDate.now()
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
    }

    @Test
    public void testConsultarEdicionInexistente() {
        String edicionInexistente = "EdInexistente_" + System.currentTimeMillis();
        assertThrows(ErrorNoExiste.class, () -> {
            edicionController.consultarEdicionCurso(edicionInexistente);
        });
    }

    @Test
    public void testInscripcionEstudianteInexistente() {
        String edicionInexistente = "EdInexistente_" + System.currentTimeMillis();
        String estudianteInexistente = "EstInexistente_" + System.currentTimeMillis();

        assertThrows(ErrorNoExiste.class, () -> {
            edicionController.inscribirNuevoEstudiante(LocalDate.now(), estudianteInexistente, edicionInexistente);
        });
    }

    @Test
    public void testAltaEdicionDuplicadaLanzaExcepcion() {
        String nombreEdi = "EdicionDup_" + System.currentTimeMillis();
        assertDoesNotThrow(() -> {
            edicionController.altaEdicionCurso(
                    nombreEdi, null, LocalDate.now(), LocalDate.now().plusMonths(3),
                    20, new HashSet<>(), new HashSet<>()
            );
        });

        assertThrows(ErrorRepetidos.class, () -> {
            edicionController.altaEdicionCurso(
                    nombreEdi, null, LocalDate.now(), LocalDate.now().plusMonths(3),
                    20, new HashSet<>(), new HashSet<>()
            );
        });
    }

    @Test
    public void testTraerEstudiantesYDocentes() {
        assertDoesNotThrow(() -> {
            assertNotNull(edicionController.traerEstudiantes());
            DataInstituto dataInst = institutoController.guardarNuevoInstituto(new DataInstituto("InstDocentes_" + System.currentTimeMillis()));
            assertNotNull(edicionController.traerDocentes(com.grupo9.edext.grupo9.servidor_central.controller.DtoMapper.toEntity(dataInst)));
        });
    }
}
