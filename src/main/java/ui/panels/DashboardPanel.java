package ui.panels;

import javax.swing.*;
import java.awt.*;
// Updated ui/panels/DashboardPanel.java
public class DashboardPanel extends JPanel {
    public DashboardPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // 1. Header with Search
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));

        // Quick Search Bar (from mockup)
        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(25);
        searchField.putClientProperty("JTextField.placeholderText", "Enter license plate...");
        searchBox.add(searchField);
        searchBox.add(new JButton("Search"));

        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(searchBox, BorderLayout.SOUTH);

        // 2. Stat Cards Section (Total Vehicles, Urgent, Scheduled)
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.add(createStatCard("Total Vehicles", "3", Color.BLUE));
        statsPanel.add(createStatCard("Urgent Maintenance", "3", Color.ORANGE));
        statsPanel.add(createStatCard("Scheduled Tasks", "3", Color.GREEN));

        add(topPanel, BorderLayout.NORTH);
        add(statsPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, String value, Color accent) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        // Add components here to match mockup...
        return card;
    }
}