package com.mycompany.oodjassignment.academicofficerGUI;

import com.mycompany.oodjassignment.functions.*;
import com.mycompany.oodjassignment.classes.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class CourseSelectionMenu {
    private DefaultTableModel tableModel;
    private Database database;
    private JPanel studentCourseSelectionPanel;
    private JTable gradeList;
    private JButton confirmButton;
    private JButton backButton;
    private JLabel chooseGradePrompt;
    private JScrollPane gradeScroll;

    public CourseSelectionMenu(String targetStudentID, Database database) {
        this.database = database;

        tableSetup();
        loadStudentGrades(targetStudentID);

        confirmButton.addActionListener(e -> {
            addPlan(targetStudentID);
        });

        backButton.addActionListener(e -> {
            closeCurrentMenu();
            openAddPlanDashboard();
        });
    }

    private void addPlan(String targetStudentID) {
        int row = gradeList.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(studentCourseSelectionPanel,
                    "Please select a student grade first.",
                    "Error.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courseID = (String) tableModel.getValueAt(row, 0);
        double GPA = (double) tableModel.getValueAt(row, 2);

        if (GPA >= 2.0) {
            JOptionPane.showMessageDialog(studentCourseSelectionPanel,
                    "Student is not eligible for recovery plan for course " + courseID + ".\n" +
                            "Student GPA is higher than 2.0." + " (" + GPA + ")",
                    "Ineligible", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        IDManager idManager = new IDManager(database.getRecPlanDB());       // Generate new Plan ID
        idManager.getHighestTaskID();
        String nextPlanID = "P" + idManager.generateNewID();
        RecoveryPlan newPlan = new RecoveryPlan(nextPlanID, targetStudentID, courseID, "A01", "0.00");
        database.addRecoveryPlan(newPlan);
        FileHandler.writeCSV(newPlan, database.getRecPlanDB());

        JOptionPane.showMessageDialog(studentCourseSelectionPanel,
                "Recovery Plan added!",
                "Success!", JOptionPane.INFORMATION_MESSAGE);

        closeCurrentMenu();
        openAddRecoveryTask(nextPlanID);
    }

    private void tableSetup() {
        String[] columnNames = {"Course ID", "Course Name", "GPA", "Grade"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            // Make cells un-editable (optional, but recommended)
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void loadStudentGrades(String targetStudentID) {
        tableModel.setRowCount(0);
        ArrayList<Grades> studentGrades = database.getStudentAllGrades(targetStudentID);
        for (Grades grade : studentGrades) {
            Course course = database.getCourse(grade.getCourseID());
            grade.setCourseObject(course);
            Object[] row = {
                    grade.getCourseID(),
                    course.getCourseName(),
                    grade.calculateGPA(),
                    grade.getLetterGrade()
            };
            tableModel.addRow(row);
        }

        gradeList.setModel(tableModel);
        gradeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void openAddRecoveryTask(String targetPlanID) {
        JFrame addRecoveryTaskFrame = new JFrame("Academic Officer System");
        AddRecoveryTask addRecoveryTask = new AddRecoveryTask(targetPlanID, database, true);
        addRecoveryTaskFrame.setContentPane(addRecoveryTask.getAddRecoveryTaskPanel());
        addRecoveryTaskFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addRecoveryTaskFrame.setSize(520, 230);
        addRecoveryTaskFrame.setLocationRelativeTo(null);
        addRecoveryTaskFrame.setVisible(true);
    }

    private void openAddPlanDashboard() {
        JFrame addPlanDashboardFrame = new JFrame("Academic Officer System");
        StudentSelectionDashboard studentSelectionDashboard = new StudentSelectionDashboard(database);
        addPlanDashboardFrame.setContentPane((studentSelectionDashboard.getAddPlanDashboardPanel()));
        addPlanDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addPlanDashboardFrame.setSize(800, 400);
        addPlanDashboardFrame.setLocationRelativeTo(null);
        addPlanDashboardFrame.setVisible(true);
    }

    public JPanel getStudentCourseSelectionPanel() {
        return studentCourseSelectionPanel;
    }

    private void closeCurrentMenu() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(studentCourseSelectionPanel);
        currentFrame.dispose();
    }

}
