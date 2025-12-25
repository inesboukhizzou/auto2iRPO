package services;

import dao.InterventionDAO;
import dao.VehicleDAO;
import entities.Intervention;
import entities.InterventionType;
import entities.MaintenanceType;
import entities.Vehicle;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

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
    public List<MaintenanceDue> displayNextMaintenances() {
        Instant now = Instant.now();
        List<MaintenanceDue> result = new ArrayList<>();

        for (Vehicle vehicle : vehicleDAO.findAll()) {
            if (vehicle == null) continue;

            int currentMileage = vehicle.getLastMileage();
            List<Intervention> interventions = interventionDAO.findByVehicle(vehicle);
            if (interventions == null || interventions.isEmpty()) continue;

            Map<Long, Intervention> lastByType = getLastInterventionsByType(interventions);

            for (Intervention last : lastByType.values()) {
                if (!(last.getInterventionType() instanceof MaintenanceType)) continue;

                MaintenanceType mt = (MaintenanceType) last.getInterventionType();

                Instant dateLimit = computeDateLimit(last, mt);
                long daysRemaining = ChronoUnit.DAYS.between(now, dateLimit);

                int kmRemaining = computeKmRemaining(last, currentMileage, mt);

                result.add(new MaintenanceDue(vehicle, mt, daysRemaining, kmRemaining));
            }
        }

        result.sort(Comparator.comparingLong(MaintenanceDue::getUrgencyScore));
        return result;
    }

    private Map<Long, Intervention> getLastInterventionsByType(List<Intervention> interventions) {
        Map<Long, Intervention> lastByType = new HashMap<>();

        for (Intervention i : interventions) {
            if (i == null || i.getInterventionType() == null || i.getDate() == null) continue;

            Long typeId = i.getInterventionType().getId();
            lastByType.merge(
                    typeId,
                    i,
                    (oldI, newI) -> newI.getDate().after(oldI.getDate()) ? newI : oldI
            );
        }
        return lastByType;
    }

    private Instant computeDateLimit(Intervention last, MaintenanceType mt) {
        long lastMillis = last.getDate().getTime();
        long durationMillis = mt.getMaxDuration() * 60L * 1000L; // minutes → ms
        return Instant.ofEpochMilli(lastMillis + durationMillis);
    }

    private int computeKmRemaining(Intervention last, int currentMileage, MaintenanceType mt) {
        int mileageLimit = last.getVehicleMileage() + mt.getMaxMileage();
        return mileageLimit - currentMileage;
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