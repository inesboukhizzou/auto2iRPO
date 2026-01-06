package dao;

import entities.MaintenanceType;
<<<<<<< HEAD
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;
import java.util.*;

public class MaintenanceTypeDAO {

    public void save(MaintenanceType maintenanceType) {
=======
import entities.InterventionType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;

import java.util.List;

public class MaintenanceTypeDAO {

    public void create(MaintenanceType maintenanceType){
>>>>>>> aa620b8d4cf27157d9c24703e53e14823a3e8688
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            em.persist(maintenanceType);
            et.commit();
        }
<<<<<<< HEAD
        catch(RuntimeException re){
            if(et.isActive()){
                et.rollback();
            }
            throw re;
        }
        finally {
=======
        catch(RuntimeException e){
            if(et.isActive()){
                et.rollback();
            }
            throw e;
        }
        finally{
>>>>>>> aa620b8d4cf27157d9c24703e53e14823a3e8688
            em.close();
        }
    }

<<<<<<< HEAD

=======
>>>>>>> aa620b8d4cf27157d9c24703e53e14823a3e8688
    public MaintenanceType findById(Long id){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.find(MaintenanceType.class, id);
        }
        finally{
            em.close();
        }
    }

<<<<<<< HEAD


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
=======
    public List<MaintenanceType> findAll(){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery(
                    "SELECT m FROM MaintenanceType m",
                    MaintenanceType.class
            ).getResultList();
        }
        finally{
>>>>>>> aa620b8d4cf27157d9c24703e53e14823a3e8688
            em.close();
        }
    }

<<<<<<< HEAD
=======
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
>>>>>>> aa620b8d4cf27157d9c24703e53e14823a3e8688

    public void setMaxDuration(Long id, int maxDuration){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            MaintenanceType maintenanceType = em.find(MaintenanceType.class, id);
            et.begin();
            maintenanceType.setMaxDuration(maxDuration);
            et.commit();
        }
<<<<<<< HEAD
        catch(RuntimeException re){
            if(et.isActive()){
                et.rollback();
            }
            throw re;
        }
        finally {
=======
        catch(RuntimeException e){
            if(et.isActive()){
                et.rollback();
            }
            throw e;
        }
        finally{
>>>>>>> aa620b8d4cf27157d9c24703e53e14823a3e8688
            em.close();
        }
    }

<<<<<<< HEAD

=======
>>>>>>> aa620b8d4cf27157d9c24703e53e14823a3e8688
    public void setMaxMileage(Long id, int maxMileage){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            MaintenanceType maintenanceType = em.find(MaintenanceType.class, id);
            et.begin();
            maintenanceType.setMaxMileage(maxMileage);
            et.commit();
        }
<<<<<<< HEAD
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
=======
        catch(RuntimeException e){
            if(et.isActive()){
                et.rollback();
            }
            throw e;
>>>>>>> aa620b8d4cf27157d9c24703e53e14823a3e8688
        }
        finally{
            em.close();
        }
    }
<<<<<<< HEAD
}
=======

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
>>>>>>> aa620b8d4cf27157d9c24703e53e14823a3e8688
