package main;

import dao.*;
import entities.*;
import utils.JPAUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * Database setup script.
 * Creates tables and populates them with sample data for testing.
 * Run this ONCE before launching the main application.
 */
public class DatabaseSetup {

    public static void main(String[] args) {
        System.out.println("=== Auto2i Database Setup ===\n");

        try {
            
            System.out.println("Initializing database connection...");
            JPAUtil.getEntityManagerFactory();
            System.out.println("✓ Database connection established\n");

            
            createSampleData();

            System.out.println("\n=== Database setup completed successfully! ===");
            System.out.println("You can now run the Auto2iApplication to start the GUI.");

        } catch (Exception e) {
            System.err.println("✗ Error during database setup: " + e.getMessage());
            e.printStackTrace();
        } finally {
            JPAUtil.close();
        }
    }

    private static void createSampleData() {
        OwnerDAO ownerDAO = new OwnerDAO();
        VehicleTypeDAO vehicleTypeDAO = new VehicleTypeDAO();
        VehicleDAO vehicleDAO = new VehicleDAO();
        RegistrationDAO registrationDAO = new RegistrationDAO();
        MaintenanceTypeDAO maintenanceTypeDAO = new MaintenanceTypeDAO();
        RepairTypeDAO repairTypeDAO = new RepairTypeDAO();
        PartDAO partDAO = new PartDAO();
        PricingDAO pricingDAO = new PricingDAO();
        InterventionDAO interventionDAO = new InterventionDAO();

        
        System.out.println("Creating owners...");

        Owner owner1 = ownerDAO.findOrCreate("Jean", "Dupont", "0612345678", "jean.dupont@email.com");
        Owner owner2 = ownerDAO.findOrCreate("Marie", "Martin", "0687654321", "marie.martin@email.com");
        Owner owner3 = ownerDAO.findOrCreate("Pierre", "Bernard", "0611223344", "pierre.bernard@email.com");
        Owner owner4 = ownerDAO.findOrCreate("Sophie", "Petit", "0699887766", "sophie.petit@email.com");
        Owner owner5 = ownerDAO.findOrCreate("Lucas", "Garcia", "0655443322", "lucas.garcia@email.com");

        System.out.println("✓ Owners created: 5");

        
        System.out.println("Creating vehicle types...");

        VehicleType vt1 = createVehicleTypeIfNotExists(vehicleTypeDAO,
                "Renault", "Clio", "Gasoline", "Manual", 5, 5, 90);
        VehicleType vt2 = createVehicleTypeIfNotExists(vehicleTypeDAO,
                "Peugeot", "308", "Diesel", "Automatic", 5, 5, 130);
        VehicleType vt3 = createVehicleTypeIfNotExists(vehicleTypeDAO,
                "Tesla", "Model 3", "Electric", "Automatic", 4, 5, 283);
        VehicleType vt4 = createVehicleTypeIfNotExists(vehicleTypeDAO,
                "Volkswagen", "Golf", "Gasoline", "Manual", 5, 5, 115);
        VehicleType vt5 = createVehicleTypeIfNotExists(vehicleTypeDAO,
                "Toyota", "Yaris", "Hybrid", "Automatic", 5, 5, 116);

        System.out.println("✓ Vehicle types created: 5");

        
        System.out.println("Creating maintenance types...");

        MaintenanceType mt1 = createMaintenanceTypeIfNotExists(maintenanceTypeDAO,
                "Oil Change", 15000, 12);
        MaintenanceType mt2 = createMaintenanceTypeIfNotExists(maintenanceTypeDAO,
                "Brake Pads Replacement", 30000, 24);
        MaintenanceType mt3 = createMaintenanceTypeIfNotExists(maintenanceTypeDAO,
                "Tire Rotation", 10000, 12);
        MaintenanceType mt4 = createMaintenanceTypeIfNotExists(maintenanceTypeDAO,
                "Coolant Flush", 50000, 36);
        MaintenanceType mt5 = createMaintenanceTypeIfNotExists(maintenanceTypeDAO,
                "Air Filter Replacement", 20000, 18);
        MaintenanceType mt6 = createMaintenanceTypeIfNotExists(maintenanceTypeDAO,
                "Timing Belt Replacement", 100000, 60);

        System.out.println("✓ Maintenance types created: 6");

        
        System.out.println("Creating repair types...");

        RepairType rt1 = createRepairTypeIfNotExists(repairTypeDAO, "Door Replacement");
        RepairType rt2 = createRepairTypeIfNotExists(repairTypeDAO, "Mirror Replacement");
        RepairType rt3 = createRepairTypeIfNotExists(repairTypeDAO, "Tire Puncture Repair");
        RepairType rt4 = createRepairTypeIfNotExists(repairTypeDAO, "Windshield Replacement");
        RepairType rt5 = createRepairTypeIfNotExists(repairTypeDAO, "Battery Replacement");

        System.out.println("✓ Repair types created: 5");

        
        System.out.println("Creating vehicle parts...");

        createPartIfNotExists(partDAO, mt1, "Engine");
        createPartIfNotExists(partDAO, mt1, "Oil Filter");
        createPartIfNotExists(partDAO, mt2, "Front Left Brake");
        createPartIfNotExists(partDAO, mt2, "Front Right Brake");
        createPartIfNotExists(partDAO, mt2, "Rear Left Brake");
        createPartIfNotExists(partDAO, mt2, "Rear Right Brake");
        createPartIfNotExists(partDAO, mt3, "Front Left Tire");
        createPartIfNotExists(partDAO, mt3, "Front Right Tire");
        createPartIfNotExists(partDAO, mt3, "Rear Left Tire");
        createPartIfNotExists(partDAO, mt3, "Rear Right Tire");
        createPartIfNotExists(partDAO, mt4, "Cooling System");
        createPartIfNotExists(partDAO, mt5, "Air Filter");
        createPartIfNotExists(partDAO, mt6, "Timing Belt");
        createPartIfNotExists(partDAO, rt1, "Door Panel");
        createPartIfNotExists(partDAO, rt2, "Side Mirror");
        createPartIfNotExists(partDAO, rt4, "Windshield");
        createPartIfNotExists(partDAO, rt5, "Battery");

        System.out.println("✓ Parts created: 17");

        
        System.out.println("Creating vehicles...");

        Vehicle v1 = createVehicleIfNotExists(vehicleDAO, registrationDAO,
                owner1, vt1, "AB", 123, "CD", 45000, -365);
        Vehicle v2 = createVehicleIfNotExists(vehicleDAO, registrationDAO,
                owner2, vt2, "EF", 456, "GH", 78000, -730);
        Vehicle v3 = createVehicleIfNotExists(vehicleDAO, registrationDAO,
                owner3, vt3, "IJ", 789, "KL", 22000, -180);
        Vehicle v4 = createVehicleIfNotExists(vehicleDAO, registrationDAO,
                owner4, vt4, "MN", 321, "OP", 95000, -1095);
        Vehicle v5 = createVehicleIfNotExists(vehicleDAO, registrationDAO,
                owner5, vt5, "QR", 654, "ST", 35000, -540);

        System.out.println("✓ Vehicles created: 5");

        
        System.out.println("Creating pricing rules...");

        createPricingIfNotExists(pricingDAO, mt1, vt1, 89.90);
        createPricingIfNotExists(pricingDAO, mt1, vt2, 99.90);
        createPricingIfNotExists(pricingDAO, mt1, vt3, 0.00); 
        createPricingIfNotExists(pricingDAO, mt1, vt4, 94.90);
        createPricingIfNotExists(pricingDAO, mt1, vt5, 79.90);
        createPricingIfNotExists(pricingDAO, mt2, vt1, 249.00);
        createPricingIfNotExists(pricingDAO, mt2, vt2, 289.00);
        createPricingIfNotExists(pricingDAO, mt2, vt3, 399.00);
        createPricingIfNotExists(pricingDAO, mt2, vt4, 269.00);
        createPricingIfNotExists(pricingDAO, mt2, vt5, 229.00);
        createPricingIfNotExists(pricingDAO, mt3, vt1, 49.00);
        createPricingIfNotExists(pricingDAO, mt3, vt2, 59.00);
        createPricingIfNotExists(pricingDAO, mt3, vt3, 69.00);

        System.out.println("✓ Pricing rules created: 13+");

        
        System.out.println("Creating sample interventions...");

        if (v1 != null) {
            createInterventionIfNotExists(interventionDAO, v1, mt1, -180, 42000, 89.90);
            createInterventionIfNotExists(interventionDAO, v1, mt3, -90, 43000, 49.00);
        }
        if (v2 != null) {
            createInterventionIfNotExists(interventionDAO, v2, mt1, -365, 65000, 99.90);
            createInterventionIfNotExists(interventionDAO, v2, mt2, -200, 72000, 289.00);
        }
        if (v3 != null) {
            createInterventionIfNotExists(interventionDAO, v3, mt3, -60, 20000, 69.00);
        }
        if (v4 != null) {
            createInterventionIfNotExists(interventionDAO, v4, mt1, -400, 90000, 94.90);
            createInterventionIfNotExists(interventionDAO, v4, mt4, -730, 70000, 149.00);
        }
        if (v5 != null) {
            createInterventionIfNotExists(interventionDAO, v5, mt1, -120, 32000, 79.90);
        }

        System.out.println("✓ Sample interventions created");
    }

    

