package com.mycompany.oodjassignment.academicofficerGUI;
import com.mycompany.oodjassignment.functions.*;
import com.mycompany.oodjassignment.classes.*;

import javax.swing.*;

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

    public AddRecoveryTask(String targetPlanID, Database database) {
        this.database = database;
        int newDuration = 0;

        RecoveryPlan recPlan = database.getRecoveryPlan(targetPlanID);
        fixedInfoArea.setText("Student ID: " + recPlan.getStudentID() +"\n" +
                "PlanID: " + recPlan.getPlanID() + "\n" +
                "CourseID: " + recPlan.getCourseID());

        confirmButton.addActionListener(e -> {
            addTask(targetPlanID);
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

        RecoveryTask newTask = new RecoveryTask(nextTaskID,targetPlanID,newDescription,newDuration,false);
        database.addRecoveryTask(newTask);
        database.updatePlanProgress(targetPlanID);
        FileHandler.writeCSV(newTask, database.getRecTaskDB());
        JOptionPane.showMessageDialog(addRecoveryTaskPanel, "Recovery Task added successfully!");

        closeCurrentMenu();
        openAddPlanDashboard();

    }
    private void openAddPlanDashboard() {
        JFrame addPlanDashboardFrame = new JFrame("Academic Officer System");
        AddPlanDashboard addPlanDashboard = new AddPlanDashboard(database);
        addPlanDashboardFrame.setContentPane((addPlanDashboard.getAddPlanDashboardPanel()));
        addPlanDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addPlanDashboardFrame.setSize(800,600);
        addPlanDashboardFrame.setLocationRelativeTo(null);
        addPlanDashboardFrame.setVisible(true);
    }

    public JPanel getAddRecoveryTaskPanel() {
        return addRecoveryTaskPanel;
    }

    private void closeCurrentMenu() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(addRecoveryTaskPanel);
        currentFrame.dispose();
    }
}
