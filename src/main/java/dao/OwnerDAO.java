package dao;

import entities.Owner;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;

import java.util.List;

/**
 * DAO for Owner entity.
 * Prevents duplicate owners by checking firstName + lastName + phoneNumber
 * combination.
 */
public class OwnerDAO {

    /**
     * Creates a new owner if not a duplicate.
     * 
     * @param owner The owner to create
     * @throws IllegalArgumentException if an owner with the same name and phone
     *                                  already exists
     */
    public void create(Owner owner) {
        // Check for duplicates
        if (existsByNameAndPhone(owner.getFirstName(), owner.getLastName(), owner.getPhoneNumber())) {
            throw new IllegalArgumentException(
                    "An owner with the name " + owner.getFirstName() + " " + owner.getLastName() +
                            " and phone number " + owner.getPhoneNumber() + " already exists.");
        }

        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(owner);
            et.commit();
        } catch (RuntimeException re) {
            if (et.isActive()) {
                et.rollback();
            }
            throw re;
        } finally {
            em.close();
        }
    }

    /**
     * Checks if an owner with the same firstName, lastName and phoneNumber already
     * exists.
     * 
     * @param firstName   First name
     * @param lastName    Last name
     * @param phoneNumber Phone number
     * @return true if exists, false otherwise
     */
    public boolean existsByNameAndPhone(String firstName, String lastName, String phoneNumber) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(o) FROM Owner o WHERE LOWER(o.firstName) = LOWER(:firstName) " +
                            "AND LOWER(o.lastName) = LOWER(:lastName) AND o.phoneNumber = :phoneNumber",
                    Long.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName)
                    .setParameter("phoneNumber", phoneNumber)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    /**
     * Finds an owner by firstName, lastName and phoneNumber.
     * 
     * @param firstName   First name
     * @param lastName    Last name
     * @param phoneNumber Phone number
     * @return The owner found or null if not found
     */
    public Owner findByNameAndPhone(String firstName, String lastName, String phoneNumber) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            List<Owner> results = em.createQuery(
                    "SELECT o FROM Owner o WHERE LOWER(o.firstName) = LOWER(:firstName) " +
                            "AND LOWER(o.lastName) = LOWER(:lastName) AND o.phoneNumber = :phoneNumber",
                    Owner.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName)
                    .setParameter("phoneNumber", phoneNumber)
                    .getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }

    /**
     * Finds or creates an owner. If the owner already exists, returns the existing
     * one.
     * 
     * @param firstName   First name
     * @param lastName    Last name
     * @param phoneNumber Phone number
     * @param email       Email address
     * @return The existing or newly created owner
     */
    public Owner findOrCreate(String firstName, String lastName, String phoneNumber, String email) {
        Owner existing = findByNameAndPhone(firstName, lastName, phoneNumber);
        if (existing != null) {
            return existing;
        }

        Owner newOwner = new Owner(firstName, lastName, phoneNumber, email);
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(newOwner);
            et.commit();
            return newOwner;
        } catch (RuntimeException re) {
            if (et.isActive()) {
                et.rollback();
            }
            throw re;
        } finally {
            em.close();
        }
    }

    public Owner findById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(Owner.class, id);
        } finally {
            em.close();
        }
    }

    public List<Owner> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT o FROM Owner o",
                    Owner.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void remove(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            Owner owner = em.find(Owner.class, id);
            et.begin();
            em.remove(owner);
            et.commit();
        } catch (RuntimeException re) {
            if (et.isActive()) {
                et.rollback();
            }
            throw re;
        } finally {
            em.close();
        }
    }

    public void setEmail(Long id, String email) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            Owner owner = em.find(Owner.class, id);
            et.begin();
            owner.setEmail(email);
            et.commit();
        } catch (RuntimeException re) {
            if (et.isActive()) {
                et.rollback();
            }
            throw re;
        } finally {
            em.close();
        }
    }

    public void setPhoneNumber(Long id, String phoneNumber) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            Owner owner = em.find(Owner.class, id);
            et.begin();
            owner.setPhoneNumber(phoneNumber);
            et.commit();
        } catch (RuntimeException re) {
            if (et.isActive()) {
                et.rollback();
            }
            throw re;
        } finally {
            em.close();
        }
    }
}