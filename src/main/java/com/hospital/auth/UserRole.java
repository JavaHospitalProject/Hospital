package com.hospital.auth;

public enum UserRole {
    ADMIN,
    PATIENT,
    DOCTOR,
    STAFF;

    @Override
    public String toString() {
        return switch (this) {
            case ADMIN -> "Administrator";
            case PATIENT -> "Patient";
            case DOCTOR -> "Doctor";
            case STAFF -> "Staff";
        };
    }
}