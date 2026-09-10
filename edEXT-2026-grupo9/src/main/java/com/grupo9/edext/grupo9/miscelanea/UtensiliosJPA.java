package com.grupo9.edext.grupo9.miscelanea;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.Properties;

public class UtensiliosJPA {
    private static EntityManagerFactory emf;
    //para no crear un EntityManagerFactory nuevo cada vez que lo usemos
    public static EntityManagerFactory getEntityManagerFactory() {
        if(emf == null) {
            emf = Persistence.createEntityManagerFactory("edEXT_PU", databaseProperties());
        }
        return emf;
    }

    private static Properties databaseProperties() {
        Properties properties = new Properties();
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        setProperty(properties, "jakarta.persistence.jdbc.url", "ED_EXT_DB_URL", dotenv);
        setProperty(properties, "jakarta.persistence.jdbc.user", "ED_EXT_DB_USER", dotenv);
        setProperty(properties, "jakarta.persistence.jdbc.password", "ED_EXT_DB_PASSWORD", dotenv);
        return properties;
    }

    private static void setProperty(Properties properties, String propertyName, String environmentName, Dotenv dotenv) {
        String value = System.getProperty(propertyName);
        if(value == null || value.isBlank()) {
            value = System.getenv(environmentName);
        }
        if(value == null || value.isBlank()) {
            value = dotenv.get(environmentName);
        }
        if(value != null) {
            properties.setProperty(propertyName, value);
        }
    }
    
    public static <T> void save(T record) {
        try {
            EntityManager em = getEntityManagerFactory().createEntityManager();
            EntityTransaction et = em.getTransaction();
            try{
                et.begin();
                em.persist(record);
                et.commit();
            }catch(Exception e){
                et.rollback();
            }
            
            em.close();
        }catch(Exception e){
            throw e;
        }
    }
}
