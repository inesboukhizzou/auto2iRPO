package ui.controller;

import dao.*;
import entities.*;
import ui.views.SearchVehicleView;

import javax.swing.*;
import java.util.List;

/**
 * Controller for vehicle search.
 * Handles search by license plate and history display.
 */
public class SearchController {

    private SearchVehicleView view;
    private VehicleDAO vehicleDAO;
    private RegistrationDAO registrationDAO;
    private InterventionDAO interventionDAO;

    /**
     * Controller constructor.
     * 
     * @param view The search view
     */
    public SearchController(SearchVehicleView view) {
        this.view = view;
        this.vehicleDAO = new VehicleDAO();
        this.registrationDAO = new RegistrationDAO();
        this.interventionDAO = new InterventionDAO();

        initEventHandlers();
    }

    /**
     * Configures event handlers.
     */
    private void initEventHandlers() {
        // Search button
        view.btnSearch.addActionListener(e -> performSearch());

        // Search on Enter in fields
        view.txtSearchPart1.addActionListener(e -> view.txtSearchPart2.requestFocus());
        view.txtSearchPart2.addActionListener(e -> view.txtSearchPart3.requestFocus());
        view.txtSearchPart3.addActionListener(e -> performSearch());
    }

    /**
     * Performs the vehicle search.
     */
    private void performSearch() {
        try {
            // Get search values
            String part1 = view.txtSearchPart1.getText().trim().toUpperCase();
            String part2Str = view.txtSearchPart2.getText().trim();
            String part3 = view.txtSearchPart3.getText().trim().toUpperCase();

            // Validation
            if (part1.isEmpty() || part2Str.isEmpty() || part3.isEmpty()) {
                JOptionPane.showMessageDialog(view,
                        "Please enter the complete license plate.",
                        "Required Fields",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int part2;
            try {
                part2 = Integer.parseInt(part2Str);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(view,
                        "The central part of the plate must be a number.",
                        "Invalid Format",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Search for registration
            Registration registration = registrationDAO.findByParts(part1, part2, part3);

            if (registration == null) {
                view.displayVehicleData(null, null);
                return;
            }

            // Search for vehicle
            Vehicle vehicle = vehicleDAO.findByRegistration(registration);

            if (vehicle == null) {
                view.displayVehicleData(null, null);
                return;
            }

            // Get intervention history
            List<Intervention> history = interventionDAO.findByVehicle(vehicle);

            // Display results
            view.displayVehicleData(vehicle, history);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Error during search: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Clears the search and results.
     */
    public void clearSearch() {
        view.clearSearch();
    }
}
