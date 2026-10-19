package com.grupo9.edext.grupo9;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Suite de Pruebas - Capa Lógica")
@SelectPackages("com.grupo9.edext.grupo9.servidor_central.controller")
public class PruebasLogicaSuite {
}
