package com.mycompany.oodjassignment.academicofficerGUI;

import com.mycompany.oodjassignment.functions.*;
import com.mycompany.oodjassignment.classes.*;

import javax.swing.*;
import java.util.ArrayList;

public class SearchStudentMenu {
    // Fields
    private String userID;
    private Database database;
    private JPanel SearchStudentPanel;
    private JLabel SearchStudentTitle;
    private JTextField txtStudentID;
    private JLabel studentIDPrompt;
    private JButton backButton;
    private JButton Search;
    private JTextArea areaStudentDetails;

    // Constructor
    public SearchStudentMenu(Database database) {
        this.database = database;

        areaStudentDetails.setEditable(false);

        backButton.addActionListener(e -> {
            openMainMenu();
        });

        Search.addActionListener(e -> {
            performSearch();
        });

        txtStudentID.addActionListener(e -> performSearch());
    }

    // --- MAIN LOGIC METHOD ---
    private void performSearch() {
        // A. Get Input inside the listener
        String targetStudentID = txtStudentID.getText().trim();

        // B. Validation
        if (targetStudentID.isEmpty()) {
            JOptionPane.showMessageDialog(SearchStudentPanel, "Please enter a Student ID.");
            return;
        }

        // C. Check Database
        if (!database.studentExist(targetStudentID)) {
            areaStudentDetails.setText(""); // Clear previous results
            JOptionPane.showMessageDialog(SearchStudentPanel,
                    "Student ID " + targetStudentID + " not found.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // D. Fetch Data
        Student student = database.getStudent(targetStudentID);
        ArrayList<Grades> grades = database.getStudentAllGrades(targetStudentID);

        // E. Build Output String
        StringBuilder result = new StringBuilder();
        result.append("Student Found\n");
        result.append("----------------------------\n");
        result.append("ID:    ").append(student.getStudentID()).append("\n");
        result.append("Name:  ").append(student.getFirstName()).append(" ").append(student.getLastName()).append("\n");
        result.append("Major: ").append(student.getMajor()).append("\n");
        result.append("----------------------------\n");
        result.append("Failed Modules:\n\n");

        boolean hasFailures = false;
        int count = 1;

        if (grades != null) {
            for (Grades grade : grades) {
                // Check for failure
                Course course = database.getCourse(grade.getCourseID());
                grade.setCourseObject(course);
                if (grade.calculateGPA() < 2.0) {

                    // Inject course to ensure no null pointer on weight calculation if needed
                    grade.setCourseObject(course);

                    result.append(count).append(". ")
                            .append(course.getCourseName())
                            .append(" (").append(course.getCourseID()).append(")\n")
                            .append("   GPA: ").append(String.format("%.2f", grade.calculateGPA())).append("\n");

                    hasFailures = true;
                    count++;
                }
            }
        }

        if (!hasFailures) {
            result.append("Good news! This student has no failed modules.");
        }

        // F. Display in GUI
        areaStudentDetails.setText(result.toString());
    }

    // --- NAVIGATION METHODS ---
    private void openMainMenu() {
        JFrame mainMenuFrame = new JFrame("Academic Officer System");
        // Create new Main Menu instance
        AcademicOfficerGUI academicOfficerMainMenu = new AcademicOfficerGUI(database, userID);

        mainMenuFrame.setContentPane(academicOfficerMainMenu.getMainPanel());
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setSize(800, 600);
        mainMenuFrame.setLocationRelativeTo(null);
        mainMenuFrame.setVisible(true);

        closeCurrentMenu();
    }

    private void closeCurrentMenu() {
        // Safe way to close the specific frame this panel belongs to
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this.SearchStudentPanel);
        if (currentFrame != null) {
            currentFrame.dispose();
        }
    }

    public JPanel getSearchStudentMenuPanel() {
        return SearchStudentPanel;
    }
}