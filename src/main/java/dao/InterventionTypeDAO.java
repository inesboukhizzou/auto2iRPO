package dao;

import entities.InterventionType;
import entities.MaintenanceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;
import java.util.*;

public class InterventionTypeDAO {

    public void save(InterventionType interventionType) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            em.persist(interventionType);
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

    // Trouve un type d'intervention par ID
    public InterventionType findById(Long id){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.find(InterventionType.class, id);
        }
        finally{
            em.close();
        }
    }



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
            em.close();
        }
    }


    public void setName(Long id, String name){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            InterventionType interventionType = em.find(InterventionType.class, id);
            et.begin();
            interventionType.setName(name);
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
    public List<InterventionType> findAll(){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery(
                    "SELECT r FROM InterventionType r",
                    InterventionType.class
            ).getResultList();
        }
        finally{
            em.close();
        }
    }
}