/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.grupo9.edext.grupo9.servidor_central.controller.usuario;

import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;

import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author Usuario
 */
public interface IUsuario {

    public void modificarUsuario(String nick, String nom, String ape, LocalDate fechaNac, String rutaImg);

    public void registrarEstudiante(String nickname, String nombre, String apellido, String email, LocalDate fechaNac, String rutaImagen) throws ErrorRepetidos;
    public void registrarDocente(String nickname, String nombre, String apellido, String email, LocalDate fechaNac, String rutaImagen, String nombreInst) throws ErrorRepetidos;
}
