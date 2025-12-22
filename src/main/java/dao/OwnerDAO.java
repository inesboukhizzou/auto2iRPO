package dao;

import entities.Owner;
import utils.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class OwnerDAO {


    public void save(Owner owner) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(owner);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error saving owner", e);
        } finally {
            em.close();
        }
    }


    public Owner findById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        Owner owner = null;
        try {
            owner = em.find(Owner.class, id);
        } finally {
            em.close();
        }
        return owner;
    }


    public List<Owner> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Owner> owners = null;
        try {
            TypedQuery<Owner> query = em.createQuery(
                    "SELECT o FROM Owner o ORDER BY o.lastName, o.firstName",
                    Owner.class
            );
            owners = query.getResultList();
        } finally {
            em.close();
        }
        return owners;
    }


    public List<Owner> findByLastName(String lastName) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Owner> owners = null;
        try {
            TypedQuery<Owner> query = em.createQuery(
                    "SELECT o FROM Owner o WHERE o.lastName LIKE :lastName ORDER BY o.firstName",
                    Owner.class
            );
            query.setParameter("lastName", "%" + lastName + "%");
            owners = query.getResultList();
        } finally {
            em.close();
        }
        return owners;
    }

    public Owner findByEmail(String email) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        Owner owner = null;
        try {
            TypedQuery<Owner> query = em.createQuery(
                    "SELECT o FROM Owner o WHERE o.email = :email",
                    Owner.class
            );
            query.setParameter("email", email);
            List<Owner> results = query.getResultList();
            if (!results.isEmpty()) {
                owner = results.get(0);
            }
        } finally {
            em.close();
        }
        return owner;
    }


    public Owner findByPhoneNumber(String phoneNumber) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        Owner owner = null;
        try {
            TypedQuery<Owner> query = em.createQuery(
                    "SELECT o FROM Owner o WHERE o.phoneNumber = :phoneNumber",
                    Owner.class
            );
            query.setParameter("phoneNumber", phoneNumber);
            List<Owner> results = query.getResultList();
            if (!results.isEmpty()) {
                owner = results.get(0);
            }
        } finally {
            em.close();
        }
        return owner;
    }


    public void update(Owner owner) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(owner);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error updating owner", e);
        } finally {
            em.close();
        }
    }


    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            Owner owner = em.find(Owner.class, id);
            if (owner != null) {
                em.remove(owner);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error deleting owner", e);
        } finally {
            em.close();
        }
    }


    public long count() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        long count = 0;
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(o) FROM Owner o",
                    Long.class
            );
            count = query.getSingleResult();
        } finally {
            em.close();
        }
        return count;
    }
}