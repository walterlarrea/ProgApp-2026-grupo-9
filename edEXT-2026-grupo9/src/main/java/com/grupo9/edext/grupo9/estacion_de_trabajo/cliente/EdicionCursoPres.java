package com.grupo9.edext.grupo9.estacion_de_trabajo.cliente;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.servidor_central.controller.ServidorCentralController;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Docente;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Estudiante;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.Instituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import static com.grupo9.edext.grupo9.servidor_central.controller.DtoMapper.toData;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class EdicionCursoPres {
    IServidorCentral servidorCentral = ServidorCentralController.getInstance();
    
    public EdicionCursoPres(){}

    public void guardarNuevaEdicion(String nombreEdi, Curso curso, LocalDate fechaInicio, LocalDate fechaFin, Integer cupo, Set<Docente> docentes){
        System.out.println("[CLIENTE] Llamada al servidor central: " + nombreEdi);
        Set<DataDocente> dataDocente = toData(docentes);
        DataCurso dataCurso = new DataCurso(toData(curso.getInstituto()),  curso.getNombreCurso(),  curso.getDescCurso(), curso.getDuracion(), curso.getCantHoras(), curso.getCantCred(), curso.getFechaReg(), curso.getUrl(), null);
        DataEdicionCurso nuevaEdicion = new DataEdicionCurso(nombreEdi, dataCurso, fechaInicio, fechaFin, cupo, dataDocente, new HashSet<>(), LocalDate.now());
        DataEdicionCurso edicionCreada = servidorCentral.guardarEdicionCurso(nuevaEdicion);
        if (edicionCreada != null) {
            System.out.println("[CLIENTE] Edición creada con éxito!");
        }else{
            System.out.println("[CLIENTE] La Edición "+ nombreEdi + " ya existe.");
        }
    }
    
    public Docente[] traerDocentes(Instituto instituto) {
        System.out.println("[CLIENTE] Consultando docentes...");
        return servidorCentral.traerDocentes(instituto);
    }
    
    public DataEdicionCurso muestroEdicionCurso(String nEdi) throws ErrorNoExiste{
        System.out.println("[CLIENTE] Consulto una edición de curso.");
        return servidorCentral.consultarUnaEdicionCurso(nEdi);
    }
    
    public EdicionCurso[] traerEdiciones(Curso curso) {
        return servidorCentral.traerEdiciones(curso);
    }
    
    public Estudiante[] traerEstudiantes(){
        return servidorCentral.traerEstudiantes();
    }
    
    public void inscribirNuevoEstudianteEdicion(String nickname, String nombreEdi, LocalDate fechaInsc) throws ErrorRepetidos{
        try {
            servidorCentral.inscribirEstudiante(fechaInsc, nickname, nombreEdi);
            System.out.println("[CLIENTE] Inscripción exitosa!");
        } catch (ErrorRepetidos e) {
            System.out.println("[CLIENTE] " + e.getMessage());
        } catch (ErrorNoExiste e) {
            System.out.println("[CLIENTE] " + e.getMessage());
        }
    }
}
