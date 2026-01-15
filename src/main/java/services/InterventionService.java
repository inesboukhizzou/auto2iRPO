package services;

import dao.InterventionDAO;
import dao.MaintenanceTypeDAO;
import dao.VehicleDAO;
import entities.Intervention;
import entities.MaintenanceType;
import entities.Vehicle;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing interventions and calculating future maintenance
 * schedules.
 * Provides methods to determine urgent maintenance based on mileage and time
 * thresholds.
 */
public class InterventionService {

    private final InterventionDAO interventionDAO = new InterventionDAO();
    private final MaintenanceTypeDAO maintenanceTypeDAO = new MaintenanceTypeDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();

    /**
     * Represents a planned future intervention with priority information.
     */
    public static class PlannedIntervention {
        private final Vehicle vehicle;
        private final MaintenanceType maintenanceType;
        private final Date plannedDate;
        private final int priority; // Higher = more urgent
        private final String reason;

        public PlannedIntervention(Vehicle vehicle, MaintenanceType maintenanceType,
                Date plannedDate, int priority, String reason) {
            this.vehicle = vehicle;
            this.maintenanceType = maintenanceType;
            this.plannedDate = plannedDate;
            this.priority = priority;
            this.reason = reason;
        }

        public Vehicle getVehicle() {
            return vehicle;
        }

        public MaintenanceType getMaintenanceType() {
            return maintenanceType;
        }

        public Date getPlannedDate() {
            return plannedDate;
        }

        public int getPriority() {
            return priority;
        }

        public String getReason() {
            return reason;
        }

        /**
         * Gets the intervention name from the maintenance type.
         */
        public String getInterventionName() {
            return maintenanceType != null ? maintenanceType.getName() : "Unknown";
        }

        /**
         * Gets the owner's full name.
         */
        public String getOwnerName() {
            if (vehicle != null && vehicle.getOwner() != null) {
                return vehicle.getOwner().getFirstName() + " " + vehicle.getOwner().getLastName();
            }
            return "N/A";
        }

        /**
         * Gets the owner's phone number.
         */
        public String getOwnerPhone() {
            if (vehicle != null && vehicle.getOwner() != null) {
                return vehicle.getOwner().getPhoneNumber() != null
                        ? vehicle.getOwner().getPhoneNumber()
                        : "N/A";
            }
            return "N/A";
        }

        /**
         * Gets the owner's email.
         */
        public String getOwnerEmail() {
            if (vehicle != null && vehicle.getOwner() != null) {
                return vehicle.getOwner().getEmail() != null
                        ? vehicle.getOwner().getEmail()
                        : "N/A";
            }
            return "N/A";
        }

        /**
         * Gets a display label for the vehicle (registration + brand/model).
         */
        public String getVehicleLabel() {
            if (vehicle == null)
                return "N/A";

            StringBuilder sb = new StringBuilder();
            if (vehicle.getRegistration() != null) {
                sb.append(vehicle.getRegistration().getPart1())
                        .append("-")
                        .append(vehicle.getRegistration().getPart2())
                        .append("-")
                        .append(vehicle.getRegistration().getPart3());
            }
            if (vehicle.getVehicleType() != null) {
                sb.append(" (")
                        .append(vehicle.getVehicleType().getBrand())
                        .append(" ")
                        .append(vehicle.getVehicleType().getModel())
                        .append(")");
            }
            return sb.toString();
        }
    }

