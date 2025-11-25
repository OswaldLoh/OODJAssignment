package com.mycompany.oodjassignment.academicofficerGUI;
import com.mycompany.oodjassignment.functions.*;
import com.mycompany.oodjassignment.classes.*;
import javax.swing.table.DefaultTableModel;

import java.util.*;
import javax.swing.*;


public class RecoveryPlanDashboard {
    private DefaultTableModel tableModel;
    private Database database;
    private JPanel recoveryPlanDashboardPanel;
    private JTable planListTable;
    private JButton searchButton;
    private JTextField txtPlanID;
    private JButton addTaskButton;
    private JButton deletePlanButton;
    private JButton addPlanButton;
    private JLabel recoveryPlanDashboardTitle;
    private JScrollPane planListScroll;
    private JButton backButton;
    private JLabel searchPrompt;
    private JButton monitorProgressButton;

    public RecoveryPlanDashboard(Database database) {
        this.database = database;

        tableSetup();
        loadRecoveryPlans();

        monitorProgressButton.addActionListener(e -> {
            monitorProgress();
        });

        addPlanButton.addActionListener(e -> {
            closeCurrentMenu();
            openStudentSelectionDashboard();
        });

        deletePlanButton.addActionListener(e -> {
            deletePlan();
        });

        addTaskButton.addActionListener(e -> {
            addTask();
        });

        searchButton.addActionListener(e -> {
            search();
        });

        backButton.addActionListener(e -> {
            closeCurrentMenu();
            openMainMenu();
        });
    }

    private void search() {
        String searchText = txtPlanID.getText().trim();

        if (searchText.isEmpty()) {
            loadRecoveryPlans();
            return;
        }
        tableModel.setRowCount(0);
        boolean found = false;

        for (RecoveryPlan plan : database.getRecPlanDB().values()) {
            if (plan.getPlanID().toLowerCase().contains(searchText.toLowerCase())) {
                Object[] row = {
                        plan.getPlanID(),
                        plan.getStudentID(),
                        plan.getCourseID(),
                        plan.getCreatedBy(),
                        plan.getProgress(),
                };
                tableModel.addRow(row);
                found = true;
            }
        }
        if (!found) {
            JOptionPane.showMessageDialog(recoveryPlanDashboardPanel,
                    "No Plan found with ID: " + searchText,
                    "Search Result",
                    JOptionPane.INFORMATION_MESSAGE);
            loadRecoveryPlans();
            txtPlanID.setText("");
        }
    }

