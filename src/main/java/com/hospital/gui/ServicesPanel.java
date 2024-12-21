package com.hospital.gui;

import com.hospital.data.ServiceData;
import com.hospital.util.UIUtils;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ServicesPanel extends JPanel {
    private final JTable servicesTable;
    private final DefaultTableModel tableModel;
    private final JTable selectedServicesTable;
    private final DefaultTableModel selectedServicesModel;
    private final JLabel totalAmountLabel;
    private double totalAmount = 0.0;

    public ServicesPanel() {
        this.tableModel = new DefaultTableModel(ServiceData.SERVICES, ServiceData.SERVICE_COLUMNS) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        this.servicesTable = new JTable(tableModel);
        this.selectedServicesModel = new DefaultTableModel(new String[]{"Service ID", "Service Name", "Price (Tk)"}, 0);
        this.selectedServicesTable = new JTable(selectedServicesModel);
        this.totalAmountLabel = new JLabel("Total Amount: Tk 0.00");

        setupPanel();
    }

    private void setupPanel() {
        setLayout(new BorderLayout(10, 10)); // Add spacing between components
        setBackground(new Color(240, 248, 255)); // Light background color

        // Create left and right panels
        JPanel leftPanel = createServicesListPanel();
        JPanel rightPanel = createSelectedServicesPanel();

        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(500);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createServicesListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 248, 255));

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(new Color(240, 248, 255));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel departmentLabel = new JLabel("Filter by Department:");
        departmentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JComboBox<String> departmentComboBox = new JComboBox<>(ServiceData.DEPARTMENTS);
        departmentComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        departmentComboBox.addActionListener(e -> filterServicesByDepartment((String) departmentComboBox.getSelectedItem()));

        filterPanel.add(departmentLabel);
        filterPanel.add(departmentComboBox);
        panel.add(filterPanel, BorderLayout.NORTH);

        // Services Table
        servicesTable.setRowHeight(25);
        servicesTable.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane tableScrollPane = new JScrollPane(servicesTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        // Add Service Button
        JButton addButton = UIUtils.createStyledButton("Add Selected Services");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(100, 149, 237));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> addSelectedServices());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.add(addButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSelectedServicesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(100, 149, 237)), "Selected Services", 0, 0, new Font("Arial", Font.BOLD, 14), new Color(100, 149, 237)));

        // Selected Services Table
        selectedServicesTable.setRowHeight(25);
        selectedServicesTable.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane selectedServicesScrollPane = new JScrollPane(selectedServicesTable);
        panel.add(selectedServicesScrollPane, BorderLayout.CENTER);

        // Total Amount Panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(new Color(240, 248, 255));

        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalAmountLabel.setForeground(new Color(34, 139, 34));
        totalPanel.add(totalAmountLabel);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton removeButton = UIUtils.createStyledButton("Remove Selected");
        removeButton.setFont(new Font("Arial", Font.BOLD, 14));
        removeButton.setBackground(new Color(255, 69, 0));
        removeButton.setForeground(Color.WHITE);
        removeButton.addActionListener(e -> removeSelectedService());
        buttonPanel.add(removeButton);

        JButton discountButton = UIUtils.createStyledButton("Apply Discount");
        discountButton.setFont(new Font("Arial", Font.BOLD, 14));
        discountButton.setBackground(new Color(100, 149, 237));
        discountButton.setForeground(Color.WHITE);
        discountButton.addActionListener(e -> applyDiscount());
        buttonPanel.add(discountButton);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(totalPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addSelectedServices() {
        int[] selectedRows = servicesTable.getSelectedRows();
        if (selectedRows.length > 0) {
            for (int row : selectedRows) {
                row = servicesTable.convertRowIndexToModel(row);
                String serviceId = (String) tableModel.getValueAt(row, 0);
                String serviceName = (String) tableModel.getValueAt(row, 1);
                String price = (String) tableModel.getValueAt(row, 3);

                // Avoid adding duplicates
                if (!isServiceAlreadyAdded(serviceId)) {
                    selectedServicesModel.addRow(new Object[]{serviceId, serviceName, price});
                }
            }
            updateTotalAmount();
        } else {
            JOptionPane.showMessageDialog(this, "Please select at least one service to add.");
        }
    }

    private boolean isServiceAlreadyAdded(String serviceId) {
        for (int i = 0; i < selectedServicesModel.getRowCount(); i++) {
            if (serviceId.equals(selectedServicesModel.getValueAt(i, 0))) {
                return true;
            }
        }
        return false;
    }

    private void removeSelectedService() {
        int selectedRow = selectedServicesTable.getSelectedRow();
        if (selectedRow != -1) {
            selectedServicesModel.removeRow(selectedRow);
            updateTotalAmount();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a service to remove.");
        }
    }

    private void updateTotalAmount() {
        totalAmount = 0.0;
        for (int i = 0; i < selectedServicesModel.getRowCount(); i++) {
            String price = (String) selectedServicesModel.getValueAt(i, 2);
            totalAmount += Double.parseDouble(price.replace(",", ""));
        }
        totalAmountLabel.setText(String.format("Total Amount: Tk %.2f", totalAmount));
    }

    private void applyDiscount() {
        String discountInput = JOptionPane.showInputDialog(this, "Enter discount percentage:", "Apply Discount", JOptionPane.PLAIN_MESSAGE);
        if (discountInput != null && !discountInput.isEmpty()) {
            try {
                double discount = Double.parseDouble(discountInput);
                if (discount < 0 || discount > 100) {
                    JOptionPane.showMessageDialog(this, "Enter a valid discount percentage (0-100).");
                } else {
                    double discountedAmount = totalAmount * (1 - discount / 100);
                    totalAmountLabel.setText(String.format("Total Amount (After %.2f%% Discount): Tk %.2f", discount, discountedAmount));
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid discount value.");
            }
        }
    }

    private void filterServicesByDepartment(String department) {
        tableModel.setRowCount(0);
        for (Object[] service : ServiceData.SERVICES) {
            if ("All Departments".equals(department) || service[2].equals(department)) {
                tableModel.addRow(service);
            }
        }
    }
}
