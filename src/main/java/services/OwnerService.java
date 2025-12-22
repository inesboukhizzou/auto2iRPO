package services;

import dao.OwnerDAO;
import entities.Owner;

public class OwnerService {

    private final OwnerDAO ownerDAO = new OwnerDAO();

    public void addOwner(Owner owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Owner cannot be null");
        }
        ownerDAO.save(owner);
    }

    public Owner findOwnerByEmail(String email) {
        return ownerDAO.findByEmail(email);
    }

    public Owner findOwnerByPhoneNumber(String phoneNumber) {
        return ownerDAO.findByPhoneNumber(phoneNumber);
    }
}