package dao;

import entities.Registration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;

import java.util.List;

public class RegistrationDAO {
    public void create(Registration registration) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(registration);
            et.commit();
        } catch (RuntimeException e) {
            if (et.isActive()) {
                et.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Registration findById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(Registration.class, id);
        } finally {
            em.close();
        }
    }

    public List<Registration> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT r FROM Registration r",
                    Registration.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void setPart1(Long id, String part1) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            Registration registration = em.find(Registration.class, id);
            et.begin();
            registration.setPart1(part1);
            et.commit();
        } catch (RuntimeException e) {
            if (et.isActive()) {
                et.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void setPart2(Long id, int part2) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            Registration registration = em.find(Registration.class, id);
            et.begin();
            registration.setPart2(part2);
            et.commit();
        } catch (RuntimeException e) {
            if (et.isActive()) {
                et.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void setPart3(Long id, String part3) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            Registration registration = em.find(Registration.class, id);
            et.begin();
            registration.setPart3(part3);
            et.commit();
        } catch (RuntimeException e) {
            if (et.isActive()) {
                et.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void remove(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            Registration registration = em.find(Registration.class, id);
            et.begin();
            em.remove(registration);
            et.commit();
        } catch (RuntimeException e) {
            if (et.isActive()) {
                et.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Recherche une immatriculation par ses trois parties (format SIV : AA-123-BB)
     * 
     * @param part1 Première partie (lettres)
     * @param part2 Partie centrale (chiffres)
     * @param part3 Troisième partie (lettres)
     * @return L'immatriculation trouvée ou null si non trouvée
     */
    public Registration findByParts(String part1, int part2, String part3) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            List<Registration> results = em.createQuery(
                    "SELECT r FROM Registration r WHERE r.part1 = :part1 AND r.part2 = :part2 AND r.part3 = :part3",
                    Registration.class)
                    .setParameter("part1", part1)
                    .setParameter("part2", part2)
                    .setParameter("part3", part3)
                    .getResultList();

            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }
}
