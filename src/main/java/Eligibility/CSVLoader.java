package Eligibility;

import java.io.*;
import java.util.*;

/**
 * Utility class to load data from CSV files into in-memory objects.
 * Handles students, courses, and student grades.
 */
public class CSVLoader {

    /**
     * Reads student_information.csv and builds a map of Student objects.
     * Key of the map is StudentID for easy lookup.
     */
    public static Map<String, Student> loadStudents(String path) throws IOException {
        Map<String, Student> students = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine(); // Skip header line.

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                // Expecting: StudentID,FirstName,LastName,Major,Year,Email
                if (parts.length < 6) continue;

                String id = parts[0].trim();
                String firstName = parts[1].trim();
                String lastName = parts[2].trim();
                String major = parts[3].trim();
                String year = parts[4].trim();
                String email = parts[5].trim();

                Student s = new Student(id, firstName, lastName, major, year, email);
                students.put(id, s);
            }
        }

        return students;
    }

    /**
     * Reads course_assessment_information.csv and builds a map of Course objects.
     * Key of the map is CourseID for easy reference.
     */
    public static Map<String, Course> loadCourses(String path) throws IOException {
        Map<String, Course> courses = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine(); // Skip header line.

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                // Expecting: CourseID,CourseName,Credits,Semester,Instructor,ExamWeight,AssignmentWeight
                if (parts.length < 7) continue;

                String courseID = parts[0].trim();
                String courseName = parts[1].trim();
                int credits = Integer.parseInt(parts[2].trim());
                String semester = parts[3].trim();
                String instructor = parts[4].trim();
                double examWeight = Double.parseDouble(parts[5].trim());
                double assignmentWeight = Double.parseDouble(parts[6].trim());

                Course c = new Course(courseID, courseName, credits, semester, instructor, examWeight, assignmentWeight);
                courses.put(courseID, c);
            }
        }

        return courses;
    }

    /**
     * Reads student_grades.csv and attaches CourseResult objects to the appropriate Student.
     * CSV format:
     *  gradeID,studentID,courseID,semester,examMark,assignmentMark
     * We ignore gradeID and semester here, because Course already stores semester,
     * and gradeID is just an identifier.
     */
    public static void loadGradesIntoStudents(
            String path,
            Map<String, Student> students,
            Map<String, Course> courses) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine(); // Skip header line.

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 6) continue;

                // Index mapping based on student_grades.csv:
                // 0 = gradeID
                // 1 = studentID
                // 2 = courseID
                // 3 = semester
                // 4 = examMark
                // 5 = assignmentMark

                String studentID = parts[1].trim();
                String courseID = parts[2].trim();
                // String semester = parts[3].trim()
                double examMark = Double.parseDouble(parts[4].trim());
                double assignmentMark = Double.parseDouble(parts[5].trim());

                Student s = students.get(studentID);
                Course c = courses.get(courseID);

                // Only create CourseResult if both student and course exist in the maps.
                if (s != null && c != null) {
                    CourseResult result = new CourseResult(c, examMark, assignmentMark);
                    s.addResult(result);
                }
            }
        }
    }
}
