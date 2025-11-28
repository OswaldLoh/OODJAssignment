package com.mycompany.oodjassignment.academicofficerGUI;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        studentCourseSelectionPanel = new JPanel();
        studentCourseSelectionPanel.setLayout(new GridLayoutManager(3, 3, new Insets(20, 20, 20, 20), -1, -1));
        studentCourseSelectionPanel.setBackground(new Color(-1));
        chooseGradePrompt = new JLabel();
        chooseGradePrompt.setText("Please choose student grades to add Recovery Plan:");
        studentCourseSelectionPanel.add(chooseGradePrompt, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gradeScroll = new JScrollPane();
        studentCourseSelectionPanel.add(gradeScroll, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        gradeList = new JTable();
        gradeScroll.setViewportView(gradeList);
        confirmButton = new JButton();
        confirmButton.setText("Confirm");
        studentCourseSelectionPanel.add(confirmButton, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        studentCourseSelectionPanel.add(spacer1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setText("Back");
        studentCourseSelectionPanel.add(backButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return studentCourseSelectionPanel;
    }

}
