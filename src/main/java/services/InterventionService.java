package services;

import dao.InterventionDAO;
import dao.MaintenanceTypeDAO;
import dao.VehicleDAO;
import entities.*;

import java.util.*;

public class InterventionService {

    private final InterventionDAO interventionDAO = new InterventionDAO();
    private final MaintenanceTypeDAO maintenanceTypeDAO = new MaintenanceTypeDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();

    /**
     * Computes future interventions ordered by priority.
     * Priority scale: 1 (low) -> 5 (high)
     */
    public List<PlannedIntervention> computePlannedInterventions() {

        List<PlannedIntervention> result = new ArrayList<>();
        Date today = new Date();

        List<Vehicle> vehicles = vehicleDAO.findAll();
        List<MaintenanceType> maintenanceTypes = maintenanceTypeDAO.findAll();

        for (Vehicle vehicle : vehicles) {

            List<Intervention> pastInterventions =
                    interventionDAO.findByVehicle(vehicle);

            for (MaintenanceType maintenanceType : maintenanceTypes) {

                // We only plan recurring maintenance interventions
                if (!isRecurringMaintenance(maintenanceType)) {
                    continue;
                }

                Intervention last = findLastIntervention(
                        pastInterventions,
                        maintenanceType
                );

                int priority = 1;
                Date plannedDate;

                if (last != null) {

                    long months = monthsBetween(last.getDate(), today);
                    int mileageDiff =
                            vehicle.getLastMileage() - last.getVehicleMileage();

                    String name = maintenanceType.getName().toLowerCase();

                    if (name.contains("oil")) {
                        if (months >= 4 || mileageDiff >= 5000) priority = 5;
                        else if (months >= 3) priority = 4;
                        plannedDate = addMonths(last.getDate(), 6);

                    } else if (name.contains("filter")) {
                        if (months >= 6) priority = 4;
                        plannedDate = addMonths(last.getDate(), 12);

                    } else if (name.contains("brake")) {
                        if (mileageDiff >= 10000) priority = 4;
                        plannedDate = addMonths(last.getDate(), 12);

                    } else {
                        // default recurring maintenance
                        if (months >= 12) priority = 3;
                        plannedDate = addMonths(last.getDate(), 12);
                    }

                } else {
                    // Never done before: proactive reminder
                    priority = 3;
                    plannedDate = addMonths(today, 3);
                }

                result.add(new PlannedIntervention(
                        vehicle,
                        maintenanceType,
                        plannedDate,
                        priority
                ));
            }
        }

        result.sort(
                Comparator.comparingInt(PlannedIntervention::getPriority).reversed()
        );

        return result;
    }

    private boolean isRecurringMaintenance(InterventionType type) {
        String name = type.getName().toLowerCase();

        return name.contains("oil")
                || name.contains("filter")
                || name.contains("brake")
                || name.contains("coolant")
                || name.contains("inspection")
                || name.contains("service");
    }

    private Intervention findLastIntervention(
            List<Intervention> interventions,
            InterventionType type
    ) {
        return interventions.stream()
                .filter(i -> i.getInterventionType().equals(type))
                .max(Comparator.comparing(Intervention::getDate))
                .orElse(null);
    }

    private long monthsBetween(Date d1, Date d2) {
        return (d2.getTime() - d1.getTime()) / (1000L * 60 * 60 * 24 * 30);
    }

    private Date addMonths(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    /**
     * DTO used only for displaying planned interventions.
     */
    public static class PlannedIntervention {

        private final Vehicle vehicle;
        private final InterventionType interventionType;
        private final Date plannedDate;
        private final int priority;

        public PlannedIntervention(
                Vehicle vehicle,
                InterventionType interventionType,
                Date plannedDate,
                int priority
        ) {
            this.vehicle = vehicle;
            this.interventionType = interventionType;
            this.plannedDate = plannedDate;
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }

        @Override
        public String toString() {
            Owner owner = vehicle.getOwner();
            VehicleType vt = vehicle.getVehicleType();

            return "Priority " + priority +
                    " | " + interventionType.getName() +
                    " | Planned: " + plannedDate +
                    " | Owner: " + owner.getPhoneNumber() + " " +
                    owner.getFirstName() + " " + owner.getLastName() +
                    " | Vehicle: " + vt.getBrand() + " " + vt.getModel();
        }
    }
}
