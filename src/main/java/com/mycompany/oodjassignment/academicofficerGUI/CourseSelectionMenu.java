package com.mycompany.oodjassignment.academicofficerGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.mycompany.oodjassignment.classes.Course;
import com.mycompany.oodjassignment.classes.Grades;
import com.mycompany.oodjassignment.classes.RecoveryPlan;
import com.mycompany.oodjassignment.classes.RecoveryTask;
import com.mycompany.oodjassignment.classes.Student;
import com.mycompany.oodjassignment.functions.Database;
import com.mycompany.oodjassignment.functions.FileHandler;
import com.mycompany.oodjassignment.functions.IDManager;
import com.mycompany.oodjassignment.functions.SendEmail;
import com.mycompany.oodjassignment.functions.TableSorter;
import com.mycompany.oodjassignment.functions.TaskGenerator;
import com.mycompany.oodjassignment.usermanagement.service.AuthenticationService;

public class CourseSelectionMenu {
    private final Runnable onExitCallback;
    private AuthenticationService authService;
    private DefaultTableModel tableModel;
    private Database database;
    private JPanel studentCourseSelectionPanel;
    private JTable gradeList;
    private JButton confirmButton;
    private JButton backButton;
    private JScrollPane gradeScroll;
    public JTextArea showStudentTxtArea;

    public CourseSelectionMenu(String targetStudentID, Database database, Runnable onExitCallback, AuthenticationService authService) {
        this.onExitCallback = onExitCallback;
        this.authService = authService;
        this.database = database;

        showStudentTxtArea.setText("Showing grades for Student \"" + targetStudentID + "\" - Please choose grade to add Recovery Plan:");
        showStudentTxtArea.setEditable(false);
        tableSetup();
        loadStudentGrades(targetStudentID);

        confirmButton.addActionListener(e -> {
            addPlan(targetStudentID);
            openRecoveryPlanDashboard();
            closeCurrentMenu();
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
        double GPA = (double) tableModel.getValueAt(row, 3);

        if (GPA >= 2.0) {
            JOptionPane.showMessageDialog(studentCourseSelectionPanel,
                    "Student is not eligible for recovery plan for course " + courseID + ".\n" +
                            "Student GPA is higher than 2.0." + " (" + GPA + ")",
                    "Ineligible", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        boolean duplicatePlan = database.checkExistingRecoveryPLan(targetStudentID, courseID);
        if (duplicatePlan) {
            JOptionPane.showMessageDialog(studentCourseSelectionPanel,
                    "Student already has recovery plan for course " + courseID + ".\n",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        IDManager idManager = new IDManager(database.getRecPlanDB());       // Generate new Plan ID
        idManager.getHighestTaskID();
        String component = database.getRecoveryPlanComponent(targetStudentID, courseID);
        String nextPlanID = "P" + idManager.generateNewID();
        String userID = authService.getCurrentUser().getUserId();
        RecoveryPlan newPlan = new RecoveryPlan(nextPlanID, targetStudentID, courseID, component, userID, "0.00");
        database.addRecoveryPlan(newPlan);
        FileHandler.writeCSV(newPlan, database.getRecPlanDB());

        // Send email notification to the student
        Student student = database.getStudent(targetStudentID);
        SendEmail sendEmail = new SendEmail(student.getEmail());
        String emailSubject = "New Recovery Plan Created";
        String emailContent = "Dear " + student.getFirstName() + " " + student.getLastName() + ",\n\n" + 
                "A new recovery plan has been created for you in the course: " + courseID + "\n\n" +   
                "Plan Details:" + "\n" +
                "Plan ID: " + nextPlanID + "\n" + 
                "Course: " + courseID + "\n" + 
                "Component: " + component + "\n" + 
                "Created by: Academic Officer" + "\n" +
                "Please check your recovery plan dashboard for more details and upcoming tasks." + "\n\n" +
                "Best regards," + "\n" + 
                "Academic Officer Team";

        // Send email in a separate thread to prevent GUI freezing
        new Thread(() -> {
            sendEmail.Notification(emailSubject, emailContent);
        }).start();

        JOptionPane.showMessageDialog(studentCourseSelectionPanel,
                "Recovery Plan added!",
                "Success!", JOptionPane.INFORMATION_MESSAGE);

        initializeTasks(component, nextPlanID);
    }

    private void initializeTasks(String component, String targetPlanID) {
        List<String> taskDescriptions = TaskGenerator.getTaskDescriptions(component);
        IDManager idManager = new IDManager(database.getRecTaskDB());
        int newWeek = 1;
        idManager.getHighestTaskID();
        for (String description : taskDescriptions) {
            String newTaskID = "T" + idManager.generateNewID();
            RecoveryTask newTask = new RecoveryTask(newTaskID, targetPlanID, description, newWeek, false);
            newWeek = newWeek + 4;
            database.addRecoveryTask(newTask);
            FileHandler.writeCSV(newTask, database.getRecTaskDB());
        }

    }

    private void tableSetup() {
        String[] columnNames = {"Course ID", "Course Name", "Attempt", "GPA", "Grade"};
        tableModel = new DefaultTableModel(columnNames, 0) {
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
                    grade.getAttempt(),
                    grade.calculateGPA(),
                    grade.getLetterGrade()
            };
            tableModel.addRow(row);
        }

        gradeList.setModel(tableModel);
        TableSorter sorter = new TableSorter(tableModel, gradeList);
        gradeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void openRecoveryPlanDashboard() {
        JFrame recoveryPlanDashboardFrame = new JFrame("Academic Officer System");
        RecoveryPlanDashboard recoveryPlanDashboard = new RecoveryPlanDashboard(database, onExitCallback, authService);
        recoveryPlanDashboardFrame.setContentPane(recoveryPlanDashboard.getRecoveryPlanDashboardPanel());
        recoveryPlanDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recoveryPlanDashboardFrame.setSize(1100, 400);
        recoveryPlanDashboardFrame.setLocationRelativeTo(null);
        recoveryPlanDashboardFrame.setVisible(true);
    }

    private void openAddPlanDashboard() {
        JFrame addPlanDashboardFrame = new JFrame("Academic Officer System");
        StudentSelectionDashboard studentSelectionDashboard = new StudentSelectionDashboard(database, onExitCallback, authService);
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
        showStudentTxtArea = new JTextArea();
        Font showStudentTxtAreaFont = this.$$$getFont$$$("Dialog", -1, -1, showStudentTxtArea.getFont());
        if (showStudentTxtAreaFont != null) showStudentTxtArea.setFont(showStudentTxtAreaFont);
        studentCourseSelectionPanel.add(showStudentTxtArea, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return studentCourseSelectionPanel;
    }

}
