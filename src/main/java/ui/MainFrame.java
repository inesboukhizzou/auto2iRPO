package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import ui.views.*;

/**
 * Main application frame for Auto2i.
 * Uses a modern design with a sidebar navigation and CardLayout
 * to switch between different views.
 */
public class MainFrame extends JFrame {

    
    private static final Color SIDEBAR_COLOR = new Color(30, 41, 59); 
    private static final Color SIDEBAR_HOVER = new Color(51, 65, 85); 
    private static final Color SIDEBAR_ACTIVE = new Color(59, 130, 246); 
    private static final Color BACKGROUND_COLOR = new Color(241, 245, 249); 
    private static final Color TEXT_COLOR = new Color(226, 232, 240); 

    
    private CardLayout cardLayout;
    private JPanel mainContent;

    
    private DashboardView dashboardView;
    private VehicleFormView vehicleFormView;
    private SearchVehicleView searchVehicleView;
    private InterventionFormView interventionFormView;

    
    private JButton btnDashboard;
    private JButton btnVehicles;
    private JButton btnSearch;
    private JButton btnInterventions;
    private JButton activeButton;

    public MainFrame() {
        setTitle("Auto2i - Garage Management");
        setSize(1400, 900);
        setMinimumSize(new Dimension(1200, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        
        setApplicationIcon();

        
        initViews();

        
        add(createSidebar(), BorderLayout.WEST);

        
        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);
        mainContent.setBackground(BACKGROUND_COLOR);

        
        mainContent.add(dashboardView, "DASHBOARD");
        mainContent.add(vehicleFormView, "VEHICLES");
        mainContent.add(searchVehicleView, "SEARCH");
        mainContent.add(interventionFormView, "INTERVENTIONS");

        add(mainContent, BorderLayout.CENTER);

        
        setActiveButton(btnDashboard);
        cardLayout.show(mainContent, "DASHBOARD");
    }

    /**
     * Sets the application icon from resources.
     */
    private void setApplicationIcon() {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/logo.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.println("Could not load application icon: " + e.getMessage());
        }
    }

    /**
     * Initializes all application views.
     */
    private void initViews() {
        dashboardView = new DashboardView();
        vehicleFormView = new VehicleFormView();
        searchVehicleView = new SearchVehicleView();
        interventionFormView = new InterventionFormView();
    }

    /**
     * Creates the navigation sidebar.
     */
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(280, 0));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(0, 0, 0, 0));

        
        JPanel logoPanel = createLogoPanel();
        sidebar.add(logoPanel);

        
        sidebar.add(Box.createVerticalStrut(30));

        
        JPanel menuLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        menuLabelPanel.setBackground(SIDEBAR_COLOR);
        menuLabelPanel.setMaximumSize(new Dimension(280, 30));

        JLabel menuLabel = new JLabel("MAIN MENU");
        menuLabel.setForeground(new Color(148, 163, 184));
        menuLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        menuLabelPanel.add(menuLabel);

        sidebar.add(menuLabelPanel);
        sidebar.add(Box.createVerticalStrut(10));

        
        btnDashboard = createNavButton("📊  Dashboard", "DASHBOARD");
        btnVehicles = createNavButton("🚗  Vehicles", "VEHICLES");
        btnSearch = createNavButton("🔍  Search", "SEARCH");
        btnInterventions = createNavButton("🛞 Interventions", "INTERVENTIONS");

        sidebar.add(btnDashboard);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnVehicles);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnSearch);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnInterventions);

        
        sidebar.add(Box.createVerticalGlue());

        
        JPanel footer = createFooter();
        sidebar.add(footer);

        return sidebar;
    }

    /**
     * Creates the logo panel with the application logo.
     */
    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBackground(SIDEBAR_COLOR);
        logoPanel.setBorder(new EmptyBorder(30, 25, 20, 25));
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoPanel.setMaximumSize(new Dimension(280, 120));

        
        JLabel logoImage = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/logoV2.png"));
            
            Image scaledImage = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            logoImage.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            
            logoImage.setText("🚗");
            logoImage.setFont(new Font("SansSerif", Font.PLAIN, 36));
            logoImage.setForeground(Color.WHITE);
        }
        logoImage.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel logoText = new JLabel("Auto2i");
        logoText.setForeground(Color.WHITE);
        logoText.setFont(new Font("SansSerif", Font.BOLD, 28));
        logoText.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Garage Management");
        subtitle.setForeground(new Color(148, 163, 184));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        logoPanel.add(logoImage);
        logoPanel.add(Box.createVerticalStrut(10));
        logoPanel.add(logoText);
        logoPanel.add(subtitle);

        return logoPanel;
    }

    /**
     * Creates a styled navigation button.
     */
    private JButton createNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(260, 50));
        btn.setPreferredSize(new Dimension(260, 50));
        btn.setBackground(SIDEBAR_COLOR);
        btn.setForeground(TEXT_COLOR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 15));
        btn.setBorder(new EmptyBorder(0, 25, 0, 0));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn != activeButton) {
                    btn.setBackground(SIDEBAR_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn != activeButton) {
                    btn.setBackground(SIDEBAR_COLOR);
                }
            }
        });

        
        btn.addActionListener(e -> {
            setActiveButton(btn);
            cardLayout.show(mainContent, cardName);
        });

        return btn;
    }

    /**
     * Sets the active button and updates styles.
     */
    private void setActiveButton(JButton btn) {
        
        if (activeButton != null) {
            activeButton.setBackground(SIDEBAR_COLOR);
            activeButton.setForeground(TEXT_COLOR);
        }

        
        activeButton = btn;
        activeButton.setBackground(SIDEBAR_ACTIVE);
        activeButton.setForeground(Color.WHITE);
    }

    /**
     * Creates the sidebar footer.
     */
    private JPanel createFooter() {
        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBackground(SIDEBAR_COLOR);
        footer.setBorder(new EmptyBorder(20, 25, 30, 25));
        footer.setMaximumSize(new Dimension(280, 80));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(71, 85, 105));
        sep.setMaximumSize(new Dimension(230, 1));

        JLabel version = new JLabel("Version 1.0.0");
        version.setForeground(new Color(100, 116, 139));
        version.setFont(new Font("SansSerif", Font.PLAIN, 11));
        version.setAlignmentX(Component.LEFT_ALIGNMENT);

        footer.add(sep);
        footer.add(Box.createVerticalStrut(15));
        footer.add(version);

        return footer;
    }

    

    public DashboardView getDashboardView() {
        return dashboardView;
    }

    public VehicleFormView getVehicleFormView() {
        return vehicleFormView;
    }

    public SearchVehicleView getSearchVehicleView() {
        return searchVehicleView;
    }

    public InterventionFormView getInterventionFormView() {
        return interventionFormView;
    }

    /**
     * Navigates to a specific view.
     */
    public void navigateTo(String viewName) {
        switch (viewName) {
            case "DASHBOARD":
                setActiveButton(btnDashboard);
                break;
            case "VEHICLES":
                setActiveButton(btnVehicles);
                break;
            case "SEARCH":
                setActiveButton(btnSearch);
                break;
            case "INTERVENTIONS":
                setActiveButton(btnInterventions);
                break;
        }
        cardLayout.show(mainContent, viewName);
    }
}
