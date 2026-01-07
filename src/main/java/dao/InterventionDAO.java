package dao;

import entities.Intervention;
import entities.InterventionType;
import entities.Vehicle;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.*;

public class InterventionDAO {

    public void save(Intervention intervention) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            // Reattach detached entities to the persistence context
            if (intervention.getVehicle() != null && intervention.getVehicle().getId() != null) {
                Vehicle managedVehicle = em.find(Vehicle.class, intervention.getVehicle().getId());
                intervention.setVehicle(managedVehicle);
            }
            if (intervention.getInterventionType() != null && intervention.getInterventionType().getId() != null) {
                InterventionType managedType = em.find(InterventionType.class,
                        intervention.getInterventionType().getId());
                intervention.setInterventionType(managedType);
            }
            em.persist(intervention);
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

    public Intervention findById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(Intervention.class, id);
        } finally {
            em.close();
        }
    }

    public void removeIntervention(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            Intervention intervention = em.find(Intervention.class, id);
            et.begin();
            em.remove(intervention);
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

    public void setInterventionDate(Long id, Date date) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            Intervention intervention = em.find(Intervention.class, id);
            et.begin();
            // À ADAPTER : Vérifiez le nom du setter dans votre entité Intervention
            intervention.setDate(date);
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

    public void setDate(Long id, Date date) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            Intervention intervention = em.find(Intervention.class, id);
            et.begin();
            intervention.setDate(date);
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

    public void setPrice(Long id, double Price) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            Intervention intervention = em.find(Intervention.class, id);
            et.begin();
            intervention.setPrice(Price);
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

    public List<Intervention> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT r FROM Intervention r",
                    Intervention.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Intervention> findByVehicle(Vehicle vehicle) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT i FROM Intervention i WHERE i.vehicle = :vehicle",
                    Intervention.class)
                    .setParameter("vehicle", vehicle)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Intervention> findByInterventionType(InterventionType interventionType) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT i FROM Intervention i WHERE i.interventionType = :interventionType",
                    Intervention.class)
                    .setParameter("interventionType", interventionType)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}