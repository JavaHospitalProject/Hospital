package com.hospital.gui;

import com.hospital.data.ServiceData;
import com.hospital.gui.components.FilterPanel;
import com.hospital.util.UIUtils;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class ServicesPanel extends JPanel {
    private final JTable servicesTable;
    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final JLabel totalAmountLabel;
    private final DefaultTableModel selectedServicesModel;
    private final JTable selectedServicesTable;
    private double totalAmount = 0.0;

    public ServicesPanel() {
        // Initialize all final fields first
        this.tableModel = new DefaultTableModel(ServiceData.SERVICES, ServiceData.SERVICE_COLUMNS) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        this.servicesTable = new JTable(tableModel);
        this.sorter = new TableRowSorter<>(tableModel);
        servicesTable.setRowSorter(sorter);
        
        String[] selectedColumns = {"Service ID", "Service Name", "Price (Tk)"};
        this.selectedServicesModel = new DefaultTableModel(selectedColumns, 0);
        this.selectedServicesTable = new JTable(selectedServicesModel);
        this.totalAmountLabel = new JLabel("Total Amount: Tk 0.00");

        // Setup the panel
        setupPanel();
    }

    private void setupPanel() {
        setLayout(new BorderLayout());

        // Main split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);

        // Left panel - Available services
        splitPane.setLeftComponent(createServicesListPanel());

        // Right panel - Selected services
        splitPane.setRightComponent(createSelectedServicesPanel());

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createServicesListPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Filter panel
        FilterPanel filterPanel = new FilterPanel(
            "Filter by Department: ",
            ServiceData.DEPARTMENTS,
            e -> filterServices()
        );
        panel.add(filterPanel, BorderLayout.NORTH);

        // Services table
        JScrollPane scrollPane = new JScrollPane(servicesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add service button
        JButton addButton = UIUtils.createStyledButton("Add Selected Service");
        addButton.addActionListener(e -> addSelectedService());
        panel.add(addButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSelectedServicesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Selected Services"));

        // Selected services table
        JScrollPane scrollPane = new JScrollPane(selectedServicesTable);

        // Total amount panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.add(totalAmountLabel);

        // Remove button
        JButton removeButton = UIUtils.createStyledButton("Remove Selected");
        removeButton.addActionListener(e -> removeSelectedService());

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(totalPanel, BorderLayout.SOUTH);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(removeButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        return panel;
    }

    private void filterServices() {
        String department = (String) ((JComboBox<?>) ((JPanel) getComponent(0)).getComponent(1)).getSelectedItem();
        if (department.equals("All Departments")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(department, 2));
        }
    }

    private void addSelectedService() {
        int selectedRow = servicesTable.getSelectedRow();
        if (selectedRow != -1) {
            selectedRow = servicesTable.convertRowIndexToModel(selectedRow);
            String serviceId = (String) tableModel.getValueAt(selectedRow, 0);
            String serviceName = (String) tableModel.getValueAt(selectedRow, 1);
            String price = (String) tableModel.getValueAt(selectedRow, 3);

            // Check if service is already added
            for (int i = 0; i < selectedServicesModel.getRowCount(); i++) {
                if (serviceId.equals(selectedServicesModel.getValueAt(i, 0))) {
                    JOptionPane.showMessageDialog(this, "Service already added!");
                    return;
                }
            }

            selectedServicesModel.addRow(new Object[]{serviceId, serviceName, price});
            updateTotalAmount();
        }
    }

    private void removeSelectedService() {
        int selectedRow = selectedServicesTable.getSelectedRow();
        if (selectedRow != -1) {
            selectedServicesModel.removeRow(selectedRow);
            updateTotalAmount();
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
}