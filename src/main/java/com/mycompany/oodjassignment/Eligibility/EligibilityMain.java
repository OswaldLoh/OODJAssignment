package com.mycompany.oodjassignment.Eligibility;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import com.mycompany.oodjassignment.Classes.Student;
import com.mycompany.oodjassignment.Helpers.Database;
import com.mycompany.oodjassignment.Helpers.SendEmail;

/**
 * Main GUI for the Eligibility Check & Enrolment module.
 * Key features implemented here:
 * - Search & filter student eligibility
 * - Auto-highlighting eligible/ineligible students (via renderer)
 * - Displaying CGPA, failed courses and eligibility status
 * - Enrolment with file storage
 * - Email notification to students
 * - Analytics for whole student population
 * - Audit log tracking
 */
public class EligibilityMain extends JFrame {

    private final Database db;                // Shared database object for reading student/course/grade data
    private final EligibilityChecker checker; // Logic class that handles all eligibility calculations

    private Map<String, Student> allStudentsMap;    // Holds ALL students from CSV
    private List<Student> displayedStudents;        // Only the students currently visible in the table (after filtering)

    private EligibilityTableModel tableModel;       // Custom table model
    private JTable table;                           // Table GUI component

    private JTextField txtSearch;                   // Search text field
    private JCheckBox chkShowOnlyIneligible;        // Checkbox filter
    private JLabel lblSelectedInfo;                 // Shows info for the selected student

    // Stores enrolled students to avoid duplicate enrolments
    private final List<String> alreadyEnrolled = new ArrayList<>();

    /**
     * Constructor initializes the Database and creates the full GUI.
     */
    public EligibilityMain() {
        super("Eligibility Check & Enrolment");

        db = new Database();
        checker = new EligibilityChecker(db);
        allStudentsMap = db.getStudentDB();

        displayedStudents = new ArrayList<>(allStudentsMap.values());

        // Load previously enrolled students into memory
        for (EnrolmentRecord r : EnrolmentFileManager.loadEnrolments()) {
            alreadyEnrolled.add(r.getStudentID());
        }

        // Sort initial table display
        displayedStudents.sort(Comparator.comparing(Student::getStudentID));

        initComponents();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
    }

    /**
     * Builds UI components and event listeners.
     */
    private void initComponents() {

        tableModel = new EligibilityTableModel(displayedStudents, checker);
        table = new JTable(tableModel);

        table.setDefaultRenderer(Object.class, new EligibilityCellRenderer());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);

        // Top search & filter panel
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

        // Bottom panel
        JButton btnAnalytics = new JButton("Progression Analytics");
        JButton btnEnrol = new JButton("Enrol Selected");
        lblSelectedInfo = new JLabel("Select a student to view details.");

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnAnalytics);
        btnPanel.add(btnEnrol);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(lblSelectedInfo, BorderLayout.CENTER);
        bottomPanel.add(btnPanel, BorderLayout.EAST);

        // Add everything to frame
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Event handlers
        btnSearch.addActionListener(e -> applyFilter());
        chkShowOnlyIneligible.addActionListener(e -> applyFilter());
        btnClear.addActionListener(e -> resetFilter());
        table.getSelectionModel().addListSelectionListener(e -> updateSelectedInfo());
        btnAnalytics.addActionListener(e -> showAnalytics());
        btnEnrol.addActionListener(e -> enrolSelectedStudent());
    }

    /**
     * Applies search and eligibility filters.
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
        displayedStudents.sort(Comparator.comparing(Student::getStudentID));
        tableModel.setStudents(displayedStudents);
    }

    /**
     * Clears all filters and restores full list.
     */
    private void resetFilter() {
        txtSearch.setText("");
        chkShowOnlyIneligible.setSelected(false);

        displayedStudents = new ArrayList<>(allStudentsMap.values());
        displayedStudents.sort(Comparator.comparing(Student::getStudentID));

        tableModel.setStudents(displayedStudents);
    }

    /**
     * Updates academic details of selected student.
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
     * Shows performance analytics for all students.
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
     * Handles enrolment, audit logging, and email notification.
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

        // Prevent double enrolment
        if (alreadyEnrolled.contains(s.getStudentID())) {
            JOptionPane.showMessageDialog(this,
                    "This student has ALREADY been enrolled.\nDuplicate enrolment is not allowed.",
                    "Already Enrolled",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double cgpa = checker.getCGPA(s);
        int fails = checker.getFailedCourses(s);

        // Prepare email sender object
        SendEmail sendEmail = new SendEmail(s.getEmail());

        // If student is not eligible → block enrolment
        if (!checker.isEligible(s)) {

            // Write audit log for blocked enrolment
            EnrolmentAudit.log(
                    s.getStudentID(),
                    s.getFirstName() + " " + s.getLastName(),
                    cgpa, fails,
                    "BLOCKED"
            );

            // using thread to prevent GUI freezing
            new Thread(() ->
                    sendEmail.Notification(
                            "Course Eligibility Notification",
                            "Dear " + s.getFirstName() + " " +  s.getLastName() + ",\n\n" +
                            "Unfortunately, you are not eligible for enrolment based on your current academic standing.\n\n" +
                            "Your CGPA: " + String.format("%.2f", cgpa) + "\n" +
                            "Failed Courses: " + fails + "\n\n" +
                            "Please contact your academic advisor for guidance on improving your eligibility.\n\n" +
                            "Best regards,\n" +
                            "Academic Administrator"
                    )
            ).start();

            JOptionPane.showMessageDialog(this,
                    "This student is NOT eligible for enrolment.\nMessage has been sent to the student's email.",
                    "Enrolment Blocked",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // SUCCESS CASE
        String fullName = s.getFirstName() + " " + s.getLastName();

        // Create enrolment record object
        EnrolmentRecord record = new EnrolmentRecord(
                s.getStudentID(), fullName, cgpa, fails, true
        );

        // Save to file for future reference
        EnrolmentFileManager.appendEnrolment(record);
        // Mark the student as already enrolled to prevent duplicates
        alreadyEnrolled.add(s.getStudentID());

        // Audit log entry for successful enrolment
        EnrolmentAudit.log(
                s.getStudentID(), fullName, cgpa, fails, "ENROLLED"
        );

        // using thread to prevent GUI freezing
        new Thread(() ->
                sendEmail.Notification(
                        "Enrolment Confirmation",
                        "Dear " + s.getFirstName() + s.getLastName() + ",\n\n" +
                        "Congratulations! You have been successfully enrolled.\n\n" +
                        "Your CGPA: " + String.format("%.2f", cgpa) + "\n" +
                        "Failed Courses: " + fails + "\n\n" +
                        "Your enrolment has been confirmed. Please check your student portal for further details.\n\n" +
                        "Best regards,\n" +
                        "Academic Administrator"
                )
        ).start();

        JOptionPane.showMessageDialog(this,
                "Enrolment successful for " + fullName +
                        "\nCGPA: " + String.format("%.2f", cgpa) +
                        "\nFailed Courses: " + fails +
                        "\nMessage has been sent to the student's email.",
                "Enrolment Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EligibilityMain().setVisible(true));
    }
}
