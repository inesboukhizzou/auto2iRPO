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
        // Load vehicles
        try {
            List<Vehicle> vehicles = vehicleDAO.findAll();
            view.comboVehicle.removeAllItems();
            for (Vehicle vehicle : vehicles) {
                view.comboVehicle.addItem(vehicle);
            }
        } catch (Exception e) {
            System.err.println("Error loading vehicles: " + e.getMessage());
        }

        // Load intervention types
        try {
            List<InterventionType> types = interventionTypeDAO.findAll();
            view.comboInterventionType.removeAllItems();
            for (InterventionType type : types) {
                view.comboInterventionType.addItem(type);
            }
        } catch (Exception e) {
            System.err.println("Error loading intervention types: " + e.getMessage());
        }

        // Load maintenance types
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
        // Calculate price button
        view.btnCalculate.addActionListener(e -> calculatePrice());

        // Save button
        view.btnSave.addActionListener(e -> saveIntervention());

        // Clear button
        view.btnClear.addActionListener(e -> view.clearForm());
    }

    /**
     * Calculates the intervention price.
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

            // Get base price
            double basePrice;
            try {
                basePrice = Double.parseDouble(view.txtBasePrice.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(view,
                        "Base price must be a valid number.",
                        "Invalid Format",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Create temporary intervention for calculation
            Intervention tempIntervention = new Intervention();
            tempIntervention.setVehicle(selectedVehicle);
            tempIntervention.setInterventionType(selectedType);
            tempIntervention.setPrice(basePrice);

            // Calculate final price
            double finalPrice = priceService.FinalPrice(tempIntervention);

            // Display result
            view.displayPrice(finalPrice);

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
            // Validate fields
            if (!validateFields()) {
                return;
            }

            // Get values
            Vehicle selectedVehicle = (Vehicle) view.comboVehicle.getSelectedItem();
            InterventionType selectedType = (InterventionType) view.comboInterventionType.getSelectedItem();

            // Date
            Date interventionDate;
            String dateText = view.txtDate.getText().trim();
            if (dateText.equals("yyyy-MM-dd") || dateText.isEmpty()) {
                interventionDate = new Date(); // Today by default
            } else {
                interventionDate = dateFormat.parse(dateText);
            }

            // Mileage
            int mileage = Integer.parseInt(view.txtMileage.getText().trim());

            // Price
            double price;
            String finalPriceText = view.txtFinalPrice.getText().trim();
            if (finalPriceText.isEmpty()) {
                // If no calculated price, use base price
                price = Double.parseDouble(view.txtBasePrice.getText().trim());
            } else {
                price = Double.parseDouble(finalPriceText.replace(",", "."));
            }

            // Create intervention
            Intervention intervention = new Intervention();
            intervention.setVehicle(selectedVehicle);
            intervention.setInterventionType(selectedType);
            intervention.setDate(interventionDate);
            intervention.setVehicleMileage(mileage);
            intervention.setPrice(price);

            // Save
            interventionDAO.save(intervention);

            // Update vehicle mileage if higher
            if (mileage > selectedVehicle.getLastMileage()) {
                vehicleDAO.setLastMileage(selectedVehicle.getId(), mileage);
            }

            // Success message
            JOptionPane.showMessageDialog(view,
                    "Intervention registered successfully!\nPrice: " + String.format("%.2f", price) + " €",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Clear the form
            view.clearForm();

            // Refresh data
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
        // Check vehicle - THIS IS REQUIRED
        if (view.comboVehicle.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(view,
                    "You MUST select a vehicle to register an intervention.",
                    "Vehicle Required",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Check intervention type
        if (view.comboInterventionType.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(view,
                    "Please select an intervention type.",
                    "Required Field",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Check mileage
        if (view.txtMileage.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Please enter the current mileage.",
                    "Required Field",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Check base price
        if (view.txtBasePrice.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Please enter a base price.",
                    "Required Field",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Refreshes ComboBox data.
     */
    public void refreshData() {
        initData();
    }
}
