package com.mycompany.oodjassignment.academicofficerGUI;
import com.mycompany.oodjassignment.functions.*;

import javax.swing.*;

public class AddRecoveryTaskMenu {
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

    public AddRecoveryTaskMenu(Database database, String targetPlanID) {
        this.database = database;
        this.targetPlanID = targetPlanID;

        addTaskButton.addActionListener(e -> {
            addNewTask();
        });

    }

    private void addNewTask() {
        String newDescription = txtNewDescription.toString().trim();
    }

    public JPanel getAddRecoveryTaskPanel() {
        return addRecoveryTaskPanel;
    }
}