    private static VehicleType createVehicleTypeIfNotExists(VehicleTypeDAO dao,
            String brand, String model, String fuelType, String gearbox,
            int doors, int seats, int power) {
        try {
            VehicleType vt = new VehicleType(brand, model, fuelType, gearbox, doors, seats, power);
            dao.create(vt);
            return vt;
        } catch (Exception e) {
            
            return dao.findAll().stream()
                    .filter(v -> v.getBrand().equals(brand) && v.getModel().equals(model))
                    .findFirst().orElse(null);
        }
    }

    private static MaintenanceType createMaintenanceTypeIfNotExists(MaintenanceTypeDAO dao,
            String name, int maxMileage, int maxDuration) {
        try {
            MaintenanceType mt = new MaintenanceType(name, maxMileage, maxDuration);
            dao.create(mt);
            return mt;
        } catch (Exception e) {
            return dao.findAll().stream()
                    .filter(m -> m.getName().equals(name))
                    .findFirst().orElse(null);
        }
    }

    private static RepairType createRepairTypeIfNotExists(RepairTypeDAO dao, String name) {
        try {
            RepairType rt = new RepairType(name);
            dao.save(rt);
            return rt;
        } catch (Exception e) {
            return dao.findAll().stream()
                    .filter(r -> r.getName().equals(name))
                    .findFirst().orElse(null);
        }
    }

