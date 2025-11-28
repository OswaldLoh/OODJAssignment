package com.mycompany.oodjassignment.academicofficerGUI;
import com.mycompany.oodjassignment.classes.*;
import com.mycompany.oodjassignment.functions.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentSelectionDashboard {
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

    public StudentSelectionDashboard(Database database) {
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
                        student.getFirstName()+" "+student.getLastName(),
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
                    "Please select a student first."  ,
                    "Error.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        closeCurrentMenu();
        String targetStudentID = (String) tableModel.getValueAt(row,0);
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
                    student.getFirstName()+" "+student.getLastName(),
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
        RecoveryPlanDashboard recoveryPlanDashboard = new RecoveryPlanDashboard(database);
        recoveryPlanDashboardFrame.setContentPane(recoveryPlanDashboard.getRecoveryPlanDashboardPanel());
        recoveryPlanDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recoveryPlanDashboardFrame.setSize(800,400);
        recoveryPlanDashboardFrame.setLocationRelativeTo(null);
        recoveryPlanDashboardFrame.setVisible(true);
    }
    private void openStudentCourseSelectionMenu(String targetStudentID) {
        JFrame studentCourseSelectionMenuFrame = new JFrame("Academic Officer System");
        CourseSelectionMenu courseSelectionMenu = new CourseSelectionMenu(targetStudentID, database);
        studentCourseSelectionMenuFrame.setContentPane(courseSelectionMenu.getStudentCourseSelectionPanel());
        studentCourseSelectionMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        studentCourseSelectionMenuFrame.setSize(800,400);
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
}
