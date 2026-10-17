package com.grupo9.edext.grupo9.servidor_central.controller.usuario;

import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEstudiante;
import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.InstitutoController;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsuarioControllerTest {

    private UsuarioController usuarioController;
    private InstitutoController institutoController;

    @BeforeEach
    public void setUp() {
        usuarioController = new UsuarioController();
        institutoController = new InstitutoController();
    }

    @Test
    public void testRegistrarYConsultarEstudiante() {
        String nick = "testStudent_" + System.currentTimeMillis();
        String email = "test_" + System.currentTimeMillis() + "@test.com";

        assertDoesNotThrow(() -> {
            usuarioController.registrarEstudiante(nick, "Juan", "Pérez", email, LocalDate.of(2000, 1, 1), null, "1234");
            DataUsuario data = usuarioController.consultarUsuario(nick);
            assertNotNull(data);
            assertTrue(data instanceof DataEstudiante);
            assertEquals(nick, data.getNickname());
            assertEquals("Juan", data.getNombre());
            assertEquals("Pérez", data.getApellido());
            assertEquals("1234", data.getPassword());
        });
    }

    @Test
    public void testRegistrarYConsultarDocente() {
        String instNombre = "InstDoc_" + System.currentTimeMillis();
        institutoController.guardarNuevoInstituto(new DataInstituto(instNombre));

        String nick = "testDoc_" + System.currentTimeMillis();
        String email = "testdoc_" + System.currentTimeMillis() + "@test.com";

        assertDoesNotThrow(() -> {
            usuarioController.registrarDocente(nick, "Maria", "Lopez", email, LocalDate.of(1985, 4, 4), "foto.png", instNombre, "passDoc123");
            DataUsuario data = usuarioController.consultarUsuario(nick);
            assertNotNull(data);
            assertTrue(data instanceof DataDocente);
            DataDocente docData = (DataDocente) data;
            assertEquals(nick, docData.getNickname());
            assertEquals(instNombre, docData.getNombreInst());
            assertEquals("passDoc123", docData.getPassword());
        });
    }

    @Test
    public void testRegistrarEstudianteDuplicado() {
        String nick = "dupNick_" + System.currentTimeMillis();
        String email = "dup_" + System.currentTimeMillis() + "@test.com";

        assertDoesNotThrow(() -> {
            usuarioController.registrarEstudiante(nick, "User", "One", email, LocalDate.of(2000, 1, 1), null, "1234");
        });

        assertThrows(ErrorRepetidos.class, () -> {
            usuarioController.registrarEstudiante(nick, "User", "Two", "other_" + System.currentTimeMillis() + "@test.com", LocalDate.of(2000, 1, 1), null, "1234");
        });

        assertThrows(ErrorRepetidos.class, () -> {
            usuarioController.registrarEstudiante("other_" + System.currentTimeMillis(), "User", "Three", email, LocalDate.of(2000, 1, 1), null, "1234");
        });
    }

    @Test
    public void testModificarUsuario() {
        String nick = "testMod_" + System.currentTimeMillis();
        String email = "testmod_" + System.currentTimeMillis() + "@test.com";

        assertDoesNotThrow(() -> {
            usuarioController.registrarEstudiante(nick, "NombreOriginal", "ApellidoOriginal", email, LocalDate.of(1995, 5, 5), null, "1234");
            usuarioController.modificarUsuario(nick, "NombreNuevo", "ApellidoNuevo", LocalDate.of(1995, 5, 5), "path/img.png", null, null);
            
            DataUsuario data = usuarioController.consultarUsuario(nick);
            assertEquals("NombreNuevo", data.getNombre());
            assertEquals("ApellidoNuevo", data.getApellido());
            assertEquals("path/img.png", data.getImagen());
            assertEquals("1234", data.getPassword());

            // Modificar contraseña
            usuarioController.modificarUsuario(nick, "NombreNuevo", "ApellidoNuevo", LocalDate.of(1995, 5, 5), "path/img.png", "1234", "nuevaPass123");
            DataUsuario dataMod = usuarioController.consultarUsuario(nick);
            assertEquals("nuevaPass123", dataMod.getPassword());
        });
    }

    @Test
    public void testListarUsuarios() {
        assertDoesNotThrow(() -> {
            String[] lista = usuarioController.listarUsuarios();
            assertNotNull(lista);
        });
    }

    @Test
    public void testEliminarUsuario() {
        String nick = "testDel_" + System.currentTimeMillis();
        String email = "testdel_" + System.currentTimeMillis() + "@test.com";

        assertDoesNotThrow(() -> {
            usuarioController.registrarEstudiante(nick, "AEliminar", "User", email, LocalDate.of(1998, 3, 3), null, "1234");
            usuarioController.eliminarUsuario(nick);
            
            assertThrows(ErrorNoExiste.class, () -> {
                usuarioController.consultarUsuario(nick);
            });
        });
    }
}
