package com.mycompany.oodjassignment.academicofficerGUI;
import com.mycompany.oodjassignment.functions.*;

import javax.swing.*;

public class RecoveryTasksMenu {
    private Database database;
    private JPanel RecoveryTaskMenuPanel;
    private JLabel RecoveryTaskMenuTitle;
    private JButton AddRecoveryTaskButton;
    private JButton DeleteRecoveryTaskButton;
    private JButton BackButton;

    public RecoveryTasksMenu(Database database) {
        this.database = database;

        // back button - back to main menu
        BackButton.addActionListener(e -> {
            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this.RecoveryTaskMenuPanel);
            AcademicOfficerGUI academicOfficerMainMenu = new AcademicOfficerGUI(database);
            currentFrame.dispose();
            openMainMenu();
        });

    }
    public JPanel getRecoveryTaskMenuPanel() {
        return RecoveryTaskMenuPanel;
    }

    private void openMainMenu() {
        JFrame mainMenuFrame = new JFrame("Academic Officer System");
        AcademicOfficerGUI academicOfficerMainMenu = new AcademicOfficerGUI(database);
        mainMenuFrame.setContentPane(academicOfficerMainMenu.getMainPanel());
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setSize(800,600);
        mainMenuFrame.setVisible(true);
    }
}
