package dao;

import entities.Registration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;

import java.util.List;

/**
 * DAO for Registration entity.
 * Prevents duplicate license plates by checking before creation.
 */
public class RegistrationDAO {

    /**
     * Creates a new registration if it doesn't already exist.
     * 
     * @param registration The registration to create
     * @throws IllegalArgumentException if a registration with the same plate
     *                                  already exists
     */
    public void create(Registration registration) {
        
        if (existsByParts(registration.getPart1(), registration.getPart2(), registration.getPart3())) {
            throw new IllegalArgumentException(
                    "A registration with license plate " +
                            registration.getPart1() + "-" + registration.getPart2() + "-" + registration.getPart3() +
                            " already exists.");
        }

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

    /**
     * Checks if a registration with the given parts already exists.
     * 
     * @param part1 First part (2 letters)
     * @param part2 Middle part (3 digits)
     * @param part3 Third part (2 letters)
     * @return true if exists, false otherwise
     */
    public boolean existsByParts(String part1, int part2, String part3) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(r) FROM Registration r WHERE UPPER(r.part1) = UPPER(:part1) AND r.part2 = :part2 AND UPPER(r.part3) = UPPER(:part3)",
                    Long.class)
                    .setParameter("part1", part1)
                    .setParameter("part2", part2)
                    .setParameter("part3", part3)
                    .getSingleResult();
            return count > 0;
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

    /**
     * Finds a registration by its three parts (SIV format: AA-123-BB).
     * 
     * @param part1 First part (letters)
     * @param part2 Middle part (digits)
     * @param part3 Third part (letters)
     * @return The registration found or null if not found
     */
    public Registration findByParts(String part1, int part2, String part3) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            List<Registration> results = em.createQuery(
                    "SELECT r FROM Registration r WHERE UPPER(r.part1) = UPPER(:part1) AND r.part2 = :part2 AND UPPER(r.part3) = UPPER(:part3)",
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

    /**
     * Finds or creates a registration. If the plate already exists, returns the
     * existing one.
     * 
     * @param part1 First part
     * @param part2 Middle part
     * @param part3 Third part
     * @return The existing or newly created registration
     */
    public Registration findOrCreate(String part1, int part2, String part3) {
        Registration existing = findByParts(part1, part2, part3);
        if (existing != null) {
            return existing;
        }

        Registration newReg = new Registration(part1, part2, part3);
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(newReg);
            et.commit();
            return newReg;
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
}
