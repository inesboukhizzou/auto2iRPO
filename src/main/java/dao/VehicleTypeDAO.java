package dao;
import entities.Vehicle;
import entities.VehicleType;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.*;

public class VehicleTypeDAO {

    public void create(VehicleType vehicleType) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            em.persist(vehicleType);
            et.commit();
        }
        catch(RuntimeException re){
            if(et.isActive()){
                et.rollback();
            }
            throw re;
        }
        finally {
            em.close();
        }
    }

    public VehicleType findById(Long id){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.find(VehicleType.class, id);
        }
        finally{
            em.close();
        }
    }

    public List<VehicleType> findAll(){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery(
                    "SELECT vt FROM VehicleType vt",
                    VehicleType.class
            ).getResultList();
        }
        finally{
            em.close();
        }
    }

    public List<VehicleType> findAllWithVehicles() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT vt FROM VehicleType vt LEFT JOIN FETCH vt.vehicles",
                    VehicleType.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public void remove(Long id){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            VehicleType vehicleType = em.find(VehicleType.class, id);
            et.begin();
            em.remove(vehicleType);
            et.commit();
        }
        catch(RuntimeException re){
            if(et.isActive()){
                et.rollback();
            }
            throw re;
        }
        finally {
            em.close();
        }
    }

    public void setBrand(Long id, String brand){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            VehicleType vehicleType = em.find(VehicleType.class, id);
            et.begin();
            vehicleType.setBrand(brand);
            et.commit();
        }
        catch(RuntimeException re){
            if(et.isActive()){
                et.rollback();
            }
            throw re;
        }
        finally{
            em.close();
        }
    }

    public void setFuelType(Long id, String fuelType){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            VehicleType vehicleType = em.find(VehicleType.class, id);
            et.begin();
            vehicleType.setFuelType(fuelType);
            et.commit();
        }
        catch(RuntimeException re){
            if(et.isActive()){
                et.rollback();
            }
            throw re;
        }
        finally{
            em.close();
        }
    }

    public void setGearbox(Long id, String gearbox){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            VehicleType vehicleType = em.find(VehicleType.class, id);
            et.begin();
            vehicleType.setGearbox(gearbox);
            et.commit();
        }
        catch(RuntimeException re){
            if(et.isActive()){
                et.rollback();
            }
            throw re;
        }
        finally{
            em.close();
        }
    }

    public void setModel(Long id, String model){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            VehicleType vehicleType = em.find(VehicleType.class, id);
            et.begin();
            vehicleType.setModel(model);
            et.commit();
        }
        catch(RuntimeException re){
            if(et.isActive()){
                et.rollback();
            }
            throw re;
        }
        finally{
            em.close();
        }
    }

    public void setNumberOfDoors(Long id, int numberOfDoors){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            VehicleType vehicleType = em.find(VehicleType.class, id);
            et.begin();
            vehicleType.setNumberOfDoors(numberOfDoors);
            et.commit();
        }
        catch(RuntimeException re){
            if(et.isActive()){
                et.rollback();
            }
            throw re;
        }
        finally{
            em.close();
        }
    }

    public void setNumberOfSeats(Long id, int numberOfSeats){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            VehicleType vehicleType = em.find(VehicleType.class, id);
            et.begin();
            vehicleType.setNumberOfSeats(numberOfSeats);
            et.commit();
        }
        catch(RuntimeException re){
            if(et.isActive()){
                et.rollback();
            }
            throw re;
        }
        finally{
            em.close();
        }
    }

    public void setPower(Long id, int power){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            VehicleType vehicleType = em.find(VehicleType.class, id);
            et.begin();
            vehicleType.setPower(power);
            et.commit();
        }
        catch(RuntimeException re){
            if(et.isActive()){
                et.rollback();
            }
            throw re;
        }
        finally{
            em.close();
        }
    }
}
