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
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class RecoveryTasksDashboard {
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

    public RecoveryTasksDashboard(Database database) {
        this.database = database;

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
            if (task.getTaskID().toLowerCase().contains(searchText.toLowerCase())) {
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
                        task.getDuration(),
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
        String[] columnNames = {"Task ID", "Plan ID", "Description", "Duration (days)", "Progress"};
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
        String targetTaskID = (String) tableModel.getValueAt(row, 0);
        String mode = modifySelection();
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
        String targetTaskID = (String) tableModel.getValueAt(row, 0);
        String targetPlanID = (String) tableModel.getValueAt(row, 1);

        ArrayList<RecoveryTask> planTasks = database.getPlanRecoveryTask(targetPlanID);
        int taskCount = planTasks.size();

        if (taskCount == 1) {
            JOptionPane.showMessageDialog(RecoveryTasksPanel,
                    "Warning! You cannot delete the last recovery task in recovery plan " + targetPlanID + "!",
                    "Error.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        database.removeRecoveryTask(targetTaskID);
        database.updatePlanProgress(targetPlanID);
        tableModel.removeRow(row);
        RecoveryTask recTask = new RecoveryTask();
        RecoveryPlan recPlan = new RecoveryPlan();
        FileHandler.writeCSV(recPlan, database.getRecPlanDB());
        FileHandler.writeCSV(recTask, database.getRecTaskDB());
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
                    task.getDuration(),
                    completionStatus
            };
            tableModel.addRow(row);
        }
        taskTable.setModel(tableModel);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
        taskTable.setRowSorter(sorter);
        sorter.setComparator(0, (a, b) -> {
            int n1 = Integer.parseInt(a.toString().substring(1));
            int n2 = Integer.parseInt(b.toString().substring(1));
            return Integer.compare(n1, n2);
        });
        sorter.setComparator(1, (a, b) -> {
            int n1 = Integer.parseInt(a.toString().substring(1));
            int n2 = Integer.parseInt(b.toString().substring(1));
            return Integer.compare(n1, n2);
        });
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private String modifySelection() {
        String[] options = {"Description", "Duration", "Completion Status"};
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
        ModifyTaskMenu modifyTaskMenu = new ModifyTaskMenu(targetTaskID, mode, database);
        modifyTaskMenuFrame.setContentPane(modifyTaskMenu.getModifyTaskPanel());
        modifyTaskMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        modifyTaskMenuFrame.setSize(800, 400);
        modifyTaskMenuFrame.setLocationRelativeTo(null); // Center it
        modifyTaskMenuFrame.setVisible(true);
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

    private void closeCurrentMenu() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(RecoveryTasksPanel);
        currentFrame.dispose();
    }

    public JPanel getRecoveryTasksPanel() {
        return RecoveryTasksPanel;
    }

}
