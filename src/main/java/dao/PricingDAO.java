package dao;

import entities.Pricing;
import entities.InterventionType;
import entities.VehicleType;
import utils.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class PricingDAO {

    /**
     * Save a new pricing to the database
     */
    public void save(Pricing pricing) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pricing);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error saving pricing", e);
        } finally {
            em.close();
        }
    }

    /**
     * Find a pricing by ID
     */
    public Pricing findById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        Pricing pricing = null;
        try {
            pricing = em.find(Pricing.class, id);
        } finally {
            em.close();
        }
        return pricing;
    }

    /**
     * Find all pricings
     */
    public List<Pricing> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Pricing> pricings = null;
        try {
            TypedQuery<Pricing> query = em.createQuery(
                    "SELECT p FROM Pricing p ORDER BY p.interventionType.name, p.vehicleType.brand",
                    Pricing.class
            );
            pricings = query.getResultList();
        } finally {
            em.close();
        }
        return pricings;
    }

    /**
     * Find pricing by intervention type and vehicle type
     * This is the most important method for getting the price of an intervention
     */
    public Pricing findByInterventionAndVehicleType(Long interventionTypeId, Long vehicleTypeId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        Pricing pricing = null;
        try {
            TypedQuery<Pricing> query = em.createQuery(
                    "SELECT p FROM Pricing p WHERE p.interventionType.id = :interventionTypeId " +
                            "AND p.vehicleType.id = :vehicleTypeId",
                    Pricing.class
            );
            query.setParameter("interventionTypeId", interventionTypeId);
            query.setParameter("vehicleTypeId", vehicleTypeId);

            List<Pricing> results = query.getResultList();
            if (!results.isEmpty()) {
                pricing = results.get(0);
            }
        } finally {
            em.close();
        }
        return pricing;
    }

    /**
     * Find pricing by intervention type and vehicle type (using objects)
     */
    public Pricing findByInterventionAndVehicleType(InterventionType interventionType, VehicleType vehicleType) {
        return findByInterventionAndVehicleType(interventionType.getId(), vehicleType.getId());
    }

    /**
     * Find all pricings for a specific intervention type
     */
    public List<Pricing> findByInterventionType(Long interventionTypeId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Pricing> pricings = null;
        try {
            TypedQuery<Pricing> query = em.createQuery(
                    "SELECT p FROM Pricing p WHERE p.interventionType.id = :interventionTypeId " +
                            "ORDER BY p.vehicleType.brand, p.vehicleType.model",
                    Pricing.class
            );
            query.setParameter("interventionTypeId", interventionTypeId);
            pricings = query.getResultList();
        } finally {
            em.close();
        }
        return pricings;
    }

    /**
     * Find all pricings for a specific vehicle type
     */
    public List<Pricing> findByVehicleType(Long vehicleTypeId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Pricing> pricings = null;
        try {
            TypedQuery<Pricing> query = em.createQuery(
                    "SELECT p FROM Pricing p WHERE p.vehicleType.id = :vehicleTypeId " +
                            "ORDER BY p.interventionType.name",
                    Pricing.class
            );
            query.setParameter("vehicleTypeId", vehicleTypeId);
            pricings = query.getResultList();
        } finally {
            em.close();
        }
        return pricings;
    }

    /**
     * Find pricings by price range
     */
    public List<Pricing> findByPriceRange(double minPrice, double maxPrice) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Pricing> pricings = null;
        try {
            TypedQuery<Pricing> query = em.createQuery(
                    "SELECT p FROM Pricing p WHERE p.price >= :minPrice AND p.price <= :maxPrice " +
                            "ORDER BY p.price",
                    Pricing.class
            );
            query.setParameter("minPrice", minPrice);
            query.setParameter("maxPrice", maxPrice);
            pricings = query.getResultList();
        } finally {
            em.close();
        }
        return pricings;
    }

    /**
     * Get the price for an intervention on a specific vehicle type
     * Returns 0.0 if no pricing found
     */
    public double getPrice(Long interventionTypeId, Long vehicleTypeId) {
        Pricing pricing = findByInterventionAndVehicleType(interventionTypeId, vehicleTypeId);
        return pricing != null ? pricing.getPrice() : 0.0;
    }

    /**
     * Get the price for an intervention on a specific vehicle type (using objects)
     */
    public double getPrice(InterventionType interventionType, VehicleType vehicleType) {
        return getPrice(interventionType.getId(), vehicleType.getId());
    }

    /**
     * Update an existing pricing
     */
    public void update(Pricing pricing) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(pricing);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error updating pricing", e);
        } finally {
            em.close();
        }
    }

    /**
     * Delete a pricing by ID
     */
    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            Pricing pricing = em.find(Pricing.class, id);
            if (pricing != null) {
                em.remove(pricing);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error deleting pricing", e);
        } finally {
            em.close();
        }
    }

    /**
     * Count total number of pricings
     */
    public long count() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        long count = 0;
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(p) FROM Pricing p",
                    Long.class
            );
            count = query.getSingleResult();
        } finally {
            em.close();
        }
        return count;
    }
}