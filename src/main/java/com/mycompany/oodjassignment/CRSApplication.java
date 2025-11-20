package com.mycompany.oodjassignment;

import com.mycompany.oodjassignment.usermanagement.gui.LoginFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main Application Entry Point for Course Recovery System (CRS)
 * User Management Module
 */
public class CRSApplication {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set look and feel: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            System.out.println("========================================");
            System.out.println("Course Recovery System (CRS)");
            System.out.println("User Management Module v1.0");
            System.out.println("========================================");
            System.out.println("Initializing application...");

            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);

            System.out.println("Application started successfully!");
            System.out.println("Default credentials:");
            System.out.println("  Username: admin");
            System.out.println("  Password: admin123");
            System.out.println("========================================");
        });
    }
}

