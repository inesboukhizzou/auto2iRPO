package ui.panels;

import ui.components.*;
import services.VehicleService;

import javax.swing.*;
import java.awt.*;

// ui/panels/VehiclePanel.java
public class VehiclePanel extends JPanel {
    private VehicleService vehicleService = new VehicleService();
    private StyledTable table;

    public VehiclePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Section
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Vehicle Fleet");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));

        // Search Bar (Innovative Search-as-you-type)
        JTextField searchField = new JTextField(15);
        searchField.setToolTipText("Search by License Plate...");

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchBar.add(new JLabel("Search: "));
        searchBar.add(searchField);

        header.add(title, BorderLayout.WEST);
        header.add(searchBar, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Data Table
        String[] cols = {"License Plate", "Brand", "Model", "Last Mileage", "Owner"};
        // In a real app, populate from vehicleDAO.findAll()
        table = new StyledTable(new Object[][]{}, cols);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}