package ui.panels;

import javax.swing.*;
import java.awt.*;

public class InterventionPanel extends JPanel {

    public InterventionPanel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Interventions", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JTable table = new JTable(
                new Object[][]{
                        {"01/02/2025", "AB-123-CD", "Maintenance", "Upcoming"},
                        {"20/01/2025", "EF-456-GH", "Repair", "Completed"}
                },
                new String[]{"Date", "Vehicle", "Type", "Status"}
        );

        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}