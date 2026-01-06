package services;

import entities.Intervention;
import entities.Vehicle;
// Assurez-vous d'importer vos entités correctement

public class PriceService {

    /*
     * Calcule le prix final basé sur le type de véhicule (rubrique model).
     * @param intervention L'intervention contenant le prix initial et le lien vers le véhicule.
     * @return Le prix final pondéré.
     */
    public double FinalPrice(Intervention intervention) {

        // 1. Récupération du prix initial depuis l'intervention
        double initialPrice = intervention.getPrice();

        // 2. Récupération du model (catégorie) via la chaîne : Intervention -> Vehicle -> VehicleType
        // Note: J'assume ici que vous avez les getters appropriés dans vos entités.
        // Exemple: intervention.getVehicle().getVehicleType().getModel()

        String vehicleModelType = "";

        try {
            // Navigation dans les objets pour trouver le type
            vehicleModelType = intervention.getVehicle().getVehicleType().getModel();
        } catch (NullPointerException e) {
            System.out.println("Error: Vehicle or VehicleType is missing information.");
            return initialPrice; // Retourne le prix de base si pas de type trouvé
        }

        // 3. Définition du coefficient multiplicateur (Weighting)
        double multiplier = 1.0;

        // On utilise toUpperCase() pour éviter les erreurs de majuscules/minuscules
        switch (vehicleModelType.toUpperCase()) {
            case "CITY_CAR":       // Citadine
                multiplier = 1.0;
                break;
            case "ELECTRIC":       // Électrique
                multiplier = 1.2;
                break;
            case "SUV":            // SUV
                multiplier = 1.3;
                break;
            case "4X4":            // 4x4
                multiplier = 1.5;
                break;
            case "SPORTS_CAR":     // Sportive
                multiplier = 2.0;
                break;
            default:
                System.out.println("Warning: Unknown vehicle model '" + vehicleModelType + "'. Please Try Again.");
                multiplier = 0;
                break;
        }

        // 4. Calcul du prix final
        double finalPrice = initialPrice * multiplier;

        // 5. Affichage du résultat (comme demandé)
        System.out.println("--- Price Calculation ---");
        System.out.println("Base Price: " + initialPrice + " €");
        System.out.println("Vehicle Category: " + vehicleModelType);
        System.out.println("Multiplier Applied: x" + multiplier);
        System.out.println("FINAL PRICE: " + finalPrice + " €");
        System.out.println("-------------------------");

        return finalPrice;
    }
}

