package com.grupo9.edext.grupo9.servidor_central.controller.busqueda;

import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import com.grupo9.edext.grupo9.servidor_central.dominio.ResultadoBusqueda;
import java.util.HashSet;
import java.util.List;

public interface IBusqueda {
	HashSet<DataCurso> buscarCursosPorNombre(String nombre);
	HashSet<DataProgramaFormacion> buscarProgramasPorNombre(String nombre);
	HashSet<DataUsuario> buscarUsuariosPorNombreYApellido(String nombre, String apellido);
	List<ResultadoBusqueda> busquedaPrincipal(String query);
}
