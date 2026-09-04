/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.grupo9.edext.grupo9.servidor_central.controller.usuario;

import java.util.Date;

/**
 *
 * @author Usuario
 */
public interface IUsuario {
    public void altaUsuario(String nick, String nom, String ape, String email, Date fechaNac, String rutaImg, String instituto);
    public void modificarUsuario(String nick, String nom, String ape, Date fechaNac, String rutaImg);
}
