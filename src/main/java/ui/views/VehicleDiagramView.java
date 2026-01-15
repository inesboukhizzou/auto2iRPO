package ui.views;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import entities.*;
import services.InterventionService.PlannedIntervention;

/**
 * Vehicle diagram view showing a 2D top-down view of a vehicle.
 * Helps users visualize interventions and urgent maintenance areas.
 */
public class VehicleDiagramView extends JPanel {

    // Theme colors
    private static final Color BACKGROUND = new Color(241, 245, 249);
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color DANGER_COLOR = new Color(239, 68, 68);
    private static final Color WARNING_COLOR = new Color(245, 158, 11);
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private static final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color BORDER_COLOR = new Color(226, 232, 240);

    // Vehicle parts with their positions (relative percentages)
    private Map<String, VehiclePart> vehicleParts;
    private VehiclePart selectedPart;
    private VehiclePart hoverPart;

    // Planned interventions data
    private List<PlannedIntervention> plannedInterventions;

    // Components
    private DiagramPanel diagramPanel;
    private JPanel legendPanel;
    private JTextArea detailsArea;

    public VehicleDiagramView() {
        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initVehicleParts();

        // Header
        add(createHeader(), BorderLayout.NORTH);

        // Main content - diagram and details
        add(createMainContent(), BorderLayout.CENTER);

        // Legend
        add(createLegendPanel(), BorderLayout.SOUTH);
    }

    /**
     * Initializes the vehicle parts with their positions and labels.
     */
    private void initVehicleParts() {
        vehicleParts = new LinkedHashMap<>();

        // Define vehicle parts with positions (x%, y%, width%, height%)
        vehicleParts.put("ENGINE", new VehiclePart("Engine", 0.35, 0.05, 0.30, 0.20,
                new String[] { "Oil Change", "Coolant", "Filter", "Belt", "Timing" }));

        vehicleParts.put("FRONT_LEFT_WHEEL", new VehiclePart("Front Left Wheel", 0.05, 0.15, 0.15, 0.20,
                new String[] { "Brake Pads", "Tire", "Suspension", "Wheel Bearing" }));

        vehicleParts.put("FRONT_RIGHT_WHEEL", new VehiclePart("Front Right Wheel", 0.80, 0.15, 0.15, 0.20,
                new String[] { "Brake Pads", "Tire", "Suspension", "Wheel Bearing" }));

        vehicleParts.put("TRANSMISSION", new VehiclePart("Transmission", 0.35, 0.30, 0.30, 0.15,
                new String[] { "Gearbox Oil", "Clutch", "Transmission" }));

        vehicleParts.put("FUEL_TANK", new VehiclePart("Fuel Tank", 0.35, 0.50, 0.30, 0.10,
                new String[] { "Fuel Filter", "Fuel Pump" }));

        vehicleParts.put("REAR_LEFT_WHEEL", new VehiclePart("Rear Left Wheel", 0.05, 0.60, 0.15, 0.20,
                new String[] { "Brake Pads", "Tire", "Suspension", "Wheel Bearing" }));

        vehicleParts.put("REAR_RIGHT_WHEEL", new VehiclePart("Rear Right Wheel", 0.80, 0.60, 0.15, 0.20,
                new String[] { "Brake Pads", "Tire", "Suspension", "Wheel Bearing" }));

        vehicleParts.put("EXHAUST", new VehiclePart("Exhaust System", 0.40, 0.85, 0.20, 0.10,
                new String[] { "Exhaust", "Catalyst", "Muffler" }));

        vehicleParts.put("BATTERY", new VehiclePart("Battery", 0.15, 0.08, 0.12, 0.10,
                new String[] { "Battery", "Electrical" }));

        vehicleParts.put("AC_SYSTEM", new VehiclePart("A/C System", 0.70, 0.08, 0.12, 0.10,
                new String[] { "A/C", "Air Conditioning", "Climate" }));

        vehicleParts.put("CABIN", new VehiclePart("Cabin", 0.25, 0.35, 0.50, 0.25,
                new String[] { "Interior", "Cabin Filter", "Air Filter" }));
    }

    /**
     * Creates the header.
     */
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("🚗 Vehicle Diagram");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Click on a part to see intervention details");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_SECONDARY);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.WEST);