    /**
     * Gets the top most urgent planned interventions across all vehicles.
     * Urgency is calculated based on:
     * - Time elapsed since last intervention of the same type vs maxDuration
     * - Mileage driven since last intervention vs maxMileage
     *
     * @param limit Maximum number of interventions to return
     * @return List of planned interventions sorted by priority (highest first)
     */
    public List<PlannedIntervention> getTopUrgentInterventions(int limit) {
        List<PlannedIntervention> allPlanned = new ArrayList<>();

        // Get all vehicles and all maintenance types
        List<Vehicle> vehicles = vehicleDAO.findAll();
        List<MaintenanceType> maintenanceTypes = maintenanceTypeDAO.findAll();

        Date today = new Date();

        for (Vehicle vehicle : vehicles) {
            // Load interventions for this vehicle
            List<Intervention> vehicleInterventions = interventionDAO.findByVehicle(vehicle);

            for (MaintenanceType mt : maintenanceTypes) {
                PlannedIntervention planned = calculatePlannedIntervention(
                        vehicle, mt, vehicleInterventions, today);

                if (planned != null && planned.getPriority() > 0) {
                    allPlanned.add(planned);
                }
            }
        }

        // Sort by priority (descending) and return top 'limit' results
        return allPlanned.stream()
                .sorted((a, b) -> Integer.compare(b.getPriority(), a.getPriority()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Calculates if a planned intervention is needed for a specific vehicle and
     * maintenance type.
     */
    private PlannedIntervention calculatePlannedIntervention(Vehicle vehicle,
            MaintenanceType maintenanceType,
            List<Intervention> interventions,
            Date today) {
        // Find the last intervention of this maintenance type for the vehicle
        Intervention lastIntervention = interventions.stream()
                .filter(i -> i.getInterventionType() != null
                        && i.getInterventionType().getId().equals(maintenanceType.getId()))
                .max(Comparator.comparing(Intervention::getDate))
                .orElse(null);

        int currentMileage = vehicle.getLastMileage();
        int maxMileage = maintenanceType.getMaxMileage();
        int maxDurationMonths = maintenanceType.getMaxDuration();

        int priority = 0;
        StringBuilder reason = new StringBuilder();
        Date plannedDate = today;

        if (lastIntervention == null) {
            // Never had this type of maintenance - use vehicle registration date
            Date baseDate = vehicle.getDateOfFirstRegistration();
            if (baseDate == null) {
                baseDate = today;
            }

            // Calculate when maintenance should be due based on maxDuration
            Calendar cal = Calendar.getInstance();
            cal.setTime(baseDate);
            cal.add(Calendar.MONTH, maxDurationMonths);
            plannedDate = cal.getTime();

            // Check if overdue by time
            if (plannedDate.before(today)) {
                long daysOverdue = (today.getTime() - plannedDate.getTime()) / (1000 * 60 * 60 * 24);
                priority += Math.min(5, (int) (daysOverdue / 30)); // +1 priority per month overdue, max 5
                reason.append("Overdue by ").append(daysOverdue).append(" days. ");
            } else {
                // Calculate days until due
                long daysUntil = (plannedDate.getTime() - today.getTime()) / (1000 * 60 * 60 * 24);
                if (daysUntil <= 30) {
                    priority += 2; // Due within a month
                    reason.append("Due in ").append(daysUntil).append(" days. ");
                } else if (daysUntil <= 90) {
                    priority += 1; // Due within 3 months
                    reason.append("Due in ").append(daysUntil).append(" days. ");
                }
            }

            // Check mileage threshold (assuming no previous intervention, use absolute
            // threshold)
            if (currentMileage >= maxMileage) {
                priority += 3; // Exceeded mileage threshold
                reason.append("Mileage exceeded (").append(currentMileage).append("/").append(maxMileage)
                        .append(" km). ");
            } else if (currentMileage >= maxMileage * 0.9) {
                priority += 2; // Near mileage threshold
                reason.append("Near mileage limit (").append(currentMileage).append("/").append(maxMileage)
                        .append(" km). ");
            }

        } else {
            // Has previous intervention - calculate from that
            Date lastDate = lastIntervention.getDate();
            int lastMileage = lastIntervention.getVehicleMileage();

            // Calculate due date based on last intervention + maxDuration
            Calendar cal = Calendar.getInstance();
            cal.setTime(lastDate);
            cal.add(Calendar.MONTH, maxDurationMonths);
            plannedDate = cal.getTime();

            // Check time-based urgency
            if (plannedDate.before(today)) {
                long daysOverdue = (today.getTime() - plannedDate.getTime()) / (1000 * 60 * 60 * 24);
                priority += Math.min(5, (int) (daysOverdue / 30) + 2); // +1 priority per month overdue
                reason.append("Overdue by ").append(daysOverdue).append(" days. ");
            } else {
                long daysUntil = (plannedDate.getTime() - today.getTime()) / (1000 * 60 * 60 * 24);
                if (daysUntil <= 30) {
                    priority += 2;
                    reason.append("Due in ").append(daysUntil).append(" days. ");
                } else if (daysUntil <= 90) {
                    priority += 1;
                    reason.append("Due in ").append(daysUntil).append(" days. ");
                }
            }

            // Check mileage-based urgency
            int mileageSinceLast = currentMileage - lastMileage;
            if (mileageSinceLast >= maxMileage) {
                priority += 3; // Exceeded mileage interval
                reason.append("Mileage interval exceeded (").append(mileageSinceLast)
                        .append("/").append(maxMileage).append(" km since last). ");
            } else if (mileageSinceLast >= maxMileage * 0.9) {
                priority += 2; // Near mileage interval
                reason.append("Near mileage interval (").append(mileageSinceLast)
                        .append("/").append(maxMileage).append(" km since last). ");
            } else if (mileageSinceLast >= maxMileage * 0.75) {
                priority += 1;
                reason.append("Approaching mileage interval (").append(mileageSinceLast)
                        .append("/").append(maxMileage).append(" km since last). ");
            }
        }

        // Only return if there's some urgency
        if (priority > 0) {
            return new PlannedIntervention(vehicle, maintenanceType, plannedDate,
                    priority, reason.toString().trim());
        }

        return null;
    }

    /**
     * Gets all planned interventions for a specific vehicle.
     *
     * @param vehicle The vehicle to check
     * @return List of planned interventions for the vehicle
     */
    public List<PlannedIntervention> getPlannedInterventionsForVehicle(Vehicle vehicle) {
        List<PlannedIntervention> planned = new ArrayList<>();
        List<MaintenanceType> maintenanceTypes = maintenanceTypeDAO.findAll();
        List<Intervention> vehicleInterventions = interventionDAO.findByVehicle(vehicle);
        Date today = new Date();

        for (MaintenanceType mt : maintenanceTypes) {
            PlannedIntervention pi = calculatePlannedIntervention(vehicle, mt, vehicleInterventions, today);
            if (pi != null) {
                planned.add(pi);
            }
        }

        return planned.stream()
                .sorted((a, b) -> Integer.compare(b.getPriority(), a.getPriority()))
                .collect(Collectors.toList());
    }
}