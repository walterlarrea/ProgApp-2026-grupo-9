package com.grupo9.edext.grupo9.servidor_central.controller.instituto;

import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import java.util.HashSet;

public interface IInstituto {
    public DataInstituto guardarNuevoInstituto(DataInstituto nuevoInstituto);
    public HashSet<DataInstituto> todosLosInstitutos();
}
