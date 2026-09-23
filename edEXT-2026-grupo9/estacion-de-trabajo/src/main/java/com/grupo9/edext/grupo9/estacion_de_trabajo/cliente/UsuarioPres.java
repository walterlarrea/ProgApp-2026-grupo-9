package com.grupo9.edext.grupo9.estacion_de_trabajo.cliente;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.servidor_central.controller.ServidorCentralController;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import java.time.LocalDate;

public class UsuarioPres {
    IServidorCentral servidorCentral = ServidorCentralController.getInstance();

    public UsuarioPres() {
        // servidorCentral.logStatus();
    }

    public String[] listarUsuarios() {
        System.out.println("[CLIENTE] Listar todos los usuarios");
        return servidorCentral.listarUsuarios();
    }

    public DataUsuario consultarUsuario(String nickname) throws ErrorNoExiste {
        System.out.println("[CLIENTE] Consultar usuario: " + nickname);
        return servidorCentral.consultarUsuario(nickname);
    }

    public void modificarUsuario(String nick, String nom, String ape, LocalDate fechaNac, String rutaImg) {
        System.out.println("[CLIENTE] Modificar usuario: " + nick);
        servidorCentral.modificarUsuario(nick, nom, ape, fechaNac, rutaImg);
    }

    public void eliminarUsuario(String nick) throws ErrorNoExiste {
        System.out.println("[CLIENTE] Eliminar usuario: " + nick);
        servidorCentral.eliminarUsuario(nick);
    }
}
