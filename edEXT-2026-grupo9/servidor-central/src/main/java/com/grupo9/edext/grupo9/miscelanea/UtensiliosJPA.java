package com.grupo9.edext.grupo9.miscelanea;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.Properties;

public class UtensiliosJPA {
    private static EntityManagerFactory emf;

    // para no crear un EntityManagerFactory nuevo cada vez que lo usemos
    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("edEXT_PU", databaseProperties());
        }
        return emf;
    }

    private static Properties databaseProperties() {
        Properties properties = new Properties();

        // 1. URL de la BD
        String url = ConfigLoader.get("db.url", null);
        if (url == null || url.isBlank()) {
            url = ConfigLoader.get("jakarta.persistence.jdbc.url", null);
        }
        if (url == null || url.isBlank()) {
            url = ConfigLoader.get("ED_EXT_DB_URL", null);
        }
        if (url != null && !url.isBlank()) {
            properties.setProperty("jakarta.persistence.jdbc.url", url);
        }

        // 2. Usuario de la BD
        String user = ConfigLoader.get("db.user", null);
        if (user == null || user.isBlank()) {
            user = ConfigLoader.get("jakarta.persistence.jdbc.user", null);
        }
        if (user == null || user.isBlank()) {
            user = ConfigLoader.get("ED_EXT_DB_USER", null);
        }
        if (user != null && !user.isBlank()) {
            properties.setProperty("jakarta.persistence.jdbc.user", user);
        }

        // 3. Password de la BD
        String password = ConfigLoader.get("db.password", null);
        if (password == null) {
            password = ConfigLoader.get("jakarta.persistence.jdbc.password", null);
        }
        if (password == null) {
            password = ConfigLoader.get("ED_EXT_DB_PASSWORD", null);
        }
        if (password != null) {
            properties.setProperty("jakarta.persistence.jdbc.password", password);
        }

        return properties;
    }

    public static <T> void save(T record) {
        try {
            EntityManager em = getEntityManagerFactory().createEntityManager();
            EntityTransaction et = em.getTransaction();
            try {
                et.begin();
                em.persist(record);
                et.commit();
            } catch (Exception e) {
                et.rollback();
            }

            em.close();
        } catch (Exception e) {
            throw e;
        }
    }
}
