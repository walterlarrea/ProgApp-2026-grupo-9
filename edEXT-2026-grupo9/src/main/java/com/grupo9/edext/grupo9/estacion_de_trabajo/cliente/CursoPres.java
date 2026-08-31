package com.grupo9.edext.grupo9.estacion_de_trabajo.cliente;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.servidor_central.controller.ServidorCentralController;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import java.util.HashSet;


public class CursoPres {
    IServidorCentral servidorCentral = ServidorCentralController.getInstance();
  
    public CursoPres(){
//        servidorCentral.logStatus();
    }

    
    public void guardarNuevoCurso(String instituto, String nombre, String descripcion, int duracion, int cantHoras, int cantCreditos, String url){
        System.out.println("[CLIENTE] Crear nuevo Curso: " + nombre);
        DataCurso nuevoCurso = new DataCurso(instituto, nombre, descripcion, duracion, cantHoras, cantCreditos, null, url);
        
        DataCurso cursoCreado = servidorCentral.guardarCurso(nuevoCurso);
        if(cursoCreado != null){
            System.out.println("[CLIENTE] Curso creado con exito!");
        } else {
            System.out.println("[CLIENTE] Algo salio mal creando el nuevo Curso");
        }
    }
    
    public HashSet<DataCurso> cargarCursos(){
        System.out.println("[CLIENTE] Consultar todos los Cursos");
        HashSet<DataCurso> cursos = servidorCentral.consultarTodosLosCursos();
        
        return cursos;
    }
}
