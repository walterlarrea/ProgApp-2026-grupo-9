package com.grupo9.edext.grupo9.servidor_central.controller.busqueda;

import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import com.grupo9.edext.grupo9.servidor_central.dominio.ResultadoBusqueda;
import java.util.HashSet;
import java.util.List;

public class BusquedaController implements IBusqueda {
    private final ManejadorBusqueda manejadorBusqueda;
    
    public BusquedaController(){
        this.manejadorBusqueda = ManejadorBusqueda.getInstance();
    }

    @Override
    public HashSet<DataCurso> buscarCursos(String query) {
        return manejadorBusqueda.buscarCursos(query);
    }

    @Override
    public HashSet<DataProgramaFormacion> buscarProgramas(String query) {
        return manejadorBusqueda.buscarProgramas(query);
    }

    @Override
    public HashSet<DataUsuario> buscarUsuarios(String nombre, String apellido) {
        return manejadorBusqueda.buscarUsuarios(nombre, apellido);
    }

    @Override
    public List<ResultadoBusqueda> busquedaPrincipal(String query) {
        return manejadorBusqueda.busquedaPrincipal(query);
    }
}
