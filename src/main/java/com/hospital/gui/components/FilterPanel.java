package com.hospital.gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class FilterPanel extends JPanel {
    private final JComboBox<String> filterCombo;
    
    public FilterPanel(String labelText, String[] items, ActionListener filterAction) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(new JLabel(labelText));
        
        filterCombo = new JComboBox<>(items);
        filterCombo.addActionListener(filterAction);
        add(filterCombo);
    }
    
    public String getSelectedItem() {
        return (String) filterCombo.getSelectedItem();
    }
}