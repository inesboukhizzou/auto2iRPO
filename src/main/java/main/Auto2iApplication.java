package main;

import services.*;
import ui.MainFrame;
import ui.controller.*;
import utils.JPAUtil;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Auto2i Application launcher.
 * Launches the graphical user interface for the garage management system.
 * 
 * Note: Run DatabaseSetup first to initialize the database with sample data.
 */
public class Auto2iApplication {

    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Unable to load native Look and Feel: " + e.getMessage());
        }

        
        InterventionService interventionService = new InterventionService();
        PriceService priceService = new PriceService();

        
        SwingUtilities.invokeLater(() -> {
            try {
                
                MainFrame frame = new MainFrame();

                

                
                DashboardController dashboardController = new DashboardController(
                        frame.getDashboardView(),
                        interventionService);
                dashboardController.loadData();

                
                VehicleController vehicleController = new VehicleController(
                        frame.getVehicleFormView());

                
                SearchController searchController = new SearchController(
                        frame.getSearchVehicleView());

                
                InterventionController interventionController = new InterventionController(
                        frame.getInterventionFormView(),
                        priceService);

                
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        
                        int result = JOptionPane.showConfirmDialog(
                                frame,
                                "Are you sure you want to exit the application?",
                                "Confirmation",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE);

                        if (result == JOptionPane.YES_OPTION) {
                            
                            JPAUtil.close();
                            System.exit(0);
                        }
                    }
                });

                
                frame.setVisible(true);

                System.out.println("=== Auto2i Application started successfully ===");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Error launching application:\n" + e.getMessage(),
                        "Critical Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();

                
                JPAUtil.close();
                System.exit(1);
            }
        });
    }
}
