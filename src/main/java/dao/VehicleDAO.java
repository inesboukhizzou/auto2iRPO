package dao;

import entities.Vehicle;
import utils.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class VehicleDAO {

    /**
     * Save a new vehicle to the database
     * @param vehicle The vehicle to save
     */
    public void save(Vehicle vehicle) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(vehicle);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error saving vehicle", e);
        } finally {
            em.close();
        }
    }

    /**
     * Find a vehicle by its ID
     * @param id The vehicle ID
     * @return The vehicle or null if not found
     */
    public Vehicle findById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        Vehicle vehicle = null;
        try {
            vehicle = em.find(Vehicle.class, id);
        } finally {
            em.close();
        }
        return vehicle;
    }

    /**
     * Find a vehicle by its registration number
     * @param registration The full registration (e.g., "AB-123-CD")
     * @return The vehicle or null if not found
     */
    public Vehicle findByRegistration(String registration) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        Vehicle vehicle = null;
        try {
            // Parse registration string (format: part1-part2-part3)
            String[] parts = registration.split("-");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid registration format. Expected: XXX-999-XXX");
            }

            String part1 = parts[0];
            int part2 = Integer.parseInt(parts[1]);
            String part3 = parts[2];

            TypedQuery<Vehicle> query = em.createQuery(
                    "SELECT v FROM Vehicle v WHERE v.registration.part1 = :part1 " +
                            "AND v.registration.part2 = :part2 AND v.registration.part3 = :part3",
                    Vehicle.class
            );
            query.setParameter("part1", part1);
            query.setParameter("part2", part2);
            query.setParameter("part3", part3);

            List<Vehicle> results = query.getResultList();
            if (!results.isEmpty()) {
                vehicle = results.get(0);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid registration format: " + registration);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return vehicle;
    }

    /**
     * Find all vehicles
     * @return List of all vehicles
     */
    public List<Vehicle> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Vehicle> vehicles = null;
        try {
            TypedQuery<Vehicle> query = em.createQuery(
                    "SELECT v FROM Vehicle v ORDER BY v.registration.part1, v.registration.part2",
                    Vehicle.class
            );
            vehicles = query.getResultList();
        } finally {
            em.close();
        }
        return vehicles;
    }

    /**
     * Find all vehicles by owner
     * @param ownerId The owner ID
     * @return List of vehicles belonging to the owner
     */
    public List<Vehicle> findByOwner(Long ownerId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Vehicle> vehicles = null;
        try {
            TypedQuery<Vehicle> query = em.createQuery(
                    "SELECT v FROM Vehicle v WHERE v.owner.id = :ownerId " +
                            "ORDER BY v.registration.part1, v.registration.part2",
                    Vehicle.class
            );
            query.setParameter("ownerId", ownerId);
            vehicles = query.getResultList();
        } finally {
            em.close();
        }
        return vehicles;
    }

    /**
     * Find all vehicles of a specific type
     * @param vehicleTypeId The vehicle type ID
     * @return List of vehicles of this type
     */
    public List<Vehicle> findByVehicleType(Long vehicleTypeId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        List<Vehicle> vehicles = null;
        try {
            TypedQuery<Vehicle> query = em.createQuery(
                    "SELECT v FROM Vehicle v WHERE v.vehicleType.id = :typeId",
                    Vehicle.class
            );
            query.setParameter("typeId", vehicleTypeId);
            vehicles = query.getResultList();
        } finally {
            em.close();
        }
        return vehicles;
    }

    /**
     * Update an existing vehicle
     * @param vehicle The vehicle to update
     */
    public void update(Vehicle vehicle) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(vehicle);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error updating vehicle", e);
        } finally {
            em.close();
        }
    }

    /**
     * Delete a vehicle by ID
     * @param id The vehicle ID
     */
    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            Vehicle vehicle = em.find(Vehicle.class, id);
            if (vehicle != null) {
                em.remove(vehicle);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error deleting vehicle", e);
        } finally {
            em.close();
        }
    }

    /**
     * Count total number of vehicles
     * @return Number of vehicles
     */
    public long count() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        long count = 0;
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(v) FROM Vehicle v",
                    Long.class
            );
            count = query.getSingleResult();
        } finally {
            em.close();
        }
        return count;
    }
}