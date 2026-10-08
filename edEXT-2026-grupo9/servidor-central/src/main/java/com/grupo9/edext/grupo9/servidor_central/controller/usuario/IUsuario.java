package com.grupo9.edext.grupo9.servidor_central.controller.usuario;

import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import java.time.LocalDate;

public interface IUsuario {

    public void modificarUsuario(String nick, String nom, String ape, LocalDate fechaNac, String rutaImg);
    public void modificarUsuario(String nick, String nom, String ape, LocalDate fechaNac, String rutaImg, String passActual, String passNueva) throws ErrorNoExiste;
    public void modificarUsuario(String nick, String nom, String ape, LocalDate fechaNac, String rutaImg, String nombreInst);
    public void modificarUsuario(String nick, String nom, String ape, LocalDate fechaNac, String rutaImg, String nombreInst, boolean esDocente);
    public void eliminarUsuario(String nick) throws ErrorNoExiste;

    public void registrarEstudiante(String nickname, String nombre, String apellido, String email, LocalDate fechaNac, String rutaImagen) throws ErrorRepetidos;
    public void registrarEstudiante(String nickname, String nombre, String apellido, String email, LocalDate fechaNac, String rutaImagen, String password) throws ErrorRepetidos;
    public void registrarDocente(String nickname, String nombre, String apellido, String email, LocalDate fechaNac, String rutaImagen, String nombreInst) throws ErrorRepetidos;
    public void registrarDocente(String nickname, String nombre, String apellido, String email, LocalDate fechaNac, String rutaImagen, String nombreInst, String password) throws ErrorRepetidos;
    public String[] listarUsuarios();
    public DataUsuario consultarUsuario(String nickname) throws ErrorNoExiste;
    public DataUsuario iniciarSesion(String nicknameOEmail, String password) throws ErrorNoExiste;
}
