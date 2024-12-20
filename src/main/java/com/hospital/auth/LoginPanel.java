package com.hospital.auth;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import com.hospital.gui.MainFrame;

public class LoginPanel extends JPanel {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton registerButton;
    private final CardLayout cardLayout;
    private final JPanel parentPanel;

    public LoginPanel(CardLayout cardLayout, JPanel parentPanel) {
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
        
        // Logo and Title
        JLabel titleLabel = new JLabel("Hospital Management System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Welcome back!");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(107, 114, 128));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Form fields
        usernameField = createStyledField(new JTextField(20), "Username");
        passwordField = createStyledPasswordField();
        
        // Buttons
        loginButton = createStyledButton("Login", new Color(37, 99, 235));
        registerButton = createStyledButton("Create Account", new Color(107, 114, 128));
        
        // Add components with spacing
        container.add(titleLabel);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(subtitleLabel);
        container.add(Box.createRigidArea(new Dimension(0, 30)));
        container.add(usernameField);
        container.add(Box.createRigidArea(new Dimension(0, 15)));
        container.add(passwordField);
        container.add(Box.createRigidArea(new Dimension(0, 20)));
        container.add(loginButton);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(registerButton);
        
        // Center the container
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(new Color(240, 242, 245));
        wrapper.add(container);
        
        add(wrapper, BorderLayout.CENTER);
        
        // Add action listeners
        loginButton.addActionListener(this::handleLogin);
        registerButton.addActionListener(e -> cardLayout.show(parentPanel, "register"));

        // Add Enter key listener for login
        KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
        InputMap inputMap = passwordField.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = passwordField.getActionMap();
        inputMap.put(enter, "enter");
        actionMap.put("enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButton.doClick();
            }
        });
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }

        if (SessionManager.getInstance().authenticateUser(username, password)) {
            SessionManager.getInstance().login(username);
            
            // Get the MainFrame instance and initialize the main panel
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof MainFrame) {
                ((MainFrame) window).initializeMainPanel();
            }
        } else {
            showError("Invalid username or password");
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Login Error",
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

    private JPasswordField createStyledPasswordField() {
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
        field.setText("Password");
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (new String(field.getPassword()).equals("Password")) {
                    field.setText("");
                    field.setEchoChar('•');
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getPassword().length == 0) {
                    field.setEchoChar((char)0);
                    field.setForeground(Color.GRAY);
                    field.setText("Password");
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