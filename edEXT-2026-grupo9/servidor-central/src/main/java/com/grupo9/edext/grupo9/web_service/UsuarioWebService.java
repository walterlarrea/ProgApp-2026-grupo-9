package com.grupo9.edext.grupo9.web_service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.servidor_central.controller.ServidorCentralFactory;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import com.grupo9.edext.grupo9.web_service.dto.DataUsuarioDTO;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(serviceName = "UsuarioWebService")
public class UsuarioWebService {

    @WebMethod(operationName = "consultarUsuario")
    public DataUsuarioDTO consultarUsuario(@WebParam(name = "nickname") String nickname) throws ErrorNoExiste {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        DataUsuario usuario = servidorCentral.consultarUsuario(nickname);
        return DtoRecordMapper.toDTO(usuario);
    }

    @WebMethod(operationName = "iniciarSesion")
    public DataUsuarioDTO iniciarSesion(
            @WebParam(name = "nicknameOEmail") String nicknameOEmail,
            @WebParam(name = "password") String password) throws ErrorNoExiste {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        DataUsuario usuario = servidorCentral.iniciarSesion(nicknameOEmail, password);
        return DtoRecordMapper.toDTO(usuario);
    }

    @WebMethod(operationName = "modificarUsuario")
    public void modificarUsuario(
            @WebParam(name = "nick") String nick,
            @WebParam(name = "nom") String nom,
            @WebParam(name = "ape") String ape,
            @WebParam(name = "fechaNac") String fechaNac,
            @WebParam(name = "rutaImg") String rutaImg,
            @WebParam(name = "passActual") String passActual,
            @WebParam(name = "passNueva") String passNueva) throws ErrorNoExiste {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        LocalDate fNac = DtoRecordMapper.toDate(fechaNac);
        servidorCentral.modificarUsuario(nick, nom, ape, fNac, rutaImg, passActual, passNueva);
    }

    @WebMethod(operationName = "registrarEstudiante")
    public void registrarEstudiante(
            @WebParam(name = "nickname") String nickname,
            @WebParam(name = "nombre") String nombre,
            @WebParam(name = "apellido") String apellido,
            @WebParam(name = "email") String email,
            @WebParam(name = "fechaNac") String fechaNac,
            @WebParam(name = "rutaImagen") String rutaImagen,
            @WebParam(name = "password") String password) throws ErrorRepetidos {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        LocalDate fNac = DtoRecordMapper.toDate(fechaNac);
        servidorCentral.registrarEstudiante(nickname, nombre, apellido, email, fNac, rutaImagen, password);
    }

    @WebMethod(operationName = "registrarDocente")
    public void registrarDocente(
            @WebParam(name = "nickname") String nickname,
            @WebParam(name = "nombre") String nombre,
            @WebParam(name = "apellido") String apellido,
            @WebParam(name = "email") String email,
            @WebParam(name = "fechaNac") String fechaNac,
            @WebParam(name = "rutaImagen") String rutaImagen,
            @WebParam(name = "nombreInst") String nombreInst,
            @WebParam(name = "password") String password) throws ErrorRepetidos {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        LocalDate fNac = DtoRecordMapper.toDate(fechaNac);
        servidorCentral.registrarDocente(nickname, nombre, apellido, email, fNac, rutaImagen, nombreInst, password);
    }

    @WebMethod(operationName = "seguirUsuario")
    public void seguirUsuario(
            @WebParam(name = "seguidorNick") String seguidorNick,
            @WebParam(name = "seguidoNick") String seguidoNick) throws ErrorNoExiste, ErrorRepetidos {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        servidorCentral.seguirUsuario(seguidorNick, seguidoNick);
    }

    @WebMethod(operationName = "dejarDeSeguirUsuario")
    public void dejarDeSeguirUsuario(
            @WebParam(name = "seguidorNick") String seguidorNick,
            @WebParam(name = "seguidoNick") String seguidoNick) throws ErrorNoExiste {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        servidorCentral.dejarDeSeguirUsuario(seguidorNick, seguidoNick);
    }

    @WebMethod(operationName = "obtenerSeguidos")
    public List<String> obtenerSeguidos(@WebParam(name = "nickname") String nickname) throws ErrorNoExiste {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        Set<String> seguidos = servidorCentral.obtenerSeguidos(nickname);
        return seguidos != null ? new ArrayList<>(seguidos) : new ArrayList<>();
    }

    @WebMethod(operationName = "obtenerSeguidores")
    public List<String> obtenerSeguidores(@WebParam(name = "nickname") String nickname) throws ErrorNoExiste {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        Set<String> seguidores = servidorCentral.obtenerSeguidores(nickname);
        return seguidores != null ? new ArrayList<>(seguidores) : new ArrayList<>();
    }

    @WebMethod(operationName = "estaSiguiendoUsuario")
    public boolean estaSiguiendoUsuario(
            @WebParam(name = "seguidorNick") String seguidorNick,
            @WebParam(name = "seguidoNick") String seguidoNick) throws ErrorNoExiste {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        return servidorCentral.estaSiguiendoUsuario(seguidorNick, seguidoNick);
    }

    @WebMethod(operationName = "listarUsuarios")
    public List<String> listarUsuarios() {
        IServidorCentral servidorCentral = ServidorCentralFactory.getServidorCentral();
        String[] usuarios = servidorCentral.listarUsuarios();
        return usuarios != null ? Arrays.asList(usuarios) : new ArrayList<>();
    }
}
