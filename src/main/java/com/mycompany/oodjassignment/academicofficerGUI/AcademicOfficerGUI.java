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
    private JButton recoveryPlanDashboardButton;

    public static void main(String[] args) {
        Database database = new Database();
        AcademicOfficer testUser = new AcademicOfficer();
        testUser.setUserID("A01");
        String userID = testUser.getUserId();
        AcademicOfficerGUI mainGUI = new AcademicOfficerGUI(database);
        JFrame frame = new JFrame("Academic Officer System");
        frame.setContentPane(mainGUI.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public AcademicOfficerGUI(Database database) {
        this.database = database;

        searchStudentsButton.addActionListener(e -> {
            closeCurrentMenu();
            openSearchStudentMenu();
        });
        recoveryPlanDashboardButton.addActionListener(e -> {
            closeCurrentMenu();
            openRecoveryPlanDashboard();
        });
        recoveryPlanMenuButton.addActionListener(e -> {
            closeCurrentMenu();
            openRecoveryPlanMenu();
        });
        recoveryTasksMenuButton.addActionListener(e -> {
            closeCurrentMenu();
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
        searchStudentMenuFrame.setLocationRelativeTo(null);
        searchStudentMenuFrame.setVisible(true);
    }
    private void openRecoveryPlanMenu() {
        JFrame recoveryPlanMenuFrame = new JFrame("Recovery Plan Menu");
        RecoveryPlanMenu recoveryPlanMenu = new RecoveryPlanMenu(database);
        recoveryPlanMenuFrame.setContentPane(recoveryPlanMenu.getRecoveryPlanMenuPanel());
        recoveryPlanMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recoveryPlanMenuFrame.setSize(800, 600);
        recoveryPlanMenuFrame.setLocationRelativeTo(null);
        recoveryPlanMenuFrame.setVisible(true);
    }
    private void openRecoveryTaskMenu() {
        JFrame recoveryTaskMenuFrame = new JFrame("Recovery Task Menu");
        RecoveryTasksMenu recoveryTasksMenu = new RecoveryTasksMenu(database);
        recoveryTaskMenuFrame.setContentPane(recoveryTasksMenu.getRecoveryTaskMenuPanel());
        recoveryTaskMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recoveryTaskMenuFrame.setSize(800, 600);
        recoveryTaskMenuFrame.setLocationRelativeTo(null);
        recoveryTaskMenuFrame.setVisible(true);
    }
    private void openRecoveryPlanDashboard() {
        JFrame recoveryPlanDashboardFrame = new JFrame ("Academic Officer System");
        RecoveryPlanDashboard recoveryPlanDashboard = new RecoveryPlanDashboard(database);
        recoveryPlanDashboardFrame.setContentPane(recoveryPlanDashboard.getRecoveryPlanDashboardPanel());
        recoveryPlanDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recoveryPlanDashboardFrame.setSize(800,600);
        recoveryPlanDashboardFrame.setLocationRelativeTo(null);
        recoveryPlanDashboardFrame.setVisible(true);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void closeCurrentMenu() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this.mainPanel);
        currentFrame.dispose();
    }
}

