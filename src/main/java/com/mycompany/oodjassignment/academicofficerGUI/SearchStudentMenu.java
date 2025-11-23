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
        backButton.addActionListener(e -> {
            closeCurrentMenu();
            openMainMenu();
        });
    }



    private void openMainMenu() {
        JFrame mainMenuFrame = new JFrame("Academic Officer System");
        AcademicOfficerGUI academicOfficerMainMenu = new AcademicOfficerGUI(database);
        mainMenuFrame.setContentPane(academicOfficerMainMenu.getMainPanel());
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setSize(800,600);
        mainMenuFrame.setVisible(true);
        closeCurrentMenu();
    }

    private void closeCurrentMenu() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this.SearchStudentPanel);
        currentFrame.dispose();
    }

    public JPanel getSearchStudentMenuPanel() {
        return SearchStudentPanel;
    }
}
