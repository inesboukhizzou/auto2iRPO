package ui.components;

import javax.swing.*;
import java.awt.*;

public class SideMenu extends JPanel {
    private final Color sidebarColor = new Color(30, 45, 110);

    public SideMenu(CardLayout layout, JPanel contentPanel) {
        setPreferredSize(new Dimension(240, 0));
        setBackground(sidebarColor);
        setLayout(new BorderLayout());

        // Brand Label
        JLabel brandLabel = new JLabel("Auto2i");
        brandLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        brandLabel.setForeground(Color.WHITE);

        // Navigation Container
        JPanel navContainer = new JPanel();
        navContainer.setLayout(new BoxLayout(navContainer, BoxLayout.Y_AXIS));
        navContainer.setOpaque(false);

        // Only Three Elements
        navContainer.add(createNavButton("📊 Dashboard", "DASHBOARD", layout, contentPanel));
        navContainer.add(Box.createVerticalStrut(10));
        navContainer.add(createNavButton("🚗 Vehicles", "VEHICLES", layout, contentPanel));
        navContainer.add(Box.createVerticalStrut(10));
        navContainer.add(createNavButton("🛠 Interventions", "INTERVENTIONS", layout, contentPanel));

        add(navContainer, BorderLayout.CENTER);
    }

    private JButton createNavButton(String text, String cardName, CardLayout layout, JPanel panel) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setForeground(new Color(203, 213, 225));
        button.addActionListener(e -> layout.show(panel, cardName));
        return button;
    }
}