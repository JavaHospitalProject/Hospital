package com.hospital.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Appointment {
    private final String appointmentId;
    private final String doctorId;
    private final String patientUsername;
    private final String patientName;
    private final String patientPhone;
    private final LocalDateTime dateTime;
    private final int serialNumber;
    private final String secretToken;
    private String status; // SCHEDULED, COMPLETED, CANCELLED

    public Appointment(String doctorId, String patientUsername, String patientName, 
                      String patientPhone, LocalDateTime dateTime, int serialNumber) {
        this.appointmentId = UUID.randomUUID().toString();
        this.doctorId = doctorId;
        this.patientUsername = patientUsername;
        this.patientName = patientName;
        this.patientPhone = patientPhone;
        this.dateTime = dateTime;
        this.serialNumber = serialNumber;
        this.secretToken = generateToken();
        this.status = "SCHEDULED";
    }

    private String generateToken() {
        // Generate a unique 8-character token
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Getters
    public String getAppointmentId() { return appointmentId; }
    public String getDoctorId() { return doctorId; }
    public String getPatientUsername() { return patientUsername; }
    public String getPatientName() { return patientName; }
    public String getPatientPhone() { return patientPhone; }
    public LocalDateTime getDateTime() { return dateTime; }
    public int getSerialNumber() { return serialNumber; }
    public String getSecretToken() { return secretToken; }
    public String getStatus() { return status; }

    // Setter for status only
    public void setStatus(String status) {
        this.status = status;
    }
}