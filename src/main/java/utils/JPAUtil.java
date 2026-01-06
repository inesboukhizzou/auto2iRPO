package utils;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory emf=Persistence.createEntityManagerFactory("auto2iPU");;

    public JPAUtil() {
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static void close() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}