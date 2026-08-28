package com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion;

import com.grupo9.edext.grupo9.dtos.DTOProgramaDeFormacion;

public class ProgramaDeFormacionController implements IProgramaDeFormacion {
    
    public void guardarNuevoProgramaDeFormacion(){
        System.out.println("[SERVIDOR] Mock de PERSISTENCIA de un nuevo programa de formación");
    }

    @Override
    public void guardarNuevoProgramaDeFormacion(DTOProgramaDeFormacion nuevoPrograma) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
