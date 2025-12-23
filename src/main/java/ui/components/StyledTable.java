package ui.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * A custom JTable component styled for a modern UI.
 * Used across the Dashboard, Vehicle, and Intervention panels.
 */
public class StyledTable extends JTable {

    public StyledTable(Object[][] data, String[] columns) {
        super(data, columns);

        // Basic configuration
        setRowHeight(35); // Increased height for better readability
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setFillsViewportHeight(true);
        setBackground(Color.WHITE);

        // Header Styling
        getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        getTableHeader().setBackground(new Color(248, 250, 252));
        getTableHeader().setForeground(new Color(71, 85, 105));
        getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        getTableHeader().setReorderingAllowed(false);

        // Selection Styling
        setSelectionBackground(new Color(241, 245, 249));
        setSelectionForeground(new Color(30, 41, 59));

        // Alignment and Padding
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Add horizontal padding

                // Zebra Striping for better data visualization
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(252, 253, 255));
                }
                return c;
            }
        };

        // Apply renderer to all columns
        for (int i = 0; i < getColumnCount(); i++) {
            getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
}