package com.mycompany.oodjassignment.academicofficerGUI;
import com.mycompany.oodjassignment.functions.*;
import com.mycompany.oodjassignment.classes.*;
import javax.swing.*;

public class AcademicOfficerGUI {
    private String userID;
    private Database database;
    private JPanel mainPanel;
    private JLabel TopTitle;
    private JButton exitButton;
    private JButton recoveryTasksMenuButton;
    private JButton searchStudentsButton;
    private JButton recoveryPlanMenuButton;

    public static void main(String[] args) {
        Database database = new Database();
        AcademicOfficer testUser = new AcademicOfficer();
        testUser.setUserID("A01");
        String userID = testUser.getUserId();
        AcademicOfficerGUI mainGUI = new AcademicOfficerGUI(database, userID);
        JFrame frame = new JFrame("Academic Officer System");
        frame.setContentPane(mainGUI.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public AcademicOfficerGUI(Database database, String userID) {
        this.database = database;
        this.userID = userID;

        searchStudentsButton.addActionListener(e -> {
            closeCurrentMenu();
            openSearchStudentMenu();
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
        RecoveryPlanMenu recoveryPlanMenu = new RecoveryPlanMenu(database, userID);
        recoveryPlanMenuFrame.setContentPane(recoveryPlanMenu.getRecoveryPlanMenuPanel());
        recoveryPlanMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recoveryPlanMenuFrame.setSize(800, 600);
        recoveryPlanMenuFrame.setLocationRelativeTo(null);
        recoveryPlanMenuFrame.setVisible(true);
    }

    private void openRecoveryTaskMenu() {
        JFrame recoveryTaskMenuFrame = new JFrame("Recovery Task Menu");
        RecoveryTasksMenu recoveryTasksMenu = new RecoveryTasksMenu(database, userID);
        recoveryTaskMenuFrame.setContentPane(recoveryTasksMenu.getRecoveryTaskMenuPanel());
        recoveryTaskMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recoveryTaskMenuFrame.setSize(800, 600);
        recoveryTaskMenuFrame.setLocationRelativeTo(null);
        recoveryTaskMenuFrame.setVisible(true);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void closeCurrentMenu() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this.mainPanel);
        currentFrame.dispose();
    }
}

