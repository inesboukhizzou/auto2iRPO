package dao;

import entities.Part;
import entities.InterventionType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class PartDAO {

    private final EntityManager em;

    public PartDAO(EntityManager em) {
        this.em = em;
    }

    public void save(Part part) {
        em.getTransaction().begin();
        if (part.getId() == null) {
            em.persist(part);
        } else {
            em.merge(part);
        }
        em.getTransaction().commit();
    }


    public Part findById(Long id) {
        return em.find(Part.class, id);
    }

    public List<Part> findAll() {
        TypedQuery<Part> query = em.createQuery(
                "SELECT p FROM Part p",
                Part.class
        );
        return query.getResultList();
    }

    public List<Part> findByInterventionType(InterventionType interventionType) {
        TypedQuery<Part> query = em.createQuery(
                "SELECT p FROM Part p WHERE p.interventionType = :it",
                Part.class
        );
        query.setParameter("it", interventionType);
        return query.getResultList();
    }

    public Part findByNameAndInterventionType(String name, InterventionType interventionType) {
        TypedQuery<Part> query = em.createQuery(
                "SELECT p FROM Part p " +
                        "WHERE p.name = :name AND p.interventionType = :it",
                Part.class
        );
        query.setParameter("name", name);
        query.setParameter("it", interventionType);
        return query.getSingleResult();
    }


    public Part update(Part part) {
        em.getTransaction().begin();
        Part merged = em.merge(part);
        em.getTransaction().commit();
        return merged;
    }


    public void delete(Long id) {
        em.getTransaction().begin();
        Part part = em.find(Part.class, id);
        if (part != null) {
            em.remove(part);
        }
        em.getTransaction().commit();
    }
}