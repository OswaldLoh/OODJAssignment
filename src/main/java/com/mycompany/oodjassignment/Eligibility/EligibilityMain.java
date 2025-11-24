package com.mycompany.oodjassignment.Eligibility;

import com.mycompany.oodjassignment.classes.Student;
import com.mycompany.oodjassignment.functions.Database;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main GUI for the Eligibility Check & Enrolment module.
 * Integrates searching, filtering, analytics, highlighting,
 * and saving enrolment decisions.
 */
public class EligibilityMain extends JFrame {

    private final Database db;              // shared project database
    private final EligibilityChecker checker;

    private Map<String, Student> allStudentsMap;
    private List<Student> displayedStudents;

    private EligibilityTableModel tableModel;
    private JTable table;

    private JTextField txtSearch;
    private JCheckBox chkShowOnlyIneligible;
    private JLabel lblSelectedInfo;

    /**
     * Constructor initializes the Database and creates the full GUI.
     */
    public EligibilityMain() {
        super("Eligibility Check & Enrolment");

        db = new Database();                       // loads all CSV data automatically
        checker = new EligibilityChecker(db);      // performs the eligibility logic
        allStudentsMap = db.getStudentDB();
        displayedStudents = new ArrayList<>(allStudentsMap.values());
        // Sort students by StudentID in ascending order
        displayedStudents.sort(Comparator.comparing(Student::getStudentID));


        initComponents();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
    }

    /**
     * Builds UI components: search bar, table, buttons, listeners.
     */
    private void initComponents() {
        tableModel = new EligibilityTableModel(displayedStudents, checker);
        table = new JTable(tableModel);
        table.setDefaultRenderer(Object.class, new EligibilityCellRenderer());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);

        // top search panel
        txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Search");
        chkShowOnlyIneligible = new JCheckBox("Show only NOT eligible");
        JButton btnClear = new JButton("Clear Filter");

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Search (ID/Name):"));
        topPanel.add(txtSearch);
        topPanel.add(btnSearch);
        topPanel.add(chkShowOnlyIneligible);
        topPanel.add(btnClear);

        // bottom panel: analytics + enrol
        JButton btnAnalytics = new JButton("Progression Analytics");
        JButton btnEnrol = new JButton("Enrol Selected");
        lblSelectedInfo = new JLabel("Select a student to view details.");

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnAnalytics);
        btnPanel.add(btnEnrol);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(lblSelectedInfo, BorderLayout.CENTER);
        bottomPanel.add(btnPanel, BorderLayout.EAST);

        // add to frame
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // event handlers
        btnSearch.addActionListener(e -> applyFilter());
        chkShowOnlyIneligible.addActionListener(e -> applyFilter());
        btnClear.addActionListener(e -> resetFilter());

        table.getSelectionModel().addListSelectionListener(e -> updateSelectedInfo());

        btnAnalytics.addActionListener(e -> showAnalytics());
        btnEnrol.addActionListener(e -> enrolSelectedStudent());
    }

    /**
     * Applies search + ineligible-only filters using student fields.
     */
    private void applyFilter() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        boolean onlyIneligible = chkShowOnlyIneligible.isSelected();

        List<Student> filtered = allStudentsMap.values().stream()
                .filter(s -> {
                    boolean matchesKeyword =
                            keyword.isEmpty()
                                    || s.getStudentID().toLowerCase().contains(keyword)
                                    || (s.getFirstName() + " " + s.getLastName()).toLowerCase().contains(keyword);

                    boolean matchesEligibility =
                            !onlyIneligible || !checker.isEligible(s);

                    return matchesKeyword && matchesEligibility;
                })
                .collect(Collectors.toList());

        displayedStudents = filtered;
        // Sort result
        displayedStudents.sort(Comparator.comparing(Student::getStudentID));
        tableModel.setStudents(displayedStudents);
    }

    /**
     * Restores full student list.
     */
    private void resetFilter() {
        txtSearch.setText("");
        chkShowOnlyIneligible.setSelected(false);
        // Convert HashMap → List
        displayedStudents = new ArrayList<>(allStudentsMap.values());
        // Sort students by StudentID in ascending order
        displayedStudents.sort(Comparator.comparing(Student::getStudentID));
        tableModel.setStudents(displayedStudents);
    }

    /**
     * Updates label showing selected student's academic performance.
     */
    private void updateSelectedInfo() {
        int row = table.getSelectedRow();
        if (row < 0) {
            lblSelectedInfo.setText("Select a student to view details.");
            return;
        }

        Student s = tableModel.getStudentAt(row);
        double cgpa = checker.getCGPA(s);
        int fails = checker.getFailedCourses(s);
        boolean eligible = checker.isEligible(s);

        lblSelectedInfo.setText(String.format(
                "Selected: %s (%s %s) | CGPA: %.2f | Failed: %d | Eligible: %s",
                s.getStudentID(), s.getFirstName(), s.getLastName(),
                cgpa, fails, eligible ? "YES" : "NO"));
    }

    /**
     * Shows general analytics for the entire student population.
     */
    private void showAnalytics() {
        int total = allStudentsMap.size();
        int eligibleCount = 0;
        double totalCgpa = 0;

        for (Student s : allStudentsMap.values()) {
            double cgpa = checker.getCGPA(s);
            totalCgpa += cgpa;

            if (checker.isEligible(s)) {
                eligibleCount++;
            }
        }

        int notEligibleCount = total - eligibleCount;
        double avgCgpa = total == 0 ? 0 : totalCgpa / total;

        String msg = String.format(
                "Total students: %d\nEligible: %d\nNot Eligible: %d\nAverage CGPA: %.2f",
                total, eligibleCount, notEligibleCount, avgCgpa);

        JOptionPane.showMessageDialog(this, msg, "Progression Analytics",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Creates and stores an enrolment record for the selected student.
     */
    private void enrolSelectedStudent() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a student first.",
                    "Enrolment",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student s = tableModel.getStudentAt(row);
        double cgpa = checker.getCGPA(s);
        int fails = checker.getFailedCourses(s);

        if (!checker.isEligible(s)) {
            JOptionPane.showMessageDialog(this,
                    "This student is NOT eligible for enrolment.",
                    "Enrolment Blocked",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String fullName = s.getFirstName() + " " + s.getLastName();

        EnrolmentRecord record = new EnrolmentRecord(
                s.getStudentID(), fullName, cgpa, fails, true
        );
        EnrolmentFileManager.appendEnrolment(record);

        JOptionPane.showMessageDialog(this,
                "Enrolment successful for " + fullName +
                        "\nCGPA: " + String.format("%.2f", cgpa) +
                        "\nFailed Courses: " + fails,
                "Enrolment Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EligibilityMain().setVisible(true));
    }
}
