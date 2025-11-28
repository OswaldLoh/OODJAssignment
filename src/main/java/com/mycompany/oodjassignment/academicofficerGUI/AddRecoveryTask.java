package com.mycompany.oodjassignment.academicofficerGUI;
import com.mycompany.oodjassignment.functions.*;
import com.mycompany.oodjassignment.classes.*;

import javax.swing.*;
import java.awt.*;

public class AddRecoveryTask {
    private Database database;
    private JPanel addRecoveryTaskPanel;
    private JTextArea fixedInfoArea;
    private JTextField txtDescription;
    private JTextField txtDuration;
    private JButton confirmButton;
    private JLabel promptDescription;
    private JLabel promptDuration;
    private JLabel promptAddRecoveryTask;
    private JButton backButton;

    public AddRecoveryTask(String targetPlanID, Database database, boolean mustAdd) {
        this.database = database;
        int newDuration = 0;

        if (mustAdd) {
            backButton.setVisible(false);
        } else {
            backButton.setVisible(true);
        }

        RecoveryPlan recPlan = database.getRecoveryPlan(targetPlanID);
        fixedInfoArea.setText("Student ID: " + recPlan.getStudentID() + "\n" +
                "PlanID: " + recPlan.getPlanID() + "\n" +
                "CourseID: " + recPlan.getCourseID());

        confirmButton.addActionListener(e -> {
            addTask(targetPlanID);
        });

        backButton.addActionListener(e -> {
            closeCurrentMenu();
            openRecoveryPlanDashboard();
        });
    }


    public void addTask(String targetPlanID) {
        int newDuration = 0;

        String newDescription = txtDescription.getText().trim();
        String newDurationString = txtDuration.getText().trim();

        if (newDescription.isEmpty()) {
            JOptionPane.showMessageDialog(addRecoveryTaskPanel,
                    "Please enter a task description.",
                    "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (newDurationString.isEmpty()) {
                JOptionPane.showMessageDialog(addRecoveryTaskPanel,
                        "Please enter a duration (days).",
                        "Error.", JOptionPane.WARNING_MESSAGE);
                return;
            }

            newDuration = Integer.parseInt(newDurationString);

            if (newDuration <= 0) {
                JOptionPane.showMessageDialog(addRecoveryTaskPanel,
                        "Duration must be greater than 0.",
                        "Error.", JOptionPane.WARNING_MESSAGE);
                return;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(addRecoveryTaskPanel,
                    "Please enter a valid whole number for duration.",
                    "Error.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        IDManager recTasKIDManager = new IDManager(database.getRecTaskDB());
        recTasKIDManager.getHighestTaskID();
        String nextTaskID = "T" + recTasKIDManager.generateNewID();

        RecoveryTask newTask = new RecoveryTask(nextTaskID, targetPlanID, newDescription, newDuration, false);
        database.addRecoveryTask(newTask);
        database.updatePlanProgress(targetPlanID);
        FileHandler.writeCSV(newTask, database.getRecTaskDB());
        JOptionPane.showMessageDialog(addRecoveryTaskPanel, "Recovery Task added successfully!");

        closeCurrentMenu();
        studentSelectionDashboard();

    }

    private void studentSelectionDashboard() {
        JFrame studentSelectionDashboardFrame = new JFrame("Academic Officer System");
        StudentSelectionDashboard studentSelectionDashboard = new StudentSelectionDashboard(database);
        studentSelectionDashboardFrame.setContentPane((studentSelectionDashboard.getAddPlanDashboardPanel()));
        studentSelectionDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        studentSelectionDashboardFrame.setSize(800, 400);
        studentSelectionDashboardFrame.setLocationRelativeTo(null);
        studentSelectionDashboardFrame.setVisible(true);
    }

    private void openRecoveryPlanDashboard() {
        JFrame recoveryPlanDashboardFrame = new JFrame("Academic Officer System");
        RecoveryPlanDashboard recoveryPlanDashboard = new RecoveryPlanDashboard(database);
        recoveryPlanDashboardFrame.setContentPane(recoveryPlanDashboard.getRecoveryPlanDashboardPanel());
        recoveryPlanDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recoveryPlanDashboardFrame.setSize(800, 400);
        recoveryPlanDashboardFrame.setLocationRelativeTo(null);
        recoveryPlanDashboardFrame.setVisible(true);
    }

    public JPanel getAddRecoveryTaskPanel() {
        return addRecoveryTaskPanel;
    }

    private void closeCurrentMenu() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(addRecoveryTaskPanel);
        currentFrame.dispose();
    }

}
