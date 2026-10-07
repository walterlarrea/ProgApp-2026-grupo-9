package com.grupo9.edext.grupo9.servidor_central.controller.busqueda;

import com.grupo9.edext.grupo9.servidor_central.controller.DtoMapper;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.ProgramaDeFormacion;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Usuario;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import com.grupo9.edext.grupo9.servidor_central.dominio.ResultadoBusqueda;
import com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ManejadorBusqueda {
    private static ManejadorBusqueda instancia = null;
    
    private EntityManager em;
    
    private ManejadorBusqueda() {
        EntityManagerFactory emf = UtensiliosJPA.getEntityManagerFactory();
        em = emf.createEntityManager();
    }
    
    public static ManejadorBusqueda getInstance() {
        if (instancia == null){
            instancia = new ManejadorBusqueda();
        }
        return instancia;
    }

    public HashSet<DataCurso> buscarCursosPorNombre(String nombre) {
        if (!tieneTexto(nombre)) {
            return new HashSet<>();
        }

        List<Curso> cursos = em.createQuery(
                "SELECT c FROM Curso c WHERE LOWER(c.nombreCurso) LIKE :nombre", Curso.class)
            .setParameter("nombre", patron(nombre))
            .getResultList();
        HashSet<DataCurso> resultados = new HashSet<>();
        cursos.forEach(curso -> resultados.add(DtoMapper.toData(curso)));
        return resultados;
    }

    public HashSet<DataProgramaFormacion> buscarProgramasPorNombre(String nombre) {
        if (!tieneTexto(nombre)) {
            return new HashSet<>();
        }

        List<ProgramaDeFormacion> programas = em.createQuery(
                "SELECT p FROM ProgramaDeFormacion p WHERE LOWER(p.nombre) LIKE :nombre", ProgramaDeFormacion.class)
            .setParameter("nombre", patron(nombre))
            .getResultList();
        HashSet<DataProgramaFormacion> resultados = new HashSet<>();
        programas.forEach(programa -> resultados.add(DtoMapper.toData(programa)));
        return resultados;
    }

    public HashSet<DataUsuario> buscarUsuariosPorNombreYApellido(String nombre, String apellido) {
        if (!tieneTexto(nombre) && !tieneTexto(apellido)) {
            return new HashSet<>();
        }

        List<Usuario> usuarios = em.createQuery(
                "SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE :nombre AND LOWER(u.apellido) LIKE :apellido",
                Usuario.class)
            .setParameter("nombre", tieneTexto(nombre) ? patron(nombre) : "%")
            .setParameter("apellido", tieneTexto(apellido) ? patron(apellido) : "%")
            .getResultList();
        HashSet<DataUsuario> resultados = new HashSet<>();
        usuarios.forEach(usuario -> resultados.add(DtoMapper.toData(usuario)));
        return resultados;
    }

    public List<ResultadoBusqueda> busquedaPrincipal(String query) {
        if (!tieneTexto(query)) {
            return List.of();
        }

        List<ResultadoBusqueda> resultados = new ArrayList<>();
        buscarCursosPorNombre(query).forEach(curso ->
                resultados.add(new ResultadoBusqueda(TipoBusqueda.CURSO, curso)));

        List<Usuario> usuarios = em.createQuery(
                "SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE :query "
                + "OR LOWER(u.apellido) LIKE :query "
                + "OR LOWER(CONCAT(CONCAT(u.nombre, ' '), u.apellido)) LIKE :query", Usuario.class)
            .setParameter("query", patron(query))
            .getResultList();
        usuarios.forEach(usuario -> resultados.add(
                new ResultadoBusqueda(TipoBusqueda.USUARIO, DtoMapper.toData(usuario))));

        buscarProgramasPorNombre(query).forEach(programa ->
                resultados.add(new ResultadoBusqueda(TipoBusqueda.PROGRAMA_FORMACION, programa)));
        return resultados;
    }

    private boolean tieneTexto(String valor) {
        return valor != null && !valor.isBlank();
    }

    private String patron(String valor) {
        return "%" + valor.trim().toLowerCase(Locale.ROOT) + "%";
    }
    
}
