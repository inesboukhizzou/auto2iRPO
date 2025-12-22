package main;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import services.*;
import entities.*;

import java.util.Date;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("auto2iPU");
        EntityManager em = emf.createEntityManager();

        // Services
        OwnerService ownerService = new OwnerService();
        VehicleService vehicleService = new VehicleService();
        InterventionTypeService itService = new InterventionTypeService(em);
        InterventionService interventionService = new InterventionService();

        System.out.println("=== START TEST ===");

        /* ================= OWNER ================= */
        Owner owner = new Owner();
        owner.setFirstName("Alice");
        owner.setLastName("Martin");
        owner.setEmail("alice@test.com");
        owner.setPhoneNumber("0612345678");

        ownerService.addOwner(owner);

        /* ================= VEHICLE TYPE ================= */
        VehicleType vehicleType = new VehicleType();
        vehicleType.setBrand("BMW");
        vehicleType.setModel("Serie 3");
        vehicleType.setFuelType("Diesel");
        vehicleType.setGearbox("Automatic");
        vehicleType.setNumberOfDoors(4);
        vehicleType.setNumberOfSeats(5);
        vehicleType.setPower(190);

        em.getTransaction().begin();
        em.persist(vehicleType);
        em.getTransaction().commit();

        /* ================= REGISTRATION ================= */
        Registration registration = new Registration("AB", 123, "CD");
        em.getTransaction().begin();
        em.persist(registration);
        em.getTransaction().commit();

        /* ================= VEHICLE ================= */
        Vehicle vehicle = new Vehicle();
        vehicle.setOwner(owner);
        vehicle.setVehicleType(vehicleType);
        vehicle.setRegistration(registration);
        vehicle.setDateOfFirstRegistration(new Date());
        vehicle.setLastMileage(120000);

        vehicleService.addVehicule(vehicle, owner);

        /* ================= INTERVENTION TYPE ================= */
        InterventionType maintenance = new InterventionType();
        maintenance.setName("Maintenance");
        itService.addInterventionType(maintenance);

        /* ================= INTERVENTION ================= */
        Intervention intervention = new Intervention();
        intervention.setVehicle(vehicle);
        intervention.setInterventionType(maintenance);
        intervention.setDate(new Date());
        intervention.setVehicleMileage(120000);
        intervention.setPrice(120.0);

        interventionService.addIntervention(intervention, vehicle);

        /* ================= DISPLAY ================= */
        System.out.println("=== HISTORIQUE ===");
        interventionService.displayHistorique(vehicle)
                .forEach(System.out::println);
        System.out.println("=== NEXT MAINTENANCES ===");

        interventionService
                .displayNextMaintenances(new Date())
                .forEach(System.out::println);
        System.out.println("=== END TEST ===");

        System.out.println("=== SEARCH VEHICLE BY REGISTRATION ===");
        Vehicle foundVehicle = vehicleService.searchVehicule(registration);
        if (foundVehicle != null) {
            System.out.println(foundVehicle);
        } else {
            System.out.println("Vehicle not found.");
        }

        em.close();
        emf.close();
    }
}