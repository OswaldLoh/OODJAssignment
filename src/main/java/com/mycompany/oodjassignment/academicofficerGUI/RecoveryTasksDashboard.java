package com.mycompany.oodjassignment.academicofficerGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.mycompany.oodjassignment.classes.RecoveryPlan;
import com.mycompany.oodjassignment.classes.RecoveryTask;
import com.mycompany.oodjassignment.classes.Student;
import com.mycompany.oodjassignment.functions.Database;
import com.mycompany.oodjassignment.functions.FileHandler;
import com.mycompany.oodjassignment.functions.SendEmail;
import com.mycompany.oodjassignment.usermanagement.service.AuthenticationService;

public class RecoveryTasksDashboard {
    private final Runnable onExitCallback;
    private AuthenticationService authService;
    private DefaultTableModel tableModel;
    private Database database;
    private JPanel RecoveryTasksPanel;
    private JTable taskTable;
    private JTextField txtTaskID;
    private JButton searchButton;
    private JButton deleteTaskButton;
    private JButton backButton;
    private JLabel promptRecoveryTask;
    private JScrollPane taskScroll;
    private JButton modifyButton;
    private JLabel seartchPrompt;

    public RecoveryTasksDashboard(Database database, Runnable onExitCallback, AuthenticationService authService) {
        this.onExitCallback = onExitCallback;
        this.database = database;
        this.authService = authService;

        tableSetup();
        loadRecoveryTasks();

        txtTaskID.addActionListener(e -> {
            search();
        });
        searchButton.addActionListener(e -> {
            search();
        });
        modifyButton.addActionListener(e -> {
            modifyTask();
        });
        deleteTaskButton.addActionListener(e -> {
            deleteTask();
        });

        backButton.addActionListener(e -> {
            closeCurrentMenu();
            openMainMenu();
        });
    }

