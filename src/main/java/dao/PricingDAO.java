package dao;

import entities.Pricing;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;

import java.util.List;

public class PricingDAO {

    public void create(Pricing pricing){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            em.persist(pricing);
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

    public Pricing findById(Long id){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.find(Pricing.class, id);
        }
        finally{
            em.close();
        }
    }

    public List<Pricing> findAll(){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery(
                    "SELECT p FROM Pricing p",
                    Pricing.class
            ).getResultList();
        }
        finally{
            em.close();
        }
    }

    public void setPrice(Long id, double price){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            Pricing pricing = em.find(Pricing.class, id);
            et.begin();
            pricing.setPrice(price);
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
            Pricing pricing = em.find(Pricing.class, id);
            et.begin();
            em.remove(pricing);
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
