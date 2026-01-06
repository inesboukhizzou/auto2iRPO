package dao;
import entities.Owner;
import jakarta.persistence.EntityTransaction;
import utils.JPAUtil;
import jakarta.persistence.EntityManager;

public class OwnerDAO {

    public void save(Owner owner) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
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
}