    private void search() {
        String searchText = txtTaskID.getText().trim();

        if (searchText.isEmpty()) {
            loadRecoveryTasks();
            return;
        }
        tableModel.setRowCount(0);
        boolean found = false;

        for (RecoveryTask task : database.getRecTaskDB().values()) {
            if (task.getTaskID().toLowerCase().contains(searchText.toLowerCase()) ||
                    task.getPlanID().toLowerCase().contains(searchText.toLowerCase())) {
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
                found = true;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(RecoveryTasksPanel,
                    "No recovery task found with ID: " + searchText,
                    "Search Result",
                    JOptionPane.INFORMATION_MESSAGE);
            loadRecoveryTasks();
            txtTaskID.setText("");
        }
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

    private void modifyTask() {
        int row = taskTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(RecoveryTasksPanel,
                    "Please select a recovery task first.",
                    "Error.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = taskTable.convertRowIndexToModel(row);
        String targetTaskID = (String) tableModel.getValueAt(modelRow, 0);
        String mode = modifySelection();
        if (mode == null) {
            return;
        }
        openModifyTaskMenu(targetTaskID, mode);
        closeCurrentMenu();
    }

    private void deleteTask() {
        int row = taskTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(RecoveryTasksPanel,
                    "Please select a recovery task first.",
                    "Error.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = taskTable.convertRowIndexToModel(row);
        String targetTaskID = (String) tableModel.getValueAt(modelRow, 0);
        String targetPlanID = (String) tableModel.getValueAt(modelRow, 1);

        ArrayList<RecoveryTask> planTasks = database.getPlanRecoveryTask(targetPlanID);
        int taskCount = planTasks.size();

        if (taskCount == 1) {
            JOptionPane.showMessageDialog(RecoveryTasksPanel,
                    "Warning! You cannot delete the last recovery task in recovery plan " + targetPlanID + "!",
                    "Error.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the task before removing it to access its details
        RecoveryTask taskToDelete = database.getRecoveryTask(targetTaskID);
        RecoveryPlan associatedPlan = database.getRecoveryPlan(targetPlanID);
        Student student = database.getStudent(associatedPlan.getStudentID());

        database.removeRecoveryTask(targetTaskID);
        database.updatePlanProgress(targetPlanID);
        tableModel.removeRow(row);
        RecoveryTask recTask = new RecoveryTask();
        RecoveryPlan recPlan = new RecoveryPlan();
        FileHandler.writeCSV(recPlan, database.getRecPlanDB());
        FileHandler.writeCSV(recTask, database.getRecTaskDB());
        
        // Send email notification about task deletion
        SendEmail sendEmail = new SendEmail(student.getEmail());
        String emailSubject = "Recovery Task Deleted";
        String emailContent = "Dear " + student.getFirstName() + ",\n\n" +
                "A recovery task has been removed from your course recovery plan (Plan ID: " + targetPlanID + ").\n\n" +
                "Task ID: " + taskToDelete.getTaskID() + "\n" +
                "Task Description: " + taskToDelete.getDescription() + "\n" +
                "Week: " + taskToDelete.getWeek() + "\n\n" +
                "Please note that this task is no longer required as part of your recovery plan.\n\n" +
                "Best regards,\n" +
                "Academic Officer Team";
                
        // using new thread prevent GUI freezing
        new Thread(() ->
                sendEmail.Notification(emailSubject,emailContent)
        ).start();

        JOptionPane.showMessageDialog(RecoveryTasksPanel, "Recovery Task deleted successfully!");
    }

    private void loadRecoveryTasks() {
        tableModel.setRowCount(0);

        for (RecoveryTask task : database.getRecTaskDB().values()) {
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
        taskTable.setModel(tableModel);
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        taskTable.getColumnModel().getColumn(1).setPreferredWidth(70);
        taskTable.getColumnModel().getColumn(2).setPreferredWidth(400);
        taskTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        taskTable.getColumnModel().getColumn(4).setPreferredWidth(100);

        TableSorter sorter = new TableSorter(tableModel, taskTable);
        sorter.sortTable(0, "ID");

        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private String modifySelection() {
        String[] options = {"Description", "Week", "Completion Status"};
        String selectedOption = (String) JOptionPane.showInputDialog(
                RecoveryTasksPanel,                 // Parent
                "Choose data to modify.",        // Message
                "Selection",                  // Title
                JOptionPane.QUESTION_MESSAGE,    // Icon type
                null,                            // Custom Icon (none)
                options,                         // The Array of options
                options[0]                       // The default selection
        );

        if (selectedOption != null) {
            return selectedOption;
        } else {
            return null;
        }
    }

    private void openModifyTaskMenu(String targetTaskID, String mode) {
        JFrame modifyTaskMenuFrame = new JFrame("Academic Officer System");
        ModifyTaskMenu modifyTaskMenu = new ModifyTaskMenu(targetTaskID, mode, database, authService, onExitCallback);
        modifyTaskMenuFrame.setContentPane(modifyTaskMenu.getModifyTaskPanel());
        modifyTaskMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        modifyTaskMenuFrame.setSize(400, 200);
        modifyTaskMenuFrame.setLocationRelativeTo(null); // Center it
        modifyTaskMenuFrame.setVisible(true);
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

    private void closeCurrentMenu() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(RecoveryTasksPanel);
        currentFrame.dispose();
    }

    public JPanel getRecoveryTasksPanel() {
        return RecoveryTasksPanel;
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
        RecoveryTasksPanel = new JPanel();
        RecoveryTasksPanel.setLayout(new GridLayoutManager(5, 4, new Insets(20, 20, 20, 20), -1, -1));
        RecoveryTasksPanel.setBackground(new Color(-1));
        promptRecoveryTask = new JLabel();
        promptRecoveryTask.setText("Recovery Tasks Dashboard");
        RecoveryTasksPanel.add(promptRecoveryTask, new GridConstraints(0, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        taskScroll = new JScrollPane();
        RecoveryTasksPanel.add(taskScroll, new GridConstraints(3, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        taskTable = new JTable();
        taskScroll.setViewportView(taskTable);
        deleteTaskButton = new JButton();
        deleteTaskButton.setText("Delete Task");
        RecoveryTasksPanel.add(deleteTaskButton, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        modifyButton = new JButton();
        modifyButton.setText("Modify Tasks");
        RecoveryTasksPanel.add(modifyButton, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setText("Back");
        RecoveryTasksPanel.add(backButton, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        searchButton = new JButton();
        searchButton.setText("Search");
        RecoveryTasksPanel.add(searchButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtTaskID = new JTextField();
        RecoveryTasksPanel.add(txtTaskID, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        seartchPrompt = new JLabel();
        seartchPrompt.setText("Search by TaskID or PlanID:");
        RecoveryTasksPanel.add(seartchPrompt, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        RecoveryTasksPanel.add(spacer1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return RecoveryTasksPanel;
    }

}
