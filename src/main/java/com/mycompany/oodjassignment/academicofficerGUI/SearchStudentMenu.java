package com.mycompany.oodjassignment.academicofficerGUI;
import com.mycompany.oodjassignment.functions.*;

import javax.swing.*;

public class SearchStudentMenu {
    private Database database;
    private JPanel SearchStudentPanel;
    private JLabel SearchStudentTitle;
    private JTextField targetStudentID;
    private JLabel studentIDPrompt;
    private JButton backButton;

    public SearchStudentMenu(Database database) {
        this.database = database;
    }
}
