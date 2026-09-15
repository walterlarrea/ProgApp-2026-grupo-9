package com.grupo9.edext.grupo9.servidor_central.controller.usuario;

import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioControllerTest {

    private UsuarioController usuarioController;

    @BeforeEach
    public void setUp() {
        usuarioController = new UsuarioController();
    }

    @Test
    public void testRegistrarYConsultarEstudiante() {
        String nick = "testStudent_" + System.currentTimeMillis();
        String email = "test_" + System.currentTimeMillis() + "@test.com";

        assertDoesNotThrow(() -> {
            usuarioController.registrarEstudiante(nick, "Juan", "Pérez", email, LocalDate.of(2000, 1, 1), null);
            DataUsuario data = usuarioController.consultarUsuario(nick);
            assertNotNull(data);
            assertEquals(nick, data.getNickname());
            assertEquals("Juan", data.getNombre());
            assertEquals("Pérez", data.getApellido());
        });
    }

    @Test
    public void testModificarUsuario() {
        String nick = "testMod_" + System.currentTimeMillis();
        String email = "testmod_" + System.currentTimeMillis() + "@test.com";

        assertDoesNotThrow(() -> {
            usuarioController.registrarEstudiante(nick, "NombreOriginal", "ApellidoOriginal", email, LocalDate.of(1995, 5, 5), null);
            
            // Modificar datos básicos
            usuarioController.modificarUsuario(nick, "NombreNuevo", "ApellidoNuevo", LocalDate.of(1995, 5, 5), "path/img.png");
            
            DataUsuario data = usuarioController.consultarUsuario(nick);
            assertEquals("NombreNuevo", data.getNombre());
            assertEquals("ApellidoNuevo", data.getApellido());
            assertEquals("path/img.png", data.getImagen());
        });
    }

    @Test
    public void testEliminarUsuario() {
        String nick = "testDel_" + System.currentTimeMillis();
        String email = "testdel_" + System.currentTimeMillis() + "@test.com";

        assertDoesNotThrow(() -> {
            usuarioController.registrarEstudiante(nick, "AEliminar", "User", email, LocalDate.of(1998, 3, 3), null);
            usuarioController.eliminarUsuario(nick);
            
            assertThrows(ErrorNoExiste.class, () -> {
                usuarioController.consultarUsuario(nick);
            });
        });
    }
}
