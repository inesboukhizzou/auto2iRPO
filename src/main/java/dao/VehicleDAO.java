package dao;
import entities.Registration;
import entities.Vehicle;
import entities.VehicleType;
import entities.Owner;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.*;

public class VehicleDAO {

    public void create(Vehicle vehicle) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            em.persist(vehicle);
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

    // finds vehicle by Id
    public Vehicle findById(Long id){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.find(Vehicle.class, id);
        }
        finally{
            em.close();
        }
    }

    public List<Vehicle> findAll(){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery(
                    "SELECT v FROM Vehicle v",
                    Vehicle.class
            ).getResultList();
        }
        finally{
            em.close();
        }
    }

    public List<Vehicle> findByVehicleType(VehicleType vehicleType){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery(
                    "SELECT v FROM Vehicle v WHERE v.vehicleType = :vehicleType",
                    Vehicle.class
            )
            .setParameter("vehicleType", vehicleType)
            .getResultList();
        }
        finally{
            em.close();
        }
    }

    public Vehicle findByRegistration(Registration registration){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery(
                    "SELECT v FROM Vehicle v WHERE v.registration = :registration",
                    Vehicle.class
            )
            .setParameter("registration", registration)
            .getSingleResult();
        }
        finally{
            em.close();
        }
    }

    public List<Vehicle> findByOwner(Owner owner){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery(
                    "SELECT v FROM Vehicle v WHERE v.owner = :owner",
                    Vehicle.class
            )
            .setParameter("owner", owner)
            .getResultList();
        }
        finally{
            em.close();
        }
    }

    public void remove(Long id){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            Vehicle vehicle = em.find(Vehicle.class, id);
            et.begin();
            em.remove(vehicle);
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

    public void setDateRegistration(Long id, Date dateOfFirstRegistration){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            Vehicle vehicle = em.find(Vehicle.class,id);
            et.begin();
            vehicle.setDateOfFirstRegistration(dateOfFirstRegistration);
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

    public void setLastMileage(Long id, int lastMileage){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            Vehicle vehicle = em.find(Vehicle.class,id);
            et.begin();
            vehicle.setLastMileage(lastMileage);
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
}
