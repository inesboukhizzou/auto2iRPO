package services;

import entities.Intervention;
import entities.Vehicle;

public class PriceService {

    public double FinalPrice(Intervention intervention) {

        double initialPrice = intervention.getPrice();

        String vehicleModelType = "";

        try {
            vehicleModelType = intervention.getVehicle().getVehicleType().getModel();
        } catch (NullPointerException e) {
            System.out.println("Error: Vehicle or VehicleType is missing information.");
            return initialPrice;
        }

        double multiplier = 1.0;

        switch (vehicleModelType.toUpperCase()) {
            case "CITY_CAR":
                multiplier = 1.0;
                break;
            case "ELECTRIC":
                multiplier = 1.2;
                break;
            case "SUV":
                multiplier = 1.3;
                break;
            case "4X4":
                multiplier = 1.5;
                break;
            case "SPORTS_CAR":
                multiplier = 2.0;
                break;
            default:
                System.out.println("Warning: Unknown vehicle model '" + vehicleModelType + "'. Please Try Again.");
                multiplier = 0;
                break;
        }

        double finalPrice = initialPrice * multiplier;

        System.out.println("--- Price Calculation ---");
        System.out.println("Base Price: " + initialPrice + " €");
        System.out.println("Vehicle Category: " + vehicleModelType);
        System.out.println("Multiplier Applied: x" + multiplier);
        System.out.println("FINAL PRICE: " + finalPrice + " €");
        System.out.println("-------------------------");

        return finalPrice;
    }
}

