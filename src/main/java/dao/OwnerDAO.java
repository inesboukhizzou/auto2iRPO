package dao;

import entities.Intervention;
import entities.Owner;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;
import java.util.*;

public class OwnerDAO {

    public void create(Owner owner) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            em.persist(owner);
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


    public Owner findById(Long id){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.find(Owner.class, id);
        }
        finally{
            em.close();
        }
    }



    public void removeOwner(Long id){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            Owner owner = em.find(Owner.class, id);
            et.begin();
            em.remove(owner);
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

    // --- Méthodes de mise à jour spécifiques ---

    public void setEmail(Long id, String email){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            Owner owner = em.find(Owner.class, id);
            et.begin();
            owner.setEmail(email);
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

    public void setFirstName(Long id, String firstName){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            Owner owner = em.find(Owner.class, id);
            et.begin();
            owner.setFirstName(firstName);
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

    public void setLastName(Long id, String lastName){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            Owner owner = em.find(Owner.class, id);
            et.begin();
            owner.setLastName(lastName);
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

    public void setPhoneNumber(Long id, String phoneNumber){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            Owner owner = em.find(Owner.class, id);
            et.begin();
            owner.setPhoneNumber(phoneNumber);
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
    public List<Owner> findAll(){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try{
            return em.createQuery(
                    "SELECT r FROM Owner r",
                    Owner.class
            ).getResultList();
        }
        finally{
            em.close();
        }
    }
}