    private void monitorProgress() {
        int row = planListTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(recoveryPlanDashboardPanel,
                    "Please select a recovery plan first."  ,
                    "Error.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String targetPlanID = (String) tableModel.getValueAt(row,0);
        RecoveryPlan targetPlan = database.getRecoveryPlan(targetPlanID);
        Student targetStudent = database.getStudent(targetPlan.getStudentID());
        ArrayList<RecoveryTask> planTasks = database.getPlanRecoveryTask(targetPlanID);

        StringBuilder progressMonitorMessage = new StringBuilder();
        progressMonitorMessage.append("                   Recovery Plan Progress\n");
        progressMonitorMessage.append("========================================\n");

        progressMonitorMessage.append(String.format("Student ID  :    %s\n", targetStudent.getStudentID()));
        progressMonitorMessage.append(String.format("Name          :    %s %s\n", targetStudent.getFirstName(), targetStudent.getLastName()));
        progressMonitorMessage.append(String.format("Plan ID        :    %s\n", targetPlanID));
        progressMonitorMessage.append(String.format("Progress    :    %.1f%%\n", targetPlan.getProgress()));
        progressMonitorMessage.append("\n"); // Empty line for separation

        progressMonitorMessage.append(String.format("%-5s %-15s %s\n", "No.", "Task ID", "Status"));
        progressMonitorMessage.append("----------------------------------------\n");

        int taskCount = 1;
        String completionStatus;

        for (RecoveryTask task : planTasks) {
            if (task.getCompletion()) {
                completionStatus = "Completed";
            } else {
                completionStatus = "Incomplete";
            }

            String line = String.format("%-10s %-15s %s\n",
                    taskCount + ".",
                    task.getTaskID(),
                    completionStatus);

            progressMonitorMessage.append(line);
            taskCount++;
        }
        JOptionPane.showMessageDialog(recoveryPlanDashboardPanel,progressMonitorMessage);
    }

    private void addTask() {
        int row = planListTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(recoveryPlanDashboardPanel,
                    "Please select a recovery plan first."  ,
                    "Error.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        closeCurrentMenu();
        String targetPlanID = (String) tableModel.getValueAt(row,0);
        openAddRecoveryTask(targetPlanID);
    }

    private void deletePlan() {
        int taskCount = 1, taskDeleteConfirmation = 0;
        int row = planListTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(recoveryPlanDashboardPanel,
                    "Please select a plan first."  ,
                    "Error.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String planID = (String) tableModel.getValueAt(row,0);
        ArrayList<RecoveryTask> planTasks = database.getPlanRecoveryTask(planID);
        StringBuilder taskDeletionMessage = new StringBuilder();
        taskDeletionMessage.append("Doing so will delete the following recovery tasks registered under " + planID + "!\n\n");
        for (RecoveryTask task : planTasks) {
            taskDeletionMessage.append(taskCount + ". ").append(task.getTaskID()).append("\n");
            taskCount++;
        }
        taskDeletionMessage.append("\nConfirm deletion?");

        int planDeleteConfirmation = JOptionPane.showConfirmDialog(recoveryPlanDashboardPanel,
                "Are you sure you want to delete Plan " + planID + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (planDeleteConfirmation == JOptionPane.YES_OPTION) {
            taskDeleteConfirmation = JOptionPane.showConfirmDialog(recoveryPlanDashboardPanel,
                    taskDeletionMessage,"Confirm Delete", JOptionPane.YES_NO_OPTION);
        } else {
            return;
        };

        if (taskDeleteConfirmation == JOptionPane.YES_OPTION) {
            database.removeRecoveryPlan(planID);
            for (RecoveryTask task : planTasks) {
                database.removeRecoveryTask(task.getTaskID());
            }
            tableModel.removeRow(row);
        } else {
            return;
        }

        RecoveryPlan recPlan = new RecoveryPlan();
        RecoveryTask recTask = new RecoveryTask();
        FileHandler.writeCSV(recPlan,database.getRecPlanDB());
        FileHandler.writeCSV(recTask,database.getRecTaskDB());
    }


    private void tableSetup() {
        String[] columnNames = {"Plan ID", "Student ID", "Course ID", "Created By", "Progress"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            // Make cells un-editable (optional, but recommended)
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void loadRecoveryPlans() {
        tableModel.setRowCount(0);
        for (RecoveryPlan plan : database.getRecPlanDB().values()) {
            Object[] row = {
                    plan.getPlanID(),
                    plan.getStudentID(),
                    plan.getCourseID(),
                    plan.getCreatedBy(),
                    plan.getProgress(),
            };
            tableModel.addRow(row);
        }
        planListTable.setModel(tableModel);
        planListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }


    public JPanel getRecoveryPlanDashboardPanel() {
        return recoveryPlanDashboardPanel;
    }

    private void openMainMenu() {
        JFrame mainMenuFrame = new JFrame("Academic Officer System");
        CourseRecovery courseRecovery = new CourseRecovery(database);
        mainMenuFrame.setContentPane(courseRecovery.getCourseRecoveryPanel());
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setSize(550, 400);
        mainMenuFrame.setLocationRelativeTo(null); // Center it
        mainMenuFrame.setVisible(true);
    }

    private void openStudentSelectionDashboard() {
        JFrame addPlanDashboardFrame = new JFrame("Academic Officer System");
        StudentSelectionDashboard studentSelectionDashboard = new StudentSelectionDashboard(database);
        addPlanDashboardFrame.setContentPane((studentSelectionDashboard.getAddPlanDashboardPanel()));
        addPlanDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addPlanDashboardFrame.setSize(800,400);
        addPlanDashboardFrame.setLocationRelativeTo(null);
        addPlanDashboardFrame.setVisible(true);
    }

    private void openAddRecoveryTask(String targetPlanID) {
        JFrame addRecoveryTaskFrame = new JFrame("Academic Officer System");
        AddRecoveryTask addRecoveryTask = new AddRecoveryTask(targetPlanID, database, false);
        addRecoveryTaskFrame.setContentPane(addRecoveryTask.getAddRecoveryTaskPanel());
        addRecoveryTaskFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addRecoveryTaskFrame.setSize(520,230);
        addRecoveryTaskFrame.setLocationRelativeTo(null);
        addRecoveryTaskFrame.setVisible(true);
    }

    private void closeCurrentMenu() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(recoveryPlanDashboardPanel);
        currentFrame.dispose();
    }
}

