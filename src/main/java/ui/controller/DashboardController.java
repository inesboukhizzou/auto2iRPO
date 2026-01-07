package ui.controller;

import services.InterventionService;
import services.InterventionService.PlannedIntervention;
import ui.views.DashboardView;

import javax.swing.*;
import java.util.List;

/**
 * Dashboard controller.
 * Handles loading and displaying urgent maintenance interventions.
 */
public class DashboardController {

    private DashboardView view;
    private InterventionService interventionService;

    /**
     * Controller constructor.
     * 
     * @param view                The dashboard view
     * @param interventionService The intervention service
     */
    public DashboardController(DashboardView view, InterventionService interventionService) {
        this.view = view;
        this.interventionService = interventionService;
    }

    /**
     * Loads the initial dashboard data.
     * Displays the 10 most urgent interventions.
     */
    public void loadData() {
        loadData(10);
    }

    /**
     * Loads dashboard data with a custom limit.
     * 
     * @param limit Maximum number of interventions to display
     */
    public void loadData(int limit) {
        try {
            List<PlannedIntervention> urgentInterventions = interventionService.getTopUrgentInterventions(limit);
            view.updateData(urgentInterventions);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Error loading data: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Refreshes the dashboard data.
     */
    public void refresh() {
        loadData();
    }
}
