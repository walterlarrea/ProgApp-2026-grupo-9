package com.grupo9.edext.grupo9.estacion_de_trabajo.cliente;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.servidor_central.controller.ServidorCentralController;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Docente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInscEdicion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import static com.grupo9.edext.grupo9.servidor_central.controller.DtoMapper.toData;
import java.time.LocalDate;
import java.util.HashSet;

public class EdicionCursoPres {
    IServidorCentral servidorCentral = ServidorCentralController.getInstance();
    
    public EdicionCursoPres(){}

    public void guardarNuevaEdicion(String nombreEdi, Curso curso, LocalDate fechaInicio, LocalDate fechaFin, Integer cupo, Docente docente){
        System.out.println("[CLIENTE] Llamada al servidor central: " + nombreEdi);
        DataDocente dataDocente = toData(docente);
        DataCurso dataCurso = new DataCurso(toData(curso.getInstituto()),  curso.getNombreCurso(),  curso.getDescCurso(), curso.getDuracion(), curso.getCantHoras(), curso.getCantCred(), curso.getFechaReg(), curso.getUrl(), null);
        DataEdicionCurso nuevaEdicion = new DataEdicionCurso(nombreEdi, dataCurso, fechaInicio, fechaFin, cupo, dataDocente, new HashSet<>(), LocalDate.now());
        DataEdicionCurso edicionCreada = servidorCentral.guardarEdicionCurso(nuevaEdicion);
        if (edicionCreada != null) {
            System.out.println("[CLIENTE] Edición creada con éxito!");
        }else{
            System.out.println("[CLIENTE] La Edición "+ nombreEdi + " ya existe.");
        }
    }
    
    public DataEdicionCurso muestroEdicionCurso(String nEdi) throws ErrorNoExiste{
        System.out.println("[CLIENTE] Consulto una edición de curso.");
        DataEdicionCurso edicion = servidorCentral.consultarUnaEdicionCurso(nEdi);
        return edicion;
    }
}
