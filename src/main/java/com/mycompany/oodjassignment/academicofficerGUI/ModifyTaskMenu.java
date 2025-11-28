package com.mycompany.oodjassignment.academicofficerGUI;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.mycompany.oodjassignment.functions.*;
import com.mycompany.oodjassignment.classes.*;
import java.awt.*;
import javax.swing.*;

public class ModifyTaskMenu {
    private String mode;
    private String targetTaskID;
    private Database database;
    private JPanel modifyTaskPanel;
    private JTextArea detailsTextArea;
    private JTextField txtNewDescription;
    private JRadioButton completeRadioButton;
    private JRadioButton incompleteRadioButton;
    private JTextField txtNewDuration;
    private JLabel modifyTitle;
    private JLabel promptDescription;
    private JLabel promptCompletion;
    private JLabel promptDuration;
    private JButton backButton;
    private JButton confirmButton;

    public ModifyTaskMenu(String targetTaskID, String mode, Database database) {
        this.mode = mode;
        this.database = database;
        this.targetTaskID = targetTaskID;

        if (mode.equals("Description")) {
            // SHOW Description fields
            promptDescription.setVisible(true);
            txtNewDescription.setVisible(true);
            txtNewDescription.setText(""); // Ensure it's empty

            // HIDE others
            promptDuration.setVisible(false);
            txtNewDuration.setVisible(false);
            promptCompletion.setVisible(false);
            completeRadioButton.setVisible(false);
            incompleteRadioButton.setVisible(false);

        } else if (mode.equals("Duration")) {
            // SHOW Duration fields
            promptDuration.setVisible(true);
            txtNewDuration.setVisible(true);
            txtNewDuration.setText(""); // Ensure it's empty

            // HIDE others
            promptDescription.setVisible(false);
            txtNewDescription.setVisible(false);
            promptCompletion.setVisible(false);
            completeRadioButton.setVisible(false);
            incompleteRadioButton.setVisible(false);

        } else if (mode.equals("Completion Status")) {
            // SHOW Completion fields
            promptCompletion.setVisible(true);
            completeRadioButton.setVisible(true);
            incompleteRadioButton.setVisible(true);

            // CRITICAL: You still need to group them so they work as a pair
            ButtonGroup group = new ButtonGroup();
            group.add(completeRadioButton);
            group.add(incompleteRadioButton);

            // Optional: Default to 'Incomplete' or clear selection
            // group.clearSelection(); // If you want neither selected
            incompleteRadioButton.setSelected(true); // Default safe option

            // HIDE others
            promptDescription.setVisible(false);
            txtNewDescription.setVisible(false);
            promptDuration.setVisible(false);
            txtNewDuration.setVisible(false);
        }

        RecoveryTask targetTask = database.getRecoveryTask(targetTaskID);
        detailsTextArea.setText("Plan ID: " + targetTask.getPlanID() +
                "\nTask ID: " + targetTask.getTaskID());

        if (targetTask.getCompletion()) {
            completeRadioButton.setSelected(true);
        } else {
            incompleteRadioButton.setSelected(true);
        }

        confirmButton.addActionListener(e -> {
            switch (mode) {
                case "Description":
                    modifyDescription();
                    break;
                case "Duration":
                    modifyDuration();
                    break;
                case "Completion Status":
                    modifyCompletion();
                    break;
            }
            JOptionPane.showMessageDialog(modifyTaskPanel, "Task modified successfully!");
            openRecoveryTaskDashboard();
            closeCurrentMenu();
        });

        backButton.addActionListener(e -> {
            closeCurrentMenu();
        });
    }

    private void modifyDescription() {
        RecoveryTask targetTask = database.getRecoveryTask(targetTaskID);
        String newDescription = txtNewDescription.getText().trim();
        if (newDescription.isEmpty()) {
            JOptionPane.showMessageDialog(modifyTaskPanel,
                    "Please enter a task description.",
                    "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }
        targetTask.setDescription(newDescription);
        RecoveryPlan recPlan = new RecoveryPlan();
        FileHandler.writeCSV(targetTask, database.getRecTaskDB());
        FileHandler.writeCSV(recPlan, database.getRecPlanDB());
    }

    private void modifyDuration() {
        RecoveryTask targetTask = database.getRecoveryTask(targetTaskID);
        String newDurationString = txtNewDuration.getText().trim();
        int newDuration = 0;
        try {
            if (newDurationString.isEmpty()) {
                JOptionPane.showMessageDialog(modifyTaskPanel,
                        "Please enter a duration.",
                        "Missing Input", JOptionPane.WARNING_MESSAGE);
                return;
            }
            newDuration = Integer.parseInt(newDurationString);

            if (newDuration <= 0) {
                JOptionPane.showMessageDialog(modifyTaskPanel,
                        "Duration must be greater than 0.",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(modifyTaskPanel,
                    "Duration must be a valid whole number.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        targetTask.setDuration(newDuration);
        RecoveryPlan recPlan = new RecoveryPlan();
        FileHandler.writeCSV(targetTask, database.getRecTaskDB());
        FileHandler.writeCSV(recPlan, database.getRecPlanDB());
    }

    private void modifyCompletion() {
        boolean newCompletionStatus = completeRadioButton.isSelected();
        RecoveryPlan recPlan = new RecoveryPlan();
        RecoveryTask targetTask = database.getRecoveryTask(targetTaskID);
        targetTask.setCompletion(newCompletionStatus);
        database.updatePlanProgress(targetTask.getPlanID());
        FileHandler.writeCSV(targetTask, database.getRecTaskDB());
        FileHandler.writeCSV(recPlan, database.getRecPlanDB());
    }

    private void openRecoveryTaskDashboard() {
        JFrame recoveryTaskDashboardFrame = new JFrame("Academic Officer System");
        RecoveryTasksDashboard recoveryTasksDashboard = new RecoveryTasksDashboard(database);
        recoveryTaskDashboardFrame.setContentPane(recoveryTasksDashboard.getRecoveryTasksPanel());
        recoveryTaskDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recoveryTaskDashboardFrame.setSize(800, 400);
        recoveryTaskDashboardFrame.setLocationRelativeTo(null);
        recoveryTaskDashboardFrame.setVisible(true);
    }

    private void closeCurrentMenu() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(modifyTaskPanel);
        currentFrame.dispose();
    }

    public JPanel getModifyTaskPanel() {
        return modifyTaskPanel;
    }

}
