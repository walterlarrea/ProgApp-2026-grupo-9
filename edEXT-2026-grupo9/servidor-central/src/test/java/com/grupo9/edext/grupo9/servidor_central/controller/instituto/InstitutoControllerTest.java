package com.grupo9.edext.grupo9.servidor_central.controller.instituto;

import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class InstitutoControllerTest {

    private InstitutoController institutoController;

    @BeforeEach
    public void setUp() {
        institutoController = new InstitutoController();
    }

    @Test
    public void testGuardarYExisteInstituto() {
        String nombreInst = "InstitutoTest_" + System.currentTimeMillis();
        DataInstituto nuevoInst = new DataInstituto(nombreInst);

        assertDoesNotThrow(() -> {
            DataInstituto res = institutoController.guardarNuevoInstituto(nuevoInst);
            assertNotNull(res);
            assertEquals(nombreInst, res.nombreI());

            Boolean existe = institutoController.existeInstituto(nombreInst);
            assertTrue(existe);
        });
    }

    @Test
    public void testTodosLosInstitutos() {
        String nombreInst = "InstTodos_" + System.currentTimeMillis();
        DataInstituto nuevoInst = new DataInstituto(nombreInst);

        assertDoesNotThrow(() -> {
            institutoController.guardarNuevoInstituto(nuevoInst);
            HashSet<DataInstituto> todos = institutoController.todosLosInstitutos();
            assertNotNull(todos);
            assertTrue(todos.stream().anyMatch(i -> i.nombreI().equals(nombreInst)));
        });
    }

    @Test
    public void testExisteInstitutoInexistente() {
        String nombreInexistente = "NonExistentInst_" + System.currentTimeMillis();
        Boolean existe = institutoController.existeInstituto(nombreInexistente);
        assertFalse(existe);
    }
}
