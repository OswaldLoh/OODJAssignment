package com.mycompany.oodjassignment.academicofficerGUI;
import com.mycompany.oodjassignment.functions.*;
import com.mycompany.oodjassignment.classes.*;
import javax.swing.table.DefaultTableModel;

import javax.swing.*;

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
        loadRecoveryPlans();

        backButton.addActionListener(e -> {
            closeCurrentMenu();
            openMainMenu();
        });
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

    private void loadRecoveryPlans() {
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
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void openMainMenu() {
        JFrame mainMenuFrame = new JFrame("Academic Officer System");
        AcademicOfficerGUI academicOfficerMainMenu = new AcademicOfficerGUI(database);
        mainMenuFrame.setContentPane(academicOfficerMainMenu.getMainPanel());
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setSize(800, 400);
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
