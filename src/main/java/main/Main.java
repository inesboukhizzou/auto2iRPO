package main;

import dao.*;
import entities.*;

import java.util.Date;
import java.util.List;

import dao.InterventionDAO;
import dao.VehicleDAO;
import dao.InterventionTypeDAO; // Assurez-vous d'avoir ce DAO ou un équivalent
import entities.Intervention;
import entities.InterventionType;
import entities.Vehicle;
import entities.VehicleType;
import services.PriceService;
import utils.JPAUtil;

import java.util.Date;

public class Main {

    public static void main(String[] args) {

        System.out.println("===== TEST VehicleDAO =====");

        VehicleDAO vehicleDAO = new VehicleDAO();
        VehicleTypeDAO vehicleTypeDAO = new VehicleTypeDAO();
        OwnerDAO ownerDAO = new OwnerDAO();
        RegistrationDAO registrationDAO = new RegistrationDAO();

        /* -----------------------------
           Création des dépendances
        ----------------------------- */

        VehicleType vehicleType = new VehicleType();
        vehicleType.setBrand("Toyota");
        vehicleType.setModel("Corolla");
        vehicleType.setFuelType("Essence");
        vehicleType.setGearbox("Manual");
        vehicleType.setNumberOfDoors(5);
        vehicleType.setNumberOfSeats(5);
        vehicleType.setPower(110);
        vehicleTypeDAO.create(vehicleType);

        Owner owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Doe");
        ownerDAO.create(owner);

        Registration registration = new Registration();
        registration.setPart1("AB");
        registration.setPart2(123);
        registration.setPart3("CD");
        registrationDAO.create(registration);

        /* -----------------------------
           CREATE
        ----------------------------- */

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleType(vehicleType);
        vehicle.setOwner(owner);
        vehicle.setRegistration(registration);
        vehicle.setDateOfFirstRegistration(new Date());
        vehicle.setLastMileage(45000);

        vehicleDAO.create(vehicle);
        System.out.println("✔ Vehicle créé avec ID = " + vehicle.getId());

        /* -----------------------------
           findById
        ----------------------------- */

        Vehicle v1 = vehicleDAO.findById(vehicle.getId());
        System.out.println("✔ findById : " + v1);

        /* -----------------------------
           findAll
        ----------------------------- */

        List<Vehicle> allVehicles = vehicleDAO.findAll();
        System.out.println("✔ findAll : " + allVehicles.size() + " véhicule(s)");

        /* -----------------------------
           findByVehicleType
        ----------------------------- */

        List<Vehicle> byType = vehicleDAO.findByVehicleType(vehicleType);
        System.out.println("✔ findByVehicleType : " + byType.size());

        /* -----------------------------
           findByOwner
        ----------------------------- */

        List<Vehicle> byOwner = vehicleDAO.findByOwner(owner);
        System.out.println("✔ findByOwner : " + byOwner.size());

        /* -----------------------------
           findByRegistration
        ----------------------------- */

        Vehicle byRegistration = vehicleDAO.findByRegistration(registration);
        System.out.println("✔ findByRegistration : véhicule ID = " + byRegistration.getId());

        /* -----------------------------
           setLastMileage
        ----------------------------- */

        vehicleDAO.setLastMileage(vehicle.getId(), 60000);
        Vehicle updatedMileage = vehicleDAO.findById(vehicle.getId());
        System.out.println("✔ setLastMileage : " + updatedMileage.getLastMileage());

        /* -----------------------------
           setDateRegistration
        ----------------------------- */

        Date newDate = new Date(120, 0, 1); // 1 Jan 2020
        vehicleDAO.setDateRegistration(vehicle.getId(), newDate);
        Vehicle updatedDate = vehicleDAO.findById(vehicle.getId());
        System.out.println("✔ setDateRegistration : " + updatedDate.getDateOfFirstRegistration());

        /* -----------------------------
           remove (désactivé pour tests InterventionDAO)
        ----------------------------- */

        // vehicleDAO.remove(vehicle.getId());
        // Vehicle deleted = vehicleDAO.findById(vehicle.getId());
        // System.out.println("✔ remove : " + (deleted == null ? "OK (supprimé)" : "ERREUR"));

        System.out.println("\n===== TEST InterventionDAO =====");

        InterventionDAO interventionDAO = new InterventionDAO();
        InterventionTypeDAO interventionTypeDAO = new InterventionTypeDAO();

        /* -----------------------------
           Dépendances nécessaires
        ----------------------------- */

        // InterventionType
        InterventionType interventionType = new InterventionType();
        interventionType.setName("Vidange");
        interventionTypeDAO.create(interventionType);

        // On réutilise le vehicle déjà créé précédemment
        Vehicle testVehicle = vehicleDAO.findById(vehicle.getId());

        /* -----------------------------
           CREATE / SAVE
        ----------------------------- */

        Intervention intervention = new Intervention();
        intervention.setVehicle(testVehicle);
        intervention.setInterventionType(interventionType);
        intervention.setDate(new Date());
        intervention.setPrice(150.0);
        intervention.setVehicleMileage(46000);

        interventionDAO.save(intervention);
        System.out.println("✔ Intervention créée avec ID = " + intervention.getId());

        /* -----------------------------
           findById
        ----------------------------- */

        Intervention i1 = interventionDAO.findById(intervention.getId());
        System.out.println("✔ findById : " + i1);

        /* -----------------------------
           findAll
        ----------------------------- */

        List<Intervention> allInterventions = interventionDAO.findAll();
        System.out.println("✔ findAll : " + allInterventions.size() + " intervention(s)");

        /* -----------------------------
           findByVehicle
        ----------------------------- */

        List<Intervention> byVehicle = interventionDAO.findByVehicle(testVehicle);
        System.out.println("✔ findByVehicle : " + byVehicle.size());

        /* -----------------------------
           findByInterventionType
        ----------------------------- */

        List<Intervention> byType1 = interventionDAO.findByInterventionType(interventionType);
        System.out.println("✔ findByInterventionType : " + byType1.size());

        /* -----------------------------
           setDate
        ----------------------------- */

        Date newDateIntervention = new Date(124, 0, 1); // 1 Jan 2024
        interventionDAO.setDate(intervention.getId(), newDateIntervention);

        Intervention updatedDate1 = interventionDAO.findById(intervention.getId());
        System.out.println("✔ setDate : " + updatedDate1.getDate());

        /* -----------------------------
           setPrice
        ----------------------------- */

        interventionDAO.setPrice(intervention.getId(), 220.0);
        Intervention updatedPrice = interventionDAO.findById(intervention.getId());
        System.out.println("✔ setPrice : " + updatedPrice.getPrice());

        /* -----------------------------
           removeIntervention
        ----------------------------- */

        interventionDAO.removeIntervention(intervention.getId());
        Intervention deletedIntervention = interventionDAO.findById(intervention.getId());

        System.out.println(
                "✔ removeIntervention : " +
                        (deletedIntervention == null ? "OK (supprimée)" : "ERREUR")
        );

        System.out.println("===== FIN TEST InterventionDAO =====");

        System.out.println("===== FIN DES TESTS =====");

        System.out.println("=== TEST AVEC VÉHICULE EXISTANT ===");

        // 1. Initialisation des DAOs
        VehicleDAO VehicleDAO = new VehicleDAO();
        InterventionDAO InterventionDAO = new InterventionDAO();
        PriceService priceService = new PriceService();

        try {
            // --- ÉTAPE 1 : Récupération d'un véhicule existant ---
            // ⚠️ CHANGEZ L'ID ICI si le véhicule n°1 n'existe pas dans votre BDD
            Long vehicleIdToFind = 1L;

            System.out.println("Recherche du véhicule ID : " + vehicleIdToFind + "...");
            Vehicle existingVehicle = vehicleDAO.findById(vehicleIdToFind);

            if (existingVehicle == null) {
                System.err.println("❌ ERREUR : Aucun véhicule trouvé avec l'ID " + vehicleIdToFind);
                System.err.println("Veuillez vérifier votre base de données et changer l'ID dans le code.");
                return; // On arrête le programme ici
            }

            // Affichage pour vérification
            String modelType = "Inconnu";
            if (existingVehicle.getVehicleType() != null) {
                modelType = existingVehicle.getVehicleType().getModel();
            }
            System.out.println("✅ Véhicule trouvé : " + existingVehicle.getRegistration());
            System.out.println("   Type détecté : " + modelType);


            // --- ÉTAPE 2 : Création d'une nouvelle Intervention pour ce véhicule ---
            System.out.println("\nCréation de l'intervention...");
            Intervention Intervention = new Intervention();
            intervention.setDate(new Date());
            intervention.setPrice(100.0); // Prix de base fixe pour faciliter le calcul mental

            // C'est ici qu'on fait le lien avec le véhicule existant
            intervention.setVehicle(existingVehicle);

            // Sauvegarde de l'intervention en base
            interventionDAO.save(intervention);
            System.out.println("-> Intervention sauvegardée (ID : " + intervention.getId() + ")");


            // --- ÉTAPE 3 : Test du Calcul de Prix ---
            System.out.println("\nCalcul du prix final...");

            // Appel du service (qui va lire le type du véhicule via l'intervention)
            double finalPrice = priceService.FinalPrice(intervention);

            // Petit récapitulatif visuel
            System.out.println("\n--- RÉSULTAT DU TEST ---");
            System.out.println("Prix de base : " + intervention.getPrice() + " €");
            System.out.println("Type véhicule : " + modelType);
            System.out.println("Prix calculé : " + finalPrice + " €");

            // Vérification logique rapide
            if (modelType.equalsIgnoreCase("SUV") && finalPrice == 130.0) {
                System.out.println("✅ OK : Le coefficient SUV (x1.3) a bien fonctionné.");
            } else if (modelType.equalsIgnoreCase("CITY_CAR") && finalPrice == 100.0) {
                System.out.println("✅ OK : Le coefficient Citadine (x1.0) a bien fonctionné.");
            } else {
                System.out.println("ℹ️ Vérifiez si le prix correspond bien à votre tableau de pondération.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JPAUtil.getEntityManagerFactory().close();
        }
    }










}