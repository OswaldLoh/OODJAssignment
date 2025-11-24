package com.mycompany.oodjassignment.Eligibility;

import com.mycompany.oodjassignment.classes.Student;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * TableModel used by JTable to display eligibility information.
 * This separates UI logic from business logic and ensures the table
 * updates cleanly when we filter or search.
 */
public class EligibilityTableModel extends AbstractTableModel {

    // Column headers displayed in the JTable
    private final String[] columns = {"Student ID", "Name", "CGPA", "Failed Courses", "Eligible"};

    private List<Student> students;           // Current list shown to the user
    private EligibilityChecker checker;       // Performs eligibility calculations

    /**
     * Constructor receives the student list and the shared EligibilityChecker.
     */
    public EligibilityTableModel(List<Student> students, EligibilityChecker checker) {
        this.students = students;
        this.checker = checker;
    }

    /**
     * Updates the student list when filters are applied.
     */
    public void setStudents(List<Student> students) {
        this.students = students;
        fireTableDataChanged(); // refresh table display
    }

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

    /**
     * Returns the value for each cell in the table based on the student object.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student s = students.get(rowIndex);

        double cgpa = checker.getCGPA(s);
        int fails = checker.getFailedCourses(s);
        boolean eligible = checker.isEligible(s);

        switch (columnIndex) {
            case 0:
                return s.getStudentID();
            case 1:
                return s.getFirstName() + " " + s.getLastName();
            case 2:
                return String.format("%.2f", cgpa);
            case 3:
                return fails;
            case 4:
                return eligible ? "Yes" : "No";
            default:
                return null;
        }
    }

    /**
     * Specifies data type for each column to improve JTable rendering.
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 3) return Integer.class; // failed courses
        return String.class;
    }
}
