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
    private JButton Search;
    private JTextField txtPlanID;
    private JButton modifyTasksButton;
    private JButton deletePlanButton;
    private JButton addPlanButton;
    private JLabel recoveryPlanDashboardTitle;
    private JScrollPane planListScroll;
    private JButton backButton;

    public RecoveryPlanDashboard(Database database) {
        this.database = database;

        tableSetup();
        loadRecoveryPlans();

        addPlanButton.addActionListener(e -> {
            closeCurrentMenu();
            openAddPlanDashboard();
        });

        deletePlanButton.addActionListener(e -> {
            deletePlan();
        });

        modifyTasksButton.addActionListener(e -> {


        });

        backButton.addActionListener(e -> {
            closeCurrentMenu();
            openMainMenu();
        });
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
        AcademicOfficerGUI academicOfficerMainMenu = new AcademicOfficerGUI(database);
        mainMenuFrame.setContentPane(academicOfficerMainMenu.getMainPanel());
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setSize(800, 600);
        mainMenuFrame.setLocationRelativeTo(null); // Center it
        mainMenuFrame.setVisible(true);
    }

    private void openAddPlanDashboard() {
        JFrame addPlanDashboardFrame = new JFrame("Academic Officer System");
        AddPlanDashboard addPlanDashboard = new AddPlanDashboard(database);
        addPlanDashboardFrame.setContentPane((addPlanDashboard.getAddPlanDashboardPanel()));
        addPlanDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addPlanDashboardFrame.setSize(800,600);
        addPlanDashboardFrame.setLocationRelativeTo(null);
        addPlanDashboardFrame.setVisible(true);
    }


    private void closeCurrentMenu() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(recoveryPlanDashboardPanel);
        currentFrame.dispose();
    }
}

