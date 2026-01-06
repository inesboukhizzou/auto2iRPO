package dao;

import entities.MaintenanceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;
import java.util.*;

public class MaintenanceTypeDAO {

    public void save(MaintenanceType maintenanceType) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            em.persist(maintenanceType);
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


    public MaintenanceType findById(Long id){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.find(MaintenanceType.class, id);
        }
        finally{
            em.close();
        }
    }



    public void removeMaintenanceType(Long id){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            MaintenanceType maintenanceType = em.find(MaintenanceType.class, id);
            et.begin();
            em.remove(maintenanceType);
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


    public void setMaxDuration(Long id, int maxDuration){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            MaintenanceType maintenanceType = em.find(MaintenanceType.class, id);
            et.begin();
            maintenanceType.setMaxDuration(maxDuration);
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


    public void setMaxMileage(Long id, int maxMileage){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            MaintenanceType maintenanceType = em.find(MaintenanceType.class, id);
            et.begin();
            maintenanceType.setMaxMileage(maxMileage);
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

    public List<MaintenanceType> findAll(){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery(
                    "SELECT r FROM MaintenanceType r",
                    MaintenanceType.class
            ).getResultList();
        }
        finally{
            em.close();
        }
    }
}