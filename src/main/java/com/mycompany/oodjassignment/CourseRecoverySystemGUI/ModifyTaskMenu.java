package com.mycompany.oodjassignment.CourseRecoverySystemGUI;

import java.awt.*;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.mycompany.oodjassignment.Entities.RecoveryPlan;
import com.mycompany.oodjassignment.Entities.RecoveryTask;
import com.mycompany.oodjassignment.Entities.Student;
import com.mycompany.oodjassignment.Helpers.Database;
import com.mycompany.oodjassignment.Helpers.FileHandler;
import com.mycompany.oodjassignment.Helpers.SendEmail;
import com.mycompany.oodjassignment.usermanagement.service.AuthenticationService;

public class ModifyTaskMenu {
    private final Runnable onExitCallback;
    private AuthenticationService authService;
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
        this.database = database;
        this.targetTaskID = targetTaskID;
        this.authService = authService;
        this.onExitCallback = onExitCallback;



        RecoveryTask targetTask = database.getRecoveryTask(targetTaskID);       // getting object of selected recovery task

        showSelectedModifyTarget(mode);            // only show specific search box or radio button based on which data to be modified

        initializeTextArea(targetTask);            // show the current details of the selected task

        if (targetTask.getCompletion()) {               // initialize selection of the completion radio button
            completeRadioButton.setSelected(true);
        } else {
            incompleteRadioButton.setSelected(true);
        }

        confirmButton.addActionListener(e -> {      // receive
            boolean modified = switch (mode) {
                case "Description" -> modifyDescription(targetTask);
                case "Week" -> modifyWeek(targetTask);
                case "Completion Status" -> modifyCompletion(targetTask);
                default -> false;
            };

            if (modified) {
                JOptionPane.showMessageDialog(modifyTaskPanel, "Task modified successfully!");
                openRecoveryTaskDashboard();
                closeCurrentMenu();
            }
        });

