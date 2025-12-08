package com.mycompany.oodjassignment.academicofficerGUI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.mycompany.oodjassignment.functions.*;
import com.mycompany.oodjassignment.classes.*;
import com.mycompany.oodjassignment.usermanagement.service.AuthenticationService;

import java.awt.*;
import javax.swing.*;

public class ModifyTaskMenu {
    private final Runnable onExitCallback;
    private AuthenticationService authService;
    private String mode;
    private String targetTaskID;
    private Database database;
    private JPanel modifyTaskPanel;
    private JTextArea detailsTextArea;
    private JTextField txtNewDescription;
    private JRadioButton completeRadioButton;
    private JRadioButton incompleteRadioButton;
    private JTextField txtNewWeek;
    private JLabel modifyTitle;
    private JLabel promptDescription;
    private JLabel promptCompletion;
    private JLabel promptWeek;
    private JButton backButton;
    private JButton confirmButton;

    public ModifyTaskMenu(String targetTaskID, String mode, Database database, AuthenticationService authService, Runnable onExitCallback) {
        this.mode = mode;
        this.database = database;
        this.targetTaskID = targetTaskID;
        this.authService = authService;
        this.onExitCallback = onExitCallback;

        if (mode.equals("Description")) {
            // show
            promptDescription.setVisible(true);
            txtNewDescription.setVisible(true);
            txtNewDescription.setText("");

            // hide
            promptWeek.setVisible(false);
            txtNewWeek.setVisible(false);
            promptCompletion.setVisible(false);
            completeRadioButton.setVisible(false);
            incompleteRadioButton.setVisible(false);

        } else if (mode.equals("Week")) {
            // show
            promptWeek.setVisible(true);
            txtNewWeek.setVisible(true);
            txtNewWeek.setText("");

            // hide
            promptDescription.setVisible(false);
            txtNewDescription.setVisible(false);
            promptCompletion.setVisible(false);
            completeRadioButton.setVisible(false);
            incompleteRadioButton.setVisible(false);

        } else if (mode.equals("Completion Status")) {
            // show
            promptCompletion.setVisible(true);
            completeRadioButton.setVisible(true);
            incompleteRadioButton.setVisible(true);

            // group radio buttons
            ButtonGroup group = new ButtonGroup();
            group.add(completeRadioButton);
            group.add(incompleteRadioButton);

            incompleteRadioButton.setSelected(true); // Default safe option

            // hide
            promptDescription.setVisible(false);
            txtNewDescription.setVisible(false);
            promptWeek.setVisible(false);
            txtNewWeek.setVisible(false);
        } else if (mode == null) {
            return;
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
                case "Week":
                    modifyWeek();
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
            openRecoveryTaskDashboard();
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

    private void modifyWeek() {
        RecoveryTask targetTask = database.getRecoveryTask(targetTaskID);
        String newWeekString = txtNewWeek.getText().trim();
        int newWeek = 0;
        try {
            if (newWeekString.isEmpty()) {
                JOptionPane.showMessageDialog(modifyTaskPanel,
                        "Please enter a week.",
                        "Missing Input", JOptionPane.WARNING_MESSAGE);
                return;
            }
            newWeek = Integer.parseInt(newWeekString);

            if (newWeek <= 0) {
                JOptionPane.showMessageDialog(modifyTaskPanel,
                        "Week must be greater than 0.",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(modifyTaskPanel,
                    "Week must be a valid whole number.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        targetTask.setWeek(newWeek);
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
        RecoveryTasksDashboard recoveryTasksDashboard = new RecoveryTasksDashboard(database, onExitCallback, authService);
        recoveryTaskDashboardFrame.setContentPane(recoveryTasksDashboard.getRecoveryTasksPanel());
        recoveryTaskDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recoveryTaskDashboardFrame.setSize(900, 400);
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        modifyTaskPanel = new JPanel();
        modifyTaskPanel.setLayout(new GridLayoutManager(7, 3, new Insets(20, 20, 20, 20), -1, -1));
        modifyTaskPanel.setBackground(new Color(-1));
        modifyTitle = new JLabel();
        modifyTitle.setText("Modifying Recovery Task Details");
        modifyTaskPanel.add(modifyTitle, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        detailsTextArea = new JTextArea();
        modifyTaskPanel.add(detailsTextArea, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        txtNewDescription = new JTextField();
        modifyTaskPanel.add(txtNewDescription, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        promptDescription = new JLabel();
        promptDescription.setText("Enter new Description:");
        modifyTaskPanel.add(promptDescription, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        completeRadioButton = new JRadioButton();
        completeRadioButton.setText("Complete");
        modifyTaskPanel.add(completeRadioButton, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        incompleteRadioButton = new JRadioButton();
        incompleteRadioButton.setText("Incomplete");
        modifyTaskPanel.add(incompleteRadioButton, new GridConstraints(5, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        promptCompletion = new JLabel();
        promptCompletion.setText("Completion Status:");
        modifyTaskPanel.add(promptCompletion, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtNewWeek = new JTextField();
        modifyTaskPanel.add(txtNewWeek, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        promptWeek = new JLabel();
        promptWeek.setText("Enter new Week:");
        modifyTaskPanel.add(promptWeek, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setText("Back");
        modifyTaskPanel.add(backButton, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        confirmButton = new JButton();
        confirmButton.setText("Confirm");
        modifyTaskPanel.add(confirmButton, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return modifyTaskPanel;
    }

}
