package ui.views;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import entities.*;

/**
 * Intervention form view.
 * Allows registering a new intervention on an existing vehicle
 * with automatic price calculation and vehicle diagram visualization.
 */
public class InterventionFormView extends JPanel {

    // Theme colors
    private static final Color BACKGROUND = new Color(241, 245, 249);
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color PRIMARY_COLOR = new Color(37, 99, 235);
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private static final Color WARNING_COLOR = new Color(173, 12, 12);
    private static final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color BORDER_COLOR = new Color(226, 232, 240);
    private static final Color INPUT_BACKGROUND = new Color(248, 250, 252);

    // Form components
    public JComboBox<Vehicle> comboVehicle;
    public JComboBox<InterventionType> comboInterventionType;
    public JComboBox<MaintenanceType> comboMaintenanceType;
    public JTextField txtDate;
    public JTextField txtMileage;
    public JTextField txtBasePrice;
    public JTextField txtFinalPrice;

    // Action buttons
    public JButton btnCalculate;
    public JButton btnSave;
    public JButton btnClear;
    public JButton btnShowDiagram;

    // Maintenance type panel (shown conditionally)
    private JPanel maintenanceTypePanel;

    // Vehicle diagram view
    private VehicleDiagramView diagramView;
    private JDialog diagramDialog;

    public InterventionFormView() {
        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        add(createHeader(), BorderLayout.NORTH);

        // Main form
        add(createFormPanel(), BorderLayout.CENTER);

        // Initialize diagram view
        diagramView = new VehicleDiagramView();
    }

    /**
     * Creates the view header.
     */
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("New Intervention");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Register an intervention on a vehicle");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.WEST);

