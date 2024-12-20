package com.hospital.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeSlot {
    private final LocalTime time;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public TimeSlot(String timeStr) {
        this.time = LocalTime.parse(timeStr, TIME_FORMATTER);
    }

    public TimeSlot(LocalTime time) {
        this.time = time;
    }

    public LocalTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        return time.format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    public static TimeSlot[] getDefaultTimeSlots() {
        return new TimeSlot[] {
            new TimeSlot("09:00"),
            new TimeSlot("09:30"),
            new TimeSlot("10:00"),
            new TimeSlot("10:30"),
            new TimeSlot("11:00"),
            new TimeSlot("11:30"),
            new TimeSlot("12:00"),
            new TimeSlot("14:00"),
            new TimeSlot("14:30"),
            new TimeSlot("15:00"),
            new TimeSlot("15:30"),
            new TimeSlot("16:00")
        };
    }
}