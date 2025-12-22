package dao;

import entities.VehicleType;
import utils.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class VehicleTypeDAO {


    public void save(VehicleType vehicleType) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(vehicleType);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error saving vehicle type", e);
        } finally {
            em.close();
        }
    }


    public VehicleType findById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        VehicleType vehicleType = null;
        try {
            vehicleType = em.find(VehicleType.class, id);
        } finally {
            em.close();
        }
        return vehicleType;
    }

    /**
     * Find vehicle type by brand and model
     * @param brand The brand name
     * @param model The model name
     * @return The vehicle type or null if not found
     */
    public VehicleType findByBrandAndModel(String brand, String model) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        VehicleType vehicleType = null;
        try {
            TypedQuery<VehicleType> query = em.createQuery(
                    "SELECT vt FROM VehicleType vt WHERE vt.brand = :brand AND vt.model = :model",
                    VehicleType.class
            );
            query.setParameter("brand", brand);
            query.setParameter("model", model);

            List<VehicleType> results = query.getResultList();
            if (!results.isEmpty()) {
                vehicleType = results.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return vehicleType;
    }

    /**
     * Find all vehicle types
     * @return List of all vehicle types
     */
    public List<VehicleType> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<VehicleType> vehicleTypes = null;
        try {
            TypedQuery<VehicleType> query = em.createQuery(
                    "SELECT vt FROM VehicleType vt ORDER BY vt.brand, vt.model",
                    VehicleType.class
            );
            vehicleTypes = query.getResultList();
        } finally {
            em.close();
        }
        return vehicleTypes;
    }

    /**
     * Find all vehicle types by brand
     * @param brand The brand name
     * @return List of vehicle types for this brand
     */
    public List<VehicleType> findByBrand(String brand) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<VehicleType> vehicleTypes = null;
        try {
            TypedQuery<VehicleType> query = em.createQuery(
                    "SELECT vt FROM VehicleType vt WHERE vt.brand = :brand ORDER BY vt.model",
                    VehicleType.class
            );
            query.setParameter("brand", brand);
            vehicleTypes = query.getResultList();
        } finally {
            em.close();
        }
        return vehicleTypes;
    }

    /**
     * Find all vehicle types by fuel type
     * @param fuelType The fuel type (essence, diesel, electric, hybrid)
     * @return List of vehicle types with this fuel type
     */
    public List<VehicleType> findByFuelType(String fuelType) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<VehicleType> vehicleTypes = null;
        try {
            TypedQuery<VehicleType> query = em.createQuery(
                    "SELECT vt FROM VehicleType vt WHERE vt.fuelType = :fuelType " +
                            "ORDER BY vt.brand, vt.model",
                    VehicleType.class
            );
            query.setParameter("fuelType", fuelType);
            vehicleTypes = query.getResultList();
        } finally {
            em.close();
        }
        return vehicleTypes;
    }

    /**
     * Find all vehicle types by gearbox type
     * @param gearbox The gearbox type (manual, automatic)
     * @return List of vehicle types with this gearbox type
     */
    public List<VehicleType> findByGearbox(String gearbox) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<VehicleType> vehicleTypes = null;
        try {
            TypedQuery<VehicleType> query = em.createQuery(
                    "SELECT vt FROM VehicleType vt WHERE vt.gearbox = :gearbox " +
                            "ORDER BY vt.brand, vt.model",
                    VehicleType.class
            );
            query.setParameter("gearbox", gearbox);
            vehicleTypes = query.getResultList();
        } finally {
            em.close();
        }
        return vehicleTypes;
    }

    /**
     * Find vehicle types with power in a range
     * @param minPower Minimum power (inclusive)
     * @param maxPower Maximum power (inclusive)
     * @return List of vehicle types in this power range
     */
    public List<VehicleType> findByPowerRange(int minPower, int maxPower) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<VehicleType> vehicleTypes = null;
        try {
            TypedQuery<VehicleType> query = em.createQuery(
                    "SELECT vt FROM VehicleType vt WHERE vt.power >= :minPower " +
                            "AND vt.power <= :maxPower ORDER BY vt.power",
                    VehicleType.class
            );
            query.setParameter("minPower", minPower);
            query.setParameter("maxPower", maxPower);
            vehicleTypes = query.getResultList();
        } finally {
            em.close();
        }
        return vehicleTypes;
    }

    /**
     * Get all distinct brands
     * @return List of unique brand names
     */
    public List<String> findAllBrands() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<String> brands = null;
        try {
            TypedQuery<String> query = em.createQuery(
                    "SELECT DISTINCT vt.brand FROM VehicleType vt ORDER BY vt.brand",
                    String.class
            );
            brands = query.getResultList();
        } finally {
            em.close();
        }
        return brands;
    }

    /**
     * Update an existing vehicle type
     * @param vehicleType The vehicle type to update
     */
    public void update(VehicleType vehicleType) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(vehicleType);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error updating vehicle type", e);
        } finally {
            em.close();
        }
    }

    /**
     * Delete a vehicle type by ID
     * @param id The vehicle type ID
     */
    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            VehicleType vehicleType = em.find(VehicleType.class, id);
            if (vehicleType != null) {
                em.remove(vehicleType);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error deleting vehicle type", e);
        } finally {
            em.close();
        }
    }

    /**
     * Count total number of vehicle types
     * @return Number of vehicle types
     */
    public long count() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        long count = 0;
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(vt) FROM VehicleType vt",
                    Long.class
            );
            count = query.getSingleResult();
        } finally {
            em.close();
        }
        return count;
    }
}