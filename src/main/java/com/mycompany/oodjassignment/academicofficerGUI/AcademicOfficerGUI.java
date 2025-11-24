package com.mycompany.oodjassignment.academicofficerGUI;
import com.mycompany.oodjassignment.functions.*;
import com.mycompany.oodjassignment.classes.*;
import javax.swing.*;

public class AcademicOfficerGUI {
    private Database database;
    private JPanel mainPanel;
    private JLabel TopTitle;
    private JButton exitButton;
    private JButton searchStudentsButton;
    private JButton recoveryPlanDashboardButton;
    private JButton recoveryTaskDashboardButton;

    public static void main(String[] args) {
        Database database = new Database();
        AcademicOfficer testUser = new AcademicOfficer();
        testUser.setUserID("A01");
        String userID = testUser.getUserId();
        AcademicOfficerGUI mainGUI = new AcademicOfficerGUI(database);
        JFrame frame = new JFrame("Academic Officer System");
        frame.setContentPane(mainGUI.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public AcademicOfficerGUI(Database database) {
        this.database = database;

        searchStudentsButton.addActionListener(e -> {
            closeCurrentMenu();
        });
        recoveryPlanDashboardButton.addActionListener(e -> {
            closeCurrentMenu();
            openRecoveryPlanDashboard();
        });
        recoveryTaskDashboardButton.addActionListener(e -> {
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

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void closeCurrentMenu() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this.mainPanel);
        currentFrame.dispose();
    }
}

