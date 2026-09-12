
package com.grupo9.edext.grupo9.servidor_central.controller.usuario;

import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.Instituto;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.ManejadorInstituto;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.ManejadorDocente;
import java.time.LocalDate;

public class UsuarioController implements IUsuario{

    @Override
    public void modificarUsuario(String nick, String nom, String ape, LocalDate fechaNac, String rutaImg) {
        ManejadorDocente md = ManejadorDocente.getInstance();
        Docente docMem = md.obtenerDocente(nick);
        
        ManejadorEstudiantes me = ManejadorEstudiantes.getInstance();
        Estudiante estMem = me.obtenerEstudiante(nick);

        jakarta.persistence.EntityManager em = com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        jakarta.persistence.EntityTransaction et = em.getTransaction();
        try {
            et.begin();

            // Actualizar datos básicos en tabla usuarios
            em.createNativeQuery("UPDATE usuarios SET nombre = :nom, apellido = :ape, fechaNac = :fecha, imagen = :img WHERE nickname = :nick")
              .setParameter("nom", nom)
              .setParameter("ape", ape)
              .setParameter("fecha", fechaNac)
              .setParameter("img", rutaImg)
              .setParameter("nick", nick)
              .executeUpdate();

            et.commit();
        } catch (Exception e) {
            if (et.isActive()) et.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        // Actualizar en memoria el objeto existente (Docente o Estudiante)
        if (docMem != null) {
            docMem.setNombre(nom);
            docMem.setApellido(ape);
            docMem.setFechaNac(fechaNac);
            docMem.setImagen(rutaImg);
        }
        if (estMem != null) {
            estMem.setNombre(nom);
            estMem.setApellido(ape);
            estMem.setFechaNac(fechaNac);
            estMem.setImagen(rutaImg);
        }
    }

    @Override
    public void modificarUsuario(String nick, String nom, String ape, LocalDate fechaNac, String rutaImg, String nombreInst) {
        modificarUsuario(nick, nom, ape, fechaNac, rutaImg);
    }

    @Override
    public void modificarUsuario(String nick, String nom, String ape, LocalDate fechaNac, String rutaImg, String nombreInst, boolean esDocente) {
        modificarUsuario(nick, nom, ape, fechaNac, rutaImg);
    }

    @Override
    public void eliminarUsuario(String nick) throws com.grupo9.edext.grupo9.mensajes.ErrorNoExiste {
        ManejadorDocente md = ManejadorDocente.getInstance();
        Docente doc = md.obtenerDocente(nick);
        
        ManejadorEstudiantes me = ManejadorEstudiantes.getInstance();
        Estudiante est = me.obtenerEstudiante(nick);

        if (doc == null && est == null) {
            jakarta.persistence.EntityManager emTest = com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA.getEntityManagerFactory().createEntityManager();
            try {
                Usuario u = emTest.find(Usuario.class, nick);
                if (u == null) {
                    throw new com.grupo9.edext.grupo9.mensajes.ErrorNoExiste("No existe el usuario con nickname: " + nick);
                }
            } finally {
                emTest.close();
            }
        }

        jakarta.persistence.EntityManager em = com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        jakarta.persistence.EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.createNativeQuery("DELETE FROM inscripciones_a_ediciones WHERE estudiane_nickname = :nick").setParameter("nick", nick).executeUpdate();
            em.createNativeQuery("DELETE FROM edicion_docente WHERE docente_nickname = :nick").setParameter("nick", nick).executeUpdate();
            em.createNativeQuery("DELETE FROM docente_instituto WHERE docente_nickname = :nick").setParameter("nick", nick).executeUpdate();
            em.createNativeQuery("DELETE FROM docentes WHERE nickname = :nick").setParameter("nick", nick).executeUpdate();
            em.createNativeQuery("DELETE FROM estudiantes WHERE nickname = :nick").setParameter("nick", nick).executeUpdate();
            em.createNativeQuery("DELETE FROM usuarios WHERE nickname = :nick").setParameter("nick", nick).executeUpdate();
            et.commit();
        } catch (Exception e) {
            if (et.isActive()) et.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }

        if (doc != null || md.obtenerDocente(nick) != null) {
            md.removerDocente(nick);
        }
        if (est != null || me.obtenerEstudiante(nick) != null) {
            me.removerEstudiante(nick);
        }

        com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.ManejadorEdiciones.getInstance().removerInscripcionesDeEstudiante(nick);
        com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.ManejadorEdiciones.getInstance().removerDocenteDeEdiciones(nick);
    }

