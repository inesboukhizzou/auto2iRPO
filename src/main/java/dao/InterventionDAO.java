package dao;

import entities.Intervention;
import entities.InterventionType;
import entities.Vehicle;
import utils.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class InterventionDAO {

    /**
     * Save a new intervention to the database
     */
    public void save(Intervention intervention) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(intervention);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error saving intervention", e);
        } finally {
            em.close();
        }
    }

    /**
     * Find an intervention by ID
     */
    public Intervention findById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        Intervention intervention = null;
        try {
            intervention = em.find(Intervention.class, id);
        } finally {
            em.close();
        }
        return intervention;
    }

    /**
     * Find all interventions
     */
    public List<Intervention> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Intervention> interventions = null;
        try {
            TypedQuery<Intervention> query = em.createQuery(
                    "SELECT i FROM Intervention i ORDER BY i.date DESC",
                    Intervention.class
            );
            interventions = query.getResultList();
        } finally {
            em.close();
        }
        return interventions;
    }

    /**
     * Find all interventions for a specific vehicle
     * IMPORTANT: This is used to display vehicle history
     */
    public List<Intervention> findByVehicle(Long vehicleId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Intervention> interventions = null;
        try {
            TypedQuery<Intervention> query = em.createQuery(
                    "SELECT i FROM Intervention i WHERE i.vehicle.id = :vehicleId ORDER BY i.date DESC",
                    Intervention.class
            );
            query.setParameter("vehicleId", vehicleId);
            interventions = query.getResultList();
        } finally {
            em.close();
        }
        return interventions;
    }

    /**
     * Find all interventions for a specific vehicle object
     */
    public List<Intervention> findByVehicle(Vehicle vehicle) {
        return findByVehicle(vehicle.getId());
    }

    /**
     * Find the last intervention of a specific type for a vehicle
     * CRITICAL: Used to calculate when next maintenance is due
     */
    public Intervention findLastInterventionByType(Long vehicleId, Long interventionTypeId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        Intervention intervention = null;
        try {
            TypedQuery<Intervention> query = em.createQuery(
                    "SELECT i FROM Intervention i WHERE i.vehicle.id = :vehicleId " +
                            "AND i.interventionType.id = :typeId ORDER BY i.date DESC",
                    Intervention.class
            );
            query.setParameter("vehicleId", vehicleId);
            query.setParameter("typeId", interventionTypeId);
            query.setMaxResults(1);

            List<Intervention> results = query.getResultList();
            if (!results.isEmpty()) {
                intervention = results.get(0);
            }
        } finally {
            em.close();
        }
        return intervention;
    }

    /**
     * Find the last intervention of a specific type for a vehicle (using objects)
     */
    public Intervention findLastInterventionByType(Vehicle vehicle, InterventionType type) {
        return findLastInterventionByType(vehicle.getId(), type.getId());
    }

    /**
     * Find interventions by date range
     */
    public List<Intervention> findByDateRange(Date startDate, Date endDate) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Intervention> interventions = null;
        try {
            TypedQuery<Intervention> query = em.createQuery(
                    "SELECT i FROM Intervention i WHERE i.date BETWEEN :startDate AND :endDate " +
                            "ORDER BY i.date DESC",
                    Intervention.class
            );
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            interventions = query.getResultList();
        } finally {
            em.close();
        }
        return interventions;
    }

    /**
     * Find interventions by intervention type
     */
    public List<Intervention> findByInterventionType(Long interventionTypeId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Intervention> interventions = null;
        try {
            TypedQuery<Intervention> query = em.createQuery(
                    "SELECT i FROM Intervention i WHERE i.interventionType.id = :typeId " +
                            "ORDER BY i.date DESC",
                    Intervention.class
            );
            query.setParameter("typeId", interventionTypeId);
            interventions = query.getResultList();
        } finally {
            em.close();
        }
        return interventions;
    }

    /**
     * Find interventions by price range
     */
    public List<Intervention> findByPriceRange(double minPrice, double maxPrice) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Intervention> interventions = null;
        try {
            TypedQuery<Intervention> query = em.createQuery(
                    "SELECT i FROM Intervention i WHERE i.price >= :minPrice AND i.price <= :maxPrice " +
                            "ORDER BY i.date DESC",
                    Intervention.class
            );
            query.setParameter("minPrice", minPrice);
            query.setParameter("maxPrice", maxPrice);
            interventions = query.getResultList();
        } finally {
            em.close();
        }
        return interventions;
    }

    /**
     * Calculate total cost of interventions for a vehicle
     * Useful for statistics
     */
    public double getTotalCostByVehicle(Long vehicleId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        double total = 0.0;
        try {
            TypedQuery<Double> query = em.createQuery(
                    "SELECT COALESCE(SUM(i.price), 0.0) FROM Intervention i WHERE i.vehicle.id = :vehicleId",
                    Double.class
            );
            query.setParameter("vehicleId", vehicleId);
            total = query.getSingleResult();
        } finally {
            em.close();
        }
        return total;
    }

    /**
     * Get the most recent intervention for a vehicle
     */
    public Intervention findMostRecentByVehicle(Long vehicleId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        Intervention intervention = null;
        try {
            TypedQuery<Intervention> query = em.createQuery(
                    "SELECT i FROM Intervention i WHERE i.vehicle.id = :vehicleId ORDER BY i.date DESC",
                    Intervention.class
            );
            query.setParameter("vehicleId", vehicleId);
            query.setMaxResults(1);

            List<Intervention> results = query.getResultList();
            if (!results.isEmpty()) {
                intervention = results.get(0);
            }
        } finally {
            em.close();
        }
        return intervention;
    }

    /**
     * Update an existing intervention
     */
    public void update(Intervention intervention) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(intervention);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error updating intervention", e);
        } finally {
            em.close();
        }
    }

    /**
     * Delete an intervention by ID
     */
    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            Intervention intervention = em.find(Intervention.class, id);
            if (intervention != null) {
                em.remove(intervention);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error deleting intervention", e);
        } finally {
            em.close();
        }
    }

    /**
     * Count total number of interventions
     */
    public long count() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        long count = 0;
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(i) FROM Intervention i",
                    Long.class
            );
            count = query.getSingleResult();
        } finally {
            em.close();
        }
        return count;
    }

    /**
     * Count interventions for a specific vehicle
     */
    public long countByVehicle(Long vehicleId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        long count = 0;
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(i) FROM Intervention i WHERE i.vehicle.id = :vehicleId",
                    Long.class
            );
            query.setParameter("vehicleId", vehicleId);
            count = query.getSingleResult();
        } finally {
            em.close();
        }
        return count;
    }
}