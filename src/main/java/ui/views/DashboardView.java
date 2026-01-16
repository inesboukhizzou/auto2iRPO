package ui.views;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import services.InterventionService.PlannedIntervention;

/**
 * Dashboard view displaying scheduled maintenance interventions.
 * Shows a list of interventions sorted by urgency.
 */
public class DashboardView extends JPanel {

    
    private static final Color BACKGROUND = new Color(241, 245, 249);
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color PRIMARY_COLOR = new Color(59, 130, 246);
    private static final Color DANGER_COLOR = new Color(239, 68, 68);
    private static final Color WARNING_COLOR = new Color(245, 158, 11);
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private static final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);

    
    private DefaultTableModel tableModel;
    private JTable maintenanceTable;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public DashboardView() {
        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        
        add(createHeader(), BorderLayout.NORTH);

        
        add(createTablePanel(), BorderLayout.CENTER);
    }

    /**
     * Creates the dashboard header.
     */
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Overview of scheduled maintenance and upcoming tasks");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.WEST);

        return header;
    }

    /**
     * Creates the maintenance table panel.
     */
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(CARD_BACKGROUND);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(0, 0, 0, 0)));

        
        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(CARD_BACKGROUND);
        tableHeader.setBorder(new EmptyBorder(20, 25, 15, 25));

        JLabel tableTitle = new JLabel("🚨 Scheduled Maintenance");
        tableTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        tableTitle.setForeground(TEXT_PRIMARY);

        JLabel tableSubtitle = new JLabel("List sorted by priority");
        tableSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tableSubtitle.setForeground(TEXT_SECONDARY);

        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS));
        titleContainer.setOpaque(false);
        titleContainer.add(tableTitle);
        titleContainer.add(tableSubtitle);

        tableHeader.add(titleContainer, BorderLayout.WEST);

        
        String[] columns = { "PRIORITY", "OWNER", "PHONE", "EMAIL", "VEHICLE", "INTERVENTION", "SCHEDULED DATE" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        maintenanceTable = new JTable(tableModel);
        maintenanceTable.setRowHeight(55);
        maintenanceTable.setShowGrid(false);
        maintenanceTable.setIntercellSpacing(new Dimension(0, 0));
        maintenanceTable.setSelectionBackground(new Color(239, 246, 255));
        maintenanceTable.setSelectionForeground(TEXT_PRIMARY);
        maintenanceTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        maintenanceTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        maintenanceTable.getTableHeader().setBackground(new Color(248, 250, 252));
        maintenanceTable.getTableHeader().setForeground(TEXT_SECONDARY);
        maintenanceTable.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        maintenanceTable.getTableHeader().setPreferredSize(new Dimension(0, 45));

        
        maintenanceTable.getColumnModel().getColumn(0).setCellRenderer(new PriorityRenderer());

        
        maintenanceTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        maintenanceTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        maintenanceTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        maintenanceTable.getColumnModel().getColumn(3).setPreferredWidth(180);
        maintenanceTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        maintenanceTable.getColumnModel().getColumn(5).setPreferredWidth(150);
        maintenanceTable.getColumnModel().getColumn(6).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(maintenanceTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(CARD_BACKGROUND);

        tablePanel.add(tableHeader, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    /**
     * Updates the dashboard data with planned interventions.
     */
    public void updateData(List<PlannedIntervention> interventions) {
        tableModel.setRowCount(0);

        for (PlannedIntervention pi : interventions) {
            String dateStr = pi.getPlannedDate() != null ? dateFormat.format(pi.getPlannedDate()) : "N/A";
            int priority = pi.getPriority();

            tableModel.addRow(new Object[] {
                    priority,
                    pi.getOwnerName(),
                    pi.getOwnerPhone(),
                    pi.getOwnerEmail(),
                    pi.getVehicleLabel(),
                    pi.getInterventionName(),
                    dateStr
            });
        }
    }

    /**
     * Custom renderer to display priority with colors.
     */
    private class PriorityRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            JLabel label = new JLabel();
            label.setOpaque(true);
            label.setHorizontalAlignment(CENTER);
            label.setFont(new Font("SansSerif", Font.BOLD, 12));

            int priority = (Integer) value;

            if (priority >= 5) {
                label.setText("CRITICAL");
                label.setBackground(new Color(254, 226, 226));
                label.setForeground(DANGER_COLOR);
            } else if (priority >= 4) {
                label.setText("URGENT");
                label.setBackground(new Color(254, 243, 199));
                label.setForeground(new Color(180, 83, 9));
            } else if (priority >= 3) {
                label.setText("MEDIUM");
                label.setBackground(new Color(254, 249, 195));
                label.setForeground(WARNING_COLOR);
            } else {
                label.setText("LOW");
                label.setBackground(new Color(220, 252, 231));
                label.setForeground(SUCCESS_COLOR);
            }

            label.setBorder(new EmptyBorder(5, 10, 5, 10));

            if (isSelected) {
                label.setBackground(table.getSelectionBackground());
            }

            return label;
        }
    }
}
