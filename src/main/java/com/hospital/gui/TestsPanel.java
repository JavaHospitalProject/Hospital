package com.hospital.gui;

import com.hospital.data.TestData;
import com.hospital.util.UIUtils;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TestsPanel extends JPanel {
    private JTable testTable;
    private DefaultTableModel tableModel;
    private JTable selectedTestsTable;
    private DefaultTableModel selectedTestsModel;
    private JLabel totalAmountLabel;
    private double totalAmount = 0.0;

    public TestsPanel() {
        setLayout(new BorderLayout(10, 10)); // Add spacing between components
        setBackground(new Color(240, 248, 255)); // Light background color

        // Left Panel - Test Categories and Test List
        JPanel leftPanel = createLeftPanel();

        // Right Panel - Selected Tests
        JPanel rightPanel = createRightPanel();

        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(500);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);

        add(splitPane, BorderLayout.CENTER);

        // Populate the table initially
        filterTestsByCategory("All Tests");
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 248, 255));

        // Category Selection Panel
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryPanel.setBackground(new Color(240, 248, 255));
        categoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel categoryLabel = new JLabel("Category: ");
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JComboBox<String> categoryCombo = new JComboBox<>(TestData.TEST_CATEGORIES);
        categoryCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        categoryCombo.setSelectedItem("All Tests"); // Default selection is "All Tests"
        categoryCombo.addActionListener(e -> filterTestsByCategory((String) categoryCombo.getSelectedItem()));

        categoryPanel.add(categoryLabel);
        categoryPanel.add(categoryCombo);

        // Test Table
        tableModel = new DefaultTableModel(TestData.TESTS, TestData.TEST_COLUMNS) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        testTable = new JTable(tableModel);
        testTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); // Enable multi-row selection
        testTable.setFont(new Font("Arial", Font.PLAIN, 14));
        testTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(testTable);

        // Add Mouse Listener for Ctrl + Right-Click
        testTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isControlDown() && SwingUtilities.isRightMouseButton(e)) {
                    addSelectedTests();
                }
            }
        });

        // Add Test Button
        JButton addButton = UIUtils.createStyledButton("Add Selected Test");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(100, 149, 237));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> addSelectedTests());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.add(addButton);

        panel.add(categoryPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(100, 149, 237)), "Selected Tests", 0, 0, new Font("Arial", Font.BOLD, 14), new Color(100, 149, 237)));

        // Selected Tests Table
        selectedTestsModel = new DefaultTableModel(new String[]{"Test ID", "Test Name", "Price (Tk)"}, 0);
        selectedTestsTable = new JTable(selectedTestsModel);
        selectedTestsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        selectedTestsTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(selectedTestsTable);

        // Total Amount Panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(new Color(240, 248, 255));

        totalAmountLabel = new JLabel("Total Amount: Tk 0.00");
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
        removeButton.addActionListener(e -> removeSelectedTest());
        buttonPanel.add(removeButton);

        JButton discountButton = UIUtils.createStyledButton("Apply Discount");
        discountButton.setFont(new Font("Arial", Font.BOLD, 14));
        discountButton.setBackground(new Color(100, 149, 237));
        discountButton.setForeground(Color.WHITE);
        discountButton.addActionListener(e -> applyDiscount());
        buttonPanel.add(discountButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(totalPanel, BorderLayout.SOUTH);
        panel.add(buttonPanel, BorderLayout.NORTH);

        return panel;
    }

    private void addSelectedTests() {
        int[] selectedRows = testTable.getSelectedRows();
        if (selectedRows.length > 0) {
            for (int selectedRow : selectedRows) {
                selectedRow = testTable.convertRowIndexToModel(selectedRow);
                String testId = (String) testTable.getValueAt(selectedRow, 0);
                String testName = (String) testTable.getValueAt(selectedRow, 1);
                String price = (String) testTable.getValueAt(selectedRow, 3);

                // Check if test is already added
                if (!isTestAlreadyAdded(testId)) {
                    selectedTestsModel.addRow(new Object[]{testId, testName, price});
                }
            }
            updateTotalAmount();
        } else {
            JOptionPane.showMessageDialog(this, "Please select at least one test to add.");
        }
    }

    private boolean isTestAlreadyAdded(String testId) {
        for (int i = 0; i < selectedTestsModel.getRowCount(); i++) {
            if (testId.equals(selectedTestsModel.getValueAt(i, 0))) {
                return true;
            }
        }
        return false;
    }

    private void removeSelectedTest() {
        int selectedRow = selectedTestsTable.getSelectedRow();
        if (selectedRow != -1) {
            selectedTestsModel.removeRow(selectedRow);
            updateTotalAmount();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a test to remove.");
        }
    }

    private void updateTotalAmount() {
        totalAmount = 0.0;
        for (int i = 0; i < selectedTestsModel.getRowCount(); i++) {
            String price = (String) selectedTestsModel.getValueAt(i, 2);
            totalAmount += Double.parseDouble(price.replace(",", ""));
        }
        totalAmountLabel.setText(String.format("Total Amount: Tk %.2f", totalAmount));
    }

    private void applyDiscount() {
        String discountInput = JOptionPane.showInputDialog(this, "Enter discount percentage:", "Apply Discount", JOptionPane.PLAIN_MESSAGE);
        if (discountInput != null && !discountInput.isEmpty()) {
            try {
                double discountPercentage = Double.parseDouble(discountInput);
                if (discountPercentage < 0 || discountPercentage > 100) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid discount percentage (0-100).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                } else {
                    double discountedAmount = totalAmount * (1 - discountPercentage / 100);
                    totalAmountLabel.setText(String.format("Total Amount (After %.2f%% Discount): Tk %.2f", discountPercentage, discountedAmount));
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for the discount percentage.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void filterTestsByCategory(String category) {
        tableModel.setRowCount(0); // Clear the table

        // Populate the table with all tests if "All Tests" is selected
        for (Object[] test : TestData.TESTS) {
            if ("All Tests".equalsIgnoreCase(category) || test[2].equals(category)) {
                tableModel.addRow(test);
            }
        }
    }
}
