package ui.controller;

import dao.*;
import entities.*;
import services.PriceService;
import ui.views.InterventionFormView;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Controller for intervention management.
 * Handles adding interventions and price calculation.
 */
public class InterventionController {

    private InterventionFormView view;
    private PriceService priceService;
    private InterventionDAO interventionDAO;
    private VehicleDAO vehicleDAO;
    private InterventionTypeDAO interventionTypeDAO;
    private MaintenanceTypeDAO maintenanceTypeDAO;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Controller constructor.
     * 
     * @param view         The intervention form view
     * @param priceService The price calculation service
     */
    public InterventionController(InterventionFormView view, PriceService priceService) {
        this.view = view;
        this.priceService = priceService;
        this.interventionDAO = new InterventionDAO();
        this.vehicleDAO = new VehicleDAO();
        this.interventionTypeDAO = new InterventionTypeDAO();
        this.maintenanceTypeDAO = new MaintenanceTypeDAO();

        initData();
        initEventHandlers();
    }

    /**
     * Initializes ComboBox data.
     */
    private void initData() {

        try {
            List<Vehicle> vehicles = vehicleDAO.findAll();
            view.comboVehicle.removeAllItems();
            for (Vehicle vehicle : vehicles) {
                view.comboVehicle.addItem(vehicle);
            }
        } catch (Exception e) {
            System.err.println("Error loading vehicles: " + e.getMessage());
        }

        try {
            List<InterventionType> types = interventionTypeDAO.findAll();
            view.comboInterventionType.removeAllItems();
            for (InterventionType type : types) {
                view.comboInterventionType.addItem(type);
            }
        } catch (Exception e) {
            System.err.println("Error loading intervention types: " + e.getMessage());
        }

        try {
            List<MaintenanceType> maintenanceTypes = maintenanceTypeDAO.findAll();
            view.comboMaintenanceType.removeAllItems();
            for (MaintenanceType mt : maintenanceTypes) {
                view.comboMaintenanceType.addItem(mt);
            }
        } catch (Exception e) {
            System.err.println("Error loading maintenance types: " + e.getMessage());
        }
    }

    /**
     * Configures event handlers.
     */
    private void initEventHandlers() {

        view.btnCalculate.addActionListener(e -> calculatePrice());

        view.btnSave.addActionListener(e -> saveIntervention());

        view.btnClear.addActionListener(e -> view.clearForm());
    }