        return header;
    }

    /**
     * Creates the main content with diagram and details.
     */
    private JPanel createMainContent() {
        JPanel content = new JPanel(new BorderLayout(20, 0));
        content.setOpaque(false);

        // Diagram panel
        diagramPanel = new DiagramPanel();
        diagramPanel.setPreferredSize(new Dimension(600, 400));
        diagramPanel.setBackground(CARD_BACKGROUND);
        diagramPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 20, 20, 20)));

        content.add(diagramPanel, BorderLayout.CENTER);

        // Details panel
        JPanel detailsPanel = createDetailsPanel();
        content.add(detailsPanel, BorderLayout.EAST);

        return content;
    }

    /**
     * Creates the details panel.
     */
    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_BACKGROUND);
        panel.setPreferredSize(new Dimension(300, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 20, 20, 20)));

        JLabel title = new JLabel("📋 Part Details");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(TEXT_PRIMARY);

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        detailsArea.setForeground(TEXT_PRIMARY);
        detailsArea.setBackground(CARD_BACKGROUND);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setText(
                "Select a vehicle part to see details.\n\nColors indicate:\n• Green: OK\n• Yellow: Needs attention\n• Red: Urgent maintenance");

        JScrollPane scrollPane = new JScrollPane(detailsArea);
        scrollPane.setBorder(new EmptyBorder(10, 0, 0, 0));
        scrollPane.getViewport().setBackground(CARD_BACKGROUND);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the legend panel.
     */
    private JPanel createLegendPanel() {
        legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        legendPanel.setBackground(CARD_BACKGROUND);
        legendPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(10, 20, 10, 20)));

        legendPanel.add(createLegendItem("OK - No maintenance needed", SUCCESS_COLOR));
        legendPanel.add(createLegendItem("Attention - Upcoming maintenance", WARNING_COLOR));
        legendPanel.add(createLegendItem("Urgent - Immediate action required", DANGER_COLOR));
        legendPanel.add(createLegendItem("Normal - Standard status", new Color(200, 200, 200)));

        return legendPanel;
    }

    /**
     * Creates a legend item.
     */
    private JPanel createLegendItem(String text, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        item.setOpaque(false);

        JPanel colorBox = new JPanel();
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(16, 16));
        colorBox.setBorder(new LineBorder(color.darker(), 1));

        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(TEXT_PRIMARY);

        item.add(colorBox);
        item.add(label);

        return item;
    }

    /**
     * Updates the view with vehicle data.
     * 
     * @param vehicle The vehicle to display (used for context, may be null)
     * @param planned List of planned interventions to visualize
     * @param past    List of past interventions (kept for API compatibility, not
     *                used internally)
     */
    public void setVehicleData(Vehicle vehicle, List<PlannedIntervention> planned, List<Intervention> past) {
        this.plannedInterventions = planned != null ? planned : new ArrayList<>();

        // Update part statuses based on interventions
        updatePartStatuses();

        diagramPanel.repaint();
    }

    /**
     * Updates the status of each part based on interventions.
     */
    private void updatePartStatuses() {
        // Reset all parts to normal
        for (VehiclePart part : vehicleParts.values()) {
            part.status = PartStatus.NORMAL;
            part.urgentInterventions.clear();
        }

        // Check planned interventions
        if (plannedInterventions != null) {
            for (PlannedIntervention pi : plannedInterventions) {
                String interventionName = pi.getInterventionName().toLowerCase();

                for (VehiclePart part : vehicleParts.values()) {
                    for (String keyword : part.keywords) {
                        if (interventionName.contains(keyword.toLowerCase())) {
                            // Set status based on priority
                            int priority = pi.getPriority();
                            if (priority >= 5) {
                                part.status = PartStatus.URGENT;
                            } else if (priority >= 3 && part.status != PartStatus.URGENT) {
                                part.status = PartStatus.WARNING;
                            } else if (part.status == PartStatus.NORMAL) {
                                part.status = PartStatus.OK;
                            }
                            part.urgentInterventions.add(pi);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Custom panel for drawing the vehicle diagram.
     */
    class DiagramPanel extends JPanel {

        public DiagramPanel() {
            setOpaque(true);

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    VehiclePart found = null;
                    for (VehiclePart part : vehicleParts.values()) {
                        if (part.containsPoint(e.getX(), e.getY(), getWidth(), getHeight())) {
                            found = part;
                            break;
                        }
                    }
                    if (found != hoverPart) {
                        hoverPart = found;
                        setCursor(
                                hoverPart != null ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR));
                        repaint();
                    }
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (VehiclePart part : vehicleParts.values()) {
                        if (part.containsPoint(e.getX(), e.getY(), getWidth(), getHeight())) {
                            selectedPart = part;
                            updateDetailsPanel(part);
                            repaint();
                            break;
                        }
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int padding = 40;
            int drawW = w - padding * 2;
            int drawH = h - padding * 2;

            // Draw vehicle body outline
            drawVehicleBody(g2d, padding, padding, drawW, drawH);

            // Draw each part
            for (VehiclePart part : vehicleParts.values()) {
                drawPart(g2d, part, padding, padding, drawW, drawH);
            }

            // Draw title
            g2d.setColor(TEXT_PRIMARY);
            g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2d.drawString("Top View", padding, 25);
        }

        /**
         * Draws the vehicle body outline.
         */
        private void drawVehicleBody(Graphics2D g2d, int x, int y, int w, int h) {
            // Main body
            g2d.setColor(new Color(230, 230, 235));
            RoundRectangle2D body = new RoundRectangle2D.Double(
                    x + w * 0.15, y + h * 0.05, w * 0.70, h * 0.90, 40, 40);
            g2d.fill(body);
            g2d.setColor(new Color(180, 180, 190));
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(body);

            // Windshield (front)
            g2d.setColor(new Color(200, 220, 240));
            g2d.fillRoundRect(x + (int) (w * 0.25), y + (int) (h * 0.22), (int) (w * 0.50), (int) (h * 0.08), 10, 10);

            // Rear window
            g2d.fillRoundRect(x + (int) (w * 0.28), y + (int) (h * 0.70), (int) (w * 0.44), (int) (h * 0.05), 10, 10);
        }

        /**
         * Draws a vehicle part.
         */
        private void drawPart(Graphics2D g2d, VehiclePart part, int offsetX, int offsetY, int w, int h) {
            int px = offsetX + (int) (part.x * w);
            int py = offsetY + (int) (part.y * h);
            int pw = (int) (part.width * w);
            int ph = (int) (part.height * h);

            // Store bounds for hit testing
            part.bounds = new Rectangle(px, py, pw, ph);

            // Determine color based on status
            Color fillColor;
            switch (part.status) {
                case URGENT:
                    fillColor = new Color(254, 202, 202);
                    break;
                case WARNING:
                    fillColor = new Color(254, 243, 199);
                    break;
                case OK:
                    fillColor = new Color(209, 250, 229);
                    break;
                default:
                    fillColor = new Color(229, 231, 235);
            }

            // Highlight if hovered or selected
            if (part == hoverPart || part == selectedPart) {
                fillColor = fillColor.brighter();
            }

            // Draw part
            g2d.setColor(fillColor);
            g2d.fillRoundRect(px, py, pw, ph, 10, 10);

            // Draw border
            Color borderColor;
            switch (part.status) {
                case URGENT:
                    borderColor = DANGER_COLOR;
                    break;
                case WARNING:
                    borderColor = WARNING_COLOR;
                    break;
                case OK:
                    borderColor = SUCCESS_COLOR;
                    break;
                default:
                    borderColor = new Color(156, 163, 175);
            }

            g2d.setColor(borderColor);
            g2d.setStroke(new BasicStroke(part == selectedPart ? 3 : 2));
            g2d.drawRoundRect(px, py, pw, ph, 10, 10);

            // Draw label
            g2d.setColor(TEXT_PRIMARY);
            g2d.setFont(new Font("SansSerif", Font.BOLD, 10));
            FontMetrics fm = g2d.getFontMetrics();
            String label = part.name;
            int labelWidth = fm.stringWidth(label);
            int labelX = px + (pw - labelWidth) / 2;
            int labelY = py + (ph + fm.getAscent()) / 2;
            g2d.drawString(label, labelX, labelY);

            // Draw warning indicator if urgent
            if (part.status == PartStatus.URGENT) {
                g2d.setColor(DANGER_COLOR);
                g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
                g2d.drawString("⚠", px + pw - 15, py + 15);
            }
        }
    }

    /**
     * Updates the details panel with part information.
     */
    private void updateDetailsPanel(VehiclePart part) {
        StringBuilder sb = new StringBuilder();
        sb.append("Part: ").append(part.name).append("\n\n");

        sb.append("Status: ");
        switch (part.status) {
            case URGENT:
                sb.append("URGENT - Immediate action required\n");
                break;
            case WARNING:
                sb.append("ATTENTION - Maintenance upcoming\n");
                break;
            case OK:
                sb.append("OK - Recently serviced\n");
                break;
            default:
                sb.append("Normal - No specific maintenance\n");
        }

        sb.append("\nRelated interventions:\n");
        for (String keyword : part.keywords) {
            sb.append("  • ").append(keyword).append("\n");
        }

        if (!part.urgentInterventions.isEmpty()) {
            sb.append("\n⚠ Scheduled interventions:\n");
            for (PlannedIntervention pi : part.urgentInterventions) {
                sb.append("  → ").append(pi.getInterventionName());
                sb.append(" (Priority: ").append(pi.getPriority()).append(")\n");
            }
        }

        detailsArea.setText(sb.toString());
    }

    /**
     * Part status enumeration.
     */
    enum PartStatus {
        NORMAL, OK, WARNING, URGENT
    }

    /**
     * Vehicle part class.
     */
    class VehiclePart {
        String name;
        double x, y, width, height;
        String[] keywords;
        PartStatus status = PartStatus.NORMAL;
        Rectangle bounds;
        List<PlannedIntervention> urgentInterventions = new ArrayList<>();

        VehiclePart(String name, double x, double y, double width, double height, String[] keywords) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.keywords = keywords;
        }

        boolean containsPoint(int px, int py, int panelWidth, int panelHeight) {
            if (bounds == null)
                return false;
            return bounds.contains(px, py);
        }
    }
}
