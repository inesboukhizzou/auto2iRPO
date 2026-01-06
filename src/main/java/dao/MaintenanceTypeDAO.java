package dao;

import entities.MaintenanceType;
import entities.InterventionType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;

import java.util.List;

public class MaintenanceTypeDAO {

    public void create(MaintenanceType maintenanceType){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            em.persist(maintenanceType);
            et.commit();
        }
        catch(RuntimeException e){
            if(et.isActive()){
                et.rollback();
            }
            throw e;
        }
        finally{
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

    public List<MaintenanceType> findAll(){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery(
                    "SELECT m FROM MaintenanceType m",
                    MaintenanceType.class
            ).getResultList();
        }
        finally{
            em.close();
        }
    }

    public List<MaintenanceType> findByInterventionType(InterventionType interventionType){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery(
                    "SELECT m FROM MaintenanceType m WHERE m.interventionType = :interventionType",
                    MaintenanceType.class
            )
            .setParameter("interventionType", interventionType)
            .getResultList();
        }
        finally{
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
        catch(RuntimeException e){
            if(et.isActive()){
                et.rollback();
            }
            throw e;
        }
        finally{
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
        catch(RuntimeException e){
            if(et.isActive()){
                et.rollback();
            }
            throw e;
        }
        finally{
            em.close();
        }
    }

    public void remove(Long id){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            MaintenanceType maintenanceType = em.find(MaintenanceType.class, id);
            et.begin();
            em.remove(maintenanceType);
            et.commit();
        }
        catch(RuntimeException e){
            if(et.isActive()){
                et.rollback();
            }
            throw e;
        }
        finally{
            em.close();
        }
    }
}
