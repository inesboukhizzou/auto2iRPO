package services;

import dao.InterventionTypeDAO;
import entities.InterventionType;
import jakarta.persistence.EntityManager;

import java.util.List;

public class InterventionTypeService {

    private final InterventionTypeDAO interventionTypeDAO;

    public InterventionTypeService(EntityManager em) {
        this.interventionTypeDAO = new InterventionTypeDAO(em);
    }

    /* ===================== addInterventionType ===================== */
    public void addInterventionType(InterventionType interventionType) {
        if (interventionType == null) {
            throw new IllegalArgumentException("InterventionType cannot be null");
        }
        interventionTypeDAO.create(interventionType);
    }

    /* ===================== findById ===================== */
    public InterventionType findById(Long id) {
        return interventionTypeDAO.findById(id);
    }

    /* ===================== findByName ===================== */
    public InterventionType findByName(String name) {
        return interventionTypeDAO.findByName(name);
    }

    /* ===================== findAll ===================== */
    public List<InterventionType> findAll() {
        return interventionTypeDAO.findAll();
    }

    /* ===================== update ===================== */
    public InterventionType update(InterventionType interventionType) {
        if (interventionType == null) {
            throw new IllegalArgumentException("InterventionType cannot be null");
        }
        return interventionTypeDAO.update(interventionType);
    }

    /* ===================== delete ===================== */
    public void delete(Long id) {
        interventionTypeDAO.delete(id);
    }
}