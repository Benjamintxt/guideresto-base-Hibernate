package ch.hearc.ig.guideresto.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
//not used - Handled in the main
public class JpaUtils {
    private static EntityManagerFactory emf;
    private static EntityManager em;

    public static EntityManager getEntityManager() {
        if (em == null || !em.isOpen()) {
            if (emf == null) {
                emf = Persistence.createEntityManagerFactory("GuideRestoPersistenceUnit");
            }
            em = emf.createEntityManager();
        }
        return em;
    }
}
