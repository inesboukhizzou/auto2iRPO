package ui.panels;

import services.InterventionService;
import services.VehicleService;
import ui.components.StyledTable;
import javax.swing.*;
import java.awt.*;
import entities.*;

public class VehiclePanel extends JPanel {
    private final Owner owner = new Owner();
    private final VehicleService vehicleService = new VehicleService();
    private final InterventionService interventionService = new InterventionService();
    public VehiclePanel(CardLayout layout, JPanel contentPanel) {
        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Header: Search Bar + Add Button
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JTextField searchField = new JTextField(20);
        searchField.setToolTipText("Search by license plate...");
        JButton btnSearch = new JButton("Search");

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchBar.setOpaque(false);
        searchBar.add(new JLabel("Search Plate: "));
        searchBar.add(searchField);
        searchBar.add(btnSearch);

        JButton btnAdd = new JButton("+ Add New Vehicle");
        btnAdd.setBackground(new Color(79, 70, 229));
        btnAdd.setForeground(Color.WHITE);
        // Switch to the ADD_VEHICLE form when pressed
        btnAdd.addActionListener(e -> layout.show(contentPanel, "ADD_VEHICLE"));

        header.add(searchBar, BorderLayout.WEST);
        header.add(btnAdd, BorderLayout.EAST);

        // Vehicle List Table
        String[] columns = {"License Plate", "Vehicle", "Owner", "Mileage", "Action"};
        Object[][] data = {}; // To be populated via vehicleDAO.findAll()

        StyledTable vehicleTable = new StyledTable(data, columns);
        add(header, BorderLayout.NORTH);
        add(new JScrollPane(vehicleTable), BorderLayout.CENTER);

    private void handleSearch(String plateInput) {
        try {
            // Standard French format: AB-123-CD
            String[] parts = plateInput.toUpperCase().split("-");
            if (parts.length != 3) {
                JOptionPane.showMessageDialog(this, "Format Error: Use AB-123-CD");
                return;
            }

            // Create the registration criteria matching your entity structure
            Registration criteria = new Registration(parts[0], Integer.parseInt(parts[1]), parts[2]);

            // Use the instance-based service call
            Vehicle found = vehicleService.searchVehicule(criteria);

            if (found != null) {
                // Display history using displayHistorique
                List<Intervention> history = interventionService.displayHistorique(found);
                updateUIWithFoundVehicle(found, history);
            } else {
                JOptionPane.showMessageDialog(this, "No vehicle found with plate: " + plateInput);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "The middle part of the plate must be a number.");
        }
    }

        public void searchVehicleByPlate(String input) {
            try {
                // Parse "AB-123-CD" into Registration parts
                String[] parts = input.split("-");
                if (parts.length != 3) throw new Exception("Use format: AB-123-CD");

                entities.Registration searchReg = new entities.Registration(
                        parts[0],
                        Integer.parseInt(parts[1]),
                        parts[2]
                );

                // Call your search logic
                entities.Vehicle found = vehicleService.searchVehicule(searchReg);

                if (found != null) {
                    // Display vehicle history using displayHistorique
                    java.util.List<entities.Intervention> history =
                            interventionService.displayHistorique(found);
                    showHistoryDialog(found, history);
                } else {
                    JOptionPane.showMessageDialog(this, "No vehicle found with that plate.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Format Error: " + e.getMessage());
            }
        }


}