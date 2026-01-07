package ui.views;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import entities.Intervention;
import entities.Vehicle;

/**
 * Vehicle search view by license plate.
 * Displays vehicle details and intervention history.
 */
public class SearchVehicleView extends JPanel {

    // Theme colors
    private static final Color BACKGROUND = new Color(241, 245, 249);
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color PRIMARY_COLOR = new Color(59, 130, 246);
    private static final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color BORDER_COLOR = new Color(226, 232, 240);
    private static final Color INPUT_BACKGROUND = new Color(248, 250, 252);
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);

    // Search components
    public JTextField txtSearchPart1;
    public JTextField txtSearchPart2;
    public JTextField txtSearchPart3;
    public JButton btnSearch;

    // Details display
    private JPanel vehicleDetailsPanel;
    private JLabel lblOwnerName;
    private JLabel lblOwnerPhone;
    private JLabel lblOwnerEmail;
    private JLabel lblVehicleBrand;
    private JLabel lblVehicleModel;
    private JLabel lblVehicleFuel;
    private JLabel lblVehicleReg;
    private JLabel lblVehicleMileage;
    private JLabel lblVehicleDate;

    // History table
    private DefaultTableModel tableModel;
    private JTable historyTable;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public SearchVehicleView() {
        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        add(createHeader(), BorderLayout.NORTH);

        // Main content
        add(createMainContent(), BorderLayout.CENTER);
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

        JLabel title = new JLabel("Search Vehicle");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Find a vehicle by its license plate");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.WEST);

        return header;
    }

    /**
     * Creates the main content.
     */
    private JPanel createMainContent() {
        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setOpaque(false);

        // Search bar
        content.add(createSearchPanel(), BorderLayout.NORTH);

        // Results area
        JPanel resultsPanel = new JPanel(new BorderLayout(0, 20));
        resultsPanel.setOpaque(false);

        // Vehicle details
        resultsPanel.add(createVehicleDetailsPanel(), BorderLayout.NORTH);

        // Intervention history
        resultsPanel.add(createHistoryPanel(), BorderLayout.CENTER);

        content.add(resultsPanel, BorderLayout.CENTER);

        return content;
    }

    /**
     * Creates the search panel.
     */
    private JPanel createSearchPanel() {
        JPanel searchCard = new JPanel(new BorderLayout(20, 0));
        searchCard.setBackground(CARD_BACKGROUND);
        searchCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(25, 30, 25, 30)));

        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("SansSerif", Font.PLAIN, 24));

        // Input panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        inputPanel.setOpaque(false);

        JLabel label = new JLabel("License plate:");
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(TEXT_PRIMARY);

        txtSearchPart1 = createSearchField(3);
        txtSearchPart1.setToolTipText("2 letters");

        JLabel dash1 = new JLabel("-");
        dash1.setFont(new Font("SansSerif", Font.BOLD, 18));
        dash1.setForeground(TEXT_SECONDARY);

        txtSearchPart2 = createSearchField(4);
        txtSearchPart2.setToolTipText("3 digits");

        JLabel dash2 = new JLabel("-");
        dash2.setFont(new Font("SansSerif", Font.BOLD, 18));
        dash2.setForeground(TEXT_SECONDARY);

        txtSearchPart3 = createSearchField(3);
        txtSearchPart3.setToolTipText("2 letters");

        inputPanel.add(label);
        inputPanel.add(Box.createHorizontalStrut(15));
        inputPanel.add(txtSearchPart1);
        inputPanel.add(dash1);
        inputPanel.add(txtSearchPart2);
        inputPanel.add(dash2);
        inputPanel.add(txtSearchPart3);

        // Search button
        btnSearch = new JButton("Search");
        btnSearch.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setBackground(PRIMARY_COLOR);
        btnSearch.setPreferredSize(new Dimension(140, 45));
        btnSearch.setFocusPainted(false);
        btnSearch.setBorderPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnSearch.setBackground(PRIMARY_COLOR.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnSearch.setBackground(PRIMARY_COLOR);
            }
        });

        searchCard.add(searchIcon, BorderLayout.WEST);
        searchCard.add(inputPanel, BorderLayout.CENTER);
        searchCard.add(btnSearch, BorderLayout.EAST);

        return searchCard;
    }

    /**
     * Creates a styled search field.
     */
    private JTextField createSearchField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("SansSerif", Font.BOLD, 16));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 45));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(5, 10, 5, 10)));
        field.setBackground(INPUT_BACKGROUND);
        return field;
    }

    /**
     * Creates the vehicle details panel.
     */
    private JPanel createVehicleDetailsPanel() {
        vehicleDetailsPanel = new JPanel(new BorderLayout());
        vehicleDetailsPanel.setBackground(CARD_BACKGROUND);
        vehicleDetailsPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(25, 30, 25, 30)));
        vehicleDetailsPanel.setVisible(false);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel detailsTitle = new JLabel("📋 Vehicle Details");
        detailsTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        detailsTitle.setForeground(TEXT_PRIMARY);

        headerPanel.add(detailsTitle, BorderLayout.WEST);

        // Content
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Owner section
        JPanel ownerSection = new JPanel();
        ownerSection.setLayout(new BoxLayout(ownerSection, BoxLayout.Y_AXIS));
        ownerSection.setOpaque(false);

        JLabel ownerTitle = new JLabel("👤 Owner");
        ownerTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        ownerTitle.setForeground(PRIMARY_COLOR);

        lblOwnerName = createDetailLabel("");
        lblOwnerPhone = createDetailLabel("");
        lblOwnerEmail = createDetailLabel("");

        ownerSection.add(ownerTitle);
        ownerSection.add(Box.createVerticalStrut(10));
        ownerSection.add(createDetailRow("Name:", lblOwnerName));
        ownerSection.add(createDetailRow("Phone:", lblOwnerPhone));
        ownerSection.add(createDetailRow("Email:", lblOwnerEmail));

        // Vehicle section
        JPanel vehicleSection = new JPanel();
        vehicleSection.setLayout(new BoxLayout(vehicleSection, BoxLayout.Y_AXIS));
        vehicleSection.setOpaque(false);

        JLabel vehicleTitle = new JLabel("🚗 Vehicle");
        vehicleTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        vehicleTitle.setForeground(PRIMARY_COLOR);

        lblVehicleBrand = createDetailLabel("");
        lblVehicleModel = createDetailLabel("");
        lblVehicleFuel = createDetailLabel("");
        lblVehicleReg = createDetailLabel("");
        lblVehicleMileage = createDetailLabel("");
        lblVehicleDate = createDetailLabel("");

        vehicleSection.add(vehicleTitle);
        vehicleSection.add(Box.createVerticalStrut(10));
        vehicleSection.add(createDetailRow("Brand:", lblVehicleBrand));
        vehicleSection.add(createDetailRow("Model:", lblVehicleModel));
        vehicleSection.add(createDetailRow("Fuel:", lblVehicleFuel));
        vehicleSection.add(createDetailRow("Plate:", lblVehicleReg));
        vehicleSection.add(createDetailRow("Mileage:", lblVehicleMileage));
        vehicleSection.add(createDetailRow("1st reg.:", lblVehicleDate));

        contentPanel.add(ownerSection);
        contentPanel.add(vehicleSection);

        vehicleDetailsPanel.add(headerPanel, BorderLayout.NORTH);
        vehicleDetailsPanel.add(contentPanel, BorderLayout.CENTER);

        return vehicleDetailsPanel;
    }

    /**
     * Creates a detail row.
     */
    private JPanel createDetailRow(String labelText, JLabel valueLabel) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        row.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(TEXT_SECONDARY);
        label.setPreferredSize(new Dimension(100, 20));

        row.add(label);
        row.add(valueLabel);

        return row;
    }

    /**
     * Creates a detail label.
     */
    private JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    /**
     * Creates the intervention history panel.
     */
    private JPanel createHistoryPanel() {
        JPanel historyCard = new JPanel(new BorderLayout());
        historyCard.setBackground(CARD_BACKGROUND);
        historyCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(0, 0, 0, 0)));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_BACKGROUND);
        headerPanel.setBorder(new EmptyBorder(20, 25, 15, 25));

        JLabel historyTitle = new JLabel("📜 Intervention History");
        historyTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        historyTitle.setForeground(TEXT_PRIMARY);

        headerPanel.add(historyTitle, BorderLayout.WEST);

        // Table
        String[] columns = { "ID", "DATE", "INTERVENTION TYPE", "MILEAGE", "PRICE (€)" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        historyTable = new JTable(tableModel);
        historyTable.setRowHeight(50);
        historyTable.setShowGrid(false);
        historyTable.setIntercellSpacing(new Dimension(0, 0));
        historyTable.setSelectionBackground(new Color(239, 246, 255));
        historyTable.setSelectionForeground(TEXT_PRIMARY);
        historyTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        historyTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        historyTable.getTableHeader().setBackground(new Color(248, 250, 252));
        historyTable.getTableHeader().setForeground(TEXT_SECONDARY);
        historyTable.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COLOR));
        historyTable.getTableHeader().setPreferredSize(new Dimension(0, 45));

        // Column widths
        historyTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        historyTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        historyTable.getColumnModel().getColumn(2).setPreferredWidth(250);
        historyTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        historyTable.getColumnModel().getColumn(4).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(CARD_BACKGROUND);

        historyCard.add(headerPanel, BorderLayout.NORTH);
        historyCard.add(scrollPane, BorderLayout.CENTER);

        return historyCard;
    }

    /**
     * Displays vehicle data and history.
     */
    public void displayVehicleData(Vehicle vehicle, List<Intervention> history) {
        if (vehicle != null) {
            vehicleDetailsPanel.setVisible(true);

            // Owner
            if (vehicle.getOwner() != null) {
                lblOwnerName.setText(vehicle.getOwner().getFirstName() + " " + vehicle.getOwner().getLastName());
                lblOwnerPhone.setText(
                        vehicle.getOwner().getPhoneNumber() != null ? vehicle.getOwner().getPhoneNumber() : "N/A");
                lblOwnerEmail.setText(vehicle.getOwner().getEmail() != null ? vehicle.getOwner().getEmail() : "N/A");
            }

            // Vehicle
            if (vehicle.getVehicleType() != null) {
                lblVehicleBrand.setText(vehicle.getVehicleType().getBrand());
                lblVehicleModel.setText(vehicle.getVehicleType().getModel());
                lblVehicleFuel.setText(vehicle.getVehicleType().getFuelType());
            }

            if (vehicle.getRegistration() != null) {
                lblVehicleReg.setText(vehicle.getRegistration().getPart1() + "-" +
                        vehicle.getRegistration().getPart2() + "-" +
                        vehicle.getRegistration().getPart3());
            }

            lblVehicleMileage.setText(vehicle.getLastMileage() + " km");
            lblVehicleDate.setText(vehicle.getDateOfFirstRegistration() != null
                    ? dateFormat.format(vehicle.getDateOfFirstRegistration())
                    : "N/A");

            // History
            tableModel.setRowCount(0);
            if (history != null) {
                for (Intervention i : history) {
                    String dateStr = i.getDate() != null ? dateFormat.format(i.getDate()) : "N/A";
                    String typeName = i.getInterventionType() != null ? i.getInterventionType().getName() : "N/A";

                    tableModel.addRow(new Object[] {
                            i.getId(),
                            dateStr,
                            typeName,
                            i.getVehicleMileage() + " km",
                            String.format("%.2f €", i.getPrice())
                    });
                }
            }
        } else {
            vehicleDetailsPanel.setVisible(false);
            tableModel.setRowCount(0);
            JOptionPane.showMessageDialog(this,
                    "No vehicle found with this license plate.",
                    "Result",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Clears search fields.
     */
    public void clearSearch() {
        txtSearchPart1.setText("");
        txtSearchPart2.setText("");
        txtSearchPart3.setText("");
        vehicleDetailsPanel.setVisible(false);
        tableModel.setRowCount(0);
    }
}
