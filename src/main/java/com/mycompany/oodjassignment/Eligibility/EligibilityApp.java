package com.mycompany.oodjassignment.Eligibility;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main GUI application for the Eligibility Check & Enrolment module.
 * Features:
 *  - Load data from CSV files
 *  - Calculate and display CGPA and failed courses
 *  - Show eligibility (Yes/No) with color highlighting
 *  - Search and filter students
 *  - Progression analytics (counts + average CGPA)
 *  - Enrol eligible students and save records to binary file
 */
public class EligibilityApp extends JFrame {

    private Map<String, Student> allStudentsMap;       // All students loaded from file.
    private List<Student> displayedStudents;           // Current list shown in the table.

    private EligibilityChecker eligibilityChecker;     // Business logic to determine eligibility.
    private EligibilityTableModel tableModel;          // Model for JTable.
    private JTable table;                              // Table to display students.

    private JTextField txtSearch;                      // Text field for ID/Name search.
    private JCheckBox chkShowOnlyIneligible;           // Checkbox to filter only non-eligible.
    private JLabel lblSelectedInfo;                    // Label to show info about selected student.

    // Constructor sets up the whole GUI and loads data.

    public EligibilityApp() {
        super("Eligibility Check & Enrolment");

        eligibilityChecker = new EligibilityChecker();
        loadData(); // Load CSV data into memory.

        // Initially, display all students.
        displayedStudents = new ArrayList<>(allStudentsMap.values());

        // Set up table model and JTable.
        tableModel = new EligibilityTableModel(displayedStudents, eligibilityChecker);
        table = new JTable(tableModel);
        table.setDefaultRenderer(Object.class, new EligibilityCellRenderer());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);

        // Search input and controls.
        txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Search");
        chkShowOnlyIneligible = new JCheckBox("Show only NOT eligible");
        JButton btnClear = new JButton("Clear Filter");

        // Analytics and enrolment buttons.
        JButton btnAnalytics = new JButton("Progression Analytics");
        JButton btnEnrol = new JButton("Enrol Selected");

        lblSelectedInfo = new JLabel("Select a student to view details.");

        // Top panel for search/filter controls.
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Search (ID/Name):"));
        topPanel.add(txtSearch);
        topPanel.add(btnSearch);
        topPanel.add(chkShowOnlyIneligible);
        topPanel.add(btnClear);

