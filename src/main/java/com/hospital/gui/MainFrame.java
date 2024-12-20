package com.hospital.gui;

import com.hospital.auth.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private CardLayout mainCardLayout;
    private JPanel mainContainer;
    private JPanel mainAppPanel;

    public MainFrame() {
        setTitle("Hospital Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        // Create main CardLayout for switching between auth and main app
        mainCardLayout = new CardLayout();
        mainContainer = new JPanel(mainCardLayout);

        // Add authentication panels
        mainContainer.add(new LoginPanel(mainCardLayout, mainContainer), "login");
        mainContainer.add(new RegisterPanel(mainCardLayout, mainContainer), "register");

        // Add the main container to frame
        add(mainContainer);

        // Start with login
        mainCardLayout.show(mainContainer, "login");
    }

    // This method will be called after successful login
    public void initializeMainPanel() {
        if (mainAppPanel == null) {
            mainAppPanel = createMainAppPanel();
            mainContainer.add(mainAppPanel, "main");
        }
        mainCardLayout.show(mainContainer, "main");
    }

    private JPanel createMainAppPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create and add side menu
        panel.add(createSideMenu(), BorderLayout.WEST);

        // Create content panel with CardLayout for different screens
        CardLayout contentCardLayout = new CardLayout();
        contentPanel = new JPanel(contentCardLayout);

        // Add panels based on user role
        if (SessionManager.getInstance().isAdmin()) {
            contentPanel.add(new DoctorListPanel(), "DoctorList");
            contentPanel.add(new AdmitFormPanel(), "AdmitForm");
            contentPanel.add(new TestsPanel(), "Tests");
            contentPanel.add(new AdminAppointmentsPanel(), "AdminAppointments");
            contentPanel.add(new AppointmentPanel(), "Appointment");
            contentPanel.add(new ServicesPanel(), "Services");
        } else {
            contentPanel.add(new AppointmentPanel(), "Appointment");
            contentPanel.add(new TestsPanel(), "Tests");
            contentPanel.add(new ServicesPanel(), "Services");
            contentPanel.add(new MyAppointmentsPanel(), "MyAppointments");
        }

        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSideMenu() {
        JPanel sideMenu = new JPanel();
        sideMenu.setPreferredSize(new Dimension(250, 0));
        sideMenu.setBackground(new Color(22, 31, 48));
        sideMenu.setLayout(new BoxLayout(sideMenu, BoxLayout.Y_AXIS));
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        sideMenu.add(headerPanel);
        
        // Separator
        addSeparator(sideMenu);

        // User info panel
        JPanel userInfoPanel = createUserInfoPanel();
        sideMenu.add(userInfoPanel);
        
        addSeparator(sideMenu);

        // Navigation buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(22, 31, 48));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));

        // Add menu buttons based on user role
        addMenuButtons(buttonPanel);

        sideMenu.add(buttonPanel);
        sideMenu.add(Box.createVerticalGlue()); // Push logout to bottom

        // Add logout button at bottom
        JPanel logoutPanel = createLogoutPanel();
        sideMenu.add(logoutPanel);

        return sideMenu;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(22, 31, 48));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        
        JLabel headerLabel = new JLabel("HMS");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subLabel = new JLabel("Healthcare Management");
        subLabel.setForeground(new Color(148, 163, 184));
        subLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(headerLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subLabel);
        
        return headerPanel;
    }

    private JPanel createUserInfoPanel() {
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBackground(new Color(22, 31, 48));
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        UserAccount currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            JLabel nameLabel = new JLabel(currentUser.getFullName());
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            
            JLabel roleLabel = new JLabel(currentUser.getRole().toString());
            roleLabel.setForeground(new Color(148, 163, 184));
            roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

            userInfoPanel.add(nameLabel);
            userInfoPanel.add(Box.createVerticalStrut(5));
            userInfoPanel.add(roleLabel);
        }

        return userInfoPanel;
    }

    private void addMenuButtons(JPanel buttonPanel) {
        UserRole userRole = SessionManager.getInstance().getCurrentUser().getRole();

        if (userRole == UserRole.ADMIN) {
            addMenuButton(buttonPanel, "Doctors", "DoctorList", "👨‍⚕️");
            addMenuButton(buttonPanel, "Admission", "AdmitForm", "🏥");
            addMenuButton(buttonPanel, "Tests", "Tests", "🔬");
            addMenuButton(buttonPanel, "All Appointments", "AdminAppointments", "📋");
            addMenuButton(buttonPanel, "Book Appointment", "Appointment", "📅");
            addMenuButton(buttonPanel, "Services", "Services", "⚕️");
        } else {
            addMenuButton(buttonPanel, "Book Appointment", "Appointment", "📅");
            addMenuButton(buttonPanel, "My Appointments", "MyAppointments", "📋");
            addMenuButton(buttonPanel, "Tests", "Tests", "🔬");
            addMenuButton(buttonPanel, "Services", "Services", "⚕️");
        }
    }

    private void addSeparator(JPanel menu) {
        JPanel separatorPanel = new JPanel();
        separatorPanel.setBackground(new Color(22, 31, 48));
        separatorPanel.setLayout(new BoxLayout(separatorPanel, BoxLayout.Y_AXIS));
        separatorPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(45, 55, 72));
        separator.setMaximumSize(new Dimension(200, 1));
        separatorPanel.add(separator);
        
        menu.add(separatorPanel);
        menu.add(Box.createVerticalStrut(20));
    }

    private JPanel createLogoutPanel() {
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoutPanel.setBackground(new Color(22, 31, 48));
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JButton logoutButton = new JButton("🚪  Logout");
        logoutButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        logoutButton.setForeground(new Color(239, 68, 68));
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        logoutButton.addActionListener(e -> {
            SessionManager.getInstance().logout();
            mainContainer.remove(mainAppPanel);
            mainAppPanel = null;
            mainCardLayout.show(mainContainer, "login");
        });

        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                logoutButton.setForeground(new Color(248, 113, 113));
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                logoutButton.setForeground(new Color(239, 68, 68));
            }
        });

        logoutPanel.add(logoutButton);
        return logoutPanel;
    }

    private void addMenuButton(JPanel menu, String text, String cardName, String icon) {
        JButton button = new JButton(icon + "  " + text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setForeground(new Color(203, 213, 225));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setIconTextGap(10);
        
        button.setMaximumSize(new Dimension(200, 45));
        button.setPreferredSize(new Dimension(200, 45));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setContentAreaFilled(true);
                button.setBackground(new Color(45, 55, 72));
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setContentAreaFilled(false);
                button.setForeground(new Color(203, 213, 225));
            }

            @Override
            public void mousePressed(MouseEvent evt) {
                button.setBackground(new Color(55, 65, 82));
            }
        });
        
        button.addActionListener(e -> ((CardLayout)contentPanel.getLayout()).show(contentPanel, cardName));
        
        menu.add(button);
        menu.add(Box.createVerticalStrut(15));
    }
}