    private static void createPartIfNotExists(PartDAO dao, InterventionType type, String name) {
        try {
            Part part = new Part(type, name);
            dao.create(part);
        } catch (Exception e) {
            
        }
    }

    private static Vehicle createVehicleIfNotExists(VehicleDAO vehicleDAO, RegistrationDAO registrationDAO,
            Owner owner, VehicleType vehicleType, String p1, int p2, String p3, int mileage, int daysAgo) {
        try {
            Registration reg = registrationDAO.findOrCreate(p1, p2, p3);

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, daysAgo);
            Date registrationDate = cal.getTime();

            Vehicle vehicle = new Vehicle(owner, vehicleType, registrationDate, mileage);
            vehicle.setRegistration(reg);
            vehicleDAO.create(vehicle);
            return vehicle;
        } catch (Exception e) {
            
            Registration reg = registrationDAO.findByParts(p1, p2, p3);
            if (reg != null) {
                return vehicleDAO.findByRegistration(reg);
            }
            return null;
        }
    }

    private static void createPricingIfNotExists(PricingDAO dao,
            InterventionType type, VehicleType vehicleType, double price) {
        try {
            Pricing pricing = new Pricing(price, type, vehicleType);
            dao.create(pricing);
        } catch (Exception e) {
            
        }
    }

    private static void createInterventionIfNotExists(InterventionDAO dao,
            Vehicle vehicle, InterventionType type, int daysAgo, int mileage, double price) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, daysAgo);
            Date date = cal.getTime();

            Intervention intervention = new Intervention(vehicle, date, mileage, price);
            intervention.setInterventionType(type);
            dao.save(intervention);
        } catch (Exception e) {
            
        }
    }
}
