package ui;
import ui.panels.*;
import ui.components.SideMenu;
import ui.panels.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;

    public MainFrame() {
        setTitle("Auto2i - Garage Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Panels
        DashboardPanel dashboardPanel = new DashboardPanel();
        VehiclePanel vehiclePanel = new VehiclePanel();
        InterventionPanel interventionPanel = new InterventionPanel();

        contentPanel.add(dashboardPanel, "DASHBOARD");
        contentPanel.add(vehiclePanel, "VEHICLES");
        contentPanel.add(interventionPanel, "INTERVENTIONS");

        SideMenu sideMenu = new SideMenu(cardLayout, contentPanel);

        add(sideMenu, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        cardLayout.show(contentPanel, "DASHBOARD");
    }
}