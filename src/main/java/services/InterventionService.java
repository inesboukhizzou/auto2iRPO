package services;

import dao.InterventionDAO;
import dao.MaintenanceTypeDAO;
import dao.VehicleDAO;
import entities.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class InterventionService {

    private final InterventionDAO interventionDAO = new InterventionDAO();
    private final MaintenanceTypeDAO maintenanceTypeDAO = new MaintenanceTypeDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();

    /**
     * Calcule les futures interventions par ordre de priorité.
     * Utilise les seuils définis dans MaintenanceType (maxMileage, maxDuration).
     */
    public List<PlannedIntervention> computePlannedInterventions() {
        List<PlannedIntervention> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        List<Vehicle> vehicles = vehicleDAO.findAll();
        List<MaintenanceType> maintenanceTypes = maintenanceTypeDAO.findAll();

        for (Vehicle vehicle : vehicles) {
            List<Intervention> pastInterventions = interventionDAO.findByVehicle(vehicle);

            for (MaintenanceType mType : maintenanceTypes) {
                if (!isRecurringMaintenance(mType))
                    continue;

                Intervention last = findLastIntervention(pastInterventions, mType);

                int priority = 1;
                LocalDate plannedDate;

                if (last != null) {
                    LocalDate lastDate = last.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    int mileageSinceLast = vehicle.getLastMileage() - last.getVehicleMileage();
                    long monthsSinceLast = ChronoUnit.MONTHS.between(lastDate, today);

                    double timeRatio = (double) monthsSinceLast / mType.getMaxDuration();
                    double kmRatio = (double) mileageSinceLast / mType.getMaxMileage();
                    double dangerRatio = Math.max(timeRatio, kmRatio);

                    if (dangerRatio < 0.5) {
                        continue;
                    }

                    priority = (dangerRatio >= 1.0) ? 5 : (dangerRatio >= 0.8) ? 4 : 3;
                    plannedDate = lastDate.plusMonths(mType.getMaxDuration());
                } else {
                    // Si jamais fait, on ne met qu'une "Inspection Générale"
                    // au lieu de lister tous les types possibles séparément
                    if (!mType.getName().toLowerCase().contains("inspection")) {
                        continue;
                    }
                    priority = 2;
                    plannedDate = today.plusMonths(1);
                }

                result.add(new PlannedIntervention(vehicle, mType,
                        Date.from(plannedDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        priority));
            }
        }
        // Tri final
        result.sort(Comparator.comparingInt(PlannedIntervention::getPriority).reversed());
        return result;
    }

    public List<PlannedIntervention> getTopUrgentInterventions(int limit) {
        return computePlannedInterventions().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    private boolean isRecurringMaintenance(MaintenanceType type) {
        String name = type.getName().toLowerCase();
        return name.contains("oil") || name.contains("filter") || name.contains("brake")
                || name.contains("coolant") || name.contains("inspection") || name.contains("service");
    }

    private Intervention findLastIntervention(List<Intervention> interventions, InterventionType type) {
        return interventions.stream()
                .filter(i -> i.getInterventionType().equals(type))
                .max(Comparator.comparing(Intervention::getDate))
                .orElse(null);
    }

    /**
     * DTO optimisé pour l'affichage Swing.
     */

    public static class PlannedIntervention {
        private final String ownerName;
        private final String ownerPhone;
        private final String ownerEmail; // Ajout de l'email
        private final String vehicleLabel;
        private final String interventionName;
        private final Date plannedDate;
        private final int priority;

        public PlannedIntervention(Vehicle v, InterventionType it, Date pd, int p) {
            this.ownerName = v.getOwner().getFirstName() + " " + v.getOwner().getLastName();
            this.ownerPhone = v.getOwner().getPhoneNumber() != null ? v.getOwner().getPhoneNumber() : "N/A";
            this.ownerEmail = v.getOwner().getEmail() != null ? v.getOwner().getEmail() : "N/A"; //
            this.vehicleLabel = v.getVehicleType().getBrand() + " " + v.getVehicleType().getModel();
            this.interventionName = it.getName();
            this.plannedDate = pd;
            this.priority = p;
        }

        // Getters
        public String getOwnerName() {
            return ownerName;
        }

        public String getOwnerPhone() {
            return ownerPhone;
        }

        public String getOwnerEmail() {
            return ownerEmail;
        }

        public String getVehicleLabel() {
            return vehicleLabel;
        }

        public String getInterventionName() {
            return interventionName;
        }

        public Date getPlannedDate() {
            return plannedDate;
        }

        public int getPriority() {
            return priority;
        }
    }
}