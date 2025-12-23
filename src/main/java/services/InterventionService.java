package services;

import dao.InterventionDAO;
import dao.VehicleDAO;
import entities.Intervention;
import entities.InterventionType;
import entities.MaintenanceType;
import entities.Vehicle;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterventionService {

    private final InterventionDAO interventionDAO = new InterventionDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();

    /* ===================== addIntervention ===================== */
    public Intervention addIntervention(Intervention intervention, Vehicle vehicle) {
        if (intervention == null) {
            throw new IllegalArgumentException("Intervention cannot be null");
        }
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        intervention.setVehicle(vehicle);
        interventionDAO.save(intervention);

        return intervention;
    }

    /* ===================== displayHistorique ===================== */
    public List<Intervention> displayHistorique(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        return interventionDAO.findByVehicle(vehicle);
    }

    /* ===================== displayNextMaintenances ===================== */
    public List<MaintenanceDue> displayNextMaintenances(Date currentDate) {
        if (currentDate == null) {
            throw new IllegalArgumentException("currentDate cannot be null");
        }

        // Safely convert the current Date to LocalDate
        LocalDate now = convertToLocalDate(currentDate);
        List<MaintenanceDue> result = new ArrayList<>();

        List<Vehicle> vehicles = vehicleDAO.findAll();
        for (Vehicle vehicle : vehicles) {
            if (vehicle == null) continue;

            int currentMileage = vehicle.getLastMileage();
            List<Intervention> interventions = interventionDAO.findByVehicle(vehicle);

            if (interventions == null || interventions.isEmpty()) continue;

            Map<Long, Intervention> lastByTypeId = new HashMap<>();
            for (Intervention intervention : interventions) {
                if (intervention == null || intervention.getInterventionType() == null || intervention.getDate() == null) {
                    continue;
                }
                InterventionType type = intervention.getInterventionType();
                Long typeId = type.getId();

                Intervention currentLast = lastByTypeId.get(typeId);
                if (currentLast == null || intervention.getDate().after(currentLast.getDate())) {
                    lastByTypeId.put(typeId, intervention);
                }
            }

            for (Intervention lastIntervention : lastByTypeId.values()) {
                InterventionType type = lastIntervention.getInterventionType();

                if (!(type instanceof MaintenanceType)) continue;

                MaintenanceType maintenanceType = (MaintenanceType) type;

                // FIX: Use the safe conversion method to avoid UnsupportedOperationException
                LocalDate lastDate = convertToLocalDate(lastIntervention.getDate());

                LocalDate dateLimit = lastDate.plusDays(maintenanceType.getMaxDuration());
                int mileageLimit = lastIntervention.getVehicleMileage() + maintenanceType.getMaxMileage();

                long daysRemaining = ChronoUnit.DAYS.between(now, dateLimit);
                int kmRemaining = mileageLimit - currentMileage;

                result.add(new MaintenanceDue(vehicle, type, daysRemaining, kmRemaining));
            }
        }

        result.sort(Comparator.comparingLong(MaintenanceDue::getUrgencyScore));
        return result;
    }

    /**
     * Helper method to safely convert java.util.Date or java.sql.Date to LocalDate.
     */
    private LocalDate convertToLocalDate(Date dateToConvert) {
        if (dateToConvert instanceof java.sql.Date) {
            return ((java.sql.Date) dateToConvert).toLocalDate();
        }
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
    /* ===================== statistics ===================== */
    public double getTotalCostByVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        return interventionDAO.getTotalCostByVehicle(vehicle.getId());
    }

    public long countInterventions(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        return interventionDAO.countByVehicle(vehicle.getId());
    }

    public static class MaintenanceDue {
        private final Vehicle vehicle;
        private final InterventionType interventionType;
        private final long daysRemaining;
        private final int kmRemaining;

        public MaintenanceDue(Vehicle vehicle, InterventionType interventionType, long daysRemaining, int kmRemaining) {
            this.vehicle = vehicle;
            this.interventionType = interventionType;
            this.daysRemaining = daysRemaining;
            this.kmRemaining = kmRemaining;
        }

        public Vehicle getVehicle() {
            return vehicle;
        }

        public InterventionType getInterventionType() {
            return interventionType;
        }

        public long getDaysRemaining() {
            return daysRemaining;
        }

        public int getKmRemaining() {
            return kmRemaining;
        }

        public long getUrgencyScore() {
            return Math.min(daysRemaining, kmRemaining);
        }

        @Override
        public String toString() {
            return "MaintenanceDue{" +
                    "vehicleId=" + (vehicle != null ? vehicle.getId() : null) +
                    ", interventionTypeId=" + (interventionType != null ? interventionType.getId() : null) +
                    ", interventionTypeName='" + (interventionType != null ? interventionType.getName() : null) + '\'' +
                    ", daysRemaining=" + daysRemaining +
                    ", kmRemaining=" + kmRemaining +
                    '}';
        }
    }
}