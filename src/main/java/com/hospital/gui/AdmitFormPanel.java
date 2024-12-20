package com.hospital.gui;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class AdmitFormPanel extends JPanel {
    private Map<String, JComponent> formFields;
    private Map<String, ButtonGroup> buttonGroups;
    
    public AdmitFormPanel() {
        formFields = new HashMap<>();
        buttonGroups = new HashMap<>();
        
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));
        setPreferredSize(new Dimension(800, 600));
        
        // Create main container
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(Color.WHITE);
        containerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create form panel
        JPanel formPanel = createFormPanel();
        
        // Create buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton submitButton = new JButton("Submit");
        JButton clearButton = new JButton("Clear");
        submitButton.setPreferredSize(new Dimension(100, 32));
        clearButton.setPreferredSize(new Dimension(100, 32));
        
        // Add clear button action listener
        clearButton.addActionListener(e -> {
            // Clear all text fields and text areas
            for (JComponent component : formFields.values()) {
                if (component instanceof JTextField) {
                    ((JTextField) component).setText("");
                } else if (component instanceof JTextArea) {
                    ((JTextArea) component).setText("");
                } else if (component instanceof JComboBox) {
                    ((JComboBox<?>) component).setSelectedIndex(0);
                }
            }
            
            // Clear all button groups
            for (ButtonGroup group : buttonGroups.values()) {
                group.clearSelection();
            }
        });
        
        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        
        // Add panels to container
        containerPanel.add(formPanel, BorderLayout.CENTER);
        containerPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(containerPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Patient Information
        addSectionHeader(formPanel, gbc, row++, "Patient Information");
        row = addPatientInfoFields(formPanel, gbc, row);
        
        // Emergency Contact
        addSectionHeader(formPanel, gbc, row++, "Emergency Contact");
        row = addEmergencyContactFields(formPanel, gbc, row);
        
        // Admission Details
        addSectionHeader(formPanel, gbc, row++, "Admission Details");
        row = addAdmissionDetailsFields(formPanel, gbc, row);
        
        // Insurance Information
        addSectionHeader(formPanel, gbc, row++, "Insurance Information");
        row = addInsuranceFields(formPanel, gbc, row);
        
        // Medical Information
        addSectionHeader(formPanel, gbc, row++, "Medical Information");
        addMedicalInfoFields(formPanel, gbc, row);
        
        return formPanel;
    }
    
    private void addTextField(JPanel panel, GridBagConstraints gbc, int row, String label, String fieldName, int width) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(width, 28));
        formFields.put(fieldName, field);
        addFormField(panel, gbc, row, label, field);
    }
    
    private void addTextArea(JPanel panel, GridBagConstraints gbc, int row, String label, String fieldName, int rows, int cols) {
        JTextArea area = new JTextArea(rows, cols);
        formFields.put(fieldName, area);
        addFormField(panel, gbc, row, label, area);
    }
    
    private ButtonGroup addRadioButtons(JPanel panel, String groupName, String... options) {
        ButtonGroup group = new ButtonGroup();
        
        for (String option : options) {
            JRadioButton button = new JRadioButton(option);
            button.setBackground(Color.WHITE);
            group.add(button);
            panel.add(button);
        }
        
        buttonGroups.put(groupName, group);
        return group;
    }
    
    private int addPatientInfoFields(JPanel panel, GridBagConstraints gbc, int row) {
        addTextField(panel, gbc, row++, "Patient Name:", "patientName", 250);
        addTextField(panel, gbc, row++, "Age:", "age", 80);
        
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        genderPanel.setBackground(Color.WHITE);
        addRadioButtons(genderPanel, "gender", "Male", "Female");
        addFormField(panel, gbc, row++, "Gender:", genderPanel);
        
        addTextField(panel, gbc, row++, "Date of Birth:", "dob", 150);
        addTextField(panel, gbc, row++, "Contact Number:", "contact", 150);
        addTextField(panel, gbc, row++, "Email:", "email", 250);
        addTextArea(panel, gbc, row++, "Address:", "address", 3, 30);
        
        return row;
    }
    
    private int addEmergencyContactFields(JPanel panel, GridBagConstraints gbc, int row) {
        addTextField(panel, gbc, row++, "Emergency Contact Name:", "emergencyName", 250);
        addTextField(panel, gbc, row++, "Emergency Contact Number:", "emergencyContact", 150);
        addTextField(panel, gbc, row++, "Relationship to Emergency Contact:", "relationship", 150);
        return row;
    }
    
    private int addAdmissionDetailsFields(JPanel panel, GridBagConstraints gbc, int row) {
        JPanel patientTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        patientTypePanel.setBackground(Color.WHITE);
        addRadioButtons(patientTypePanel, "patientType", "Inpatient", "Outpatient");
        addFormField(panel, gbc, row++, "Patient Type:", patientTypePanel);
        
        JPanel roomTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        roomTypePanel.setBackground(Color.WHITE);
        addRadioButtons(roomTypePanel, "roomType", "Cabin", "General Ward", "ICU", "Special Ward");
        addFormField(panel, gbc, row++, "Room Type:", roomTypePanel);
        
        addTextField(panel, gbc, row++, "Admission Date:", "admissionDate", 150);
        addTextField(panel, gbc, row++, "Admission Time:", "admissionTime", 150);
        
        JComboBox<String> doctorCombo = new JComboBox<>(new String[]{
            "Dr. Mahmudul Hasan", "Dr. Shahnaz Parvin", "Dr. Tanvir Ahmed", "Dr. Rina Begum"
        });
        doctorCombo.setPreferredSize(new Dimension(250, 28));
        formFields.put("doctor", doctorCombo);
        addFormField(panel, gbc, row++, "Attending Doctor:", doctorCombo);
        
        return row;
    }
    
    private int addInsuranceFields(JPanel panel, GridBagConstraints gbc, int row) {
        addTextField(panel, gbc, row++, "Insurance Provider:", "provider", 250);
        addTextField(panel, gbc, row++, "Policy Number:", "policy", 150);
        return row;
    }
    
    private void addMedicalInfoFields(JPanel panel, GridBagConstraints gbc, int row) {
        addTextField(panel, gbc, row++, "Known Allergies:", "allergies", 250);
        addTextField(panel, gbc, row++, "Chronic Illnesses:", "illnesses", 250);
        addTextField(panel, gbc, row++, "Special Requirements:", "requirements", 250);
    }
    
    private void addSectionHeader(JPanel panel, GridBagConstraints gbc, int row, String text) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 8, 5);
        
        JLabel headerLabel = new JLabel(text);
        headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD, 14f));
        headerLabel.setForeground(new Color(51, 51, 51));
        panel.add(headerLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.0;
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setPreferredSize(new Dimension(200, 28));
        labelComponent.setForeground(new Color(51, 51, 51));
        panel.add(labelComponent, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        
        if (field instanceof JTextArea) {
            JScrollPane scrollPane = new JScrollPane(field);
            scrollPane.setPreferredSize(new Dimension(250, 75));
            panel.add(scrollPane, gbc);
        } else {
            panel.add(field, gbc);
        }
    }
}