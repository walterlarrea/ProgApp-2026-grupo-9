package com.grupo9.edext.grupo9.estacion_de_trabajo.cliente;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.miscelanea.Fabrica;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEstudiante;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class EdicionCursoPres {
    IServidorCentral servidorCentral = Fabrica.getInstance().getIServidorCentral();
    
    public EdicionCursoPres(){}

    public void guardarNuevaEdicion(String nombreEdi, DataCurso dataCurso, LocalDate fechaInicio, LocalDate fechaFin, Integer cupo, Set<DataDocente> docentes){
        System.out.println("[CLIENTE] Llamada al servidor central: " + nombreEdi);
        DataEdicionCurso nuevaEdicion = new DataEdicionCurso(nombreEdi, dataCurso, fechaInicio, fechaFin, cupo, new HashSet<>(docentes), new HashSet<>(), LocalDate.now());
        DataEdicionCurso edicionCreada = servidorCentral.guardarEdicionCurso(nuevaEdicion);
        if (edicionCreada != null) {
            System.out.println("[CLIENTE] Edición creada con éxito!");
        } else {
            System.out.println("[CLIENTE] La Edición " + nombreEdi + " ya existe.");
        }
    }
    
    public DataDocente[] traerDocentes(DataInstituto instituto) {
        System.out.println("[CLIENTE] Consultando docentes...");
        return servidorCentral.traerDocentes(instituto);
    }
    
    public DataEdicionCurso muestroEdicionCurso(String nEdi) throws ErrorNoExiste {
        System.out.println("[CLIENTE] Consulto una edición de curso.");
        return servidorCentral.consultarUnaEdicionCurso(nEdi);
    }
    
    public DataEdicionCurso[] traerEdiciones(DataCurso dataCurso) {
        return servidorCentral.traerEdiciones(dataCurso);
    }

    public DataEstudiante[] traerEstudiantes(){
        return servidorCentral.traerEstudiantes();
    }
    
    public void inscribirNuevoEstudianteEdicion(String nickname, String nombreEdi, LocalDate fechaInsc) throws ErrorRepetidos {
        try {
            servidorCentral.inscribirEstudiante(fechaInsc, nickname, nombreEdi);
            System.out.println("[CLIENTE] Inscripción exitosa!");
        } catch (ErrorRepetidos e) {
            System.out.println("[CLIENTE] " + e.getMessage());
            throw e;
        } catch (ErrorNoExiste e) {
            System.out.println("[CLIENTE] " + e.getMessage());
        }
    }
}
