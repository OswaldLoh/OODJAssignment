package com.mycompany.oodjassignment.academicofficerGUI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.mycompany.oodjassignment.functions.*;
import com.mycompany.oodjassignment.classes.*;
import com.mycompany.oodjassignment.usermanagement.service.AuthenticationService;

import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;


public class RecoveryPlanDashboard {
    private final Runnable onExitCallback;
    private AuthenticationService authService;
    private String userID;
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

    public RecoveryPlanDashboard(Database database, Runnable onExitCallback, AuthenticationService authService) {
        this.onExitCallback = onExitCallback;
        this.database = database;
        this.authService = authService;

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

        txtPlanID.addActionListener(e -> {
            search();
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
                    "Please select a recovery plan first.",
                    "Error.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = planListTable.convertRowIndexToModel(row);
        String targetPlanID = (String) tableModel.getValueAt(modelRow, 0);
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
        JOptionPane.showMessageDialog(recoveryPlanDashboardPanel, progressMonitorMessage);
    }

    private void addTask() {
        int row = planListTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(recoveryPlanDashboardPanel,
                    "Please select a recovery plan first.",
                    "Error.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        closeCurrentMenu();
        int modelRow = planListTable.convertRowIndexToModel(row);
        String targetPlanID = (String) tableModel.getValueAt(modelRow, 0);
        openAddRecoveryTask(targetPlanID);
    }

    private void deletePlan() {
        int taskCount = 1, taskDeleteConfirmation = 0;
        int row = planListTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(recoveryPlanDashboardPanel,
                    "Please select a plan first.",
                    "Error.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = planListTable.convertRowIndexToModel(row);
        String planID = (String) tableModel.getValueAt(modelRow, 0);
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
                    taskDeletionMessage, "Confirm Delete", JOptionPane.YES_NO_OPTION);
        } else {
            return;
        }
        ;

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
        FileHandler.writeCSV(recPlan, database.getRecPlanDB());
        FileHandler.writeCSV(recTask, database.getRecTaskDB());
    }


    private void tableSetup() {
        String[] columnNames = {"Plan ID", "Student ID", "Course ID", "Created By", "Progress"};
        tableModel = new DefaultTableModel(columnNames, 0) {
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
                    String.format("%.1f", plan.getProgress())
            };
            tableModel.addRow(row);
        }
        planListTable.setModel(tableModel);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
        planListTable.setRowSorter(sorter);
        sorter.setComparator(0, (a, b) -> {
            int n1 = Integer.parseInt(a.toString().substring(1));
            int n2 = Integer.parseInt(b.toString().substring(1));
            return Integer.compare(n1, n2);
        });
        planListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


    }


    public JPanel getRecoveryPlanDashboardPanel() {
        return recoveryPlanDashboardPanel;
    }

    private void openMainMenu() {
        JFrame mainMenuFrame = new JFrame("Academic Officer System");
        CourseRecovery courseRecovery = new CourseRecovery(database, onExitCallback, authService);
        mainMenuFrame.setContentPane(courseRecovery.getCourseRecoveryPanel());
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setSize(550, 400);
        mainMenuFrame.setLocationRelativeTo(null); // Center it
        mainMenuFrame.setVisible(true);
    }

    private void openStudentSelectionDashboard() {
        JFrame addPlanDashboardFrame = new JFrame("Academic Officer System");
        StudentSelectionDashboard studentSelectionDashboard = new StudentSelectionDashboard(database, onExitCallback, authService);
        addPlanDashboardFrame.setContentPane((studentSelectionDashboard.getAddPlanDashboardPanel()));
        addPlanDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addPlanDashboardFrame.setSize(800, 400);
        addPlanDashboardFrame.setLocationRelativeTo(null);
        addPlanDashboardFrame.setVisible(true);
    }

    private void openAddRecoveryTask(String targetPlanID) {
        JFrame addRecoveryTaskFrame = new JFrame("Academic Officer System");
        AddRecoveryTask addRecoveryTask = new AddRecoveryTask(targetPlanID, authService, onExitCallback, database, false);
        addRecoveryTaskFrame.setContentPane(addRecoveryTask.getAddRecoveryTaskPanel());
        addRecoveryTaskFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addRecoveryTaskFrame.setSize(520, 230);
        addRecoveryTaskFrame.setLocationRelativeTo(null);
        addRecoveryTaskFrame.setVisible(true);
    }

    private void closeCurrentMenu() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(recoveryPlanDashboardPanel);
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
        recoveryPlanDashboardPanel = new JPanel();
        recoveryPlanDashboardPanel.setLayout(new GridLayoutManager(5, 22, new Insets(20, 20, 20, 20), -1, -1));
        recoveryPlanDashboardPanel.setBackground(new Color(-1));
        recoveryPlanDashboardTitle = new JLabel();
        recoveryPlanDashboardTitle.setText("Recovery Plan Dashboard");
        recoveryPlanDashboardPanel.add(recoveryPlanDashboardTitle, new GridConstraints(0, 0, 1, 22, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        planListScroll = new JScrollPane();
        recoveryPlanDashboardPanel.add(planListScroll, new GridConstraints(3, 0, 1, 22, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        planListTable = new JTable();
        planListScroll.setViewportView(planListTable);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        recoveryPlanDashboardPanel.add(panel1, new GridConstraints(1, 11, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        searchPrompt = new JLabel();
        searchPrompt.setText("Search by PlanID:");
        recoveryPlanDashboardPanel.add(searchPrompt, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtPlanID = new JTextField();
        recoveryPlanDashboardPanel.add(txtPlanID, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        addPlanButton = new JButton();
        addPlanButton.setText("Create New Plan");
        recoveryPlanDashboardPanel.add(addPlanButton, new GridConstraints(4, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deletePlanButton = new JButton();
        deletePlanButton.setText("Delete Recovery Plan");
        recoveryPlanDashboardPanel.add(deletePlanButton, new GridConstraints(4, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        searchButton = new JButton();
        searchButton.setText("Search");
        recoveryPlanDashboardPanel.add(searchButton, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addTaskButton = new JButton();
        addTaskButton.setText("Add Recovery Task");
        recoveryPlanDashboardPanel.add(addTaskButton, new GridConstraints(4, 21, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setText("Back");
        recoveryPlanDashboardPanel.add(backButton, new GridConstraints(2, 21, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        monitorProgressButton = new JButton();
        monitorProgressButton.setText("Monitor Progress");
        recoveryPlanDashboardPanel.add(monitorProgressButton, new GridConstraints(4, 20, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return recoveryPlanDashboardPanel;
    }

}

