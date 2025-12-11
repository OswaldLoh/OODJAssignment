package com.mycompany.oodjassignment.academicofficerGUI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.mycompany.oodjassignment.classes.*;
import com.mycompany.oodjassignment.functions.*;
import com.mycompany.oodjassignment.usermanagement.service.AuthenticationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentSelectionDashboard {
    private final Runnable onExitCallback;
    private AuthenticationService authService;
    private String userID;
    private DefaultTableModel tableModel;
    private Database database;
    private JPanel addPlanDashboardPanel;
    private JTable studentListTable;
    private JButton addPlanButton;
    private JButton backButton;
    private JTextField txtStudentID;
    private JButton searchButton;
    private JLabel titleAddPlan;
    private JLabel searchStudentIDPrompt;
    private JScrollPane studentListScrollPane;
    private JButton viewButton;

    public StudentSelectionDashboard(Database database, Runnable onExitCallback, AuthenticationService authService) {
        this.onExitCallback = onExitCallback;
        this.authService = authService;
        this.database = database;

        tableSetup();
        loadStudents();

        txtStudentID.addActionListener(e -> {
            search();
        });
        searchButton.addActionListener(e -> {
            search();
        });

        addPlanButton.addActionListener(e -> {
            addPlan();

        });
        backButton.addActionListener(e -> {
            closeCurrentMenu();
            openRecoveryPlanDashboard();
        });
    }

    private void search() {
        String searchText = txtStudentID.getText().trim();

        if (searchText.isEmpty()) {
            loadStudents();
            return;
        }
        tableModel.setRowCount(0);
        boolean found = false;

        for (Student student : database.getStudentDB().values()) {
            if (student.getStudentID().toLowerCase().contains(searchText.toLowerCase())) {
                Object[] row = {
                        student.getStudentID(),
                        student.getFirstName() + " " + student.getLastName(),
                        student.getMajor(),
                        student.getYear()
                };
                tableModel.addRow(row);
                found = true;
            }
        }
        if (!found) {
            JOptionPane.showMessageDialog(addPlanDashboardPanel,
                    "No Student found with ID: " + searchText,
                    "Search Result",
                    JOptionPane.INFORMATION_MESSAGE);
            loadStudents();
            txtStudentID.setText("");
        }
    }

    private void addPlan() {
        int row = studentListTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(addPlanDashboardPanel,
                    "Please select a student first.",
                    "Error.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        closeCurrentMenu();
        int modelRow = studentListTable.convertRowIndexToModel(row);
        String targetStudentID = (String) tableModel.getValueAt(modelRow, 0);
        openStudentCourseSelectionMenu(targetStudentID);
    }

    private void tableSetup() {
        String[] columnNames = {"Student ID", "Name", "Major", "Year"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        for (Student student : database.getStudentDB().values()) {
            Object[] row = {
                    student.getStudentID(),
                    student.getFirstName() + " " + student.getLastName(),
                    student.getMajor(),
                    student.getYear()
            };
            tableModel.addRow(row);
        }
        studentListTable.setModel(tableModel);
        studentListTable.setAutoCreateRowSorter(true);
        studentListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    // for back button, go back to main menu

    private void openRecoveryPlanDashboard() {
        JFrame recoveryPlanDashboardFrame = new JFrame("Academic Officer System");
        RecoveryPlanDashboard recoveryPlanDashboard = new RecoveryPlanDashboard(database, onExitCallback, authService);
        recoveryPlanDashboardFrame.setContentPane(recoveryPlanDashboard.getRecoveryPlanDashboardPanel());
        recoveryPlanDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recoveryPlanDashboardFrame.setSize(800, 400);
        recoveryPlanDashboardFrame.setLocationRelativeTo(null);
        recoveryPlanDashboardFrame.setVisible(true);
    }

    private void openStudentCourseSelectionMenu(String targetStudentID) {
        JFrame studentCourseSelectionMenuFrame = new JFrame("Academic Officer System");
        CourseSelectionMenu courseSelectionMenu = new CourseSelectionMenu(targetStudentID, database, onExitCallback, authService);
        studentCourseSelectionMenuFrame.setContentPane(courseSelectionMenu.getStudentCourseSelectionPanel());
        studentCourseSelectionMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        studentCourseSelectionMenuFrame.setSize(800, 400);
        studentCourseSelectionMenuFrame.setLocationRelativeTo(null);
        studentCourseSelectionMenuFrame.setVisible(true);
    }

    public JPanel getAddPlanDashboardPanel() {
        return addPlanDashboardPanel;
    }

    private void closeCurrentMenu() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(addPlanDashboardPanel);
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
        addPlanDashboardPanel = new JPanel();
        addPlanDashboardPanel.setLayout(new GridLayoutManager(4, 10, new Insets(30, 30, 30, 30), -1, -1));
        addPlanDashboardPanel.setBackground(new Color(-1));
        studentListScrollPane = new JScrollPane();
        addPlanDashboardPanel.add(studentListScrollPane, new GridConstraints(2, 0, 1, 10, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        studentListTable = new JTable();
        studentListScrollPane.setViewportView(studentListTable);
        txtStudentID = new JTextField();
        addPlanDashboardPanel.add(txtStudentID, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        searchStudentIDPrompt = new JLabel();
        searchStudentIDPrompt.setText("Search by StudentID");
        addPlanDashboardPanel.add(searchStudentIDPrompt, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        titleAddPlan = new JLabel();
        titleAddPlan.setText("Choose student to add recovery plan:");
        addPlanDashboardPanel.add(titleAddPlan, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        addPlanDashboardPanel.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        searchButton = new JButton();
        searchButton.setText("Search");
        addPlanDashboardPanel.add(searchButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addPlanButton = new JButton();
        addPlanButton.setText("Next");
        addPlanDashboardPanel.add(addPlanButton, new GridConstraints(3, 9, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setText("Back");
        addPlanDashboardPanel.add(backButton, new GridConstraints(1, 9, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return addPlanDashboardPanel;
    }

}
