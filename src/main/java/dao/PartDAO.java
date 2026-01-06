package dao;

import entities.Part;
import entities.InterventionType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;

import java.util.List;

public class PartDAO {

    public void create(Part part){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            em.persist(part);
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

    public Part findById(Long id){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.find(Part.class, id);
        }
        finally{
            em.close();
        }
    }

    public List<Part> findAll(){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery(
                    "SELECT p FROM Part p",
                    Part.class
            ).getResultList();
        }
        finally{
            em.close();
        }
    }

    public List<Part> findByInterventionType(InterventionType interventionType){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery(
                    "SELECT p FROM Part p WHERE p.interventionType = :interventionType",
                    Part.class
            )
            .setParameter("interventionType", interventionType)
            .getResultList();
        }
        finally{
            em.close();
        }
    }

    public void setName(Long id, String name){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            Part part = em.find(Part.class, id);
            et.begin();
            part.setName(name);
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
            Part part = em.find(Part.class, id);
            et.begin();
            em.remove(part);
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
