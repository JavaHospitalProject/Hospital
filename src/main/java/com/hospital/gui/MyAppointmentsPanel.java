package com.hospital.gui;

import com.hospital.auth.SessionManager;
import com.hospital.data.DoctorData;
import com.hospital.model.Appointment;
import com.hospital.model.AppointmentManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MyAppointmentsPanel extends JPanel {
    private final JTable appointmentsTable;
    private final DefaultTableModel tableModel;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final JComboBox<String> statusFilterCombo;
    private TableRowSorter<DefaultTableModel> sorter;

    public MyAppointmentsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245));

        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(new Color(240, 242, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(240, 242, 245));

        JLabel titleLabel = new JLabel("My Appointments");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // Button to go back to booking
        JButton bookButton = createStyledButton("Book New Appointment", new Color(37, 99, 235));
        bookButton.addActionListener(e -> {
            Container parent = getParent();
            if (parent != null && parent.getLayout() instanceof CardLayout) {
                CardLayout layout = (CardLayout) parent.getLayout();
                layout.show(parent, "Appointment");
            }
        });
        titlePanel.add(bookButton, BorderLayout.EAST);

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(new Color(240, 242, 245));

        // Status Filter
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        String[] statuses = {"All Status", "SCHEDULED", "COMPLETED", "CANCELLED"};
        statusFilterCombo = new JComboBox<>(statuses);
        statusFilterCombo.setPreferredSize(new Dimension(150, 30));
        statusFilterCombo.addActionListener(e -> applyFilters());

        filterPanel.add(statusLabel);
        filterPanel.add(statusFilterCombo);

        headerPanel.add(titlePanel, BorderLayout.NORTH);
        headerPanel.add(filterPanel, BorderLayout.CENTER);

        // Create table
        String[] columns = {
            "Date", "Doctor", "Specialization", 
            "Serial No.", "Token", "Status"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        appointmentsTable = new JTable(tableModel);
        appointmentsTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        appointmentsTable.setRowHeight(35);
        appointmentsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        appointmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        appointmentsTable.setShowGrid(true);
        appointmentsTable.setGridColor(new Color(230, 230, 230));

        // Set up sorting
        sorter = new TableRowSorter<>(tableModel);
        appointmentsTable.setRowSorter(sorter);

        // Style the table
        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(new Color(240, 242, 245));
        
        JButton refreshButton = createStyledButton("Refresh", new Color(37, 99, 235));
        JButton cancelButton = createStyledButton("Cancel Selected", new Color(220, 38, 38));
        
        refreshButton.addActionListener(e -> refreshAppointments());
        cancelButton.addActionListener(e -> cancelSelectedAppointment());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(cancelButton);

        // Add components to panel
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Initial load
        refreshAppointments();
    }

    private void applyFilters() {
        RowFilter<DefaultTableModel, Object> statusFilter = null;
        String selectedStatus = (String) statusFilterCombo.getSelectedItem();

        if (selectedStatus != null && !selectedStatus.equals("All Status")) {
            statusFilter = RowFilter.regexFilter(selectedStatus, 5); // Status column index updated
        }

        sorter.setRowFilter(statusFilter);
    }

    public void refreshAppointments() {
        tableModel.setRowCount(0);
        String currentUser = SessionManager.getInstance().getCurrentUser().getUsername();
        List<Appointment> appointments = AppointmentManager.getInstance()
            .getPatientAppointments(currentUser);

        for (Appointment appointment : appointments) {
            Object[] doctorInfo = findDoctorInfo(appointment.getDoctorId());
            
            Object[] row = {
                appointment.getDateTime().format(dateFormatter),
                doctorInfo[1],  // Doctor name
                doctorInfo[2],  // Specialization
                appointment.getSerialNumber(),
                appointment.getSecretToken(),
                appointment.getStatus()
            };
            tableModel.addRow(row);
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

    private void cancelSelectedAppointment() {
        int selectedRow = appointmentsTable.getSelectedRow();
        if (selectedRow >= 0) {
            selectedRow = appointmentsTable.convertRowIndexToModel(selectedRow);
            String status = (String) tableModel.getValueAt(selectedRow, 5); // Status column index updated
            if (status.equals("CANCELLED")) {
                showMessage("This appointment is already cancelled", "Cannot Cancel", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (status.equals("COMPLETED")) {
                showMessage("Cannot cancel a completed appointment", "Cannot Cancel", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this appointment?",
                "Cancel Appointment",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
                
            if (confirm == JOptionPane.YES_OPTION) {
                String token = (String) tableModel.getValueAt(selectedRow, 4); // Token column index updated
                String currentUser = SessionManager.getInstance().getCurrentUser().getUsername();
                
                List<Appointment> appointments = AppointmentManager.getInstance()
                    .getPatientAppointments(currentUser);
                
                appointments.stream()
                    .filter(a -> a.getSecretToken().equals(token))
                    .findFirst()
                    .ifPresent(appointment -> {
                        AppointmentManager.getInstance().cancelAppointment(appointment.getAppointmentId());
                        refreshAppointments();
                        showMessage("Appointment cancelled successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    });
            }
        } else {
            showMessage("Please select an appointment to cancel", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 35));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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