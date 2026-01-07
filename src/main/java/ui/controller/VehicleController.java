package ui.controller;

import dao.*;
import entities.*;
import ui.views.VehicleFormView;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Controller for vehicle management.
 * Handles adding new vehicles, owners, and vehicle types.
 */
public class VehicleController {

    private VehicleFormView view;
    private VehicleDAO vehicleDAO;
    private OwnerDAO ownerDAO;
    private VehicleTypeDAO vehicleTypeDAO;
    private RegistrationDAO registrationDAO;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Controller constructor.
     * 
     * @param view The vehicle form view
     */
    public VehicleController(VehicleFormView view) {
        this.view = view;
        this.vehicleDAO = new VehicleDAO();
        this.ownerDAO = new OwnerDAO();
        this.vehicleTypeDAO = new VehicleTypeDAO();
        this.registrationDAO = new RegistrationDAO();

        initData();
        initEventHandlers();
    }

    /**
     * Initializes ComboBox data.
     */
    private void initData() {
        // Load owners
        try {
            List<Owner> owners = ownerDAO.findAll();
            view.comboOwner.removeAllItems();
            for (Owner owner : owners) {
                view.comboOwner.addItem(owner);
            }
        } catch (Exception e) {
            System.err.println("Error loading owners: " + e.getMessage());
        }

        // Load vehicle types
        try {
            List<VehicleType> types = vehicleTypeDAO.findAll();
            view.comboVehicleType.removeAllItems();
            for (VehicleType type : types) {
                view.comboVehicleType.addItem(type);
            }
        } catch (Exception e) {
            System.err.println("Error loading vehicle types: " + e.getMessage());
        }
    }

    /**
     * Configures event handlers.
     */
    private void initEventHandlers() {
        // Save button
        view.btnSave.addActionListener(e -> saveVehicle());

        // Clear button
        view.btnClear.addActionListener(e -> view.clearForm());

        // New owner button
        view.btnAddOwner.addActionListener(e -> createNewOwner());

        // Note: btnAddVehicleType toggles the form visibility in the view
        // We need to handle saving the new vehicle type when the main save is clicked
        // or add a separate save button for vehicle type
    }

