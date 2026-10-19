package com.grupo9.edext.grupo9;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Suite de Pruebas - Capa GUI")
@SelectPackages("com.grupo9.edext.grupo9.estacion_de_trabajo.gui")
public class PruebasGuiSuite {
}
