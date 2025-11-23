package com.mycompany.oodjassignment.academicofficerGUI;
import com.mycompany.oodjassignment.functions.*;
import com.mycompany.oodjassignment.classes.*;
import javax.swing.*;

public class AcademicOfficerGUI {
    private Database database;
    private JPanel mainPanel;
    private JLabel TopTitle;
    private JButton exitButton;
    private JButton recoveryTasksMenuButton;
    private JButton searchStudentsButton;
    private JButton recoveryPlanMenuButton;

    public static void main(String[] args) {
        Database database = new Database();
        AcademicOfficerGUI mainGUI = new AcademicOfficerGUI(database);
        JFrame frame = new JFrame("Academic Officer System");
        frame.setContentPane(mainGUI.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    public AcademicOfficerGUI(Database database) {
        this.database = database;

        searchStudentsButton.addActionListener(e -> {
            openSearchStudentMenu();
        });
        recoveryPlanMenuButton.addActionListener(e -> {
            openRecoveryPlanMenu();
        });
        recoveryTasksMenuButton.addActionListener(e -> {
            openRecoveryTaskMenu();
        });
        exitButton.addActionListener(e -> {
            closeCurrentMenu();
        });
    }
    private void openSearchStudentMenu() {
        JFrame searchStudentMenuFrame = new JFrame("Search Students");
        SearchStudentMenu searchStudentMenu = new SearchStudentMenu(database);
        searchStudentMenuFrame.setContentPane(searchStudentMenu.getSearchStudentMenuPanel());
        searchStudentMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        searchStudentMenuFrame.setSize(800,600);
        searchStudentMenuFrame.setVisible(true);
        closeCurrentMenu();

    }
    private void openRecoveryPlanMenu() {
        JFrame recoveryPlanMenuFrame = new JFrame("Recovery Plan Menu");
        RecoveryPlanMenu recoveryPlanMenu = new RecoveryPlanMenu(database);
        recoveryPlanMenuFrame.setContentPane(recoveryPlanMenu.getRecoveryPlanMenuPanel());
        recoveryPlanMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recoveryPlanMenuFrame.setSize(800, 600);
        recoveryPlanMenuFrame.setVisible(true);
        closeCurrentMenu();
    }

    private void openRecoveryTaskMenu() {
        JFrame recoveryTaskMenuFrame = new JFrame("Recovery Task Menu");
        RecoveryTasksMenu recoveryTasksMenu = new RecoveryTasksMenu(database);
        recoveryTaskMenuFrame.setContentPane(recoveryTasksMenu.getRecoveryTaskMenuPanel());
        recoveryTaskMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recoveryTaskMenuFrame.setSize(800, 600);
        recoveryTaskMenuFrame.setVisible(true);
        closeCurrentMenu();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    // Helper methods
    private void closeCurrentMenu() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this.mainPanel);
        currentFrame.dispose();
    }
}

