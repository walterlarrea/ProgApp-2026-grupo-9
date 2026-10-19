package com.grupo9.edext.grupo9.servidor_central.controller.usuario;

import com.grupo9.edext.grupo9.servidor_central.controller.instituto.Instituto;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.InscEdicion;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.ManejadorInstituto;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.ManejadorEdiciones;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.InscripcionPrograma;
import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.ProgramaDeFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEstudiante;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInscPrograma;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UsuarioController implements IUsuario {

    @Override
    public void modificarUsuario(String nick, String nom, String ape, LocalDate fechaNac, String rutaImg) {
        ManejadorDocente md = ManejadorDocente.getInstance();
        Docente docMem = md.obtenerDocente(nick);

        ManejadorEstudiantes me = ManejadorEstudiantes.getInstance();
        Estudiante estMem = me.obtenerEstudiante(nick);

        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.createNativeQuery(
                    "UPDATE usuarios SET nombre = :nom, apellido = :ape, fechaNac = :fecha, imagen = :img WHERE nickname = :nick")
                    .setParameter("nom", nom)
                    .setParameter("ape", ape)
                    .setParameter("fecha", fechaNac)
                    .setParameter("img", rutaImg)
                    .setParameter("nick", nick)
                    .executeUpdate();
            et.commit();
        } catch (Exception e) {
            if (et.isActive())
                et.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

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
    public void modificarUsuario(String nick, String nom, String ape, LocalDate fechaNac, String rutaImg,
            String passActual, String passNueva) throws ErrorNoExiste {
        ManejadorDocente md = ManejadorDocente.getInstance();
        Docente docMem = md.obtenerDocente(nick);

        ManejadorEstudiantes me = ManejadorEstudiantes.getInstance();
        Estudiante estMem = me.obtenerEstudiante(nick);

        Usuario u = (docMem != null) ? docMem : estMem;
        if (u == null) {
            throw new ErrorNoExiste("No existe el usuario con nickname: " + nick);
        }

        String nuevaPassword = u.getPassword();
        if (passNueva != null && !passNueva.trim().isEmpty()) {
            if (u.getPassword() != null && !u.getPassword().isEmpty()) {
                if (passActual == null || !u.getPassword().equals(passActual.trim())) {
                    throw new IllegalArgumentException("La contraseña actual no es correcta.");
                }
            }
            nuevaPassword = passNueva.trim();
        }

        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();

            em.createNativeQuery(
                    "UPDATE usuarios SET nombre = :nom, apellido = :ape, fechaNac = :fecha, imagen = :img, password = :pass WHERE nickname = :nick")
                    .setParameter("nom", nom)
                    .setParameter("ape", ape)
                    .setParameter("fecha", fechaNac)
                    .setParameter("img", rutaImg)
                    .setParameter("pass", nuevaPassword)
                    .setParameter("nick", nick)
                    .executeUpdate();

            et.commit();
        } catch (Exception e) {
            if (et.isActive())
                et.rollback();
            throw e;
        } finally {
            em.close();
        }

        // Actualizar en memoria el objeto existente (Docente o Estudiante)
        if (docMem != null) {
            docMem.setNombre(nom);
            docMem.setApellido(ape);
            docMem.setFechaNac(fechaNac);
            docMem.setImagen(rutaImg);
            docMem.setPassword(nuevaPassword);
        }
        if (estMem != null) {
            estMem.setNombre(nom);
            estMem.setApellido(ape);
            estMem.setFechaNac(fechaNac);
            estMem.setImagen(rutaImg);
            estMem.setPassword(nuevaPassword);
        }
    }

    @Override
    public void modificarUsuario(String nick, String nom, String ape, LocalDate fechaNac, String rutaImg,
            String nombreInst) {
        modificarUsuario(nick, nom, ape, fechaNac, rutaImg);
    }

    @Override
    public void modificarUsuario(String nick, String nom, String ape, LocalDate fechaNac, String rutaImg,
            String nombreInst, boolean esDocente) {
        modificarUsuario(nick, nom, ape, fechaNac, rutaImg);
    }

    @Override
    public void eliminarUsuario(String nick) throws ErrorNoExiste {
        ManejadorDocente md = ManejadorDocente.getInstance();
        Docente doc = md.obtenerDocente(nick);

        ManejadorEstudiantes me = ManejadorEstudiantes.getInstance();
        Estudiante est = me.obtenerEstudiante(nick);

        if (doc == null && est == null) {
            EntityManager emTest = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
            try {
                Usuario u = emTest.find(Usuario.class, nick);
                if (u == null) {
                    throw new ErrorNoExiste("No existe el usuario con nickname: " + nick);
                }
            } finally {
                emTest.close();
            }
        }

        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.createNativeQuery("DELETE FROM inscripciones_a_ediciones WHERE estudiane_nickname = :nick")
                    .setParameter("nick", nick).executeUpdate();
            em.createNativeQuery("DELETE FROM edicion_docente WHERE docente_nickname = :nick")
                    .setParameter("nick", nick).executeUpdate();
            em.createNativeQuery("DELETE FROM docente_instituto WHERE docente_nickname = :nick")
                    .setParameter("nick", nick).executeUpdate();
            em.createNativeQuery("DELETE FROM docentes WHERE nickname = :nick").setParameter("nick", nick)
                    .executeUpdate();
            em.createNativeQuery("DELETE FROM estudiantes WHERE nickname = :nick").setParameter("nick", nick)
                    .executeUpdate();
            em.createNativeQuery("DELETE FROM usuarios WHERE nickname = :nick").setParameter("nick", nick)
                    .executeUpdate();
            et.commit();
        } catch (Exception e) {
            if (et.isActive())
                et.rollback();
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

        ManejadorEdiciones.getInstance().removerInscripcionesDeEstudiante(nick);
        ManejadorEdiciones.getInstance().removerDocenteDeEdiciones(nick);
    }

    private boolean existeEmail(String email) {
        ManejadorDocente md = ManejadorDocente.getInstance();
        ManejadorEstudiantes me = ManejadorEstudiantes.getInstance();

        if (md.getDocente() != null) {
            for (Docente d : md.getDocente()) {
                if (d.getEmail().equalsIgnoreCase(email))
                    return true;
            }
        }
        if (me.getEstudiante() != null) {
            for (Estudiante e : me.getEstudiante()) {
                if (e.getEmail().equalsIgnoreCase(email))
                    return true;
            }
        }
        return false;
    }

    @Override
    public void registrarEstudiante(String nickname, String nombre, String apellido, String email, LocalDate fechaNac,
            String rutaImagen) throws ErrorRepetidos {
        registrarEstudiante(nickname, nombre, apellido, email, fechaNac, rutaImagen, null);
    }

    public void registrarEstudiante(String nickname, String nombre, String apellido, String email, LocalDate fechaNac, String rutaImagen, String password) throws ErrorRepetidos {
        ManejadorDocente mDocente = ManejadorDocente.getInstance();
        ManejadorEstudiantes mEstudiante = ManejadorEstudiantes.getInstance();

        // Verificar si ya existe como docente o como estudiante
        if (mDocente.obtenerDocente(nickname) != null || mEstudiante.obtenerEstudiante(nickname) != null) {
            throw new ErrorRepetidos("Ya existe un usuario registrado con el nickname: " + nickname);
        }

        if (existeEmail(email)) {
            throw new ErrorRepetidos("Ya existe un usuario registrado con el email: " + email);
        }

        Estudiante est = new Estudiante(nickname, nombre, apellido, email, fechaNac, rutaImagen, password);
        mEstudiante.addEstudiante(est);
    }

    @Override
    public void registrarDocente(String nickname, String nombre, String apellido, String email, LocalDate fechaNac,
            String rutaImagen, String nombreInst) throws ErrorRepetidos {
        registrarDocente(nickname, nombre, apellido, email, fechaNac, rutaImagen, nombreInst, null);
    }

    public void registrarDocente(String nickname, String nombre, String apellido, String email, LocalDate fechaNac, String rutaImagen, String nombreInst, String password) throws ErrorRepetidos {
        ManejadorDocente mDocente = ManejadorDocente.getInstance();
        ManejadorEstudiantes mEstudiante = ManejadorEstudiantes.getInstance();

        if (mDocente.obtenerDocente(nickname) != null || mEstudiante.obtenerEstudiante(nickname) != null) {
            throw new ErrorRepetidos("Ya existe un usuario registrado con el nickname: " + nickname);
        }

        if (existeEmail(email)) {
            throw new ErrorRepetidos("Ya existe un usuario registrado con el email: " + email);
        }

        Docente doc = new Docente(nickname, nombre, apellido, email, fechaNac, rutaImagen, nombreInst, password);

        Instituto instituto = ManejadorInstituto.getInstance().obtenerInstituto(nombreInst);

        if (instituto != null) {
            doc.getInstitutos().add(instituto);
            instituto.getDocentes().add(doc);
        }

        mDocente.addDocente(doc);
    }

    @Override
    public void inscribirEstudianteEnPrograma(LocalDate fechaInsc, String nickname, String nombrePrograma)
            throws ErrorRepetidos, ErrorNoExiste {
        ManejadorEstudiantes manejadorEstudiantes = ManejadorEstudiantes.getInstance();
        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        try {
            Estudiante estudiante = em.find(Estudiante.class, nickname);
            if (estudiante == null) {
                throw new ErrorNoExiste("No existe el estudiante con nickname: " + nickname);
            }
            ProgramaDeFormacion programa = em.find(ProgramaDeFormacion.class, nombrePrograma);
            if (programa == null) {
                throw new ErrorNoExiste("No existe el programa de formación: " + nombrePrograma);
            }

            Long cantidadInscripciones = em.createQuery(
                    "SELECT COUNT(i) FROM InscripcionPrograma i WHERE i.estudiante.nickname = :nickname AND i.programa.nombre = :programa",
                    Long.class)
                    .setParameter("nickname", nickname)
                    .setParameter("programa", nombrePrograma)
                    .getSingleResult();
            if (cantidadInscripciones > 0) {
                throw new ErrorRepetidos("El estudiante ya está inscrito en el programa: " + nombrePrograma);
            }

            InscripcionPrograma nuevaInscripcion = new InscripcionPrograma(estudiante, programa, fechaInsc);
            EntityTransaction et = em.getTransaction();
            try {
                et.begin();
                em.persist(nuevaInscripcion);
                if (estudiante.getInscripcionesProgramas() == null) {
                    estudiante.setInscripcionesProgramas(new HashSet<>());
                }
                estudiante.getInscripcionesProgramas().add(nuevaInscripcion);
                if (programa.getInscripciones() == null) {
                    programa.setInscripciones(new HashSet<>());
                }
                programa.getInscripciones().add(nuevaInscripcion);
                et.commit();
                estudiante.getInscripciones().size();
                estudiante.getInscripcionesProgramas().size();
                manejadorEstudiantes.addEstudianteDirect(estudiante);
            } catch (Exception e) {
                if (et.isActive()) {
                    et.rollback();
                }
                if (e instanceof ErrorRepetidos) {
                    throw (ErrorRepetidos) e;
                }
                throw e;
            }
        } finally {
            em.close();
        }
    }

    @Override
    public boolean estaSiguiendoUsuario(String seguidorNick, String seguidoNick) throws ErrorNoExiste {
        if (seguidorNick == null || seguidoNick == null) {
            return false;
        }
        Usuario seguidor = buscarUsuarioMemoria(seguidorNick);
        Usuario seguido = buscarUsuarioMemoria(seguidoNick);
        if (seguidor == null) {
            throw new ErrorNoExiste("No existe el usuario seguidor: " + seguidorNick);
        }
        if (seguido == null) {
            throw new ErrorNoExiste("No existe el usuario a seguir: " + seguidoNick);
        }
        Set<String> seguidos = obtenerSeguidos(seguidorNick);
        return seguidos != null && seguidos.contains(seguidoNick);
    }

    @Override
    public DataUsuario iniciarSesion(String nicknameOEmail, String password) throws ErrorNoExiste {
        ManejadorDocente md = ManejadorDocente.getInstance();
        Docente[] docentes = md.getDocente();
        if (docentes != null) {
            for (Docente d : docentes) {
                if (d.getNickname().equalsIgnoreCase(nicknameOEmail) || d.getEmail().equalsIgnoreCase(nicknameOEmail)) {
                    if (d.getPassword() == null || d.getPassword().equals(password)) {
                        return consultarUsuario(d.getNickname());
                    } else {
                        throw new ErrorNoExiste("Contraseña incorrecta.");
                    }
                }
            }
        }

        ManejadorEstudiantes me = ManejadorEstudiantes.getInstance();
        Estudiante[] estudiantes = me.getEstudiante();
        if (estudiantes != null) {
            for (Estudiante e : estudiantes) {
                if (e.getNickname().equalsIgnoreCase(nicknameOEmail) || e.getEmail().equalsIgnoreCase(nicknameOEmail)) {
                    if (e.getPassword() == null || e.getPassword().equals(password)) {
                        return consultarUsuario(e.getNickname());
                    } else {
                        throw new ErrorNoExiste("Contraseña incorrecta.");
                    }
                }
            }
        }

        throw new ErrorNoExiste("No existe un usuario registrado con ese nickname o email.");
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
    public DataUsuario consultarUsuario(String nickname) throws ErrorNoExiste {
        ManejadorDocente md = ManejadorDocente.getInstance();
        Docente doc = md.obtenerDocente(nickname);
        if (doc != null) {
            Set<String> ediciones = new HashSet<>();
            if (doc.getEdiciones() != null) {
                for (EdicionCurso ed : doc.getEdiciones()) {
                    ediciones.add(ed.getNombreEdi());
                }
            }
            Set<String> cursos = new HashSet<>();
            DataDocente dd = new DataDocente(
                doc.getNickname(),
                doc.getNombre(),
                doc.getApellido(),
                doc.getEmail(),
                doc.getFechaNac(),
                doc.getImagen(),
                doc.getNombreInst());
            dd.setPassword(doc.getPassword());
            if (ediciones != null) dd.getEdiciones().addAll(ediciones);
            if (cursos != null) dd.getCursos().addAll(cursos);
            dd.setSeguidos(obtenerSeguidos(nickname));
            dd.setSeguidores(obtenerSeguidores(nickname));
            return dd;
        }

        ManejadorEstudiantes me = ManejadorEstudiantes.getInstance();
        Estudiante est = me.obtenerEstudiante(nickname);
        if (est != null) {
            Set<String> edicionesInsc = new HashSet<>();
            if (est.getInscripciones() != null) {
                for (InscEdicion insc : est.getInscripciones()) {
                    if (insc.getEdicion() != null) {
                        edicionesInsc.add(insc.getEdicion().getNombreEdi());
                    }
                }
            }
            
            Set<DataInscPrograma> inscripcionesProgramas = new HashSet<>();
            if (est.getInscripcionesProgramas() != null) {
                for (InscripcionPrograma inscripcion : est.getInscripcionesProgramas()) {
                    if (inscripcion != null && inscripcion.getPrograma() != null) {
                        inscripcionesProgramas.add(new DataInscPrograma(
                                est.getNickname(),
                                inscripcion.getPrograma().getNombre(),
                                inscripcion.getFechaInscripcion()));
                    }
                }
            }
            DataEstudiante de = new DataEstudiante(
                est.getNickname(),
                est.getNombre(),
                est.getApellido(),
                est.getEmail(),
                est.getFechaNac(),
                est.getImagen(),
                edicionesInsc,
                inscripcionesProgramas);
            de.setPassword(est.getPassword());
            de.setSeguidos(obtenerSeguidos(nickname));
            de.setSeguidores(obtenerSeguidores(nickname));
            return de;
        }

        throw new ErrorNoExiste("El usuario con nickname " + nickname + " no existe.");
    }

    private Usuario buscarUsuarioMemoria(String nickname) {
        Docente d = ManejadorDocente.getInstance().obtenerDocente(nickname);
        if (d != null) return d;
        return ManejadorEstudiantes.getInstance().obtenerEstudiante(nickname);
    }

    @Override
    public void seguirUsuario(String seguidorNick, String seguidoNick) throws ErrorNoExiste, ErrorRepetidos {
        if (seguidorNick == null || seguidoNick == null || seguidorNick.equalsIgnoreCase(seguidoNick)) {
            return;
        }
        Usuario seguidor = buscarUsuarioMemoria(seguidorNick);
        Usuario seguido = buscarUsuarioMemoria(seguidoNick);
        if (seguidor == null) {
            throw new ErrorNoExiste("No existe el usuario seguidor: " + seguidorNick);
        }
        if (seguido == null) {
            throw new ErrorNoExiste("No existe el usuario a seguir: " + seguidoNick);
        }
        if (seguidor.loSigue(seguidoNick)) {
            throw new ErrorRepetidos("Ya sigues a este usuario.");
        }

        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            // Insertar si no existe
            List<?> existe = em.createNativeQuery(
                "SELECT 1 FROM usuario_seguidos WHERE usuario_nick = :usr AND seguido_nick = :seg")
                .setParameter("usr", seguidorNick)
                .setParameter("seg", seguidoNick)
                .getResultList();
            if (!existe.isEmpty()) {
                throw new ErrorRepetidos("Ya sigues a este usuario.");
            }
            em.createNativeQuery(
                "INSERT INTO usuario_seguidos (usuario_nick, seguido_nick) VALUES (:usr, :seg)")
                .setParameter("usr", seguidorNick)
                .setParameter("seg", seguidoNick)
                .executeUpdate();
            et.commit();
        } catch (Exception e) {
            if (et.isActive()) et.rollback();
            throw e;
        } finally {
            em.close();
        }

        seguidor.seguir(seguidoNick);
    }

    @Override
    public void dejarDeSeguirUsuario(String seguidorNick, String seguidoNick) throws ErrorNoExiste {
        if (seguidorNick == null || seguidoNick == null) {
            return;
        }
        Usuario seguidor = buscarUsuarioMemoria(seguidorNick);
        if (seguidor == null) {
            throw new ErrorNoExiste("No existe el usuario seguidor: " + seguidorNick);
        }

        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.createNativeQuery(
                "DELETE FROM usuario_seguidos WHERE usuario_nick = :usr AND seguido_nick = :seg")
                .setParameter("usr", seguidorNick)
                .setParameter("seg", seguidoNick)
                .executeUpdate();
            et.commit();
        } catch (Exception e) {
            if (et.isActive()) et.rollback();
            throw e;
        } finally {
            em.close();
        }

        seguidor.dejarDeSeguir(seguidoNick);
    }

    @Override
    public Set<String> obtenerSeguidos(String nickname) throws ErrorNoExiste {
        Usuario u = buscarUsuarioMemoria(nickname);
        if (u == null) {
            throw new ErrorNoExiste("No existe el usuario con nickname: " + nickname);
        }

        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        try {
            @SuppressWarnings("unchecked")
            List<String> res = em.createNativeQuery(
                "SELECT seguido_nick FROM usuario_seguidos WHERE usuario_nick = :usr")
                .setParameter("usr", nickname)
                .getResultList();
            Set<String> seguidos = new HashSet<>(res);
            u.setSeguidos(seguidos);
            return seguidos;
        } catch (Exception e) {
            return u.getSeguidos();
        } finally {
            em.close();
        }
    }

    @Override
    public Set<String> obtenerSeguidores(String nickname) throws ErrorNoExiste {
        Usuario u = buscarUsuarioMemoria(nickname);
        if (u == null) {
            throw new ErrorNoExiste("No existe el usuario con nickname: " + nickname);
        }

        EntityManager em = UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        try {
            @SuppressWarnings("unchecked")
            List<String> res = em.createNativeQuery(
                "SELECT usuario_nick FROM usuario_seguidos WHERE seguido_nick = :usr")
                .setParameter("usr", nickname)
                .getResultList();
            return new HashSet<>(res);
        } catch (Exception e) {
            return new HashSet<>();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean esSeguidor(String seguidorNick, String seguidoNick) {
        if (seguidorNick == null || seguidoNick == null) return false;
        try {
            Set<String> seguidos = obtenerSeguidos(seguidorNick);
            return seguidos != null && seguidos.contains(seguidoNick);
        } catch (Exception e) {
            return false;
        }
    }
}
