package ui.panels;

import entities.Owner;
import entities.Registration;
import entities.Vehicle;
import services.*;
import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class AddVehiclePanel extends JPanel {
    private JTextField txtPlate, txtBrand, txtModel, txtMileage, txtOwnerFN,txtOwnerLN,  txtPhone, txtEmail;
    private JComboBox<String> cbEnergy, cbGearbox;
    private VehicleService vehicleService = new VehicleService();
    private OwnerService ownerService = new OwnerService();

    private final InterventionService interventionService = new InterventionService();

    public AddVehiclePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 251));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Header
        JLabel title = new JLabel("Add New Vehicle");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Form Container
        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 0.5;

        // Section: Vehicle Info
        addSectionHeader(formGrid, "Vehicle Information", 0, gbc);
        txtPlate = addField(formGrid, "License Plate *", 1, 0, gbc);
        txtBrand = addField(formGrid, "Brand *", 1, 1, gbc);
        txtModel = addField(formGrid, "Model *", 2, 0, gbc);
        txtMileage = addField(formGrid, "Current Mileage (km) *", 2, 1, gbc);

        cbEnergy = new JComboBox<>(new String[]{"Petrol", "Diesel", "Electric", "Hybrid"});
        addCustomField(formGrid, "Energy Type *", cbEnergy, 3, 0, gbc);

        cbGearbox = new JComboBox<>(new String[]{"Manual", "Automatic"});
        addCustomField(formGrid, "Gearbox *", cbGearbox, 3, 1, gbc);

        // Section: Owner Info
        addSectionHeader(formGrid, "Owner Information", 4, gbc);
        txtOwnerFN = addField(formGrid, "Owner First Name *", 5, 0, gbc);
        txtOwnerLN = addField(formGrid, "Owner Last Name *", 5, 0, gbc);
        txtPhone = addField(formGrid, "Phone Number *", 5, 1, gbc);
        txtEmail = addField(formGrid, "Email Address *", 6, 0, gbc);

        JScrollPane scrollPane = new JScrollPane(formGrid);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        // Action Buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        JButton btnAdd = new JButton("Add Vehicle");
        btnAdd.setBackground(new Color(79, 70, 229));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> handleSave());

        JButton btnCancel = new JButton("Cancel");
        footer.add(btnCancel);
        footer.add(btnAdd);
        add(footer, BorderLayout.SOUTH);
    }

    private JTextField addField(JPanel p, String label, int row, int col, GridBagConstraints gbc) {
        JTextField field = new JTextField();
        addCustomField(p, label, field, row, col, gbc);
        return field;
    }

    private void addCustomField(JPanel p, String label, JComponent comp, int row, int col, GridBagConstraints gbc) {
        gbc.gridy = row;
        gbc.gridx = col;
        JPanel container = new JPanel(new BorderLayout(5, 5));
        container.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        container.add(lbl, BorderLayout.NORTH);
        container.add(comp, BorderLayout.CENTER);
        p.add(container, gbc);
    }

    private void addSectionHeader(JPanel p, String text, int row, GridBagConstraints gbc) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        JLabel header = new JLabel(text);
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setForeground(new Color(71, 85, 105));
        p.add(header, gbc);
        gbc.gridwidth = 1; // Reset
        gbc.insets = new Insets(10, 10, 10, 10);
    }


    // Inside AddVehiclePanel handleSave() method
    private void handleSave() {
        try {
            // 1. Create Owner with both Names
            Owner owner = new Owner(txtFirstName.getText(), txtLastName.getText());
            owner.setPhoneNumber(txtPhone.getText());
            owner.setEmail(txtEmail.getText());
            ownerService.addOwner(owner); // Persist owner first

            // 2. Define Vehicle Type based on requirements
            // In a real app, you might find existing type via VehicleTypeDAO
            VehicleType type = new VehicleType();
            type.setBrand(txtBrand.getText());
            type.setModel(txtModel.getText());
            type.setFuelType((String)cbEnergy.getSelectedItem());
            type.setPower(Integer.parseInt(txtPower.getText())); // Missing arg
            type.setNumberOfDoors(Integer.parseInt(txtDoors.getText())); // Missing arg

            // Persist type if new (requires a VehicleTypeDAO or manual EM access)

            // 3. Create Registration
            String[] p = txtPlate.getText().split("-");
            Registration reg = new Registration(p[0], Integer.parseInt(p[1]), p[2]);

            // 4. Create and Save Vehicle
            Vehicle v = new Vehicle();
            v.setRegistration(reg);
            v.setVehicleType(type);
            v.setLastMileage(Integer.parseInt(txtMileage.getText()));
            v.setDateOfFirstRegistration(new Date());

            vehicleService.addVehicule(v, owner); // Use service to save

            JOptionPane.showMessageDialog(this, "Success: Vehicle and Owner Created!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Save Failed: Ensure all fields are filled.");
        }
    }

    // ui/panels/VehiclePanel.java snippet


    private void showHistoryDialog(entities.Vehicle v, java.util.List<entities.Intervention> history) {
        // Creative popup displaying the vehicle history list
        StringBuilder sb = new StringBuilder("History for " + v.toString() + ":\n");
        history.forEach(i -> sb.append("- ").append(i.toString()).append("\n")); //
        JOptionPane.showMessageDialog(this, sb.toString());
    }
}