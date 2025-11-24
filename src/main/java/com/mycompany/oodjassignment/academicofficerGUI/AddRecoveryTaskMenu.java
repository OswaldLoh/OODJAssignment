package com.mycompany.oodjassignment.academicofficerGUI;
import com.mycompany.oodjassignment.functions.*;
import com.mycompany.oodjassignment.classes.*;

import javax.swing.*;

public class AddRecoveryTaskMenu {
    private boolean mustAdd;
    private String targetPlanID;
    private Database database;
    private JPanel addRecoveryTaskPanel;
    private JLabel addRecoveryTaskTitle;
    private JTextField txtNewDescription;
    private JTextField txtNewDuration;
    private JButton addTaskButton;
    private JTextArea recommendText;
    private JLabel studentRecommendTitle;
    private JLabel descriptionPrompt;
    private JButton backButton;
    private JLabel durationPrompt;

    public AddRecoveryTaskMenu(Database database, String targetPlanID, boolean mustAdd) {
        this.database = database;
        this.targetPlanID = targetPlanID;
        this.mustAdd = mustAdd;

        if (mustAdd) {
            backButton.setVisible(false);
        } else {
            backButton.setVisible(true);
        }
        recommendText.setEditable(false);

        addTaskButton.addActionListener(e -> {
            addNewTask();
        });

        backButton.addActionListener(e -> {
            closeCurrentFrame();
            openRecoveryPlanMenu();
        });

    }

    private void addNewTask() {
        String newDescription = txtNewDescription.getText().trim();
        String newDurationString = txtNewDuration.getText().trim();

        if (newDescription.isEmpty()) {
            JOptionPane.showMessageDialog(addRecoveryTaskPanel,
                    "Please enter a task description.",
                    "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int newDuration = 0;
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
        JOptionPane.showMessageDialog(addRecoveryTaskPanel, "Recovery Task added successfully!");
        database.updatePlanProgress(targetPlanID);
        FileHandler.writeCSV(newTask, database.getRecTaskDB());
        closeCurrentFrame();
        openRecoveryPlanMenu();

    }
    private void closeCurrentFrame() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(addRecoveryTaskPanel);
        currentFrame.dispose();
    }
    private void openRecoveryPlanMenu() {
        JFrame recoveryPlanFrame = new JFrame("Academic Officer System");
        RecoveryPlanMenu recoveryPlanMenu = new RecoveryPlanMenu(database);
        recoveryPlanFrame.setContentPane(recoveryPlanMenu.getRecoveryPlanMenuPanel());
        recoveryPlanFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recoveryPlanFrame.setSize(800,600);
        recoveryPlanFrame.setLocationRelativeTo(null);
        recoveryPlanFrame.setVisible(true);

    }
    private void openMainMenu() {
        JFrame mainMenuFrame = new JFrame("Academic Officer System");
        AcademicOfficerGUI academicOfficerMainMenu = new AcademicOfficerGUI(database);
        mainMenuFrame.setContentPane(academicOfficerMainMenu.getMainPanel());
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setSize(800, 600);
        mainMenuFrame.setLocationRelativeTo(null); // Center it
        mainMenuFrame.setVisible(true);

    }
    public JPanel getAddRecoveryTaskPanel() {
        return addRecoveryTaskPanel;
    }
}
