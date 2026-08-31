package com.grupo9.edext.grupo9.estacion_de_trabajo.cliente;

import com.grupo9.edext.grupo9.dtos.DTOCurso;
import com.grupo9.edext.grupo9.interfaces.IServidorCentral;
import com.grupo9.edext.grupo9.servidor_central.controller.ServidorCentralController;
import java.util.HashSet;


public class CursoPres {
    IServidorCentral servidorCentral = ServidorCentralController.getInstance();
  
    public CursoPres(){
//        servidorCentral.logStatus();
    }

    
    public void guardarNuevoCurso(String instituto, String nombre, String descripcion, int duracion, int cantHoras, int cantCreditos, String url){
        System.out.println("[CLIENTE] Crear nuevo Curso: " + nombre);
        DTOCurso nuevoPrograma = new DTOCurso(instituto, nombre, descripcion, duracion, cantHoras, cantCreditos, url);
        
        DTOCurso cursoCreado = servidorCentral.guardarCurso(nuevoPrograma);
        if(cursoCreado != null){
            System.out.println("[CLIENTE] Curso creado con exito!");
        } else {
            System.out.println("[CLIENTE] Algo salio mal creando el nuevo Curso");
        }
    }
    
    public HashSet<DTOCurso> cargarCursos(){
        System.out.println("[CLIENTE] Consultar todos los Cursos");
        HashSet<DTOCurso> cursos = servidorCentral.consultarTodosLosCursos();
        
        return cursos;
    }
}
