package com.hospital.auth;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private static SessionManager instance;
    private UserAccount currentUser;
    private final Map<String, UserAccount> userAccounts;

    private SessionManager() {
        userAccounts = new HashMap<>();
        // Add default admin account
        addUser(new UserAccount(
            "admin",
            "admin",
            "admin@hospital.com",
            UserRole.ADMIN,
            "System Administrator"
        ));
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void addUser(UserAccount user) {
        userAccounts.put(user.getUsername(), user);
    }

    public boolean authenticateUser(String username, String password) {
        UserAccount user = userAccounts.get(username);
        return user != null && user.getPassword().equals(password);
    }

    public void login(String username) {
        currentUser = userAccounts.get(username);
    }

    public void logout() {
        currentUser = null;
    }

    public UserAccount getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return isLoggedIn() && currentUser.getRole() == UserRole.ADMIN;
    }

    public boolean userExists(String username) {
        return userAccounts.containsKey(username);
    }

    public boolean emailExists(String email) {
        return userAccounts.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    // For testing/development purposes
    public void clearAllUsers() {
        userAccounts.clear();
        // Re-add admin account
        addUser(new UserAccount(
            "admin",
            "admin",
            "admin@hospital.com",
            UserRole.ADMIN,
            "System Administrator"
        ));
    }
}