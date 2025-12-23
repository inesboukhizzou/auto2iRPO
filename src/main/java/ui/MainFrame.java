package ui;

import javax.swing.*;
import java.awt.*;
import ui.panels.*;
import ui.components.SideMenu;

public class MainFrame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel contentPanel = new JPanel(cardLayout);

    public MainFrame() {
        setTitle("Auto2i - Garage Management System");
        setSize(1280, 832);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Pass layout and panel to VehiclePanel so it can navigate to ADD_VEHICLE
        contentPanel.add(new DashboardPanel(), "DASHBOARD");
        contentPanel.add(new VehiclePanel(cardLayout, contentPanel), "VEHICLES");
        contentPanel.add(new InterventionPanel(), "INTERVENTIONS");
        contentPanel.add(new AddVehiclePanel(), "ADD_VEHICLE");

        SideMenu sideMenu = new SideMenu(cardLayout, contentPanel);
        add(sideMenu, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        cardLayout.show(contentPanel, "DASHBOARD");
    }
}