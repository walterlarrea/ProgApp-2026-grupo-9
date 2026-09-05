package com.grupo9.edext.grupo9.estacion_de_trabajo.cliente;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.servidor_central.controller.ServidorCentralController;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInscEdicion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import java.time.LocalDate;
import java.util.HashSet;

public class EdicionCursoPres {
    IServidorCentral servidorCentral = ServidorCentralController.getInstance();
    
    public EdicionCursoPres(){}

    public void guardarNuevaEdicion(String nombreEdi, DataCurso curso, LocalDate fechaInicio, LocalDate fechaFin, Integer cupo, DataDocente docente){
        System.out.println("[CLIENTE] Llamada al servidor central: " + nombreEdi);
        DataEdicionCurso nuevaEdicion = new DataEdicionCurso(
            nombreEdi,
            curso,
            fechaInicio,
            fechaFin,
            cupo,
            docente,
            new HashSet<>(),
            LocalDate.now());
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
