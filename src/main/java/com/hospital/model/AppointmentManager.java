package com.hospital.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class AppointmentManager {
    private static AppointmentManager instance;
    private final Map<String, List<Appointment>> appointmentsByDoctor;
    private final Map<String, List<Appointment>> appointmentsByPatient;
    private static final int MAX_APPOINTMENTS_PER_DAY = 20;

    private AppointmentManager() {
        appointmentsByDoctor = new HashMap<>();
        appointmentsByPatient = new HashMap<>();
    }

    public static AppointmentManager getInstance() {
        if (instance == null) {
            instance = new AppointmentManager();
        }
        return instance;
    }

    public synchronized Appointment createAppointment(String doctorId, String patientUsername, 
            String patientName, String patientPhone, LocalDateTime dateTime) {
        // Get current serial number for the doctor on this date
        int serialNumber = getNextSerialNumber(doctorId, dateTime.toLocalDate());
        
        if (serialNumber > MAX_APPOINTMENTS_PER_DAY) {
            throw new RuntimeException("Maximum appointments reached for this day");
        }

        // Check if slot is available
        if (!isSlotAvailable(doctorId, dateTime)) {
            throw new RuntimeException("This time slot is not available");
        }

        Appointment appointment = new Appointment(doctorId, patientUsername, 
            patientName, patientPhone, dateTime, serialNumber);
        
        // Add to doctor's appointments
        appointmentsByDoctor.computeIfAbsent(doctorId, k -> new ArrayList<>()).add(appointment);
        
        // Add to patient's appointments
        appointmentsByPatient.computeIfAbsent(patientUsername, k -> new ArrayList<>()).add(appointment);
        
        return appointment;
    }

    private int getNextSerialNumber(String doctorId, LocalDate date) {
        List<Appointment> doctorAppointments = getDoctorAppointments(doctorId);
        return (int) doctorAppointments.stream()
                .filter(a -> a.getDateTime().toLocalDate().equals(date))
                .filter(a -> !a.getStatus().equals("CANCELLED"))
                .count() + 1;
    }

    public List<Appointment> getDoctorAppointments(String doctorId) {
        return appointmentsByDoctor.getOrDefault(doctorId, new ArrayList<>()).stream()
                .filter(a -> !a.getStatus().equals("CANCELLED"))
                .collect(Collectors.toList());
    }

    public List<Appointment> getPatientAppointments(String patientUsername) {
        return appointmentsByPatient.getOrDefault(patientUsername, new ArrayList<>());
    }

    public List<Appointment> getTodayAppointments() {
        LocalDate today = LocalDate.now();
        return appointmentsByDoctor.values().stream()
                .flatMap(List::stream)
                .filter(a -> a.getDateTime().toLocalDate().equals(today))
                .filter(a -> !a.getStatus().equals("CANCELLED"))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return appointmentsByDoctor.values().stream()
                .flatMap(List::stream)
                .filter(a -> a.getDateTime().toLocalDate().equals(date))
                .filter(a -> !a.getStatus().equals("CANCELLED"))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAllAppointments() {
        // Consolidate all appointments from all doctors
        return appointmentsByDoctor.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public void cancelAppointment(String appointmentId) {
        appointmentsByDoctor.values().forEach(appointments ->
            appointments.stream()
                .filter(a -> a.getAppointmentId().equals(appointmentId))
                .findFirst()
                .ifPresent(a -> a.setStatus("CANCELLED")));

        appointmentsByPatient.values().forEach(appointments ->
            appointments.stream()
                .filter(a -> a.getAppointmentId().equals(appointmentId))
                .findFirst()
                .ifPresent(a -> a.setStatus("CANCELLED")));
    }

    public boolean isSlotAvailable(String doctorId, LocalDateTime dateTime) {
        List<Appointment> doctorAppointments = getDoctorAppointments(doctorId);
        long appointmentsAtTime = doctorAppointments.stream()
                .filter(a -> a.getDateTime().equals(dateTime))
                .filter(a -> !a.getStatus().equals("CANCELLED"))
                .count();
        
        long appointmentsOnDate = doctorAppointments.stream()
                .filter(a -> a.getDateTime().toLocalDate().equals(dateTime.toLocalDate()))
                .filter(a -> !a.getStatus().equals("CANCELLED"))
                .count();
                
        return appointmentsAtTime == 0 && appointmentsOnDate < MAX_APPOINTMENTS_PER_DAY;
    }

    public int getAppointmentCount(LocalDate date) {
        return (int) appointmentsByDoctor.values().stream()
                .flatMap(List::stream)
                .filter(a -> a.getDateTime().toLocalDate().equals(date))
                .filter(a -> !a.getStatus().equals("CANCELLED"))
                .count();
    }
}
