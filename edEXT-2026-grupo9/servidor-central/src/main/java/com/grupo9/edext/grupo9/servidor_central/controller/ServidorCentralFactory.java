package com.grupo9.edext.grupo9.servidor_central.controller;

import com.grupo9.edext.grupo9.interfaces.IServidorCentral;

public class ServidorCentralFactory {
    private ServidorCentralFactory() {}

    public static IServidorCentral getServidorCentral() {
        return ServidorCentralController.getInstance();
    }
}
