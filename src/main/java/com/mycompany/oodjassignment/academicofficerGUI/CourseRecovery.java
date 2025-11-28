package com.mycompany.oodjassignment.academicofficerGUI;

import com.mycompany.oodjassignment.classes.AcademicOfficer;
import com.mycompany.oodjassignment.functions.*;

import javax.swing.*;

public class CourseRecovery {
    private Database database;
    private JPanel courseRecoveryPanel;
    private JButton recoveryPlansButton;
    private JButton exitButton;
    private JButton recoveryTasksButton;
    private JLabel txtArea;

    public static void main(String[] args) {
        Database database = new Database();
        AcademicOfficer testUser = new AcademicOfficer();
        testUser.setUserID("A01");
        String userID = testUser.getUserId();
        CourseRecovery courseRecovery = new CourseRecovery(database);
        JFrame frame = new JFrame("Academic Officer System");
        frame.setContentPane(courseRecovery.getCourseRecoveryPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(550, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public CourseRecovery(Database database) {
        this.database = database;
        txtArea.setText("\n\nWelcome to Course Recovery System! Please choose one of the dashboards below.");

        
        recoveryPlansButton.addActionListener(e -> {
            closeCurrentMenu();
            openRecoveryPlanDashboard();
        });
        recoveryTasksButton.addActionListener(e -> {
            closeCurrentMenu();
            openRecoveryTaskDashboard();
        });

        exitButton.addActionListener(e -> {
            closeCurrentMenu();
        });
    }

    private void openRecoveryTaskDashboard() {
        JFrame recoveryTaskDashboardFrame = new JFrame ("Academic Officer System");
        RecoveryTasksDashboard recoveryTasksDashboard = new RecoveryTasksDashboard(database);
        recoveryTaskDashboardFrame.setContentPane(recoveryTasksDashboard.getRecoveryTasksPanel());
        recoveryTaskDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recoveryTaskDashboardFrame.setSize(800,400);
        recoveryTaskDashboardFrame.setLocationRelativeTo(null);
        recoveryTaskDashboardFrame.setVisible(true);
    }

    private void openRecoveryPlanDashboard() {
        JFrame recoveryPlanDashboardFrame = new JFrame ("Academic Officer System");
        RecoveryPlanDashboard recoveryPlanDashboard = new RecoveryPlanDashboard(database);
        recoveryPlanDashboardFrame.setContentPane(recoveryPlanDashboard.getRecoveryPlanDashboardPanel());
        recoveryPlanDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recoveryPlanDashboardFrame.setSize(800,400);
        recoveryPlanDashboardFrame.setLocationRelativeTo(null);
        recoveryPlanDashboardFrame.setVisible(true);
    }

    public JPanel getCourseRecoveryPanel() {
        return courseRecoveryPanel;
    }

    private void closeCurrentMenu() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(courseRecoveryPanel);
        currentFrame.dispose();
    }
}
