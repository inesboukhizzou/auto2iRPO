package dao;

import entities.InterventionType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;

import java.util.List;

public class InterventionTypeDAO {

    public void create(InterventionType interventionType){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            em.persist(interventionType);
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

    public InterventionType findById(Long id){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.find(InterventionType.class, id);
        }
        finally{
            em.close();
        }
    }

    public List<InterventionType> findAll(){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery(
                    "SELECT i FROM InterventionType i",
                    InterventionType.class
            ).getResultList();
        }
        finally{
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