    private boolean existeEmail(String email) {
        ManejadorDocente md = ManejadorDocente.getInstance();
        ManejadorEstudiantes me = ManejadorEstudiantes.getInstance();

        if (md.getDocente() != null) {
            for (Docente d : md.getDocente()) {
                if (d.getEmail().equalsIgnoreCase(email)) return true;
            }
        }
        if (me.getEstudiante() != null) {
            for (Estudiante e : me.getEstudiante()) {
                if (e.getEmail().equalsIgnoreCase(email)) return true;
            }
        }
        return false;
    }

    @Override
    public void registrarEstudiante(String nickname, String nombre, String apellido, String email, LocalDate fechaNac, String rutaImagen) throws ErrorRepetidos {
        ManejadorDocente mDocente = ManejadorDocente.getInstance();
        ManejadorEstudiantes mEstudiante = ManejadorEstudiantes.getInstance();

        // Verificar si ya existe como docente o como estudiante
        if (mDocente.obtenerDocente(nickname) != null || mEstudiante.obtenerEstudiante(nickname) != null) {
            throw new ErrorRepetidos("Ya existe un usuario registrado con el nickname: " + nickname);
        }

        if (existeEmail(email)) {
            throw new ErrorRepetidos("Ya existe un usuario registrado con el email: " + email);
        }

        Estudiante est = new Estudiante( nickname,  nombre,  apellido,  email,  fechaNac, rutaImagen);
        mEstudiante.addEstudiante(est);
    }

    @Override
    public void registrarDocente(String nickname, String nombre, String apellido, String email, LocalDate fechaNac, String rutaImagen,String nombreInst) throws ErrorRepetidos {
        ManejadorDocente mDocente = ManejadorDocente.getInstance();
        ManejadorEstudiantes mEstudiante = ManejadorEstudiantes.getInstance();

        if (mDocente.obtenerDocente(nickname) != null || mEstudiante.obtenerEstudiante(nickname) != null) {
            throw new ErrorRepetidos("Ya existe un usuario registrado con el nickname: " + nickname);
        }

        if (existeEmail(email)) {
            throw new ErrorRepetidos("Ya existe un usuario registrado con el email: " + email);
        }

        Docente doc = new Docente(nickname, nombre, apellido, email, fechaNac, rutaImagen, nombreInst);
        
        Instituto instituto = ManejadorInstituto.getInstance().obtenerInstituto(nombreInst);

        if (instituto != null) {
            doc.getInstitutos().add(instituto);
            instituto.getDocentes().add(doc);
        }

        mDocente.addDocente(doc);
    }

    @Override
    public String[] listarUsuarios() {
        ManejadorDocente md = ManejadorDocente.getInstance();
        ManejadorEstudiantes me = ManejadorEstudiantes.getInstance();

        java.util.Set<String> nicknames = new java.util.TreeSet<>();

        Docente[] docentes = md.getDocente();
        if (docentes != null) {
            for (Docente d : docentes) {
                nicknames.add(d.getNickname());
            }
        }

        Estudiante[] estudiantes = me.getEstudiante();
        if (estudiantes != null) {
            for (Estudiante e : estudiantes) {
                nicknames.add(e.getNickname());
            }
        }

        return nicknames.toArray(new String[0]);
    }

    @Override
    public com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario consultarUsuario(String nickname) throws com.grupo9.edext.grupo9.mensajes.ErrorNoExiste {
        ManejadorDocente md = ManejadorDocente.getInstance();
        Docente doc = md.obtenerDocente(nickname);
        if (doc != null) {
            java.util.Set<String> ediciones = new java.util.HashSet<>();
            if (doc.getEdiciones() != null) {
                for (com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EdicionCurso ed : doc.getEdiciones()) {
                    ediciones.add(ed.getNombreEdi());
                }
            }
            java.util.Set<String> cursos = new java.util.HashSet<>();
            return new com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente(
                doc.getNickname(),
                doc.getNombre(),
                doc.getApellido(),
                doc.getEmail(),
                doc.getFechaNac(),
                doc.getImagen());
        }

        ManejadorEstudiantes me = ManejadorEstudiantes.getInstance();
        Estudiante est = me.obtenerEstudiante(nickname);
        if (est != null) {
            java.util.Set<String> edicionesInsc = new java.util.HashSet<>();
            if (est.getInscripciones() != null) {
                for (com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.InscEdicion insc : est.getInscripciones()) {
                    if (insc.getEdicion() != null) {
                        edicionesInsc.add(insc.getEdicion().getNombreEdi());
                    }
                }
            }
            return new com.grupo9.edext.grupo9.servidor_central.dominio.DataEstudiante(
                est.getNickname(),
                est.getNombre(),
                est.getApellido(),
                est.getEmail(),
                est.getFechaNac(),
                est.getImagen());
        }

        throw new com.grupo9.edext.grupo9.mensajes.ErrorNoExiste("El usuario con nickname " + nickname + " no existe.");
    }
}
    



