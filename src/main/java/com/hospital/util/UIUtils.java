package com.hospital.util;

import javax.swing.*;
import java.awt.*;

public class UIUtils {
    public static void setUIFont() {
        try {
            // Set Bangla compatible font
            Font banglaFont = new Font("SolaimanLipi", Font.PLAIN, 14);
            UIManager.put("Button.font", banglaFont);
            UIManager.put("Label.font", banglaFont);
            UIManager.put("TextField.font", banglaFont);
            UIManager.put("ComboBox.font", banglaFont);
            UIManager.put("Table.font", banglaFont);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }

    public static void addHoverEffect(JButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(button.getBackground().darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(button.getBackground().brighter());
            }
        });
    }
}