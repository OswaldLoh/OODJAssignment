package com.mycompany.oodjassignment.academicofficerGUI;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.mycompany.oodjassignment.functions.*;
import com.mycompany.oodjassignment.classes.*;

import javax.swing.*;
import java.awt.*;

public class AddRecoveryTask {
    private String userID;
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

    public AddRecoveryTask(String targetPlanID, String userID, Database database, boolean mustAdd) {
        this.userID = userID;
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
        StudentSelectionDashboard studentSelectionDashboard = new StudentSelectionDashboard(database, userID);
        studentSelectionDashboardFrame.setContentPane((studentSelectionDashboard.getAddPlanDashboardPanel()));
        studentSelectionDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        studentSelectionDashboardFrame.setSize(800, 400);
        studentSelectionDashboardFrame.setLocationRelativeTo(null);
        studentSelectionDashboardFrame.setVisible(true);
    }

    private void openRecoveryPlanDashboard() {
        JFrame recoveryPlanDashboardFrame = new JFrame("Academic Officer System");
        RecoveryPlanDashboard recoveryPlanDashboard = new RecoveryPlanDashboard(database, userID);
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
        addRecoveryTaskPanel = new JPanel();
        addRecoveryTaskPanel.setLayout(new GridLayoutManager(5, 5, new Insets(20, 20, 20, 20), -1, -1));
        addRecoveryTaskPanel.setBackground(new Color(-1));
        promptDescription = new JLabel();
        promptDescription.setText("Enter Description:");
        addRecoveryTaskPanel.add(promptDescription, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtDescription = new JTextField();
        addRecoveryTaskPanel.add(txtDescription, new GridConstraints(2, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtDuration = new JTextField();
        addRecoveryTaskPanel.add(txtDuration, new GridConstraints(3, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        promptDuration = new JLabel();
        promptDuration.setText("Enter Duration (days):");
        addRecoveryTaskPanel.add(promptDuration, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fixedInfoArea = new JTextArea();
        addRecoveryTaskPanel.add(fixedInfoArea, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        promptAddRecoveryTask = new JLabel();
        promptAddRecoveryTask.setText("Add first Recovery Task:");
        addRecoveryTaskPanel.add(promptAddRecoveryTask, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        confirmButton = new JButton();
        confirmButton.setText("Confirm");
        addRecoveryTaskPanel.add(confirmButton, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setText("Back");
        addRecoveryTaskPanel.add(backButton, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return addRecoveryTaskPanel;
    }
}
