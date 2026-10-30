package com.grupo9.edext.grupo9.web_service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;

/**
 * DTO base de Usuario para SOAP. Nunca transporta la contraseña.
 * Las subclases (Estudiante / Docente) viajan con xsi:type gracias a @XmlSeeAlso.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataUsuarioDTO")
@XmlSeeAlso({ DataEstudianteDTO.class, DataDocenteDTO.class })
public class DataUsuarioDTO implements Serializable {

    private String nickname;
    private String nombre;
    private String apellido;
    private String email;
    private String fechaNac;
    private String imagen;
    private String tipo;
    private List<String> seguidos = new ArrayList<>();
    private List<String> seguidores = new ArrayList<>();

    public DataUsuarioDTO() {
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<String> getSeguidos() {
        return seguidos;
    }

    public void setSeguidos(List<String> seguidos) {
        this.seguidos = seguidos;
    }

    public List<String> getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(List<String> seguidores) {
        this.seguidores = seguidores;
    }
}
