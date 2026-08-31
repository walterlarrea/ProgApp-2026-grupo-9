package com.grupo9.edext.grupo9.servidor_central.controller.instituto;

import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public class InstitutoController implements IInstituto {

    @Override
    public void altaInstituto(String nombre) throws ErrorRepetidos {
        EntityManagerFactory emf = UtensiliosJPA.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        try {
            Instituto existente = em.find(Instituto.class, nombre);
            if (existente != null) {
                throw new ErrorRepetidos("Ya existe un instituto con el nombre \"" + nombre + "\".");
            }

            Instituto nuevo = new Instituto();
            nuevo.setNombreI(nombre);

            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(nuevo);
            tx.commit();
        } finally {
            em.close();
        }
    }
}