package com.hospital.gui;

import com.hospital.auth.SessionManager;
import com.hospital.auth.UserAccount;
import com.hospital.data.DoctorData;
import com.hospital.model.AppointmentManager;
import com.hospital.model.TimeSlot;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class AppointmentPanel extends JPanel {
    private final JTextField patientNameField;
    private final JTextField contactNumberField;
    private final JComboBox<String> doctorDropdown;
    private final JTextField dateField;
    private final JComboBox<TimeSlot> timeDropdown;
    private final JTextArea purposeField;
    private final CardLayout cardLayout;
    private final JPanel contentPanel;
    private MyAppointmentsPanel myAppointmentsPanel;

    public AppointmentPanel() {
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Booking Form Panel
        JPanel bookingPanel = new JPanel(new BorderLayout(20, 20));
        bookingPanel.setBackground(new Color(240, 242, 245));
        bookingPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(240, 242, 245));
        
        JLabel titleLabel = new JLabel("Book Appointment");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 242, 245));
        JButton viewAppointmentsButton = createStyledButton("View My Appointments", new Color(37, 99, 235));
        viewAppointmentsButton.addActionListener(e -> cardLayout.show(contentPanel, "MyAppointments"));
        buttonPanel.add(viewAppointmentsButton);
        
        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(buttonPanel, BorderLayout.EAST);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Get current user info
        UserAccount currentUser = SessionManager.getInstance().getCurrentUser();
        
        // Initialize form fields
        patientNameField = createStyledTextField(20);
        patientNameField.setText(currentUser.getFullName());
        addFormField(formPanel, gbc, "Patient Name", patientNameField);

        contactNumberField = createStyledTextField(15);
        contactNumberField.setText(currentUser.getPhoneNumber());
        addFormField(formPanel, gbc, "Contact Number", contactNumberField);

        doctorDropdown = createStyledComboBox(getDoctorNames());
        addFormField(formPanel, gbc, "Select Doctor", doctorDropdown);

        dateField = createStyledTextField(10);
        addFormField(formPanel, gbc, "Appointment Date (dd-MM-yyyy)", dateField);

        timeDropdown = new JComboBox<>(TimeSlot.getDefaultTimeSlots());
        timeDropdown.setPreferredSize(new Dimension(300, 35));
        timeDropdown.setBackground(Color.WHITE);
        timeDropdown.setFont(new Font("SansSerif", Font.PLAIN, 14));
        addFormField(formPanel, gbc, "Select Time Slot", timeDropdown);

        purposeField = createStyledTextArea(3, 20);
        addFormField(formPanel, gbc, "Purpose of Visit", new JScrollPane(purposeField));

        // Button Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setBackground(Color.WHITE);
        
        JButton clearButton = createStyledButton("Clear", new Color(107, 114, 128));
        JButton bookButton = createStyledButton("Book Appointment", new Color(37, 99, 235));

        actionPanel.add(clearButton);
        actionPanel.add(bookButton);

        // Add action listeners
        clearButton.addActionListener(e -> handleClearForm());
        bookButton.addActionListener(e -> handleBookAppointment());

        // Add components to booking panel
        bookingPanel.add(titlePanel, BorderLayout.NORTH);
        bookingPanel.add(formPanel, BorderLayout.CENTER);
        bookingPanel.add(actionPanel, BorderLayout.SOUTH);

        // Create MyAppointmentsPanel
        myAppointmentsPanel = new MyAppointmentsPanel();

        // Add panels to card layout
        contentPanel.add(bookingPanel, "booking");
        contentPanel.add(myAppointmentsPanel, "MyAppointments");

        // Add content panel to main panel
        add(contentPanel, BorderLayout.CENTER);
        
        // Show booking panel by default
        cardLayout.show(contentPanel, "booking");
    }

    private void handleBookAppointment() {
        try {
            String patientName = patientNameField.getText().trim();
            String contactNumber = contactNumberField.getText().trim();
            String selectedDoctor = (String) doctorDropdown.getSelectedItem();
            String dateText = dateField.getText().trim();

            // Validation
            if (patientName.isEmpty() || contactNumber.isEmpty() || dateText.isEmpty()) {
                showError("All fields are required");
                return;
            }

            // Parse date
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate date = LocalDate.parse(dateText, dateFormatter);

            // Get time directly from TimeSlot object
            TimeSlot selectedTimeSlot = (TimeSlot) timeDropdown.getSelectedItem();
            if (selectedTimeSlot == null) {
                showError("Please select a time slot");
                return;
            }

            // Combine date and time
            LocalDateTime dateTime = LocalDateTime.of(date, selectedTimeSlot.getTime());

            // Check if date is in the past
            if (dateTime.isBefore(LocalDateTime.now())) {
                showError("Cannot book appointments in the past");
                return;
            }

            // Get doctor ID
            String doctorId = getDoctorId(selectedDoctor);
            if (doctorId == null) {
                showError("Invalid doctor selection");
                return;
            }

            String username = SessionManager.getInstance().getCurrentUser().getUsername();

            // Create appointment
            AppointmentManager.getInstance().createAppointment(
                doctorId,
                username,
                patientName,
                contactNumber,
                dateTime
            );

            JOptionPane.showMessageDialog(this,
                "Appointment booked successfully!\nYour appointment is on " +
                date.format(dateFormatter) + " at " + selectedTimeSlot,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            myAppointmentsPanel.refreshAppointments();
            cardLayout.show(contentPanel, "MyAppointments");

        } catch (DateTimeParseException e) {
            showError("Invalid date format. Please use dd-MM-yyyy");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void handleClearForm() {
        UserAccount currentUser = SessionManager.getInstance().getCurrentUser();
        patientNameField.setText(currentUser.getFullName());
        contactNumberField.setText(currentUser.getPhoneNumber());
        dateField.setText("");
        timeDropdown.setSelectedIndex(0);
        doctorDropdown.setSelectedIndex(0);
        purposeField.setText("");
    }

    private String getDoctorId(String doctorName) {
        // Extract the doctor name without specialization
        int index = doctorName.indexOf(" (");
        String name = index > 0 ? doctorName.substring(0, index) : doctorName;
        
        for (Object[] doctor : DoctorData.DOCTORS) {
            if (doctor[1].equals(name)) {
                return (String) doctor[0];
            }
        }
        return null;
    }

    private String[] getDoctorNames() {
        List<String> names = new ArrayList<>();
        for (Object[] doctor : DoctorData.DOCTORS) {
            names.add(doctor[1] + " (" + doctor[2] + ")");
        }
        return names.toArray(new String[0]);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return field;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setPreferredSize(new Dimension(300, 35));
        comboBox.setBackground(Color.WHITE);
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return comboBox;
    }

    private JTextArea createStyledTextArea(int rows, int cols) {
        JTextArea area = new JTextArea(rows, cols);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("SansSerif", Font.PLAIN, 14));
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return area;
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        gbc.gridx = 0;
        panel.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
        
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
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