        backButton.addActionListener(e -> {
            openRecoveryTaskDashboard();
            closeCurrentMenu();
        });
    }

    private boolean modifyDescription(RecoveryTask targetTask) {
        String newDescription = txtNewDescription.getText().trim();
        if (newDescription.isEmpty()) {
            JOptionPane.showMessageDialog(modifyTaskPanel,
                    "Please enter a task description.",
                    "Missing Input", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Store old description for email
        String oldDescription = targetTask.getDescription();

        targetTask.setDescription(newDescription);
        RecoveryPlan recPlan = new RecoveryPlan();
        FileHandler.writeCSV(targetTask, database.getRecTaskDB());
        FileHandler.writeCSV(recPlan, database.getRecPlanDB());

        // Send email notification about description change
        RecoveryPlan associatedPlan = database.getRecoveryPlan(targetTask.getPlanID());
        Student student = database.getStudent(associatedPlan.getStudentID());
        SendEmail sendEmail = new SendEmail(student.getEmail());
        String emailSubject = "Recovery Task Description Updated";
        String emailContent = "Dear " + student.getFirstName() + " " + student.getLastName() + ",\n\n" +
                "The description of a recovery task in your course recovery plan has been updated.\n\n" +
                "Plan ID: " + targetTask.getPlanID() + "\n" +
                "Task ID: " + targetTask.getTaskID() + "\n" +
                "Old Description: " + oldDescription + "\n" +
                "New Description: " + newDescription + "\n\n" +
                "Please take note of the updated task requirements.\n\n" +
                "Best regards,\n" +
                "Academic Officer Team";

        // using new thread prevent GUI freezing
        new Thread(() ->
                sendEmail.Notification(emailSubject, emailContent)
        ).start();
        return true;
    }

    private boolean modifyWeek(RecoveryTask targetTask) {
        String newWeekString = txtNewWeek.getText().trim();
        int newWeek = 0;
        try {
            if (newWeekString.isEmpty()) {
                JOptionPane.showMessageDialog(modifyTaskPanel,
                        "Please enter a week.",
                        "Missing Input", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            newWeek = Integer.parseInt(newWeekString);

            if (newWeek <= 0) {
                JOptionPane.showMessageDialog(modifyTaskPanel,
                        "Week must be greater than 0.",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(modifyTaskPanel,
                    "Week must be a valid whole number.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Store old week for email
        int oldWeek = targetTask.getWeek();

        targetTask.setWeek(newWeek);
        RecoveryPlan recPlan = new RecoveryPlan();
        FileHandler.writeCSV(targetTask, database.getRecTaskDB());
        FileHandler.writeCSV(recPlan, database.getRecPlanDB());

        // Send email notification about week change
        RecoveryPlan associatedPlan = database.getRecoveryPlan(targetTask.getPlanID());
        Student student = database.getStudent(associatedPlan.getStudentID());
        SendEmail sendEmail = new SendEmail(student.getEmail());
        String emailSubject = "Recovery Task Week Updated";
        String emailContent = "Dear " + student.getFirstName() + " " + student.getLastName() + ",\n\n" +
                "The week deadline of a recovery task in your course recovery plan has been updated.\n\n" +
                "Plan ID: " + targetTask.getPlanID() + "\n" +
                "Task ID: " + targetTask.getTaskID() + "\n" +
                "Old Week: " + oldWeek + "\n" +
                "New Week: " + newWeek + "\n\n" +
                "Please adjust your schedule to meet the new deadline.\n\n" +
                "Best regards,\n" +
                "Academic Officer Team";

        // using new thread prevent GUI freezing
        new Thread(() ->
                sendEmail.Notification(emailSubject, emailContent)
        ).start();
        return true;
    }

    private boolean modifyCompletion(RecoveryTask targetTask) {
        boolean newCompletionStatus = completeRadioButton.isSelected();
        RecoveryPlan recPlan = new RecoveryPlan();
        boolean oldCompletionStatus = targetTask.getCompletion(); // Store old status for email
        targetTask.setCompletion(newCompletionStatus);
        database.updatePlanProgress(targetTask.getPlanID());
        FileHandler.writeCSV(targetTask, database.getRecTaskDB());
        FileHandler.writeCSV(recPlan, database.getRecPlanDB());

        // Send email notification about completion status change
        RecoveryPlan associatedPlan = database.getRecoveryPlan(targetTask.getPlanID());
        Student student = database.getStudent(associatedPlan.getStudentID());
        SendEmail sendEmail = new SendEmail(student.getEmail());
        String emailSubject = "Recovery Task Completion Status Updated";
        String emailContent = "Dear " + student.getFirstName() + " " + student.getLastName() + ",\n\n" +
                "The completion status of a recovery task in your course recovery plan has been updated.\n\n" +
                "Plan ID: " + targetTask.getPlanID() + "\n" +
                "Task ID: " + targetTask.getTaskID() + "\n" +
                "Previous Status: " + oldCompletionStatus + "\n" +
                "Current Status: " + newCompletionStatus + "\n\n" +
                "Please check your recovery plan for the updated status.\n\n" +
                "Best regards,\n" +
                "Academic Officer Team";

        // using new thread prevent GUI freezing
        new Thread(() ->
                sendEmail.Notification(emailSubject, emailContent)
        ).start();
        return true;
    }
    private void showSelectedModifyTarget(String mode) {
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
        }
    }

    private void initializeTextArea(RecoveryTask targetTask) {
        String completionStatus;
        if (targetTask.getCompletion()) {               // parsing the completion status from boolean to completion
            completionStatus = "Complete";
        } else {
            completionStatus = "Incomplete";
        }

        detailsTextArea.setEditable(false);                             // disallow editing the text area
        detailsTextArea.setText("Plan ID: " + targetTask.getPlanID() +  // display the current details of the selected task
                "\nTask ID: " + targetTask.getTaskID() +
                "\nDescription: " + targetTask.getDescription() +
                "\nWeek: " + targetTask.getWeek() +
                "\nCompletion Status: " + completionStatus);
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
        Font detailsTextAreaFont = this.$$$getFont$$$("Dialog", -1, -1, detailsTextArea.getFont());
        if (detailsTextAreaFont != null) detailsTextArea.setFont(detailsTextAreaFont);
        modifyTaskPanel.add(detailsTextArea, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        txtNewDescription = new JTextField();
        modifyTaskPanel.add(txtNewDescription, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        promptDescription = new JLabel();
        promptDescription.setText("Enter new Description:");
        modifyTaskPanel.add(promptDescription, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        completeRadioButton = new JRadioButton();
        completeRadioButton.setBackground(new Color(-1));
        completeRadioButton.setText("Complete");
        modifyTaskPanel.add(completeRadioButton, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        incompleteRadioButton = new JRadioButton();
        incompleteRadioButton.setBackground(new Color(-1));
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
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return modifyTaskPanel;
    }

}
