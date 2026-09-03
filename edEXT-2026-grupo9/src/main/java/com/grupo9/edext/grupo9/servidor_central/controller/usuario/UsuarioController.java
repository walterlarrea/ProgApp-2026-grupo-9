
package com.grupo9.edext.grupo9.servidor_central.controller.usuario;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;

import java.time.LocalDate;
import java.util.Date;
/**
 *
 * @author Usuario
 */

public class UsuarioController implements IUsuario{

    @Override
    public void modificarUsuario(String nick, String nom, String ape, LocalDate fechaNac, String rutaImg) {
        // Tu lógica de actualización
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
        mDocente.addDocente(doc);
    }


}
    



