package com.mycompany.oodjassignment.academicofficerGUI;
import com.mycompany.oodjassignment.functions.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecoveryPlanMenu {
    private Database database;
    private JLabel RecoveryPlanMenuTitle;
    private JButton addRecoveryPlanButton;
    private JButton updateRecoveryPlanButton;
    private JButton deleteRecoveryPlanButton;
    private JButton monitorRecoveryPlanButton;
    private JButton backButton;
    private JPanel recoveryPlanMenuPanel;

    public RecoveryPlanMenu(Database database) {
        this.database = database;
        backButton.addActionListener(e -> {
            openMainMenu();
        });
    }

    public JPanel getRecoveryPlanMenuPanel() {
        return recoveryPlanMenuPanel;
    }

    private void openMainMenu() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this.recoveryPlanMenuPanel);
        JFrame mainMenuFrame = new JFrame("Academic Officer System");
        AcademicOfficerGUI academicOfficerMainMenu = new AcademicOfficerGUI(database);
        mainMenuFrame.setContentPane(academicOfficerMainMenu.getMainPanel());
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setSize(800,600);
        mainMenuFrame.setVisible(true);
        currentFrame.dispose();
    }
}
