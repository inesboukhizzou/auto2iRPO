package ui.views;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import entities.Owner;
import entities.VehicleType;

/**
 * Vehicle registration form view.
 * Allows entering all information needed to register a new vehicle.
 */
public class VehicleFormView extends JPanel {

    
    private static final Color BACKGROUND = new Color(241, 245, 249);
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color SECONDARY_COLOR = new Color(99, 102, 241); 
    private static final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color BORDER_COLOR = new Color(226, 232, 240);
    private static final Color INPUT_BACKGROUND = new Color(248, 250, 252);

    
    public JComboBox<Owner> comboOwner;
    public JButton btnAddOwner;

    
    public JComboBox<VehicleType> comboVehicleType;
    public JButton btnAddVehicleType;

    
    public JTextField txtBrand;
    public JTextField txtModel;
    public JComboBox<String> comboFuelType;
    public JComboBox<String> comboGearbox;
    public JTextField txtDoors;
    public JTextField txtSeats;
    public JTextField txtPower;

    
    public JTextField txtMileage;
    public JTextField txtDateRegistration;

    
    public JTextField txtRegPart1;
    public JTextField txtRegPart2;
    public JTextField txtRegPart3;

    
    public JButton btnSave;
    public JButton btnClear;

    
    private JPanel vehicleTypeFormPanel;
    private boolean vehicleTypeFormVisible = false;

    public VehicleFormView() {
        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        
        add(createHeader(), BorderLayout.NORTH);

        
        add(createFormPanel(), BorderLayout.CENTER);
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

        JLabel title = new JLabel("Add a Vehicle");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Register a new vehicle in the system");
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

        
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setOpaque(false);

        
        formContainer.add(createOwnerSection());
        formContainer.add(Box.createVerticalStrut(20));

        
        formContainer.add(createVehicleTypeSection());
        formContainer.add(Box.createVerticalStrut(20));

        
        formContainer.add(createVehicleInfoSection());

        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(formContainer, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BACKGROUND);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        return mainPanel;
    }

    /**
     * Creates the owner section.
     */
    private JPanel createOwnerSection() {
        JPanel section = createSection("👤 Owner");
        JPanel content = (JPanel) section.getComponent(1);

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 20);

        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        grid.add(createLabel("Select owner:"), gbc);

