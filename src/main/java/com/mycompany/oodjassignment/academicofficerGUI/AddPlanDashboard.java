package com.mycompany.oodjassignment.academicofficerGUI;
import com.mycompany.oodjassignment.classes.*;
import com.mycompany.oodjassignment.functions.*;

import javax.swing.*;

public class AddPlanDashboard {
    private Database database;
    private JPanel addPlanDashboardPanel;
    private JTable studentListTable;
    private JButton addPlanButton;
    private JButton backButton;
    private JTextField txtStudentID;
    private JButton searchButton;
    private JLabel titleAddPlan;
    private JLabel searchStudentIDPrompt;
    private JScrollPane studentListScrollPane;

    public AddPlanDashboard(Database database) {
        this.database = database;

        backButton.addActionListener(e -> {
            closeCurrentFrame();
            openRecoveryPlanDashboard();
        });

    }



    // for back button, go back to main menu
    private void openRecoveryPlanDashboard() {
        JFrame recoveryPlanDashboardFrame = new JFrame("Academic Officer System");
        RecoveryPlanDashboard recoveryPlanDashboard = new RecoveryPlanDashboard(database);
        recoveryPlanDashboardFrame.setContentPane(recoveryPlanDashboard.getRecoveryPlanDashboardPanel());
        recoveryPlanDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recoveryPlanDashboardFrame.setSize(800,600);
        recoveryPlanDashboardFrame.setLocationRelativeTo(null);
        recoveryPlanDashboardFrame.setVisible(true);
    }
    public JPanel getAddPlanDashboardPanel() {
        return addPlanDashboardPanel;
    }
    private void closeCurrentFrame() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(addPlanDashboardPanel);
        currentFrame.dispose();
    }
}