    /**
     * Saves a new vehicle.
     */
    private void saveVehicle() {
        try {
            // First, check if a new vehicle type needs to be created
            if (view.isVehicleTypeFormVisible()) {
                VehicleType newType = createNewVehicleType();
                if (newType == null) {
                    return; // Validation failed
                }
                view.comboVehicleType.addItem(newType);
                view.comboVehicleType.setSelectedItem(newType);
                view.hideVehicleTypeForm();
            }

            // Validate fields
            if (!validateFields()) {
                return;
            }

            // Get values
            Owner selectedOwner = (Owner) view.comboOwner.getSelectedItem();
            VehicleType selectedType = (VehicleType) view.comboVehicleType.getSelectedItem();

            String part1 = view.txtRegPart1.getText().trim().toUpperCase();
            int part2 = Integer.parseInt(view.txtRegPart2.getText().trim());
            String part3 = view.txtRegPart3.getText().trim().toUpperCase();

            int mileage = Integer.parseInt(view.txtMileage.getText().trim());

            Date dateOfFirstReg;
            String dateText = view.txtDateRegistration.getText().trim();
            if (dateText.equals("yyyy-MM-dd") || dateText.isEmpty()) {
                dateOfFirstReg = new Date(); // Today by default
            } else {
                dateOfFirstReg = dateFormat.parse(dateText);
            }

            // Create registration
            Registration registration = new Registration(part1, part2, part3);
            registrationDAO.create(registration);

            // Create vehicle
            Vehicle vehicle = new Vehicle();
            vehicle.setOwner(selectedOwner);
            vehicle.setVehicleType(selectedType);
            vehicle.setRegistration(registration);
            vehicle.setLastMileage(mileage);
            vehicle.setDateOfFirstRegistration(dateOfFirstReg);

            vehicleDAO.create(vehicle);

            // Success message
            String plateNumber = part1 + "-" + part2 + "-" + part3;
            JOptionPane.showMessageDialog(view,
                    "Vehicle " + plateNumber + " registered successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Clear the form
            view.clearForm();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view,
                    "Input error:\n- Mileage must be an integer\n- The central part of the plate must be a number",
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
     * Creates a new vehicle type from the form fields.
     */
    private VehicleType createNewVehicleType() {
        try {
            String brand = view.txtBrand.getText().trim();
            String model = view.txtModel.getText().trim();
            String fuelType = (String) view.comboFuelType.getSelectedItem();
            String gearbox = (String) view.comboGearbox.getSelectedItem();
            int doors = Integer.parseInt(view.txtDoors.getText().trim());
            int seats = Integer.parseInt(view.txtSeats.getText().trim());
            int power = Integer.parseInt(view.txtPower.getText().trim());

            // Validation
            if (brand.isEmpty() || model.isEmpty()) {
                JOptionPane.showMessageDialog(view,
                        "Brand and Model are required for a new vehicle type.",
                        "Required Fields",
                        JOptionPane.WARNING_MESSAGE);
                return null;
            }

            // Create and save the vehicle type
            VehicleType newType = new VehicleType(brand, model, fuelType, gearbox, doors, seats, power);
            vehicleTypeDAO.create(newType);

            JOptionPane.showMessageDialog(view,
                    "Vehicle type " + brand + " " + model + " created successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            return newType;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view,
                    "Doors, Seats, and Power must be valid numbers.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Error creating vehicle type: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Validates form fields.
     */
    private boolean validateFields() {
        // Check owner
        if (view.comboOwner.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(view,
                    "Please select or create an owner.",
                    "Required Field",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Check vehicle type
        if (view.comboVehicleType.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(view,
                    "Please select or create a vehicle type.",
                    "Required Field",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Check license plate
        String part1 = view.txtRegPart1.getText().trim();
        String part2 = view.txtRegPart2.getText().trim();
        String part3 = view.txtRegPart3.getText().trim();

        if (part1.isEmpty() || part2.isEmpty() || part3.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Please enter the complete license plate.",
                    "Required Field",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (part1.length() != 2 || !part1.matches("[A-Za-z]+")) {
            JOptionPane.showMessageDialog(view,
                    "The first part of the plate must contain 2 letters.",
                    "Invalid Format",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!part2.matches("\\d{1,3}")) {
            JOptionPane.showMessageDialog(view,
                    "The central part of the plate must contain 1 to 3 digits.",
                    "Invalid Format",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (part3.length() != 2 || !part3.matches("[A-Za-z]+")) {
            JOptionPane.showMessageDialog(view,
                    "The third part of the plate must contain 2 letters.",
                    "Invalid Format",
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

        return true;
    }

    /**
     * Creates a new owner via a dialog.
     */
    private void createNewOwner() {
        // Custom input panel
        JPanel panel = new JPanel(new java.awt.GridLayout(4, 2, 10, 10));

        JTextField txtFirstName = new JTextField();
        JTextField txtLastName = new JTextField();
        JTextField txtPhone = new JTextField();
        JTextField txtEmail = new JTextField();

        panel.add(new JLabel("First Name:"));
        panel.add(txtFirstName);
        panel.add(new JLabel("Last Name:"));
        panel.add(txtLastName);
        panel.add(new JLabel("Phone:"));
        panel.add(txtPhone);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);

        int result = JOptionPane.showConfirmDialog(view, panel,
                "New Owner",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String firstName = txtFirstName.getText().trim();
            String lastName = txtLastName.getText().trim();
            String phone = txtPhone.getText().trim();
            String email = txtEmail.getText().trim();

            // Validation
            if (firstName.isEmpty() || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(view,
                        "First name and last name are required.",
                        "Required Fields",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Create owner
                Owner newOwner = new Owner(firstName, lastName, phone, email);
                ownerDAO.create(newOwner);

                // Add to ComboBox and select
                view.comboOwner.addItem(newOwner);
                view.comboOwner.setSelectedItem(newOwner);

                JOptionPane.showMessageDialog(view,
                        "Owner " + firstName + " " + lastName + " created successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(view,
                        "Error creating owner: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
     * Refreshes ComboBox data.
     */
    public void refreshData() {
        initData();
    }
}
