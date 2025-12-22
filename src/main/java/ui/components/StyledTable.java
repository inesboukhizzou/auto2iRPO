package ui.components;

import javax.swing.*;

public class StyledTable extends JTable {
    public StyledTable(Object[][] data, String[] columns) {
        super(data, columns);
        setRowHeight(28);
    }
}