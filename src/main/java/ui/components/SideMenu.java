package ui.components;

import javax.swing.*;
import java.awt.*;

// Enhanced SideMenu.java snippet
public class SideMenu extends JPanel {
    public SideMenu(CardLayout layout, JPanel contentPanel) {
        setPreferredSize(new Dimension(220, 0));
        setBackground(new Color(28, 35, 59)); // Deep Midnight Blue
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Navigation Buttons with modern styling
        add(createNavButton("📊 Dashboard", "DASHBOARD", layout, contentPanel));
        add(createNavButton("🚗 Vehicles", "VEHICLES", layout, contentPanel));
        add(createNavButton("🛠 Interventions", "INTERVENTIONS", layout, contentPanel));
        add(createNavButton("💰 Pricing", "PRICING", layout, contentPanel));
    }

    private JButton createNavButton(String text, String cardName, CardLayout layout, JPanel panel) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.addActionListener(e -> layout.show(panel, cardName));
        return btn;
    }
}