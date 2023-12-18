package ch.hearc.ig.guideresto.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateService {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private HibernateService() {
    }

    private static SessionFactory buildSessionFactory() {
        try {
            // Using JPA EntityManagerFactory instead of Hibernate Configuration
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GuideRestoPersistenceUnit");

            // Extracting Hibernate SessionFactory from JPA EntityManagerFactory
            SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

            if (sessionFactory == null) {
                throw new IllegalStateException("EntityManagerFactory is not a Hibernate EntityManagerFactory");
            }

            return sessionFactory;
        } catch (Exception ex) {
            throw new ExceptionInInitializerError("Failed to create session factory: " + ex.getMessage());
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
