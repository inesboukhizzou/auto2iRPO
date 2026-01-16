package dao;

import entities.Intervention;
import entities.InterventionType;
import entities.Vehicle;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * DAO for Intervention entity.
 * Prevents duplicate interventions (same vehicle + same intervention type +
 * same date).
 */
public class InterventionDAO {

    /**
     * Saves a new intervention if it's not a duplicate.
     * 
     * @param intervention The intervention to save
     * @throws IllegalArgumentException if an intervention for the same vehicle and
     *                                  type exists on the same date
     */
    public void save(Intervention intervention) {

        if (existsByVehicleTypeAndDate(intervention.getVehicle(),
                intervention.getInterventionType(),
                intervention.getDate())) {
            throw new IllegalArgumentException(
                    "An intervention of this type already exists for this vehicle on this date.");
        }

        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();

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

    /**
     * Checks if an intervention exists for the given vehicle, type, and date.
     * Compares dates by day only (ignoring time).
     * 
     * @param vehicle          The vehicle
     * @param interventionType The intervention type
     * @param date             The date
     * @return true if exists, false otherwise
     */
    public boolean existsByVehicleTypeAndDate(Vehicle vehicle, InterventionType interventionType, Date date) {
        if (vehicle == null || vehicle.getId() == null ||
                interventionType == null || interventionType.getId() == null || date == null) {
            return false;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startOfDay = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date endOfDay = cal.getTime();

        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(i) FROM Intervention i WHERE i.vehicle.id = :vehicleId " +
                            "AND i.interventionType.id = :typeId " +
                            "AND i.date >= :startOfDay AND i.date < :endOfDay",
                    Long.class)
                    .setParameter("vehicleId", vehicle.getId())
                    .setParameter("typeId", interventionType.getId())
                    .setParameter("startOfDay", startOfDay)
                    .setParameter("endOfDay", endOfDay)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    /**
     * Finds all interventions for a vehicle on a specific date.
     * 
     * @param vehicle The vehicle
     * @param date    The date
     * @return List of interventions on that date
     */
    public List<Intervention> findByVehicleAndDate(Vehicle vehicle, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startOfDay = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date endOfDay = cal.getTime();

        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT i FROM Intervention i WHERE i.vehicle = :vehicle " +
                            "AND i.date >= :startOfDay AND i.date < :endOfDay",
                    Intervention.class)
                    .setParameter("vehicle", vehicle)
                    .setParameter("startOfDay", startOfDay)
                    .setParameter("endOfDay", endOfDay)
                    .getResultList();
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

    public void remove(Long id) {
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

    public void setPrice(Long id, double price) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            Intervention intervention = em.find(Intervention.class, id);
            et.begin();
            intervention.setPrice(price);
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
                    "SELECT i FROM Intervention i LEFT JOIN FETCH i.vehicle LEFT JOIN FETCH i.interventionType",
                    Intervention.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Intervention> findByVehicle(Vehicle vehicle) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT i FROM Intervention i LEFT JOIN FETCH i.interventionType WHERE i.vehicle = :vehicle ORDER BY i.date DESC",
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