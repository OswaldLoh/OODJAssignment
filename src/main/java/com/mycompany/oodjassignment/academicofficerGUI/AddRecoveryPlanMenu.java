package com.mycompany.oodjassignment.academicofficerGUI;
import com.mycompany.oodjassignment.functions.*;
import com.mycompany.oodjassignment.classes.*;

import javax.swing.*;
import java.util.ArrayList;

public class AddRecoveryPlanMenu {
    private String userID;
    private Database database;
    private JTextField txtStudentID;
    private JButton addPlanButton;
    private JPanel AddRecoveryPlanPanel;
    private JLabel AddRecoveryPlanTitle;
    private JLabel studentIDPrompt;
    private JTextField txtCourseID;
    private JLabel courseIDPrompt;
    private JButton backButton;
    private JTextArea textEligibleBox;
    private JLabel studentEligibleText;

    // constructor
    public AddRecoveryPlanMenu(Database database) {
        this.database  = database;
        textEligibleBox.setEditable(false);

        addPlanButton.addActionListener(e -> {
            addPlan(userID);

        });
        backButton.addActionListener(e -> {
            closeCurrentMenu();
            openMainMenu();
        });


    }

    private void addPlan(String userID) {
        String targetStudentID = txtStudentID.getText().trim();
        String targetCourseID = txtCourseID.getText().trim();

        if (targetStudentID.isEmpty()) {
            JOptionPane.showMessageDialog(AddRecoveryPlanPanel,"Please enter a Student ID.");
            return;
        }
        if (!database.studentExist(targetStudentID)) {
            txtStudentID.setText("");
            JOptionPane.showMessageDialog(AddRecoveryPlanPanel,
                    "Student ID " + targetStudentID + " not found in database.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (targetCourseID.isEmpty()) {
            JOptionPane.showMessageDialog(AddRecoveryPlanPanel,"Please enter a Student ID.");
            return;
        }
        if (!database.courseExist(targetCourseID)) {
            txtCourseID.setText("");
            JOptionPane.showMessageDialog(AddRecoveryPlanPanel,
                    "Course ID " + targetStudentID + " not found in database.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ArrayList<String> studentCourse = database.getStudentCourse(targetStudentID);
        ArrayList<Grades> studentGrades = database.getStudentAllGrades(targetStudentID);
        ArrayList<RecoveryPlan> studentExistingPlans = database.getStudentRecoveryPlan(targetStudentID);

        if (!studentCourse.contains(targetCourseID)) {
            JOptionPane.showMessageDialog(AddRecoveryPlanPanel,
                    "Student '" + targetStudentID + "' is not registered to course " + targetCourseID ,
                    "Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (studentExistingPlans != null) {
            for (RecoveryPlan plan : studentExistingPlans) {
                if (plan.getCourseID().equals(targetCourseID)) {
                    JOptionPane.showMessageDialog(AddRecoveryPlanPanel,
                            "Student already has an active Recovery Plan for " + targetCourseID + ".",
                            "Duplicate Plan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        }
        boolean eligible = false;
        double studentGPA = 0;
        for (Grades grade : studentGrades) {
            if (grade.getCourseID().equals(targetCourseID)) {
                grade.setCourseObject(database.getCourse(targetCourseID));
                studentGPA = grade.calculateGPA();
                if (studentGPA < 2.0) {
                    eligible = true;
                }
                break;
            }
        }

        if (!eligible) {
            JOptionPane.showMessageDialog(AddRecoveryPlanPanel,
                    "Student is not eligible for a recovery plan.\n" +
                            "Course: " + targetCourseID + "\n" +
                            "GPA: " + studentGPA,
                    "", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        IDManager idManager = new IDManager(database.getRecPlanDB());       // Generate new Plan ID
        idManager.getHighestTaskID();
        String nextPlanID = "P" + idManager.generateNewID();
        RecoveryPlan newPlan = new RecoveryPlan(nextPlanID, targetStudentID, targetCourseID, "A01", "0.00");
        database.addRecoveryPlan(newPlan);
        FileHandler.writeCSV(newPlan, database.getRecPlanDB());

        JOptionPane.showMessageDialog(AddRecoveryPlanPanel,
                "Recovery Plan added!",
                "Success!",JOptionPane.INFORMATION_MESSAGE);

        closeCurrentMenu();
        openAddRecoveryTaskPanel(nextPlanID);
    }

    private void openMainMenu() {
        JFrame mainMenuFrame = new JFrame("Academic Officer System");
        AcademicOfficerGUI academicOfficerMainMenu = new AcademicOfficerGUI(database);
        mainMenuFrame.setContentPane(academicOfficerMainMenu.getMainPanel());
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setSize(800, 600);
        mainMenuFrame.setLocationRelativeTo(null); // Center it
        mainMenuFrame.setVisible(true);
    }

    private void openAddRecoveryTaskPanel(String targetPlanID) {
        JFrame addRecoveryTaskFrame= new JFrame("Academic Officer System");
        AddRecoveryTaskMenu addRecoveryTaskMenu = new AddRecoveryTaskMenu(database,targetPlanID,true);
        addRecoveryTaskFrame.setContentPane(addRecoveryTaskMenu.getAddRecoveryTaskPanel());
        addRecoveryTaskFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addRecoveryTaskFrame.setSize(800,600);
        addRecoveryTaskFrame.setLocationRelativeTo(null);
        addRecoveryTaskFrame.setVisible(true);
    }

    private void closeCurrentMenu() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(AddRecoveryPlanPanel);
        currentFrame.dispose();
    }

    public JPanel getAddRecoveryPlanPanel() {
        return AddRecoveryPlanPanel;
    }
}


