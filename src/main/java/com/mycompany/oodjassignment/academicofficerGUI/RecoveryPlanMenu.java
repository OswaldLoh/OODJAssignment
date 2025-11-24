package com.mycompany.oodjassignment.academicofficerGUI;
import com.mycompany.oodjassignment.functions.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecoveryPlanMenu {
    private String userID;
    private Database database;
    private JLabel RecoveryPlanMenuTitle;
    private JButton addRecoveryPlanButton;
    private JButton updateRecoveryPlanButton;
    private JButton deleteRecoveryPlanButton;
    private JButton monitorRecoveryPlanButton;
    private JButton backButton;
    private JPanel recoveryPlanMenuPanel;

    // Constructor
    public RecoveryPlanMenu(Database database) {
        this.database = database;
        addRecoveryPlanButton.addActionListener(e -> {
            closeCurrentMenu();

        });
        backButton.addActionListener(e -> {
            closeCurrentMenu();
            openMainMenu();
        });
    }

    public JPanel getRecoveryPlanMenuPanel() {
        return recoveryPlanMenuPanel;
    }

    private void openMainMenu() {
        JFrame mainMenuFrame = new JFrame("Academic Officer System");
        AcademicOfficerGUI academicOfficerMainMenu = new AcademicOfficerGUI(database);
        mainMenuFrame.setContentPane(academicOfficerMainMenu.getMainPanel());
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setSize(800,600);
        mainMenuFrame.setLocationRelativeTo(null);
        mainMenuFrame.setVisible(true);
    }

    private void closeCurrentMenu() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this.recoveryPlanMenuPanel);
        currentFrame.dispose();
    }
}
