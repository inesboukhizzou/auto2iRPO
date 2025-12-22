package services;

import dao.VehicleDAO;
import entities.Owner;
import entities.Registration;
import entities.Vehicle;

public class VehicleService {

    private final VehicleDAO vehicleDAO = new VehicleDAO();

    public Vehicle addVehicule(Vehicle vehicle, Owner owner) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        if (owner == null) {
            throw new IllegalArgumentException("Owner cannot be null");
        }

        vehicle.setOwner(owner);
        vehicleDAO.save(vehicle);
        return vehicle;
    }

    public Vehicle searchVehicule(Registration registration) {
        return vehicleDAO.findByRegistration(registration);
    }

    public void displayHistorique(Vehicle vehicle) {
        if (vehicle == null) {
            System.out.println("Vehicle not found");
            return;
        }

        vehicle.getInterventions().forEach(System.out::println);
    }
}