package com.mycompany.oodjassignment.Eligibility;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Table model for displaying student eligibility information in a JTable.
 * Uses data from Student objects and EligibilityChecker.
 */
public class EligibilityTableModel extends AbstractTableModel {

    private final String[] columns = {"Student ID", "Name", "CGPA", "Failed Courses", "Eligible"};

    private List<Student> students;
    private EligibilityChecker eligibilityChecker;

    // Constructor takes a list of students and an eligibility checker.

    public EligibilityTableModel(List<Student> students, EligibilityChecker checker) {
        this.students = students;
        this.eligibilityChecker = checker;
    }

    // Allows the GUI to reset the list of students (ex:after filtering).

    public void setStudents(List<Student> students) {
        this.students = students;
        fireTableDataChanged(); // Notifies JTable that the data has changed.
    }

    // Returns the Student object at a given row index.

    public Student getStudentAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= students.size()) {
            return null;
        }
        return students.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return students == null ? 0 : students.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    // Provides values for each cell in the table based on the student list.

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student s = students.get(rowIndex);
        double cgpa = s.calculateCGPA();
        int fails = s.countFailedCourses();
        boolean eligible = eligibilityChecker.isEligible(s);

        switch (columnIndex) {
            case 0:
                return s.getId();
            case 1:
                return s.getFullName();
            case 2:
                // Format CGPA to 2 decimal places for display.
                return String.format("%.2f", cgpa);
            case 3:
                return fails;
            case 4:
                return eligible ? "Yes" : "No";
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        // CGPA is shown as String because of formatting, failed courses as Integer.
        if (columnIndex == 2) return String.class;
        if (columnIndex == 3) return Integer.class;
        return String.class;
    }
}
