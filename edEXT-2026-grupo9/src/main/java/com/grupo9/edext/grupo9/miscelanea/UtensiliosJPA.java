package com.grupo9.edext.grupo9.miscelanea;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class UtensiliosJPA {
    private static EntityManagerFactory emf;
    //para no crear un EntityManagerFactory nuevo cada vez que lo usemos
    public static EntityManagerFactory getEntityManagerFactory() {
        if(emf == null) {
            emf = Persistence.createEntityManagerFactory("edEXT_PU");
        }
        return emf;
    }
}
