package com.hospital.auth;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class RegisterPanel extends JPanel {
    private final JTextField usernameField;
    private final JTextField fullNameField;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;
    private final JTextField phoneField;
    private final CardLayout cardLayout;
    private final JPanel parentPanel;

    public RegisterPanel(CardLayout cardLayout, JPanel parentPanel) {
        this.cardLayout = cardLayout;
        this.parentPanel = parentPanel;
        
        setLayout(new BorderLayout());
        setBackground(new Color(240, 242, 245));
        
        // Create main container
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        
        // Title
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Join our healthcare community");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(107, 114, 128));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Form fields
        fullNameField = createStyledField(new JTextField(20), "Full Name");
        usernameField = createStyledField(new JTextField(20), "Username");
        emailField = createStyledField(new JTextField(20), "Email Address");
        phoneField = createStyledField(new JTextField(20), "Phone Number");
        passwordField = createStyledPasswordField("Password");
        confirmPasswordField = createStyledPasswordField("Confirm Password");
        
        // Buttons
        JButton registerButton = createStyledButton("Create Account", new Color(37, 99, 235));
        JButton backButton = createStyledButton("Back to Login", new Color(107, 114, 128));
        
        // Add components with spacing
        container.add(titleLabel);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(subtitleLabel);
        container.add(Box.createRigidArea(new Dimension(0, 30)));
        container.add(fullNameField);
        container.add(Box.createRigidArea(new Dimension(0, 15)));
        container.add(usernameField);
        container.add(Box.createRigidArea(new Dimension(0, 15)));
        container.add(emailField);
        container.add(Box.createRigidArea(new Dimension(0, 15)));
        container.add(phoneField);
        container.add(Box.createRigidArea(new Dimension(0, 15)));
        container.add(passwordField);
        container.add(Box.createRigidArea(new Dimension(0, 15)));
        container.add(confirmPasswordField);
        container.add(Box.createRigidArea(new Dimension(0, 20)));
        container.add(registerButton);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(backButton);
        
        // Center the container
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(new Color(240, 242, 245));
        wrapper.add(container);
        
        add(wrapper, BorderLayout.CENTER);
        
        // Add action listeners
        registerButton.addActionListener(this::handleRegistration);
        backButton.addActionListener(e -> cardLayout.show(parentPanel, "login"));
    }

    private void handleRegistration(ActionEvent e) {
        String username = usernameField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Validation
        if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || 
            phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("All fields are required");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        if (username.equals("admin")) {
            showError("Username 'admin' is reserved");
            return;
        }

        if (SessionManager.getInstance().userExists(username)) {
            showError("Username already exists");
            return;
        }

        if (SessionManager.getInstance().emailExists(email)) {
            showError("Email already registered");
            return;
        }

        // Create new user account
        UserAccount newUser = new UserAccount(username, password, email, UserRole.PATIENT, fullName);
        newUser.setPhoneNumber(phone);
        SessionManager.getInstance().addUser(newUser);

        // Show success message and return to login
        JOptionPane.showMessageDialog(this,
            "Registration successful! Please login with your credentials.",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        cardLayout.show(parentPanel, "login");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Registration Error",
            JOptionPane.ERROR_MESSAGE);
    }

    private JTextField createStyledField(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(300, 40));
        field.setPreferredSize(new Dimension(300, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        // Add placeholder
        field.setForeground(Color.GRAY);
        field.setText(placeholder);
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
        
        return field;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(20);
        field.setMaximumSize(new Dimension(300, 40));
        field.setPreferredSize(new Dimension(300, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        // Add placeholder
        field.setForeground(Color.GRAY);
        field.setEchoChar((char)0);
        field.setText(placeholder);
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (new String(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setEchoChar('•');
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getPassword().length == 0) {
                    field.setEchoChar((char)0);
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
        
        return field;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(300, 40));
        button.setPreferredSize(new Dimension(300, 40));
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