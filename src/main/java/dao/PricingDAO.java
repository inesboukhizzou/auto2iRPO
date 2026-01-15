package dao;

import entities.InterventionType;
import entities.Pricing;
import entities.VehicleType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;

import java.util.List;

/**
 * DAO for Pricing entity.
 * Manages price lookups based on intervention type and vehicle type
 * combinations.
 */
public class PricingDAO {

    public void create(Pricing pricing) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(pricing);
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

    public Pricing findById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(Pricing.class, id);
        } finally {
            em.close();
        }
    }

    public List<Pricing> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM Pricing p",
                    Pricing.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Finds the price for a specific intervention type and vehicle type
     * combination.
     * This is the core method for price calculation.
     * 
     * @param interventionType The type of intervention
     * @param vehicleType      The type of vehicle
     * @return The Pricing entity if found, null otherwise
     */
    public Pricing findByInterventionTypeAndVehicleType(InterventionType interventionType, VehicleType vehicleType) {
        if (interventionType == null || vehicleType == null) {
            return null;
        }

        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            List<Pricing> results = em.createQuery(
                    "SELECT p FROM Pricing p WHERE p.interventionType.id = :typeId AND p.vehicleType.id = :vehicleId",
                    Pricing.class)
                    .setParameter("typeId", interventionType.getId())
                    .setParameter("vehicleId", vehicleType.getId())
                    .getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }

    /**
     * Gets all pricing rules for a specific intervention type.
     * 
     * @param interventionType The intervention type
     * @return List of pricing rules for that type
     */
    public List<Pricing> findByInterventionType(InterventionType interventionType) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM Pricing p WHERE p.interventionType = :type",
                    Pricing.class)
                    .setParameter("type", interventionType)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Gets all pricing rules for a specific vehicle type.
     * 
     * @param vehicleType The vehicle type
     * @return List of pricing rules for that vehicle type
     */
    public List<Pricing> findByVehicleType(VehicleType vehicleType) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM Pricing p WHERE p.vehicleType = :type",
                    Pricing.class)
                    .setParameter("type", vehicleType)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void setPrice(Long id, double price) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            Pricing pricing = em.find(Pricing.class, id);
            et.begin();
            pricing.setPrice(price);
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
            Pricing pricing = em.find(Pricing.class, id);
            et.begin();
            em.remove(pricing);
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
