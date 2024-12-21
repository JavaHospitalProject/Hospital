package com.hospital.gui;

import com.hospital.data.DoctorData;
import com.hospital.model.Appointment;
import com.hospital.model.AppointmentManager;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class AdminAppointmentsPanel extends JPanel {
    private final JTable appointmentsTable;
    private final DefaultTableModel tableModel;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
    private final JLabel totalAppointmentsLabel;
    private final JComboBox<String> doctorFilterCombo;
    private final JComboBox<String> statusFilterCombo;
    private TableRowSorter<DefaultTableModel> sorter;

    public AdminAppointmentsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245));

        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 242, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title and total appointments
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(240, 242, 245));

        JLabel titleLabel = new JLabel("Appointments Dashboard");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        totalAppointmentsLabel = new JLabel("Total Appointments: 0");
        totalAppointmentsLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titlePanel.add(totalAppointmentsLabel, BorderLayout.EAST);

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(new Color(240, 242, 245));

        // Doctor Filter
        JLabel doctorLabel = new JLabel("Doctor:");
        doctorFilterCombo = new JComboBox<>(getDoctorNames());
        doctorFilterCombo.insertItemAt("All Doctors", 0);
        doctorFilterCombo.setSelectedIndex(0);
        doctorFilterCombo.addActionListener(e -> applyFilters());

        // Status Filter
        JLabel statusLabel = new JLabel("Status:");
        String[] statuses = {"All Status", "SCHEDULED", "COMPLETED", "CANCELLED"};
        statusFilterCombo = new JComboBox<>(statuses);
        statusFilterCombo.addActionListener(e -> applyFilters());

        filterPanel.add(doctorLabel);
        filterPanel.add(doctorFilterCombo);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(statusLabel);
        filterPanel.add(statusFilterCombo);

        headerPanel.add(titlePanel, BorderLayout.NORTH);
        headerPanel.add(filterPanel, BorderLayout.CENTER);

        // Create table
        String[] columns = {
            "Date", "Time", "Doctor", "Specialization", "Patient Name",
            "Contact", "Serial No.", "Token", "Status"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        appointmentsTable = new JTable(tableModel);
        appointmentsTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        appointmentsTable.setRowHeight(30);
        appointmentsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        // Set up sorting
        sorter = new TableRowSorter<>(tableModel);
        appointmentsTable.setRowSorter(sorter);

        // Style the table
        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        // Create action panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(new Color(240, 242, 245));

        JButton refreshButton = createStyledButton("Refresh", new Color(37, 99, 235));
        JButton completeButton = createStyledButton("Mark Complete", new Color(21, 128, 61));
        JButton cancelButton = createStyledButton("Cancel Selected", new Color(220, 38, 38));

        refreshButton.addActionListener(e -> refreshAppointments());
        completeButton.addActionListener(e -> markAppointmentComplete());
        cancelButton.addActionListener(e -> cancelSelectedAppointment());

        actionPanel.add(refreshButton);
        actionPanel.add(completeButton);
        actionPanel.add(cancelButton);

        // Add components to panel
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        // Initial load
        refreshAppointments();
    }

    private String[] getDoctorNames() {
        String[] names = new String[DoctorData.DOCTORS.length];
        for (int i = 0; i < DoctorData.DOCTORS.length; i++) {
            names[i] = (String) DoctorData.DOCTORS[i][1];
        }
        return names;
    }

    public void refreshAppointments() {
        tableModel.setRowCount(0); // Clear the table
        List<Appointment> appointments = AppointmentManager.getInstance().getAllAppointments();

        for (Appointment appointment : appointments) {
            Object[] doctorInfo = findDoctorInfo(appointment.getDoctorId());

            Object[] row = {
                appointment.getDateTime().format(dateFormatter),
                appointment.getDateTime().format(timeFormatter),
                doctorInfo[1],  // Doctor name
                doctorInfo[2],  // Specialization
                appointment.getPatientName(),
                appointment.getPatientPhone(),
                appointment.getSerialNumber(),
                appointment.getSecretToken(),
                appointment.getStatus()
            };
            tableModel.addRow(row);
        }

        totalAppointmentsLabel.setText("Total Appointments: " + appointments.size());
    }

    private void applyFilters() {
        RowFilter<DefaultTableModel, Object> doctorFilter = null;
        RowFilter<DefaultTableModel, Object> statusFilter = null;

        String selectedDoctor = (String) doctorFilterCombo.getSelectedItem();
        String selectedStatus = (String) statusFilterCombo.getSelectedItem();

        if (selectedDoctor != null && !selectedDoctor.equals("All Doctors")) {
            doctorFilter = RowFilter.regexFilter(selectedDoctor, 2); // Doctor name column
        }

        if (selectedStatus != null && !selectedStatus.equals("All Status")) {
            statusFilter = RowFilter.regexFilter(selectedStatus, 8); // Status column
        }

        if (doctorFilter != null && statusFilter != null) {
            sorter.setRowFilter(RowFilter.andFilter(List.of(doctorFilter, statusFilter)));
        } else if (doctorFilter != null) {
            sorter.setRowFilter(doctorFilter);
        } else if (statusFilter != null) {
            sorter.setRowFilter(statusFilter);
        } else {
            sorter.setRowFilter(null);
        }
    }

    private Object[] findDoctorInfo(String doctorId) {
        for (Object[] doctor : DoctorData.DOCTORS) {
            if (doctor[0].equals(doctorId)) {
                return doctor;
            }
        }
        return new Object[]{"Unknown", "Unknown", "Unknown"};
    }

    private void markAppointmentComplete() {
        updateAppointmentStatus("COMPLETED");
    }

    private void cancelSelectedAppointment() {
        updateAppointmentStatus("CANCELLED");
    }

    private void updateAppointmentStatus(String newStatus) {
        int selectedRow = appointmentsTable.getSelectedRow();
        if (selectedRow >= 0) {
            selectedRow = appointmentsTable.convertRowIndexToModel(selectedRow);
            String currentStatus = (String) tableModel.getValueAt(selectedRow, 8);

            if (currentStatus.equals(newStatus)) {
                JOptionPane.showMessageDialog(this,
                    "Appointment is already " + newStatus.toLowerCase(),
                    "Status Update",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            String token = (String) tableModel.getValueAt(selectedRow, 7);
            List<Appointment> appointments = AppointmentManager.getInstance().getAllAppointments();

            appointments.stream()
                .filter(a -> a.getSecretToken().equals(token))
                .findFirst()
                .ifPresent(appointment -> {
                    appointment.setStatus(newStatus);
                    refreshAppointments();
                    JOptionPane.showMessageDialog(this,
                        "Appointment status updated to " + newStatus,
                        "Status Update",
                        JOptionPane.INFORMATION_MESSAGE);
                });
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select an appointment",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(130, 35));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }
}
