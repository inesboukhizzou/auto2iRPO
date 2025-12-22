package ui.panels;

import javax.swing.*;
import java.awt.*;

public class VehiclePanel extends JPanel {

    public VehiclePanel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Vehicle Management", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JTable table = new JTable(
                new Object[][]{
                        {"AB-123-CD", "Renault Clio", "85,000 km"},
                        {"EF-456-GH", "Peugeot 308", "42,000 km"}
                },
                new String[]{"License Plate", "Model", "Mileage"}
        );

        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}