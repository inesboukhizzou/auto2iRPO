package ui.views;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import entities.*;
import services.InterventionService.PlannedIntervention;

/**
 * VehicleDiagramView - Interactive 2D vehicle diagram for visualizing
 * interventions.
 * Supports MAINTENANCE and REPAIR modes with distinct color themes.
 * Users can click on vehicle parts to see available actions and schedule
 * interventions.
 */
public class VehicleDiagramView extends JPanel {

    
    
    
    private static final Color BACKGROUND = new Color(241, 245, 249); 
    private static final Color CARD_BACKGROUND = Color.WHITE; 
    private static final Color PRIMARY_COLOR = new Color(59, 130, 246); 
    private static final Color DANGER_COLOR = new Color(220, 38, 38); 
    private static final Color WARNING_COLOR = new Color(245, 158, 11); 
    private static final Color SUCCESS_COLOR = new Color(22, 163, 74); 
    private static final Color TEXT_PRIMARY = new Color(30, 41, 59); 
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139); 
    private static final Color BORDER_COLOR = new Color(226, 232, 240); 

    
    
    
    private static final Color CAR_BODY_COLOR = new Color(225, 230, 235); 
    private static final Color CAR_OUTLINE_COLOR = new Color(180, 190, 200); 
    private static final Color LABEL_BG = new Color(30, 41, 59, 220); 

    
    
    
    public enum InterventionMode {
        MAINTENANCE, 
        REPAIR 
    }

    
    private InterventionMode currentMode = InterventionMode.MAINTENANCE;

    
    
    
    private Map<String, VehiclePart> vehicleParts; 
    private VehiclePart selectedPart; 
    private VehiclePart hoverPart; 
    private Vehicle currentVehicle; 

    
    private List<PlannedIntervention> plannedInterventions;

    
    
    
    private DiagramPanel diagramPanel; 
    private JTextArea detailsArea; 
    private JPanel actionPanel; 
    private JButton btnMaintenance; 
    private JButton btnRepair; 
    private JPanel modeBanner; 
    private JLabel modeBannerLabel; 

    /**
     * Constructor - Sets up the complete vehicle diagram view.
     */
    public VehicleDiagramView() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BACKGROUND);

        
        initVehicleParts();

        
        add(createHeader(), BorderLayout.NORTH);

        
        JPanel contentWrapper = new JPanel(new BorderLayout(20, 0));
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(20, 20, 20, 20));

        contentWrapper.add(createMainContent(), BorderLayout.CENTER);
        contentWrapper.add(createDetailsPanel(), BorderLayout.EAST);

        add(contentWrapper, BorderLayout.CENTER);
        add(createLegendPanel(), BorderLayout.SOUTH);

        
        updateModeVisuals();
    }

    
    
    

    /**
     * Creates the header panel with title, mode toggle buttons, and mode banner.
     * 
     * @return The complete header panel
     */
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(15, 20, 5, 20));

        
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("🚗 Vehicle Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Interactive 2D View - Click on parts to see details");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_SECONDARY);

        titlePanel.add(title);
        titlePanel.add(subtitle);

        
        JPanel centerContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerContainer.setOpaque(false);

        JPanel toggleGroup = new JPanel(new GridLayout(1, 2, 0, 0));
        toggleGroup.setPreferredSize(new Dimension(350, 45));
        toggleGroup.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        
        btnMaintenance = createToggleButton("MAINTENANCE");
        btnMaintenance.addActionListener(e -> setMode(InterventionMode.MAINTENANCE));

        btnRepair = createToggleButton("REPAIR");
        btnRepair.addActionListener(e -> setMode(InterventionMode.REPAIR));

        toggleGroup.add(btnMaintenance);
        toggleGroup.add(btnRepair);
        centerContainer.add(toggleGroup);

        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(centerContainer, BorderLayout.CENTER);
        headerPanel.add(Box.createHorizontalStrut(200), BorderLayout.EAST); 

        
        JPanel mainHeaderContainer = new JPanel(new BorderLayout());
        mainHeaderContainer.setOpaque(false);
        mainHeaderContainer.add(headerPanel, BorderLayout.NORTH);

        
        modeBanner = new JPanel(new FlowLayout(FlowLayout.CENTER));
        modeBanner.setBorder(new MatteBorder(1, 0, 1, 0, new Color(0, 0, 0, 20)));

        modeBannerLabel = new JLabel("ACTIVE MODE: MAINTENANCE");
        modeBannerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        modeBannerLabel.setForeground(Color.WHITE);
        modeBanner.add(modeBannerLabel);

        mainHeaderContainer.add(modeBanner, BorderLayout.SOUTH);

        return mainHeaderContainer;
    }

    /**
     * Creates a styled toggle button for mode selection.
     * 
     * @param text Button label text
     * @return Styled JButton
     */
    private JButton createToggleButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Switches the current mode and refreshes the UI.
     * 
     * @param mode The new mode to switch to
     */
    private void setMode(InterventionMode mode) {
        if (this.currentMode == mode)
            return; 

        this.currentMode = mode;
        updateModeVisuals();

        
        if (selectedPart != null) {
            updateDetailsPanel(selectedPart);
        }

        diagramPanel.repaint();
    }

    /**
     * Updates button styles and banner based on current mode.
     * Green theme for MAINTENANCE, Red theme for REPAIR.
     */
    private void updateModeVisuals() {
        boolean isMaintenance = (currentMode == InterventionMode.MAINTENANCE);

        if (isMaintenance) {
            
            styleActiveButton(btnMaintenance, SUCCESS_COLOR);
            styleInactiveButton(btnRepair);
            modeBanner.setBackground(SUCCESS_COLOR);
            modeBannerLabel.setText("ACTIVE MODE: MAINTENANCE (Routine checks, Wear & Tear...)");
        } else {
            
            styleInactiveButton(btnMaintenance);
            styleActiveButton(btnRepair, DANGER_COLOR);
            modeBanner.setBackground(DANGER_COLOR);
            modeBannerLabel.setText("ACTIVE MODE: REPAIR (Breakdowns, Replacements...)");
        }
    }

    /**
     * Styles a button as active (filled with color).
     */
    private void styleActiveButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
    }

    /**
     * Styles a button as inactive (white background).
     */
    private void styleInactiveButton(JButton btn) {
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.GRAY);
    }

    
    
    

    /**
     * Initializes all vehicle parts with their positions and intervention keywords.
     * Each part has separate lists for MAINTENANCE and REPAIR actions.
     */
    private void initVehicleParts() {
        vehicleParts = new LinkedHashMap<>();

        
        String[] wheelMaint = { "Tire Pressure Check", "Tire Rotation" };
        String[] wheelRepair = { "Tire Replacement", "Wheel Alignment" };

        
        vehicleParts.put("WHEEL_FL",
                new VehiclePart("Front L. Wheel", 0.02, 0.12, 0.14, 0.18, wheelMaint, wheelRepair));
        vehicleParts.put("WHEEL_FR",
                new VehiclePart("Front R. Wheel", 0.84, 0.12, 0.14, 0.18, wheelMaint, wheelRepair));
        vehicleParts.put("WHEEL_RL", new VehiclePart("Rear L. Wheel", 0.02, 0.70, 0.14, 0.18, wheelMaint, wheelRepair));
        vehicleParts.put("WHEEL_RR", new VehiclePart("Rear R. Wheel", 0.84, 0.70, 0.14, 0.18, wheelMaint, wheelRepair));

        
        vehicleParts.put("BRAKES", new VehiclePart("Braking System", 0.30, 0.28, 0.40, 0.10,
                new String[] { "Brake Fluid Check", "Brake Pads Check" },
                new String[] { "Replace Brake Discs", "Replace Brake Pads" }));

        
        vehicleParts.put("ENGINE", new VehiclePart("Engine / Front", 0.25, 0.02, 0.50, 0.25,
                new String[] { "Oil Level Check", "Coolant Check", "Filter Change", "Front Lights Check" },
                new String[] { "Engine Repair", "Front Bumper Replacement" }));

        
        vehicleParts.put("BATTERY", new VehiclePart("Power Supply", 0.25, 0.40, 0.50, 0.30,
                new String[] { "Battery Check", "Electrical System Check" },
                new String[] { "Battery Replacement", "Alternator Repair" }));

        
        vehicleParts.put("EXHAUST", new VehiclePart("Rear / Exhaust", 0.30, 0.85, 0.40, 0.12,
                new String[] { "Rear Lights Check", "Exhaust Inspection" },
                new String[] { "Rear Bumper Replacement", "Exhaust System Repair" }));
    }

    
    
    

    /**
     * Creates the main content panel containing the diagram.
     * 
     * @return Panel with the diagram
     */
    private JPanel createMainContent() {
        diagramPanel = new DiagramPanel();
        diagramPanel.setPreferredSize(new Dimension(700, 500));
        diagramPanel.setBackground(CARD_BACKGROUND);
        diagramPanel.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        return diagramPanel;
    }

    /**
     * Creates the side panel for displaying part details and action buttons.
     * 
     * @return The details panel
     */
    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_BACKGROUND);
        panel.setPreferredSize(new Dimension(320, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 20, 20, 20)));

        
        JLabel title = new JLabel("📋 Part Details");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT_PRIMARY);

        
        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailsArea.setForeground(TEXT_PRIMARY);
        detailsArea.setBackground(CARD_BACKGROUND);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setText("Select an area on the 2D view to see available actions.");

        JScrollPane scrollPane = new JScrollPane(detailsArea);
        scrollPane.setBorder(new EmptyBorder(10, 0, 10, 0));
        scrollPane.getViewport().setBackground(CARD_BACKGROUND);

        
        actionPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        actionPanel.setBackground(CARD_BACKGROUND);

        
        JPanel actionWrapper = new JPanel(new BorderLayout());
        actionWrapper.setBackground(CARD_BACKGROUND);
        actionWrapper.add(actionPanel, BorderLayout.NORTH);

        JScrollPane actionScroll = new JScrollPane(actionWrapper);
        actionScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                "Available Actions",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                TEXT_PRIMARY));
        actionScroll.setPreferredSize(new Dimension(280, 200));

        
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(CARD_BACKGROUND);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(actionScroll, BorderLayout.SOUTH);

        panel.add(title, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the legend panel at the bottom of the view.
     * 
     * @return The legend panel
     */
    private JPanel createLegendPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        p.setBackground(CARD_BACKGROUND);
        p.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(5, 20, 5, 20)));

        p.add(createBadge(SUCCESS_COLOR, "Maintenance Mode Area"));
        p.add(createBadge(DANGER_COLOR, "Repair Mode Area"));
        p.add(createBadge(PRIMARY_COLOR, "Selected Part"));

        return p;
    }

    /**
     * Creates a color badge for the legend.
     * 
     * @param c   Badge color
     * @param txt Badge label text
     * @return Badge panel
     */
    private JPanel createBadge(Color c, String txt) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.setOpaque(false);

        JPanel colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(18, 18));
        colorBox.setBackground(c);
        colorBox.setBorder(new LineBorder(c.darker()));

        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(TEXT_PRIMARY);

        p.add(colorBox);
        p.add(l);

        return p;
    }

    
    
    

    /**
     * Updates the view with vehicle data and interventions.
     * 
     * @param vehicle The vehicle to display
     * @param planned List of planned interventions
     * @param past    List of past interventions (for API compatibility)
     */
    public void setVehicleData(Vehicle vehicle, List<PlannedIntervention> planned, List<Intervention> past) {
        this.currentVehicle = vehicle;
        this.plannedInterventions = planned != null ? planned : new ArrayList<>();

        
        diagramPanel.repaint();

        if (selectedPart != null) {
            updateDetailsPanel(selectedPart);
        }
    }

    /**
     * Updates the details panel with information about the selected part.
     * Shows different actions based on current mode.
     * 
     * @param part The selected vehicle part
     */
    private void updateDetailsPanel(VehiclePart part) {
        
        String[] actions = part.getKeywords(currentMode);

        
        StringBuilder sb = new StringBuilder();
        sb.append("📍 ").append(part.name.toUpperCase()).append("\n\n");
        sb.append("Mode: ").append(currentMode == InterventionMode.MAINTENANCE ? "Maintenance" : "Repair").append("\n");
        sb.append("Available actions: ").append(actions.length).append("\n");

        detailsArea.setText(sb.toString());

        
        actionPanel.removeAll();

        if (actions.length == 0) {
            JLabel noActions = new JLabel("No actions available in this mode.");
            noActions.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            noActions.setForeground(TEXT_SECONDARY);
            actionPanel.add(noActions);
        } else {
            
            Color themeColor = (currentMode == InterventionMode.MAINTENANCE) ? SUCCESS_COLOR : DANGER_COLOR;

            
            for (String action : actions) {
                JButton btn = createActionButton(action, themeColor, part);
                actionPanel.add(btn);
            }
        }

        actionPanel.revalidate();
        actionPanel.repaint();
    }

    /**
     * Creates a styled action button for an intervention.
     * 
     * @param text  Button text
     * @param color Theme color for the button
     * @param part  The vehicle part this action relates to
     * @return Styled JButton
     */
    private JButton createActionButton(String text, Color color, VehiclePart part) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.WHITE);
        btn.setForeground(color);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(color, 2),
                new EmptyBorder(10, 15, 10, 15)));

        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.WHITE);
            }
        });

        
        btn.addActionListener(e -> {
            String message = String.format(
                    "Action Selected:\n\n• Part: %s\n• Action: %s\n• Mode: %s",
                    part.name, text, currentMode.name());
            JOptionPane.showMessageDialog(this, message, "Action Selection", JOptionPane.INFORMATION_MESSAGE);
        });

        return btn;
    }

    
    
    

    /**
     * Custom panel that handles the 2D vehicle drawing and mouse interactions.
     */
    class DiagramPanel extends JPanel {

        private Font labelFont = new Font("Segoe UI", Font.BOLD, 11);

        public DiagramPanel() {
            setOpaque(true);

            
            MouseAdapter mouseHandler = new MouseAdapter() {

                
                @Override
                public void mouseMoved(MouseEvent e) {
                    VehiclePart found = findPartAtPoint(e.getX(), e.getY());
                    if (found != hoverPart) {
                        hoverPart = found;
                        repaint();
                        setCursor(found != null
                                ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                                : Cursor.getDefaultCursor());
                    }
                }

                
                @Override
                public void mouseClicked(MouseEvent e) {
                    VehiclePart found = findPartAtPoint(e.getX(), e.getY());
                    if (found != null) {
                        selectedPart = found;
                        updateDetailsPanel(found);
                        repaint();
                    }
                }
            };

            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseHandler);
        }

        /**
         * Finds which part contains the given point.
         * Only returns parts that have actions in the current mode.
         * 
         * @param x Mouse X coordinate
         * @param y Mouse Y coordinate
         * @return The part at that point, or null if none
         */
        private VehiclePart findPartAtPoint(int x, int y) {
            for (VehiclePart p : vehicleParts.values()) {
                
                if (p.containsPoint(x, y) && p.getKeywords(currentMode).length > 0) {
                    return p;
                }
            }
            return null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            
            int w = getWidth();
            int h = getHeight();
            int marginX = w / 10;
            int marginY = h / 10;
            int drawW = w - 2 * marginX;
            int drawH = h - 2 * marginY;

            
            int carBodyWidth = drawW * 6 / 10;
            int bodyStartX = marginX + (drawW - carBodyWidth) / 2;

            
            drawVehicleBody(g2, bodyStartX, marginY, carBodyWidth, drawH);

            
            drawParts(g2, marginX, marginY, drawW, drawH);

            
            drawLabels(g2);
        }

        /**
         * Draws all interactive vehicle parts.
         */
        private void drawParts(Graphics2D g2, int marginX, int marginY, int drawW, int drawH) {
            for (VehiclePart p : vehicleParts.values()) {
                
                boolean isActive = p.getKeywords(currentMode).length > 0;

                
                int px = marginX + (int) (p.x * drawW);
                int py = marginY + (int) (p.y * drawH);
                int pw = (int) (p.w * drawW);
                int ph = (int) (p.h * drawH);

                
                p.rect = new Rectangle(px, py, pw, ph);

                
                if (isActive) {
                    
                    Color baseColor = (currentMode == InterventionMode.MAINTENANCE)
                            ? SUCCESS_COLOR
                            : DANGER_COLOR;

                    
                    if (p == selectedPart) {
                        g2.setColor(baseColor);
                    } else if (p == hoverPart) {
                        g2.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 150));
                    } else {
                        g2.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 50));
                    }

                    
                    g2.fillRoundRect(px, py, pw, ph, 15, 15);

                    
                    if (p == selectedPart) {
                        g2.setColor(baseColor.darker());
                        g2.setStroke(new BasicStroke(3));
                    } else {
                        g2.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 200));
                        g2.setStroke(new BasicStroke(1));
                    }
                    g2.drawRoundRect(px, py, pw, ph, 15, 15);
                }
            }
        }

        /**
         * Draws labels for all active parts.
         */
        private void drawLabels(Graphics2D g2) {
            g2.setFont(labelFont);
            FontMetrics fm = g2.getFontMetrics();

            for (VehiclePart p : vehicleParts.values()) {
                boolean isActive = p.getKeywords(currentMode).length > 0;

                if (isActive && p.rect != null) {
                    String labelText = p.name;
                    int textWidth = fm.stringWidth(labelText);
                    int labelPadding = 6;
                    int labelBoxWidth = textWidth + labelPadding * 2;
                    int labelBoxHeight = fm.getHeight() + labelPadding;

                    
                    int labelX = p.rect.x + (p.rect.width - labelBoxWidth) / 2;
                    int labelY = p.rect.y + (p.rect.height - labelBoxHeight) / 2;

                    
                    g2.setColor(LABEL_BG);
                    g2.fillRoundRect(labelX, labelY, labelBoxWidth, labelBoxHeight, 10, 10);

                    
                    g2.setColor(Color.WHITE);
                    g2.drawString(labelText, labelX + labelPadding, labelY + labelPadding + fm.getAscent() - 2);
                }
            }
        }

        /**
         * Draws the schematic vehicle body with wheel wells and windows.
         */
        private void drawVehicleBody(Graphics2D g2, int x, int y, int w, int h) {
            
            g2.setColor(CAR_BODY_COLOR);
            g2.fillRoundRect(x, y, w, h, 60, 60);

            
            int wheelWellW = w / 6;
            int wheelWellH = h / 5;
            int wheelWellStickOut = w / 8;

            
            g2.fillRoundRect(x - wheelWellStickOut, y + h / 10, wheelWellW, wheelWellH, 20, 20);
            
            g2.fillRoundRect(x + w - wheelWellW + wheelWellStickOut, y + h / 10, wheelWellW, wheelWellH, 20, 20);
            
            g2.fillRoundRect(x - wheelWellStickOut, y + h - h / 10 - wheelWellH, wheelWellW, wheelWellH, 20, 20);
            
            g2.fillRoundRect(x + w - wheelWellW + wheelWellStickOut, y + h - h / 10 - wheelWellH, wheelWellW,
                    wheelWellH, 20, 20);

            
            g2.setColor(CAR_OUTLINE_COLOR);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x, y, w, h, 60, 60);

            
            g2.setColor(new Color(190, 215, 235));
            int glassMargin = w / 10;

            
            g2.fillRoundRect(x + glassMargin, y + h / 5, w - 2 * glassMargin, h / 6, 15, 15);
            
            g2.fillRoundRect(x + glassMargin, y + h - h / 4, w - 2 * glassMargin, h / 8, 15, 15);

            
            g2.setColor(new Color(210, 215, 220, 150));
            g2.fillRoundRect(x + glassMargin / 2, y + h / 5 + h / 7, w - glassMargin, h / 3, 10, 10);
        }
    }

    
    
    

    /**
     * Represents a clickable vehicle part with position and intervention keywords.
     */
    class VehiclePart {
        String name; 
        double x, y, w, h; 
        String[] maintenanceKeywords; 
        String[] repairKeywords; 
        Rectangle rect; 

        /**
         * Constructor for a vehicle part.
         * 
         * @param name        Display name
         * @param x           X position as percentage
         * @param y           Y position as percentage
         * @param w           Width as percentage
         * @param h           Height as percentage
         * @param maintenance Actions for maintenance mode
         * @param repair      Actions for repair mode
         */
        public VehiclePart(String name, double x, double y, double w, double h,
                String[] maintenance, String[] repair) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.maintenanceKeywords = maintenance;
            this.repairKeywords = repair;
        }

        /**
         * Checks if a point is inside this part's bounds.
         * 
         * @param px X coordinate to check
         * @param py Y coordinate to check
         * @return true if point is inside, false otherwise
         */
        boolean containsPoint(int px, int py) {
            return rect != null && rect.contains(px, py);
        }

        /**
         * Gets the appropriate keywords based on the current mode.
         * 
         * @param mode Current intervention mode
         * @return Array of keywords/actions for that mode
         */
        String[] getKeywords(InterventionMode mode) {
            return (mode == InterventionMode.MAINTENANCE) ? maintenanceKeywords : repairKeywords;
        }
    }
}
