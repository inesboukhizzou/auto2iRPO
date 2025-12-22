package ui.components;

import javax.swing.*;
import java.awt.*;

public class SideMenu extends JPanel {

    public SideMenu(CardLayout layout, JPanel contentPanel) {
        setPreferredSize(new Dimension(220, 0));
        setBackground(new Color(30, 45, 110));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(Box.createVerticalStrut(20));
        add(createButton("Dashboard", () -> layout.show(contentPanel, "DASHBOARD")));
        add(createButton("Vehicles", () -> layout.show(contentPanel, "VEHICLES")));
        add(createButton("Interventions", () -> layout.show(contentPanel, "INTERVENTIONS")));
    }

    private JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> action.run());
        return button;
    }
}