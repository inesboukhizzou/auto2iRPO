package dao;
import entities.Registration;
import entities.Vehicle;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.*;

import java.security.spec.ECField;

public class VehicleDAO {

    public void save(Vehicle vehicle) {
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

    /*TO DO : WRITE FIND VEHICLE BY REGISTRATION */

    public void removeVehicle(Long id){
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
