package com.hospital.gui;

import com.hospital.data.DoctorData;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

public class DoctorListPanel extends JPanel {
    private JTable doctorTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;

    public DoctorListPanel() {
        setLayout(new BorderLayout(10, 10)); // Add spacing between components
        setBackground(new Color(240, 248, 255)); // Light blue background

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(new Color(240, 248, 255));

        JLabel searchLabel = new JLabel("Search Doctor:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));

        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createLineBorder(new Color(100, 149, 237), 2, true));
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { search(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { search(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { search(); }
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        // Doctor table
        tableModel = new DefaultTableModel(DoctorData.DOCTORS, DoctorData.DOCTOR_COLUMNS) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        doctorTable = new JTable(tableModel);
        doctorTable.setFont(new Font("Arial", Font.PLAIN, 14));
        doctorTable.setRowHeight(25);
        doctorTable.setSelectionBackground(new Color(173, 216, 230)); // Light blue selection color
        doctorTable.setSelectionForeground(Color.BLACK);

        JTableHeader tableHeader = doctorTable.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 14));
        tableHeader.setBackground(new Color(100, 149, 237)); // Cornflower blue header
        tableHeader.setForeground(Color.WHITE);

        sorter = new TableRowSorter<>(tableModel);
        doctorTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(doctorTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 149, 237), 2, true));

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void search() {
        String text = searchField.getText();
        if (text.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }
}
