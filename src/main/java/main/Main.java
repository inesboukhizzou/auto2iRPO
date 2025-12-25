package main;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import services.*;
import entities.*;

import java.util.Date;
import java.util.List;
import java.util.logging.*;
public class Main {

    public static void main(String[] args) {

        ////////////////LEO///////////////////
        Logger rootLogger = Logger.getLogger("org.hibernate");
        rootLogger.setLevel(Level.SEVERE);

        Logger.getLogger("org.hibernate.SQL").setLevel(Level.OFF);
        Logger.getLogger("org.hibernate.tool.hbm2ddl").setLevel(Level.OFF);
        ////////////////LEO///////////////////

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("auto2iPU");
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
        InterventionType maintenanceType = new InterventionType();
        maintenanceType.setName("Maintenance annuelle");
        itService.addInterventionType(maintenanceType);

        /* ================= INTERVENTION ================= */
        Intervention intervention = new Intervention();
        intervention.setVehicle(vehicle);
        intervention.setInterventionType(maintenanceType); // toujours InterventionType
        intervention.setDate(new Date());
        intervention.setVehicleMileage(120000);
        intervention.setPrice(120.0);
        interventionService.addIntervention(intervention, vehicle);

        /* ================= DISPLAY HISTORIQUE ================= */
        printList(interventionService.displayHistorique(vehicle), "HISTORIQUE");

        /* ================= DISPLAY NEXT MAINTENANCES ================= */
        printList(interventionService.displayNextMaintenances(), "NEXT MAINTENANCES");

        /* ================= SEARCH VEHICLE BY REGISTRATION ================= */
        System.out.println("=== SEARCH VEHICLE BY REGISTRATION ===");
        Vehicle foundVehicle = vehicleService.searchVehicule(registration);
        if (foundVehicle != null) {
            System.out.println(foundVehicle);
        } else {
            System.out.println("Vehicle not found.");
        }

        System.out.println("=== END TEST ===");

        em.close();
        emf.close();
    }

    public static <T> void printList(List<T> list, String title) {
        System.out.println("=== " + title + " ===");
        for (T item : list) {
            System.out.println(item);
        }
        System.out.println("===================");
    }
}