        return header;
    }

    /**
     * Creates the main form panel.
     */
    private JPanel createFormPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 25));
        mainPanel.setOpaque(false);

        // Sections container
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setOpaque(false);

        // Vehicle section (REQUIRED - emphasized)
        formContainer.add(createVehicleSection());
        formContainer.add(Box.createVerticalStrut(20));

        // Intervention type section
        formContainer.add(createInterventionTypeSection());
        formContainer.add(Box.createVerticalStrut(20));

        // Intervention details section
        formContainer.add(createDetailsSection());
        formContainer.add(Box.createVerticalStrut(20));

        // Price section
        formContainer.add(createPriceSection());

        // Wrapper
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(formContainer, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BACKGROUND);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Action buttons
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        return mainPanel;
    }

    /**
     * Creates the vehicle selection section (REQUIRED).
     */
    private JPanel createVehicleSection() {
        JPanel section = new JPanel(new BorderLayout(0, 15));
        section.setBackground(CARD_BACKGROUND);
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(WARNING_COLOR, 2, true), // Orange border for emphasis
                        new LineBorder(BORDER_COLOR, 1, true)),
                new EmptyBorder(25, 30, 25, 30)));
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        // Title with required indicator
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("🚗 Vehicle Selection");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel requiredLabel = new JLabel("  (REQUIRED)");
        requiredLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        requiredLabel.setForeground(WARNING_COLOR);

        titlePanel.add(titleLabel);
        titlePanel.add(requiredLabel);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel vehicleLabel = createLabel("Select the vehicle:");
        vehicleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        grid.add(vehicleLabel, gbc);

        comboVehicle = new JComboBox<>();
        comboVehicle.setPreferredSize(new Dimension(400, 50));
        comboVehicle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        styleComboBox(comboVehicle);

        // Custom renderer to show vehicle info clearly
        comboVehicle.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Vehicle) {
                    Vehicle v = (Vehicle) value;
                    String reg = v.getRegistration() != null
                            ? v.getRegistration().getPart1() + "-" + v.getRegistration().getPart2() + "-"
                                    + v.getRegistration().getPart3()
                            : "N/A";
                    String brand = v.getVehicleType() != null
                            ? v.getVehicleType().getBrand() + " " + v.getVehicleType().getModel()
                            : "";
                    String owner = v.getOwner() != null
                            ? " - " + v.getOwner().getFirstName() + " " + v.getOwner().getLastName()
                            : "";
                    setText("🚗 " + reg + " | " + brand + owner);
                    setFont(new Font("SansSerif", Font.PLAIN, 14));
                }
                return this;
            }
        });

        gbc.gridx = 1;
        gbc.weightx = 1;
        grid.add(comboVehicle, gbc);

        // Show Diagram button
        btnShowDiagram = new JButton("📊 View Diagram");
        btnShowDiagram.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnShowDiagram.setForeground(Color.WHITE);
        btnShowDiagram.setBackground(PRIMARY_COLOR);
        btnShowDiagram.setPreferredSize(new Dimension(140, 50));
        btnShowDiagram.setFocusPainted(false);
        btnShowDiagram.setBorderPainted(false);
        btnShowDiagram.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnShowDiagram.addActionListener(e -> showVehicleDiagram());

        btnShowDiagram.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnShowDiagram.setBackground(PRIMARY_COLOR.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnShowDiagram.setBackground(PRIMARY_COLOR);
            }
        });

        gbc.gridx = 2;
        gbc.weightx = 0;
        grid.add(btnShowDiagram, gbc);

        // Help text
        JLabel helpText = new JLabel(
                "⚠ You must select a vehicle to register an intervention. Click 'View Diagram' to visualize maintenance areas.");
        helpText.setFont(new Font("SansSerif", Font.ITALIC, 12));
        helpText.setForeground(WARNING_COLOR);
        helpText.setBorder(new EmptyBorder(10, 0, 0, 0));

        contentPanel.add(grid);
        contentPanel.add(helpText);

        section.add(titlePanel, BorderLayout.NORTH);
        section.add(contentPanel, BorderLayout.CENTER);

        return section;
    }

    /**
     * Shows the vehicle diagram in a dialog.
     */
    private void showVehicleDiagram() {
        Vehicle selectedVehicle = (Vehicle) comboVehicle.getSelectedItem();

        if (selectedVehicle == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a vehicle first.",
                    "No Vehicle Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Update diagram with vehicle data
        diagramView.setVehicleData(selectedVehicle, null, null);

        // Create dialog if not exists
        if (diagramDialog == null) {
            Window window = SwingUtilities.getWindowAncestor(this);
            diagramDialog = new JDialog(window instanceof Frame ? (Frame) window : null,
                    "Vehicle Diagram", true);
            diagramDialog.setSize(950, 650);
            diagramDialog.setLocationRelativeTo(this);
            diagramDialog.add(diagramView);
        }

        diagramDialog.setTitle("Vehicle Diagram - " +
                (selectedVehicle.getRegistration() != null ? selectedVehicle.getRegistration().getPart1() + "-" +
                        selectedVehicle.getRegistration().getPart2() + "-" +
                        selectedVehicle.getRegistration().getPart3() : ""));

        diagramDialog.setVisible(true);
    }

    /**
     * Creates the intervention type selection section.
     */
    private JPanel createInterventionTypeSection() {
        JPanel section = createSection("🔧 Intervention Type");
        JPanel content = (JPanel) section.getComponent(1);

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 20);
        gbc.anchor = GridBagConstraints.WEST;

        // Intervention type
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        grid.add(createLabel("Type of intervention:"), gbc);

        comboInterventionType = new JComboBox<>();
        comboInterventionType.setPreferredSize(new Dimension(350, 45));
        comboInterventionType.setFont(new Font("SansSerif", Font.PLAIN, 14));
        styleComboBox(comboInterventionType);

        // Renderer to show type name
        comboInterventionType.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof InterventionType) {
                    setText(((InterventionType) value).getName());
                }
                return this;
            }
        });

        gbc.gridx = 1;
        gbc.weightx = 1;
        grid.add(comboInterventionType, gbc);

        // Maintenance type (optional)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel lblMaintenance = createLabel("Maintenance type:");
        grid.add(lblMaintenance, gbc);

        maintenanceTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        maintenanceTypePanel.setOpaque(false);

        comboMaintenanceType = new JComboBox<>();
        comboMaintenanceType.setPreferredSize(new Dimension(350, 45));
        comboMaintenanceType.setFont(new Font("SansSerif", Font.PLAIN, 14));
        styleComboBox(comboMaintenanceType);

        // Renderer for maintenance type
        comboMaintenanceType.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof MaintenanceType) {
                    MaintenanceType mt = (MaintenanceType) value;
                    setText(mt.getName() + " (max " + mt.getMaxMileage() + " km / " + mt.getMaxDuration() + " months)");
                }
                return this;
            }
        });

        JLabel optionalLabel = new JLabel("  (optional for maintenance)");
        optionalLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        optionalLabel.setForeground(TEXT_SECONDARY);

        maintenanceTypePanel.add(comboMaintenanceType);
        maintenanceTypePanel.add(optionalLabel);

        gbc.gridx = 1;
        gbc.weightx = 1;
        grid.add(maintenanceTypePanel, gbc);

        content.add(grid);
        return section;
    }

    /**
     * Creates the intervention details section.
     */
    private JPanel createDetailsSection() {
        JPanel section = createSection("📋 Intervention Details");
        JPanel content = (JPanel) section.getComponent(1);

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 30);
        gbc.anchor = GridBagConstraints.WEST;

        // Intervention date
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        grid.add(createLabel("Intervention date:"), gbc);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePanel.setOpaque(false);

        txtDate = createStyledTextField(12);
        txtDate.setText("yyyy-MM-dd");
        txtDate.setForeground(TEXT_SECONDARY);
        txtDate.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtDate.getText().equals("yyyy-MM-dd")) {
                    txtDate.setText("");
                    txtDate.setForeground(TEXT_PRIMARY);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtDate.getText().isEmpty()) {
                    txtDate.setText("yyyy-MM-dd");
                    txtDate.setForeground(TEXT_SECONDARY);
                }
            }
        });

        JLabel dateHint = new JLabel("  Format: 2024-01-15");
        dateHint.setFont(new Font("SansSerif", Font.ITALIC, 11));
        dateHint.setForeground(TEXT_SECONDARY);

        datePanel.add(txtDate);
        datePanel.add(dateHint);

        gbc.gridx = 1;
        gbc.weightx = 1;
        grid.add(datePanel, gbc);

        // Mileage
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        grid.add(createLabel("Current mileage:"), gbc);

        JPanel mileagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        mileagePanel.setOpaque(false);

        txtMileage = createStyledTextField(10);

        JLabel kmLabel = new JLabel("km");
        kmLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        kmLabel.setForeground(TEXT_SECONDARY);

        mileagePanel.add(txtMileage);
        mileagePanel.add(kmLabel);

        gbc.gridx = 1;
        gbc.weightx = 1;
        grid.add(mileagePanel, gbc);

        content.add(grid);
        return section;
    }

    /**
     * Creates the price calculation section.
     */
    private JPanel createPriceSection() {
        JPanel section = createSection("💰 Pricing");
        JPanel content = (JPanel) section.getComponent(1);

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 30);
        gbc.anchor = GridBagConstraints.WEST;

        // Base price
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        grid.add(createLabel("Base price:"), gbc);

        JPanel basePricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        basePricePanel.setOpaque(false);

        txtBasePrice = createStyledTextField(10);
        txtBasePrice.setText("100.00");

        JLabel euroLabel1 = new JLabel("€");
        euroLabel1.setFont(new Font("SansSerif", Font.PLAIN, 14));
        euroLabel1.setForeground(TEXT_SECONDARY);

        basePricePanel.add(txtBasePrice);
        basePricePanel.add(euroLabel1);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        grid.add(basePricePanel, gbc);

        // Calculate button
        btnCalculate = new JButton("Calculate Price");
        btnCalculate.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCalculate.setForeground(Color.WHITE);
        btnCalculate.setBackground(SUCCESS_COLOR);
        btnCalculate.setPreferredSize(new Dimension(160, 45));
        btnCalculate.setFocusPainted(false);
        btnCalculate.setBorderPainted(false);
        btnCalculate.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnCalculate.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnCalculate.setBackground(SUCCESS_COLOR.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnCalculate.setBackground(SUCCESS_COLOR);
            }
        });

        gbc.gridx = 2;
        gbc.weightx = 0;
        grid.add(btnCalculate, gbc);

        // Final price
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel lblFinal = createLabel("Final price (calculated):");
        lblFinal.setFont(new Font("SansSerif", Font.BOLD, 14));
        grid.add(lblFinal, gbc);

        JPanel finalPricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        finalPricePanel.setOpaque(false);

        txtFinalPrice = createStyledTextField(10);
        txtFinalPrice.setEditable(false);
        txtFinalPrice.setBackground(new Color(220, 252, 231));
        txtFinalPrice.setFont(new Font("SansSerif", Font.BOLD, 16));
        txtFinalPrice.setForeground(new Color(22, 101, 52));

        JLabel euroLabel2 = new JLabel("€");
        euroLabel2.setFont(new Font("SansSerif", Font.BOLD, 16));
        euroLabel2.setForeground(new Color(22, 101, 52));

        finalPricePanel.add(txtFinalPrice);
        finalPricePanel.add(euroLabel2);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        grid.add(finalPricePanel, gbc);

        // Explanatory note
        JLabel noteLabel = new JLabel("Price is calculated based on vehicle type");
        noteLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        noteLabel.setForeground(TEXT_SECONDARY);

        gbc.gridx = 2;
        gbc.weightx = 0;
        grid.add(noteLabel, gbc);

        content.add(grid);
        return section;
    }

    /**
     * Creates the action buttons panel.
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        btnClear = new JButton("Clear");
        styleSecondaryButton(btnClear);

        btnSave = new JButton("Save Intervention");
        styleSecondaryButton(btnSave);

        buttonPanel.add(btnClear);
        buttonPanel.add(btnSave);

        return buttonPanel;
    }

    // ===== UTILITY METHODS =====

    /**
     * Creates a section with title and content.
     */
    private JPanel createSection(String title) {
        JPanel section = new JPanel(new BorderLayout(0, 15));
        section.setBackground(CARD_BACKGROUND);
        section.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(25, 30, 25, 30)));
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_PRIMARY);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        section.add(titleLabel, BorderLayout.NORTH);
        section.add(contentPanel, BorderLayout.CENTER);

        return section;
    }

    /**
     * Creates a styled label.
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(TEXT_PRIMARY);
        label.setPreferredSize(new Dimension(180, 45));
        return label;
    }

    /**
     * Creates a styled text field.
     */
    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 45));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(5, 12, 5, 12)));
        field.setBackground(INPUT_BACKGROUND);
        return field;
    }

    /**
     * Styles a ComboBox.
     */
    private void styleComboBox(JComboBox<?> combo) {
        combo.setBackground(INPUT_BACKGROUND);
        combo.setBorder(new LineBorder(BORDER_COLOR, 1, true));
    }

    /**
     * Styles a secondary button.
     */
    private void styleSecondaryButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setForeground(TEXT_PRIMARY);
        button.setBackground(Color.WHITE);
        button.setPreferredSize(new Dimension(120, 45));
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(248, 250, 252));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });
    }

    /**
     * Clears all form fields.
     */
    public void clearForm() {
        txtDate.setText("yyyy-MM-dd");
        txtDate.setForeground(TEXT_SECONDARY);
        txtMileage.setText("");
        txtBasePrice.setText("100.00");
        txtFinalPrice.setText("");
        if (comboVehicle.getItemCount() > 0) {
            comboVehicle.setSelectedIndex(0);
        }
        if (comboInterventionType.getItemCount() > 0) {
            comboInterventionType.setSelectedIndex(0);
        }
        if (comboMaintenanceType.getItemCount() > 0) {
            comboMaintenanceType.setSelectedIndex(0);
        }
    }

    /**
     * Displays the calculated price.
     */
    public void displayPrice(double price) {
        txtFinalPrice.setText(String.format("%.2f", price));
    }

    /**
     * Gets the diagram view for external updates.
     */
    public VehicleDiagramView getDiagramView() {
        return diagramView;
    }
}
