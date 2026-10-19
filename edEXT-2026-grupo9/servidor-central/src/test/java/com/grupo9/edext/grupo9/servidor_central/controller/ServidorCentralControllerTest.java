package com.grupo9.edext.grupo9.servidor_central.controller;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEstudiante;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ServidorCentralControllerTest {

    private IServidorCentral servidorCentral;

    @BeforeEach
    public void setUp() {
        servidorCentral = ServidorCentralController.getInstance();
    }

    @Test
    public void testSingletonInstance() {
        IServidorCentral instance1 = ServidorCentralController.getInstance();
        IServidorCentral instance2 = ServidorCentralController.getInstance();

        assertNotNull(instance1);
        assertSame(instance1, instance2);
    }

    @Test
    public void testLogStatus() {
        assertDoesNotThrow(() -> servidorCentral.logStatus());
    }

    @Test
    public void testDelegacionInstitutos() {
        String nombreInst = "InstServidor_" + System.currentTimeMillis();
        DataInstituto inst = new DataInstituto(nombreInst);

        assertDoesNotThrow(() -> {
            DataInstituto res = servidorCentral.guardarInstituto(inst);
            assertNotNull(res);
            assertEquals(nombreInst, res.nombreI());

            Boolean existe = servidorCentral.existeInstituto(nombreInst);
            assertTrue(existe);

            HashSet<DataInstituto> institutos = servidorCentral.consultarTodosLosInstitutos();
            assertNotNull(institutos);
            assertTrue(institutos.stream().anyMatch(i -> i.nombreI().equals(nombreInst)));
        });
    }

    @Test
    public void testDelegacionProgramasFormacion() {
        String nombreProg = "ProgServidor_" + System.currentTimeMillis();
        DataProgramaFormacion prog = new DataProgramaFormacion(
                nombreProg, "Desc", new HashSet<>(),
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), LocalDate.now(), "imagenes/p.jpg");

        assertDoesNotThrow(() -> {
            DataProgramaFormacion res = servidorCentral.guardarProgramaDeFormacion(prog);
            assertNotNull(res);

            Boolean existe = servidorCentral.existeProgramaDeFormacion(nombreProg);
            assertTrue(existe);

            DataProgramaFormacion porId = servidorCentral.traerPorNombreId(nombreProg);
            assertNotNull(porId);
            assertEquals("imagenes/p.jpg", porId.imagen());

            HashSet<DataProgramaFormacion> todos = servidorCentral.consultarTodosLosProgramas();
            assertNotNull(todos);
        });
    }

    @Test
    public void testDelegacionUsuariosYListar() {
        assertDoesNotThrow(() -> {
            String[] usuarios = servidorCentral.listarUsuarios();
            assertNotNull(usuarios);
        });
    }

    @Test
    public void testCursoConCategoriasYAsociacionAPrograma() throws Exception {
        String sufijo = Long.toString(System.currentTimeMillis());
        String instNombre = "InstSC_" + sufijo;
        servidorCentral.guardarInstituto(new DataInstituto(instNombre));

        String cursoNombre = "CursoSC_" + sufijo;
        HashSet<String> cats = new HashSet<>();
        cats.add("Educativo");
        cats.add("Social");

        DataCurso curso = new DataCurso(
                new DataInstituto(instNombre), cursoNombre, "Descripcion curso", 10, 40, 4,
                LocalDate.now(), "http://curso-sc.com", new HashSet<>(), new HashSet<>(), cats, "img/curso.png");

        servidorCentral.guardarCurso(curso);

        DataCurso cursoRecuperado = servidorCentral.buscarCurso(cursoNombre);
        assertNotNull(cursoRecuperado);
        assertEquals(cursoNombre, cursoRecuperado.nombreCurso());
        assertNotNull(cursoRecuperado.categorias());
        assertTrue(cursoRecuperado.categorias().contains("Educativo"));
        assertTrue(cursoRecuperado.categorias().contains("Social"));

        String progNombre = "ProgSC_" + sufijo;
        servidorCentral.guardarProgramaDeFormacion(new DataProgramaFormacion(
                progNombre, "Programa test SC", new HashSet<>(),
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31), LocalDate.now(), "img/prog.png"));

        Boolean asociado = servidorCentral.agregarCursoAProgramaDeFormacion(progNombre, cursoNombre);
        assertTrue(asociado);

        HashSet<DataProgramaFormacion> progs = servidorCentral.programasPorCurso(cursoNombre);
        assertNotNull(progs);
        assertTrue(progs.stream().anyMatch(p -> progNombre.equals(p.nombre())));
    }

    @Test
    public void testInscripcionProgramaYClaveCompuesta() throws Exception {
        String sufijo = Long.toString(System.currentTimeMillis());
        String estudianteA = "estA_" + sufijo;
        String estudianteB = "estB_" + sufijo;
        String prog1 = "Prog1_" + sufijo;
        String prog2 = "Prog2_" + sufijo;

        servidorCentral.registrarEstudiante(estudianteA, "Est", "A", estudianteA + "@test.com", LocalDate.of(2001, 2, 2), null);
        servidorCentral.registrarEstudiante(estudianteB, "Est", "B", estudianteB + "@test.com", LocalDate.of(2002, 3, 3), null);

        servidorCentral.guardarProgramaDeFormacion(new DataProgramaFormacion(
                prog1, "Desc 1", new HashSet<>(), LocalDate.now(), LocalDate.now().plusMonths(3), LocalDate.now(), null));
        servidorCentral.guardarProgramaDeFormacion(new DataProgramaFormacion(
                prog2, "Desc 2", new HashSet<>(), LocalDate.now(), LocalDate.now().plusMonths(3), LocalDate.now(), null));

        // Estudiante A se inscribe a prog1
        servidorCentral.inscribirseAPrograma(LocalDate.now(), estudianteA, prog1);

        DataUsuario usrA = servidorCentral.consultarUsuario(estudianteA);
        assertTrue(usrA instanceof DataEstudiante);
        DataEstudiante estAData = (DataEstudiante) usrA;
        assertTrue(estAData.getInscripcionesProgramas().stream().anyMatch(i -> prog1.equals(i.getProgramaNombre())));

        // Inscripción duplicada de estudiante A al mismo programa prog1 debe fallar
        assertThrows(ErrorRepetidos.class, () -> {
            servidorCentral.inscribirseAPrograma(LocalDate.now(), estudianteA, prog1);
        });

        // Clave compuesta: mismo estudiante a prog2 debe tener éxito
        assertDoesNotThrow(() -> {
            servidorCentral.inscribirseAPrograma(LocalDate.now(), estudianteA, prog2);
        });

        // Clave compuesta: estudiante B a prog1 debe tener éxito
        assertDoesNotThrow(() -> {
            servidorCentral.inscribirseAPrograma(LocalDate.now(), estudianteB, prog1);
        });

        DataEstudiante estAActualizado = (DataEstudiante) servidorCentral.consultarUsuario(estudianteA);
        assertEquals(2, estAActualizado.getInscripcionesProgramas().size());

        DataEstudiante estBActualizado = (DataEstudiante) servidorCentral.consultarUsuario(estudianteB);
        assertEquals(1, estBActualizado.getInscripcionesProgramas().size());
    }

    @Test
    public void testSeguimientoDeUsuariosCompleto() throws Exception {
        String sufijo = Long.toString(System.currentTimeMillis());
        String user1 = "user1_" + sufijo;
        String user2 = "user2_" + sufijo;

        servidorCentral.registrarEstudiante(user1, "User", "One", user1 + "@test.com", LocalDate.of(1999, 1, 1), null);
        servidorCentral.registrarEstudiante(user2, "User", "Two", user2 + "@test.com", LocalDate.of(1999, 2, 2), null);

        assertFalse(servidorCentral.estaSiguiendoUsuario(user1, user2));

        // Seguir usuario
        servidorCentral.seguirUsuario(user1, user2);
        assertTrue(servidorCentral.estaSiguiendoUsuario(user1, user2));

        // Seguir nuevamente debe lanzar ErrorRepetidos
        assertThrows(ErrorRepetidos.class, () -> {
            servidorCentral.seguirUsuario(user1, user2);
        });

        // Dejar de seguir
        servidorCentral.dejarDeSeguirUsuario(user1, user2);
        assertFalse(servidorCentral.estaSiguiendoUsuario(user1, user2));

        // Dejar de seguir cuando ya no lo sigue no debe fallar con excepción inesperada
        assertDoesNotThrow(() -> {
            servidorCentral.dejarDeSeguirUsuario(user1, user2);
        });
    }
}
