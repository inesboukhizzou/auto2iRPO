package services;

import dao.PartDAO;
import entities.InterventionType;
import entities.Part;

import java.util.List;

public class PartService {

    private final PartDAO partDAO;

    public PartService(jakarta.persistence.EntityManager em) {
        this.partDAO = new PartDAO(em);
    }

    public void addPart(Part part, InterventionType interventionType) {
        if (part == null || interventionType == null) {
            throw new IllegalArgumentException("Part and intervention type are required");
        }

        part.setInterventionType(interventionType);
        partDAO.save(part);
    }

    public List<Part> getPartsByInterventionType(InterventionType interventionType) {
        if (interventionType == null) {
            throw new IllegalArgumentException("Intervention type is required");
        }
        return partDAO.findByInterventionType(interventionType);
    }
}