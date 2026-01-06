package dao;

import entities.InterventionType;
<<<<<<< HEAD
import entities.MaintenanceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;
import java.util.*;

public class InterventionTypeDAO {

    public void save(InterventionType interventionType) {
=======
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;

import java.util.List;

public class InterventionTypeDAO {

    public void create(InterventionType interventionType){
>>>>>>> aa620b8d4cf27157d9c24703e53e14823a3e8688
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            em.persist(interventionType);
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
    // Trouve un type d'intervention par ID
=======
>>>>>>> aa620b8d4cf27157d9c24703e53e14823a3e8688
    public InterventionType findById(Long id){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.find(InterventionType.class, id);
        }
        finally{
            em.close();
        }
    }

<<<<<<< HEAD


    public void removeInterventionType(Long id){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            InterventionType interventionType = em.find(InterventionType.class, id);
            et.begin();
            em.remove(interventionType);
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
    public List<InterventionType> findAll(){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery(
                    "SELECT i FROM InterventionType i",
                    InterventionType.class
            ).getResultList();
        }
        finally{
>>>>>>> aa620b8d4cf27157d9c24703e53e14823a3e8688
            em.close();
        }
    }

<<<<<<< HEAD

=======
>>>>>>> aa620b8d4cf27157d9c24703e53e14823a3e8688
    public void setName(Long id, String name){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            InterventionType interventionType = em.find(InterventionType.class, id);
            et.begin();
            interventionType.setName(name);
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
    public List<InterventionType> findAll(){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery(
                    "SELECT r FROM InterventionType r",
                    InterventionType.class
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
            InterventionType interventionType = em.find(InterventionType.class, id);
            et.begin();
            em.remove(interventionType);
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
