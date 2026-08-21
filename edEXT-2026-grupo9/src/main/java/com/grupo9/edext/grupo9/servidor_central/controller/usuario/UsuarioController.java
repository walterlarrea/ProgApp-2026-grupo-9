/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo9.edext.grupo9.servidor_central.controller.usuario;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.IUsuario;
import java.util.Date;
/**
 *
 * @author Usuario
 */
public class UsuarioController implements IUsuario{
    @Override
    public void altaUsuario(String nick, String nom, String ape, String email, Date fechaNac, String rutaImg, String instituto) {
        // Tu lógica de negocio para crear el Docente o Estudiante
    }

    @Override
    public void modificarUsuario(String nick, String nom, String ape, Date fechaNac, String rutaImg) {
        // Tu lógica de actualización
    }
}
