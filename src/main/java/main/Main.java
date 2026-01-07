package main;

import services.*;
import ui.MainFrame;
import ui.controller.*;
import utils.JPAUtil;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Entry point for the Auto2i application.
 * Initializes services, views, and controllers.
 */
public class Main {

    public static void main(String[] args) {
        // Configure native Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default Look and Feel if failed
            System.err.println("Unable to load native Look and Feel: " + e.getMessage());
        }

        // Initialize services
        InterventionService interventionService = new InterventionService();
        PriceService priceService = new PriceService();

        // Launch GUI on EDT
        SwingUtilities.invokeLater(() -> {
            try {
                // Create main frame
                MainFrame frame = new MainFrame();

                // ===== INITIALIZE CONTROLLERS =====

                // Dashboard Controller
                DashboardController dashboardController = new DashboardController(
                        frame.getDashboardView(),
                        interventionService);
                dashboardController.loadData();

                // Vehicle Controller
                VehicleController vehicleController = new VehicleController(
                        frame.getVehicleFormView());

                // Search Controller
                SearchController searchController = new SearchController(
                        frame.getSearchVehicleView());

                // Intervention Controller
                InterventionController interventionController = new InterventionController(
                        frame.getInterventionFormView(),
                        priceService);

                // ===== WINDOW CLOSE HANDLING =====
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        // Confirmation dialog
                        int result = JOptionPane.showConfirmDialog(
                                frame,
                                "Are you sure you want to exit the application?",
                                "Confirmation",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE);

                        if (result == JOptionPane.YES_OPTION) {
                            // Clean JPA connection close
                            JPAUtil.close();
                            System.exit(0);
                        }
                    }
                });

                // Display the frame
                frame.setVisible(true);

                System.out.println("=== Auto2i Application started successfully ===");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Error launching application:\n" + e.getMessage(),
                        "Critical Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();

                // Clean close on error
                JPAUtil.close();
                System.exit(1);
            }
        });
    }
}