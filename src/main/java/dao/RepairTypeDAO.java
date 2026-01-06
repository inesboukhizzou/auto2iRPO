package dao;

import entities.RepairType;
import entities.InterventionType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;

import java.util.List;

public class RepairTypeDAO {

    public void save(RepairType repairType){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            em.persist(repairType);
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

    public RepairType findById(Long id){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.find(RepairType.class, id);
        }
        finally{
            em.close();
        }
    }

    public List<RepairType> findAll(){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery(
                    "SELECT r FROM RepairType r",
                    RepairType.class
            ).getResultList();
        }
        finally{
            em.close();
        }
    }

    public List<RepairType> findByInterventionType(InterventionType interventionType){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery(
                    "SELECT r FROM RepairType r WHERE r.interventionType = :interventionType",
                    RepairType.class
            )
            .setParameter("interventionType", interventionType)
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
            RepairType repairType = em.find(RepairType.class, id);
            et.begin();
            em.remove(repairType);
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
