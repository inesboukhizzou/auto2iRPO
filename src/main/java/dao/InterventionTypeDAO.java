package dao;

import entities.InterventionType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class InterventionTypeDAO {

    private final EntityManager em;

    public InterventionTypeDAO(EntityManager em) {
        this.em = em;
    }


    public void create(InterventionType interventionType) {
        em.getTransaction().begin();
        em.persist(interventionType);
        em.getTransaction().commit();
    }


    public InterventionType findById(Long id) {
        return em.find(InterventionType.class, id);
    }

    public List<InterventionType> findAll() {
        TypedQuery<InterventionType> query = em.createQuery(
                "SELECT it FROM InterventionType it",
                InterventionType.class
        );
        return query.getResultList();
    }

    public InterventionType findByName(String name) {
        TypedQuery<InterventionType> query = em.createQuery(
                "SELECT it FROM InterventionType it WHERE it.name = :name",
                InterventionType.class
        );
        query.setParameter("name", name);
        return query.getSingleResult();
    }


    public InterventionType update(InterventionType interventionType) {
        em.getTransaction().begin();
        InterventionType merged = em.merge(interventionType);
        em.getTransaction().commit();
        return merged;
    }



    public void delete(Long id) {
        em.getTransaction().begin();
        InterventionType interventionType = em.find(InterventionType.class, id);
        if (interventionType != null) {
            em.remove(interventionType);
        }
        em.getTransaction().commit();
    }
}