package com.mycompany.oodjassignment.academicofficerGUI;
import com.mycompany.oodjassignment.functions.*;

import javax.swing.*;

public class AddRecoveryTaskMenu {
    private Database database;
    private JPanel addRecoveryTaskPanel;
    private JLabel addRecoveryTaskTitle;
    private JTextField textField1;
    private JTextField textField2;
    private JButton addTaskButton;
    private JTextArea textArea1;
    private JLabel studentRecommendTitle;
    private JLabel descriptionPrompt;

    public AddRecoveryTaskMenu(Database database) {
        this.database = database;


    }

    public JPanel getAddRecoveryTaskPanel() {
        return addRecoveryTaskPanel;
    }
}
