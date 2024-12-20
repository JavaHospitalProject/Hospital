package com.hospital.auth;

public class UserAccount {
    private final String username;
    private final String password;
    private final String email;
    private final UserRole role;
    private final String fullName;
    private String phoneNumber;

    public UserAccount(String username, String password, String email, UserRole role, String fullName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.fullName = fullName;
    }

    // Copy constructor
    public UserAccount(UserAccount other) {
        this.username = other.username;
        this.password = other.password;
        this.email = other.email;
        this.role = other.role;
        this.fullName = other.fullName;
        this.phoneNumber = other.phoneNumber;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Setters for mutable fields
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}