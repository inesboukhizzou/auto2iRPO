package services;

import dao.PricingDAO;
import entities.Intervention;
import entities.InterventionType;
import entities.Pricing;
import entities.VehicleType;

/**
 * Service for calculating intervention prices.
 * 
 * The price of an intervention depends on:
 * 1. The type of intervention (Oil Change, Brake Pads, etc.)
 * 2. The type of vehicle (Renault Clio, Tesla Model 3, etc.)
 * 
 * Prices are stored in the Pricing table which links InterventionType +
 * VehicleType to a price.
 */
public class PriceService {

    private final PricingDAO pricingDAO = new PricingDAO();

    
    private static final double DEFAULT_BASE_PRICE = 50.00;

    /**
     * Calculates the final price for an intervention based on the vehicle type
     * and intervention type using the Pricing table.
     * 
     * @param intervention The intervention to calculate price for
     * @return The calculated price
     */
    public double calculatePrice(Intervention intervention) {
        if (intervention == null) {
            return DEFAULT_BASE_PRICE;
        }

        InterventionType interventionType = intervention.getInterventionType();
        VehicleType vehicleType = null;

        
        if (intervention.getVehicle() != null && intervention.getVehicle().getVehicleType() != null) {
            vehicleType = intervention.getVehicle().getVehicleType();
        }

        
        Pricing pricing = pricingDAO.findByInterventionTypeAndVehicleType(interventionType, vehicleType);

        if (pricing != null) {
            System.out.println("--- Price Calculation ---");
            System.out.println("Intervention: " + (interventionType != null ? interventionType.getName() : "N/A"));
            System.out.println("Vehicle: "
                    + (vehicleType != null ? vehicleType.getBrand() + " " + vehicleType.getModel() : "N/A"));
            System.out.println("Price from database: " + pricing.getPrice() + " €");
            System.out.println("-------------------------");
            return pricing.getPrice();
        }

        
        return calculateFallbackPrice(intervention, interventionType, vehicleType);
    }

    /**
     * Fallback price calculation when no specific pricing rule exists.
     * Uses a base price with multipliers based on vehicle characteristics.
     */
    private double calculateFallbackPrice(Intervention intervention,
            InterventionType interventionType,
            VehicleType vehicleType) {
        
        double basePrice = intervention.getPrice() > 0 ? intervention.getPrice() : DEFAULT_BASE_PRICE;

        
        double multiplier = 1.0;

        if (vehicleType != null) {
            
            int power = vehicleType.getPower();
            if (power > 200) {
                multiplier += 0.5; 
            } else if (power > 150) {
                multiplier += 0.3; 
            } else if (power > 100) {
                multiplier += 0.1; 
            }
            

            
            String fuelType = vehicleType.getFuelType();
            if (fuelType != null) {
                switch (fuelType.toLowerCase()) {
                    case "electric":
                        multiplier += 0.2; 
                        break;
                    case "hybrid":
                        multiplier += 0.15; 
                        break;
                    case "diesel":
                        multiplier += 0.05; 
                        break;
                    
                }
            }
        }

        double finalPrice = basePrice * multiplier;

        System.out.println("--- Fallback Price Calculation ---");
        System.out.println("Intervention: " + (interventionType != null ? interventionType.getName() : "N/A"));
        System.out.println(
                "Vehicle: " + (vehicleType != null ? vehicleType.getBrand() + " " + vehicleType.getModel() : "N/A"));
        System.out.println("Base Price: " + basePrice + " €");
        System.out.println("Multiplier: x" + String.format("%.2f", multiplier));
        System.out.println("FINAL PRICE: " + String.format("%.2f", finalPrice) + " €");
        System.out.println("---------------------------------");

        return finalPrice;
    }

    /**
     * Gets the price directly from the Pricing table without fallback.
     * Returns null if no pricing rule exists.
     * 
     * @param interventionType The intervention type
     * @param vehicleType      The vehicle type
     * @return The price or null if not found
     */
    public Double getExactPrice(InterventionType interventionType, VehicleType vehicleType) {
        Pricing pricing = pricingDAO.findByInterventionTypeAndVehicleType(interventionType, vehicleType);
        return pricing != null ? pricing.getPrice() : null;
    }

    /**
     * Legacy method for backwards compatibility.
     * 
     * @deprecated Use calculatePrice() instead
     */
    @Deprecated
    public double FinalPrice(Intervention intervention) {
        return calculatePrice(intervention);
    }
}