        // Bottom panel for status label and action buttons.
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnAnalytics);
        btnPanel.add(btnEnrol);

        bottomPanel.add(lblSelectedInfo, BorderLayout.CENTER);
        bottomPanel.add(btnPanel, BorderLayout.EAST);

        // Add panels and table to the main frame.
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Event listeners for buttons and selection changes.

        // Perform search and filter when Search button is clicked or checkbox changes.
        btnSearch.addActionListener(e -> applyFilter());
        chkShowOnlyIneligible.addActionListener(e -> applyFilter());

        // Clear filter and show all students again.
        btnClear.addActionListener(e -> {
            txtSearch.setText("");
            chkShowOnlyIneligible.setSelected(false);
            resetFilter();
        });

        // Update label when the selected row in the table changes.
        table.getSelectionModel().addListSelectionListener(e -> updateSelectedInfo());

        // Show progression analytics popup.
        btnAnalytics.addActionListener(e -> showAnalytics());

        // Enrol the selected student if eligible.
        btnEnrol.addActionListener(e -> enrolSelectedStudent());

        // Basic frame settings.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null); // Center on screen.
    }

    /**
     * Loads students, courses, and grades from CSV files.
     * If any error occurs, an empty map is created and an error message is shown.
     */
    private void loadData() {
        try {
            Map<String, Student> students = CSVLoader.loadStudents("student_information.csv");
            Map<String, Course> courses = CSVLoader.loadCourses("course_assessment_information.csv");
            CSVLoader.loadGradesIntoStudents("student_grades.csv", students, courses);

            this.allStudentsMap = students;
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading CSV files: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            this.allStudentsMap = new LinkedHashMap<>();
        }
    }

    /**
     * Applies search and filter conditions to the student list.
     * - Filters by ID or name substring
     * - Optionally filters to show only non-eligible students
     */
    private void applyFilter() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        boolean onlyIneligible = chkShowOnlyIneligible.isSelected();

        List<Student> filtered = allStudentsMap.values().stream()
                .filter(s -> {
                    // Search by ID or full name.
                    boolean matchesKeyword =
                            keyword.isEmpty()
                                    || s.getId().toLowerCase().contains(keyword)
                                    || s.getFullName().toLowerCase().contains(keyword);

                    // Optionally restrict to non-eligible students only.
                    boolean matchesEligibility = true;
                    if (onlyIneligible) {
                        matchesEligibility = !eligibilityChecker.isEligible(s);
                    }

                    return matchesKeyword && matchesEligibility;
                })
                .collect(Collectors.toList());

        displayedStudents = filtered;
        tableModel.setStudents(displayedStudents);
    }

    // Resets the table to show all students without any filters.

    private void resetFilter() {
        displayedStudents = new ArrayList<>(allStudentsMap.values());
        tableModel.setStudents(displayedStudents);
    }

    // Updates the bottom status label with information about the currently selected student.

    private void updateSelectedInfo() {
        int row = table.getSelectedRow();
        if (row < 0) {
            lblSelectedInfo.setText("Select a student to view details.");
            return;
        }

        Student s = tableModel.getStudentAt(row);
        double cgpa = s.calculateCGPA();
        int fails = s.countFailedCourses();
        boolean eligible = eligibilityChecker.isEligible(s);

        lblSelectedInfo.setText(String.format(
                "Selected: %s | CGPA: %.2f | Failed: %d | Eligible: %s",
                s.toString(), cgpa, fails, eligible ? "YES" : "NO"));
    }

    /**
     * Shows aggregated statistics of the student population:
     * - Total number of students
     * - Number of eligible / non-eligible students
     * - Average CGPA
     */
    private void showAnalytics() {
        if (allStudentsMap.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No data available.",
                    "Analytics",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int total = allStudentsMap.size();
        int eligibleCount = 0;
        int notEligibleCount = 0;
        double totalCgpa = 0.0;

        for (Student s : allStudentsMap.values()) {
            double cgpa = s.calculateCGPA();
            totalCgpa += cgpa;

            if (eligibilityChecker.isEligible(s)) {
                eligibleCount++;
            } else {
                notEligibleCount++;
            }
        }

        double avgCgpa = total == 0 ? 0.0 : totalCgpa / total;

        String msg = String.format(
                "Total students: %d\nEligible: %d\nNot Eligible: %d\nAverage CGPA: %.2f",
                total, eligibleCount, notEligibleCount, avgCgpa);

        JOptionPane.showMessageDialog(this,
                msg,
                "Progression Analytics",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Attempts to enrol the selected student.
     * - Only allows enrolment if the student is eligible.
     * - Saves an EnrolmentRecord to a binary file if successful.
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
        double cgpa = s.calculateCGPA();
        int fails = s.countFailedCourses();
        boolean eligible = eligibilityChecker.isEligible(s);

        // Block enrolment if the student is not eligible.
        if (!eligible) {
            JOptionPane.showMessageDialog(this,
                    "This student is NOT eligible for enrolment.\nCannot proceed.",
                    "Enrolment Blocked",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a new enrolment record and append to file.
        EnrolmentRecord record = new EnrolmentRecord(s, cgpa, fails, true);
        EnrolmentFileManager.appendEnrolment(record);

        JOptionPane.showMessageDialog(this,
                "Enrolment successful for " + s.getFullName()
                        + "\nCGPA: " + String.format("%.2f", cgpa)
                        + "\nFailed Courses: " + fails,
                "Enrolment Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Main entry point to start the GUI application.

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EligibilityApp().setVisible(true);
        });
    }
}