        comboOwner = new JComboBox<>();
        comboOwner.setPreferredSize(new Dimension(300, 40));
        comboOwner.setFont(new Font("SansSerif", Font.PLAIN, 14));
        styleComboBox(comboOwner);

        
        comboOwner.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Owner) {
                    Owner owner = (Owner) value;
                    setText(owner.getFirstName() + " " + owner.getLastName());
                }
                return this;
            }
        });

        gbc.gridx = 1;
        gbc.weightx = 1;
        grid.add(comboOwner, gbc);

        
        btnAddOwner = new JButton("+ New Owner");
        styleSecondaryButton(btnAddOwner);

        gbc.gridx = 2;
        gbc.weightx = 0;
        grid.add(btnAddOwner, gbc);

        content.add(grid);
        return section;
    }

    /**
     * Creates the vehicle type section with option to add new type.
     */
    private JPanel createVehicleTypeSection() {
        JPanel section = createSection("🚗 Vehicle Type");
        JPanel content = (JPanel) section.getComponent(1);

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 20);
        gbc.anchor = GridBagConstraints.WEST;

        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        grid.add(createLabel("Select type:"), gbc);

        comboVehicleType = new JComboBox<>();
        comboVehicleType.setPreferredSize(new Dimension(350, 40));
        comboVehicleType.setFont(new Font("SansSerif", Font.PLAIN, 14));
        styleComboBox(comboVehicleType);

        
        comboVehicleType.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof VehicleType) {
                    VehicleType vt = (VehicleType) value;
                    setText(vt.getBrand() + " " + vt.getModel() + " (" + vt.getFuelType() + ")");
                }
                return this;
            }
        });

        gbc.gridx = 1;
        gbc.weightx = 1;
        grid.add(comboVehicleType, gbc);

        
        btnAddVehicleType = new JButton("+ New Vehicle Type");
        styleSecondaryButton(btnAddVehicleType);
        btnAddVehicleType.addActionListener(e -> toggleVehicleTypeForm());

        gbc.gridx = 2;
        gbc.weightx = 0;
        grid.add(btnAddVehicleType, gbc);

        content.add(grid);

        
        vehicleTypeFormPanel = createVehicleTypeFormPanel();
        vehicleTypeFormPanel.setVisible(false);
        content.add(vehicleTypeFormPanel);

        return section;
    }

    /**
     * Creates the form panel for adding a new vehicle type.
     */
    private JPanel createVehicleTypeFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 0, 0, 0, BORDER_COLOR),
                new EmptyBorder(20, 0, 0, 0)));

        JLabel formTitle = new JLabel("➕ Add New Vehicle Type");
        formTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        formTitle.setForeground(SECONDARY_COLOR);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(formTitle);
        formPanel.add(Box.createVerticalStrut(15));

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 20);
        gbc.anchor = GridBagConstraints.WEST;

        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        grid.add(createSmallLabel("Brand:"), gbc);
        txtBrand = createStyledTextField(15);
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        grid.add(txtBrand, gbc);

        
        gbc.gridx = 2;
        gbc.weightx = 0;
        grid.add(createSmallLabel("Model:"), gbc);
        txtModel = createStyledTextField(15);
        gbc.gridx = 3;
        gbc.weightx = 0.5;
        grid.add(txtModel, gbc);

        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        grid.add(createSmallLabel("Fuel:"), gbc);
        comboFuelType = new JComboBox<>(new String[] { "Gasoline", "Diesel", "Electric", "Hybrid", "LPG" });
        comboFuelType.setPreferredSize(new Dimension(150, 35));
        styleComboBox(comboFuelType);
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        grid.add(comboFuelType, gbc);

        
        gbc.gridx = 2;
        gbc.weightx = 0;
        grid.add(createSmallLabel("Gearbox:"), gbc);
        comboGearbox = new JComboBox<>(new String[] { "Manual", "Automatic", "Semi-automatic" });
        comboGearbox.setPreferredSize(new Dimension(150, 35));
        styleComboBox(comboGearbox);
        gbc.gridx = 3;
        gbc.weightx = 0.5;
        grid.add(comboGearbox, gbc);

        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        grid.add(createSmallLabel("Doors:"), gbc);
        txtDoors = createStyledTextField(5);
        txtDoors.setText("5");
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        grid.add(txtDoors, gbc);

        
        gbc.gridx = 2;
        gbc.weightx = 0;
        grid.add(createSmallLabel("Seats:"), gbc);
        txtSeats = createStyledTextField(5);
        txtSeats.setText("5");
        gbc.gridx = 3;
        gbc.weightx = 0.5;
        grid.add(txtSeats, gbc);

        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        grid.add(createSmallLabel("Power (HP):"), gbc);
        txtPower = createStyledTextField(5);
        txtPower.setText("100");
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        grid.add(txtPower, gbc);

        formPanel.add(grid);

        return formPanel;
    }

    /**
     * Toggles the visibility of the vehicle type form.
     */
    private void toggleVehicleTypeForm() {
        vehicleTypeFormVisible = !vehicleTypeFormVisible;
        vehicleTypeFormPanel.setVisible(vehicleTypeFormVisible);
        btnAddVehicleType.setText(vehicleTypeFormVisible ? "✕ Cancel" : "+ New Vehicle Type");
        revalidate();
        repaint();
    }

    /**
     * Creates the vehicle information section.
     */
    private JPanel createVehicleInfoSection() {
        JPanel section = createSection("📋 Vehicle Information");
        JPanel content = (JPanel) section.getComponent(1);

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 30);
        gbc.anchor = GridBagConstraints.WEST;

        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        grid.add(createLabel("License plate:"), gbc);

        JPanel regPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        regPanel.setOpaque(false);

        txtRegPart1 = createStyledTextField(3);
        txtRegPart1.setHorizontalAlignment(JTextField.CENTER);
        txtRegPart1.setToolTipText("2 letters (e.g., AB)");

        JLabel dash1 = new JLabel("-");
        dash1.setFont(new Font("SansSerif", Font.BOLD, 18));
        dash1.setForeground(TEXT_SECONDARY);

        txtRegPart2 = createStyledTextField(4);
        txtRegPart2.setHorizontalAlignment(JTextField.CENTER);
        txtRegPart2.setToolTipText("3 digits (e.g., 123)");

        JLabel dash2 = new JLabel("-");
        dash2.setFont(new Font("SansSerif", Font.BOLD, 18));
        dash2.setForeground(TEXT_SECONDARY);

        txtRegPart3 = createStyledTextField(3);
        txtRegPart3.setHorizontalAlignment(JTextField.CENTER);
        txtRegPart3.setToolTipText("2 letters (e.g., CD)");

        regPanel.add(txtRegPart1);
        regPanel.add(dash1);
        regPanel.add(txtRegPart2);
        regPanel.add(dash2);
        regPanel.add(txtRegPart3);

        JLabel formatHint = new JLabel("  SIV format: AA-123-BB");
        formatHint.setFont(new Font("SansSerif", Font.ITALIC, 11));
        formatHint.setForeground(TEXT_SECONDARY);
        regPanel.add(formatHint);

        gbc.gridx = 1;
        gbc.weightx = 1;
        grid.add(regPanel, gbc);

        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        grid.add(createLabel("Current mileage:"), gbc);

        JPanel mileagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        mileagePanel.setOpaque(false);

        txtMileage = createStyledTextField(10);
        txtMileage.setToolTipText("Mileage in km");

        JLabel kmLabel = new JLabel("km");
        kmLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        kmLabel.setForeground(TEXT_SECONDARY);

        mileagePanel.add(txtMileage);
        mileagePanel.add(kmLabel);

        gbc.gridx = 1;
        gbc.weightx = 1;
        grid.add(mileagePanel, gbc);

        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        grid.add(createLabel("First registration date:"), gbc);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePanel.setOpaque(false);

        txtDateRegistration = createStyledTextField(12);
        txtDateRegistration.setText("yyyy-MM-dd");
        txtDateRegistration.setForeground(TEXT_SECONDARY);
        txtDateRegistration.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtDateRegistration.getText().equals("yyyy-MM-dd")) {
                    txtDateRegistration.setText("");
                    txtDateRegistration.setForeground(TEXT_PRIMARY);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtDateRegistration.getText().isEmpty()) {
                    txtDateRegistration.setText("yyyy-MM-dd");
                    txtDateRegistration.setForeground(TEXT_SECONDARY);
                }
            }
        });

        JLabel dateHint = new JLabel("  Format: 2024-01-15");
        dateHint.setFont(new Font("SansSerif", Font.ITALIC, 11));
        dateHint.setForeground(TEXT_SECONDARY);

        datePanel.add(txtDateRegistration);
        datePanel.add(dateHint);

        gbc.gridx = 1;
        gbc.weightx = 1;
        grid.add(datePanel, gbc);

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

        btnSave = new JButton("Save Vehicle");
        styleSecondaryButton(btnSave);

        buttonPanel.add(btnClear);
        buttonPanel.add(btnSave);

        return buttonPanel;
    }

    

    /**
     * Creates a section with title and content.
     */
    private JPanel createSection(String title) {
        JPanel section = new JPanel(new BorderLayout(0, 15));
        section.setBackground(CARD_BACKGROUND);
        section.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(25, 30, 25, 30)));
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

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
        label.setPreferredSize(new Dimension(180, 40));
        return label;
    }

    /**
     * Creates a small label for the vehicle type form.
     */
    private JLabel createSmallLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(TEXT_PRIMARY);
        label.setPreferredSize(new Dimension(80, 35));
        return label;
    }

    /**
     * Creates a styled text field.
     */
    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 40));
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
        button.setPreferredSize(new Dimension(180, 45));
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
        txtRegPart1.setText("");
        txtRegPart2.setText("");
        txtRegPart3.setText("");
        txtMileage.setText("");
        txtDateRegistration.setText("yyyy-MM-dd");
        txtDateRegistration.setForeground(TEXT_SECONDARY);
        if (comboOwner.getItemCount() > 0) {
            comboOwner.setSelectedIndex(0);
        }
        if (comboVehicleType.getItemCount() > 0) {
            comboVehicleType.setSelectedIndex(0);
        }
        
        if (txtBrand != null)
            txtBrand.setText("");
        if (txtModel != null)
            txtModel.setText("");
        if (txtDoors != null)
            txtDoors.setText("5");
        if (txtSeats != null)
            txtSeats.setText("5");
        if (txtPower != null)
            txtPower.setText("100");
        if (comboFuelType != null && comboFuelType.getItemCount() > 0)
            comboFuelType.setSelectedIndex(0);
        if (comboGearbox != null && comboGearbox.getItemCount() > 0)
            comboGearbox.setSelectedIndex(0);
        
        if (vehicleTypeFormVisible) {
            toggleVehicleTypeForm();
        }
    }

    /**
     * Checks if the vehicle type form is visible.
     */
    public boolean isVehicleTypeFormVisible() {
        return vehicleTypeFormVisible;
    }

    /**
     * Hides the vehicle type form.
     */
    public void hideVehicleTypeForm() {
        if (vehicleTypeFormVisible) {
            toggleVehicleTypeForm();
        }
    }
}
