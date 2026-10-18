package com.grupo9.edext.grupo9.servidor_central.controller.usuario;


import jakarta.persistence.*;
import java.io.Serializable;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "usuarios")
public class Usuario implements Serializable {
    @Id
    private String nickname;
    private String nombre;
    private String apellido;
    private String email;
    private LocalDate fechaNac;
    private String imagen;

    private String fechaNac_str; // Optional helper if used
    private String password;
    @ManyToMany(mappedBy = "seguidos")
    private Set<Usuario> seguidores = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_seguidos", joinColumns = @JoinColumn(name = "usuario_nick"))
    @Column(name = "seguido_nick")
    private Set<String> seguidos = new HashSet<>();

    public Usuario(String nickname, String nombre, String apellido, String email, LocalDate fechaNac, String rutaImagen, String password) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.fechaNac = fechaNac;
        this.imagen = rutaImagen;
        this.password = password;
        this.seguidos = new HashSet<>();
    }

    public Usuario() {

    }

    public Usuario(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public LocalDate getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(LocalDate fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getImagen() {
        return imagen;
    }

    public String getRutaImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Set<String> getSeguidos() {
        if (seguidos == null) {
            seguidos = new HashSet<>();
        }
        return seguidos;
    }

    public void setSeguidos(Set<String> seguidos) {
        this.seguidos = seguidos;
    }

    public void seguir(String nick) {
        getSeguidos().add(nick);
    }

    public void dejarDeSeguir(String nick) {
        getSeguidos().remove(nick);
    }

    public boolean loSigue(String nick) {
        return getSeguidos().contains(nick);
    }
}
