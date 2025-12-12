package com.mycompany.oodjassignment.AcademicOfficerGUI;
import com.mycompany.oodjassignment.Classes.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.mycompany.oodjassignment.Classes.RecoveryTask;
import com.mycompany.oodjassignment.Helpers.Database;
import com.mycompany.oodjassignment.Helpers.TableSorter;
import com.mycompany.oodjassignment.usermanagement.service.AuthenticationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MonitorProgress {
    private DefaultTableModel tableModel;
    private final AuthenticationService authService;
    private final Database database;
    private final Runnable onExitCallback;
    public JPanel monitorProgressPanel;
    public JPanel topPanel;
    public JLabel topTitle;
    public JTable milestoneTable;
    public JButton backButton;
    public JTextArea analyticsTextArea;
    public JTextArea viewingStudentBox;
    public JScrollPane scrollPane;
    public JPanel viewBoxPanel;
    public JPanel analyticsPanel;
    public JPanel buttonPanel;

    public MonitorProgress(AuthenticationService authenticationService, Runnable onExitCallback, Database database, String targetPlanID) {
        this.authService = authenticationService;
        this.onExitCallback = onExitCallback;
        this.database = database;

        tableSetup();
        loadRecoveryTasks(targetPlanID);
        loadStudentDetails(targetPlanID);

        backButton.addActionListener(e -> {
            openRecoveryPlanDashboard();
            closeCurrentMenu();
        });


    }

    private void loadStudentDetails(String targetPlanID) {
        RecoveryPlan targetPlan = database.getRecoveryPlan(targetPlanID);
        Student targetStudent = database.getStudent(targetPlan.getStudentID());
        Course targetCourse = database.getCourse(targetPlan.getCourseID());
        String studentText = "Student ID: " + targetStudent.getStudentID() + "\n" +
                "Name: " + targetStudent.getFirstName() + " " + targetStudent.getLastName() + "\n" +
                "Major: " + targetStudent.getMajor() + "\n" +
                "Year: " + targetStudent.getYear() + "\n" +
                "Viewing Recovery Milestones for Course " + targetCourse.getCourseID() + " - " + targetCourse.getCourseName() + ":";

        viewingStudentBox.setText(studentText);
        viewingStudentBox.setEditable(false);
        String analyticsText = "Overall Completion Rate: " + targetPlan.getProgress() + "%";
        analyticsTextArea.setText(analyticsText);
        analyticsTextArea.setEditable(false);
    }

    private void loadRecoveryTasks(String targetPlanID) {
        tableModel.setRowCount(0);

        for (RecoveryTask task : database.getRecTaskDB().values()) {
            if (task.getPlanID().equals(targetPlanID)) {
                String completionStatus;
                if (task.getCompletion()) {
                    completionStatus = "Completed";
                } else {
                    completionStatus = "Incomplete";
                }
                Object[] row = {
                        task.getTaskID(),
                        task.getPlanID(),
                        task.getDescription(),
                        task.getWeek(),
                        completionStatus
                };
                tableModel.addRow(row);
            }
        }
        milestoneTable.setModel(tableModel);
        milestoneTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        milestoneTable.getColumnModel().getColumn(1).setPreferredWidth(70);
        milestoneTable.getColumnModel().getColumn(2).setPreferredWidth(400);
        milestoneTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        milestoneTable.getColumnModel().getColumn(4).setPreferredWidth(100);

        TableSorter sorter = new TableSorter(tableModel, milestoneTable);


        milestoneTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void tableSetup() {
        String[] columnNames = {"Task ID", "Plan ID", "Description", "Week", "Progress"};
        tableModel = new DefaultTableModel(columnNames, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
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

    private void closeCurrentMenu() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(monitorProgressPanel);
        currentFrame.dispose();
    }

    public JPanel getMonitorProgressPanel() {
        return monitorProgressPanel;
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
        monitorProgressPanel = new JPanel();
        monitorProgressPanel.setLayout(new GridLayoutManager(4, 1, new Insets(20, 20, 20, 20), -1, -1));
        monitorProgressPanel.setBackground(new Color(-1));
        topPanel = new JPanel();
        topPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        monitorProgressPanel.add(topPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        topTitle = new JLabel();
        topTitle.setHorizontalAlignment(0);
        topTitle.setHorizontalTextPosition(0);
        topTitle.setText("Monitor Recovery Milestones");
        topPanel.add(topTitle, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        viewBoxPanel = new JPanel();
        viewBoxPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        topPanel.add(viewBoxPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        viewingStudentBox = new JTextArea();
        viewBoxPanel.add(viewingStudentBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        scrollPane = new JScrollPane();
        monitorProgressPanel.add(scrollPane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        milestoneTable = new JTable();
        scrollPane.setViewportView(milestoneTable);
        analyticsPanel = new JPanel();
        analyticsPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        monitorProgressPanel.add(analyticsPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        analyticsTextArea = new JTextArea();
        analyticsPanel.add(analyticsTextArea, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        monitorProgressPanel.add(buttonPanel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setText("Back");
        buttonPanel.add(backButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        buttonPanel.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return monitorProgressPanel;
    }

}
