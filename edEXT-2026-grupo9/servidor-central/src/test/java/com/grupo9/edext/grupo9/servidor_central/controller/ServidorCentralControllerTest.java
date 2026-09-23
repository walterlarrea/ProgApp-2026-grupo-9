package com.grupo9.edext.grupo9.servidor_central.controller;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;

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
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), LocalDate.now()
        );

        assertDoesNotThrow(() -> {
            DataProgramaFormacion res = servidorCentral.guardarProgramaDeFormacion(prog);
            assertNotNull(res);

            Boolean existe = servidorCentral.existeProgramaDeFormacion(nombreProg);
            assertTrue(existe);

            DataProgramaFormacion porId = servidorCentral.traerPorNombreId(nombreProg);
            assertNotNull(porId);

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
}