    /**
     * Calculates the intervention price.
     * Uses PriceService to get the price from the Pricing table based on
     * InterventionType and VehicleType combination.
     * 
     * The PriceService.calculatePrice() method:
     * 1. First tries to find the exact price in the Pricing table
     * 2. If not found, uses fallback calculation with multipliers based on vehicle
     * characteristics
     */
    private void calculatePrice() {
        try {
            Vehicle selectedVehicle = (Vehicle) view.comboVehicle.getSelectedItem();
            InterventionType selectedType = (InterventionType) view.comboInterventionType.getSelectedItem();

            if (selectedVehicle == null) {
                JOptionPane.showMessageDialog(view,
                        "Please select a vehicle first.",
                        "Required Field",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (selectedType == null) {
                JOptionPane.showMessageDialog(view,
                        "Please select an intervention type first.",
                        "Required Field",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Log the selected values for debugging
            VehicleType vehicleType = selectedVehicle.getVehicleType();
            System.out.println("=== Price Calculation Request ===");
            System.out.println("Selected Vehicle: " + selectedVehicle);
            System.out.println("Vehicle Type: " + (vehicleType != null
                    ? vehicleType.getBrand() + " " + vehicleType.getModel() + " (ID: " + vehicleType.getId() + ")"
                    : "NULL"));
            System.out.println("Intervention Type: " + selectedType.getName() + " (ID: " + selectedType.getId() + ")");

            // Parse base price if provided (used for fallback calculation)
            double basePrice = 0.0;
            String basePriceText = view.txtBasePrice.getText().trim();
            if (!basePriceText.isEmpty()) {
                try {
                    basePrice = Double.parseDouble(basePriceText);
                } catch (NumberFormatException e) {
                    // Ignore, will use default
                }
            }

            // Create temporary intervention for price calculation
            Intervention tempIntervention = new Intervention();
            tempIntervention.setVehicle(selectedVehicle);
            tempIntervention.setInterventionType(selectedType);
            tempIntervention.setPrice(basePrice); // Base price for fallback only

            // Use PriceService.calculatePrice() which:
            // 1. Checks the Pricing table for exact InterventionType + VehicleType match
            // 2. If found, returns that price
            // 3. If not found, calculates fallback price with multipliers
            double finalPrice = priceService.calculatePrice(tempIntervention);

            // Display the calculated price
            view.displayPrice(finalPrice);

            System.out.println("Final calculated price: " + finalPrice + " €");
            System.out.println("=================================");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Error calculating price: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Saves a new intervention.
     */
    private void saveIntervention() {
        try {

            if (!validateFields()) {
                return;
            }

            Vehicle selectedVehicle = (Vehicle) view.comboVehicle.getSelectedItem();
            InterventionType selectedType = (InterventionType) view.comboInterventionType.getSelectedItem();

            Date interventionDate;
            String dateText = view.txtDate.getText().trim();
            if (dateText.equals("yyyy-MM-dd") || dateText.isEmpty()) {
                interventionDate = new Date();
            } else {
                interventionDate = dateFormat.parse(dateText);
            }

            int mileage = Integer.parseInt(view.txtMileage.getText().trim());

            // Calculate the price using PriceService
            double price;
            String finalPriceText = view.txtFinalPrice.getText().trim();

            if (!finalPriceText.isEmpty()) {
                // Use the already calculated final price if available
                price = Double.parseDouble(finalPriceText.replace(",", "."));
            } else {
                // Calculate price automatically using PriceService
                VehicleType vehicleType = selectedVehicle.getVehicleType();

                // First try to get exact price from database
                Double exactPrice = priceService.getExactPrice(selectedType, vehicleType);

                if (exactPrice != null) {
                    price = exactPrice;
                } else {
                    // Use fallback calculation with base price if provided
                    double basePrice = 0.0;
                    String basePriceText = view.txtBasePrice.getText().trim();
                    if (!basePriceText.isEmpty()) {
                        try {
                            basePrice = Double.parseDouble(basePriceText);
                        } catch (NumberFormatException e) {
                            // Use default
                        }
                    }

                    Intervention tempIntervention = new Intervention();
                    tempIntervention.setVehicle(selectedVehicle);
                    tempIntervention.setInterventionType(selectedType);
                    tempIntervention.setPrice(basePrice);

                    price = priceService.calculatePrice(tempIntervention);
                }
            }

            Intervention intervention = new Intervention();
            intervention.setVehicle(selectedVehicle);
            intervention.setInterventionType(selectedType);
            intervention.setDate(interventionDate);
            intervention.setVehicleMileage(mileage);
            intervention.setPrice(price);

            interventionDAO.save(intervention);

            if (mileage > selectedVehicle.getLastMileage()) {
                vehicleDAO.setLastMileage(selectedVehicle.getId(), mileage);
            }

            JOptionPane.showMessageDialog(view,
                    "Intervention registered successfully!\nPrice: " + String.format("%.2f", price) + " €",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            view.clearForm();

            initData();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view,
                    "Input error:\n- Mileage must be an integer\n- Price must be a valid number",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(view,
                    "Invalid date format.\nUse format: yyyy-MM-dd (e.g., 2024-01-15)",
                    "Format Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Error during registration: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Validates form fields.
     */
    private boolean validateFields() {

        if (view.comboVehicle.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(view,
                    "You MUST select a vehicle to register an intervention.",
                    "Vehicle Required",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (view.comboInterventionType.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(view,
                    "Please select an intervention type.",
                    "Required Field",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (view.txtMileage.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Please enter the current mileage.",
                    "Required Field",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Note: Base price is optional - if not provided, price will be
        // calculated from the Pricing table or using fallback logic

        return true;
    }

    /**
     * Refreshes ComboBox data.
     */
    public void refreshData() {
        initData();
    }
}
