package ui.panels;

import services.InterventionService;
import services.VehicleService;
import ui.components.StatCard;
import ui.components.StyledTable;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

/**
 * Creative Dashboard that provides an overview of garage operations.
 * Displays key statistics and a list of urgent maintenance tasks.
 */
public class DashboardPanel extends JPanel {

    private final InterventionService interventionService = new InterventionService();
    private final VehicleService vehicleService = new VehicleService();

    public DashboardPanel() {
        setLayout(new BorderLayout(25, 25));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // 1. Header Section
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));

        JLabel subtitle = new JLabel("Overview of garage operations and upcoming tasks");
        subtitle.setForeground(new Color(100, 116, 139));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);
        header.add(titlePanel, BorderLayout.WEST);

        // 2. Stat Cards Grid (Top)
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setOpaque(false);

        // Mock data for display - in a full implementation, these would call count methods
        statsPanel.add(new StatCard("Total Vehicles", "12", new Color(79, 70, 229)));
        statsPanel.add(new StatCard("Urgent Maintenance", "3", new Color(220, 38, 38)));
        statsPanel.add(new StatCard("Scheduled Tasks", "5", new Color(5, 150, 105)));

        // 3. Urgent Operations Section (Center)
        JPanel tableContainer = new JPanel(new BorderLayout(0, 15));
        tableContainer.setOpaque(false);

        JLabel tableTitle = new JLabel("Upcoming Maintenance Operations");
        tableTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        tableContainer.add(tableTitle, BorderLayout.NORTH);

        // Fetch Data from InterventionService
        List<InterventionService.MaintenanceDue> urgentTasks = interventionService.displayNextMaintenances(new Date());

        String[] columns = {"URGENCY", "VEHICLE", "TYPE", "MILEAGE LEFT", "PRICE ESTIMATE"};
        Object[][] data = new Object[urgentTasks.size()][5];

        for (int i = 0; i < urgentTasks.size(); i++) {
            InterventionService.MaintenanceDue task = urgentTasks.get(i);
            data[i][0] = task.getDaysRemaining() < 0 ? "⚠️ Overdue" : task.getDaysRemaining() + " Days";
            data[i][1] = task.getVehicle().getRegistration().getPart1() + "-" +
                    task.getVehicle().getRegistration().getPart2() + "-" +
                    task.getVehicle().getRegistration().getPart3();
            data[i][2] = task.getInterventionType().getName();
            data[i][3] = task.getKmRemaining() + " km";
            data[i][4] = "€ ---"; // Pricing would be fetched from PricingDAO
        }

        StyledTable urgentTable = new StyledTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(urgentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        // Assemble Dashboard
        JPanel mainContent = new JPanel(new BorderLayout(0, 30));
        mainContent.setOpaque(false);
        mainContent.add(statsPanel, BorderLayout.NORTH);
        mainContent.add(tableContainer, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(mainContent, BorderLayout.CENTER);
    }
}