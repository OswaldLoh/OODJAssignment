package com.mycompany.oodjassignment.functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mycompany.oodjassignment.classes.Course;
import com.mycompany.oodjassignment.classes.Grades;
import com.mycompany.oodjassignment.classes.RecoveryPlan;
import com.mycompany.oodjassignment.classes.RecoveryTask;
import com.mycompany.oodjassignment.classes.Student;

public class Database {
    private HashMap<String, Course> courseDB;
    private HashMap<String, RecoveryPlan> recPlanDB;
    private HashMap<String, RecoveryTask> recTaskDB;
    private HashMap<String, Student> studentDB;
    private HashMap<String, Grades> gradesDB;

    public Database() {
        Course Course = new Course();
        RecoveryPlan RecoveryPlan = new RecoveryPlan();
        RecoveryTask RecoveryTask = new RecoveryTask();
        Student Student = new Student();
        Grades Grades = new Grades();
        this.courseDB = FileHandler.readCSV(Course);
        this.recPlanDB = FileHandler.readCSV(RecoveryPlan);
        this.recTaskDB = FileHandler.readCSV(RecoveryTask);
        this.studentDB = FileHandler.readCSV(Student);
        this.gradesDB = FileHandler.readCSV(Grades);

    }
    // Object adder
    public void addRecoveryPlan(RecoveryPlan recPlan) {
        recPlanDB.put(recPlan.getPlanID(),recPlan);
    }
    public void addRecoveryTask(RecoveryTask recTask) {
        recTaskDB.put(recTask.getTaskID(),recTask);
    }

    // Object removers
    public void removeRecoveryPlan(String planID) {
        recPlanDB.remove(planID);
    }
    public void removeRecoveryTask(String taskID) {
        recTaskDB.remove(taskID);
    }
    
    // Object getters
    public Grades getGrades(String targetStudentID, String targetCourseID) {
        Grades newGrade = new Grades();
        for (Grades grade : gradesDB.values()) {
            if (grade.getStudentID().equals(targetStudentID) && grade.getCourseID().equals(targetCourseID)) {
                newGrade = grade;
                grade.setCourseObject(courseDB.get(targetCourseID));
            }
        }
        return newGrade;
    }
    public Student getStudent(String studentID) {
        return studentDB.get(studentID);
    }
    public Course getCourse(String courseID) {
        return courseDB.get(courseID);
    }
    public RecoveryPlan getRecoveryPlan(String planID) {
        return recPlanDB.get(planID);
    }
    public RecoveryTask getRecoveryTask(String taskID) {
        return recTaskDB.get(taskID);
    }

    // HashMap getters
    public HashMap<String, Student> getStudentDB() {
        return studentDB;
    }
    public HashMap<String, Course> getCourseDB() {
        return courseDB;
    }
    public HashMap<String, Grades> getGradeDB() {
        return gradesDB;
    }
    public HashMap<String, RecoveryPlan> getRecPlanDB() {
        return recPlanDB;
        }
    public HashMap<String, RecoveryTask> getRecTaskDB() {
        return recTaskDB;
    }

    public ArrayList<Grades> getStudentAllGrades(String targetStudentID) {
        HashMap<String, Grades> latestGradesMap = new HashMap<>();

        for (Grades grade : gradesDB.values()) {
            if (grade.getStudentID().equals(targetStudentID)) {             // check if grade belongs to target Student
                if (latestGradesMap.containsKey(grade.getCourseID())) {                 // if the hashmap already has this course ID
                    Grades existingGrade = latestGradesMap.get(grade.getCourseID());    // cast the existing grade into existingGrade
                    if (grade.getAttempt() > existingGrade.getAttempt()) {              // compare attempt of grade vs existingGrade
                        latestGradesMap.put(grade.getCourseID(), grade);                // replace if attempt is larger
                    }
                } else {
                    latestGradesMap.put(grade.getCourseID(), grade);                    // first time checking the grade, place it in
                }
            }
        }

        return new ArrayList<>(latestGradesMap.values());
    }


    // Check existence in database
    public boolean studentExist(String targetStudentID) {
        return studentDB.containsKey(targetStudentID);
    }

    public boolean studentSemesterExist(String targetStudentID, String targetSemester) {
        if (!studentExist(targetStudentID)) {
            return false;
        }

        // Check if the student has grades for the specified semester
        for (Grades grade : gradesDB.values()) {
            if (grade.getStudentID().equals(targetStudentID) && 
                grade.getSemester() == Integer.parseInt(targetSemester)) {
                return true;
            }
        }
        return false;
    }

    // RecoveryPlan helper methods
    public boolean checkExistingRecoveryPLan(String targetStudentID, String targetCourseID) {
        for (RecoveryPlan plan : recPlanDB.values()) {
            if (plan.getStudentID().equals(targetStudentID) && plan.getCourseID().equals(targetCourseID)) {
                System.out.println("Duplicate recovery plan found");
                return true;
            }
        }
        return false;
    }

    public void updatePlanProgress(String planID) {
        double totalTaskCount = 0;
        double completeCount = 0;
        double newProgress;
        for (RecoveryTask task : recTaskDB.values()) {
            if (planID.equals(task.getPlanID())) {
                if (task.getCompletion()) {
                    completeCount++;
                }
                totalTaskCount++;
            }
        }
        newProgress = completeCount/totalTaskCount * 100;
        getRecoveryPlan(planID).setProgress(newProgress);
    }


    public String getRecoveryPlanComponent(String targetStudentID, String targetCourseID) {
        String retakenComponent = "hi";
        int attemptCount = 0;
        ArrayList<Grades> courseAttempts = new ArrayList<>();
        for (Grades grade : gradesDB.values()) {
            if (grade.getStudentID().equals(targetStudentID) && grade.getCourseID().equals(targetCourseID)) {
                courseAttempts.add(grade);
                attemptCount ++;
            }
        }
        if (attemptCount == 3) {
            retakenComponent = "Module Retake";
        } else {
            Course course = courseDB.get(targetCourseID);
            for (Grades grade : courseAttempts) {
                if (grade.getAttempt() == attemptCount) {
                    grade.setCourseObject(course);
                    if (grade.getWeightedAssignmentMark() < (course.getAssignmentWeight() * 0.4) && grade.getWeightedExamMark() < (course.getExamWeight() * 0.4)) {
                        retakenComponent = "Examination and Assignment";
                    } else if (grade.getWeightedExamMark() < (course.getExamWeight() * 0.4)) {
                        retakenComponent = "Examination";
                    } else if (grade.getWeightedAssignmentMark() < (course.getAssignmentWeight() * 0.4)) {
                        retakenComponent = "Assignment";
                    }
                }
            }
        }
        return retakenComponent;
    }

    public ArrayList<RecoveryTask> getPlanRecoveryTask(String targetRecoveryPlanID) {
        ArrayList<RecoveryTask> taskInPlan = new ArrayList<>();
        for (RecoveryTask task : recTaskDB.values()) {
            if ((targetRecoveryPlanID).equals(task.getPlanID())) {
                taskInPlan.add(task);
            }
        }
        return taskInPlan;
    }
    
    // Check if a student has grades for a specific year (at least one semester)
    public boolean studentYearExist(String targetStudentID, int targetYear) {
        if (!studentExist(targetStudentID)) {
            return false;
        }

        int startSemester = (targetYear - 1) * 2 + 1;  // First semester of the year
        int endSemester = startSemester + 1;           // Last semester of the year
        
        boolean hasDataInYear = false;
        for (Grades grade : gradesDB.values()) {
            if (grade.getStudentID().equals(targetStudentID) && 
                grade.getSemester() >= startSemester && 
                grade.getSemester() <= endSemester) {
                hasDataInYear = true;
                break;
            }
        }
        return hasDataInYear;
    }
    
    // Calculate CGPA for a specific academic year
    public double calculateCGPAByYear(String targetStudentID, int targetYear) {
        if (!studentExist(targetStudentID)) {
            throw new IllegalArgumentException("Student ID does not exist: " + targetStudentID);
        }
        
        double totalGPA = 0.0;
        int courseCount = 0;
        
        int startSemester = (targetYear - 1) * 2 + 1;  // First semester of the year
        int endSemester = startSemester + 1;           // Last semester of the year
        
        for (Grades grade : gradesDB.values()) {
            if (grade.getStudentID().equals(targetStudentID) && 
                grade.getSemester() >= startSemester && 
                grade.getSemester() <= endSemester) {
                
                // Set course object for proper GPA calculation
                grade.setCourseObject(courseDB.get(grade.getCourseID()));
                
                // Add the GPA from this course to the total
                totalGPA += grade.calculateGPA();
                courseCount++;
            }
        }
        
        // Return average GPA for the year, or 0.0 if no courses found
        if(courseCount > 0){
            return totalGPA / courseCount;
        }
        else{
            return 0.0;
        }
    }
    
    // Calculate overall CGPA for all years
    public double calculateOverallCGPA(String targetStudentID) {
        if (!studentExist(targetStudentID)) {
            throw new IllegalArgumentException("Student ID does not exist: " + targetStudentID);
        }
        
        double totalGPA = 0.0;
        int courseCount = 0;
        
        for (Grades grade : gradesDB.values()) {
            if (grade.getStudentID().equals(targetStudentID)) {
                // Set course object for proper GPA calculation
                grade.setCourseObject(courseDB.get(grade.getCourseID()));
                
                // Add the GPA from this course to the total
                totalGPA += grade.calculateGPA();
                courseCount++;
            }
        }
        
        // Return average GPA for all courses taken by the student
        return courseCount > 0 ? totalGPA / courseCount : 0.0;
    }
    
    // Get all available years for a student
    public Set<Integer> getAvailableYears(String targetStudentID) {
        Set<Integer> years = new HashSet<>();
        
        for (Grades grade : gradesDB.values()) {
            if (grade.getStudentID().equals(targetStudentID)) {
                // Calculate which year this semester belongs to
                int year = (int) Math.ceil(grade.getSemester() / 2.0);
                years.add(year);
            }
        }
        
        return years;
    }
    
    // Get all available semesters for a student
    public Set<Integer> getAvailableSemesters(String targetStudentID) {
        Set<Integer> semesters = new HashSet<>();
        
        for (Grades grade : gradesDB.values()) {
            if (grade.getStudentID().equals(targetStudentID)) {
                semesters.add(grade.getSemester());
            }
        }
        
        return semesters;
    }
    
    // Data processing methods for charts
    
    // Get GPA distribution data
    public int[] getGPADistributionData() {
        HashMap<String, Grades> gradeDB = getGradeDB();
        
        // Count grades in different GPA ranges
        int gradeA = 0, gradeB = 0, gradeC = 0, gradeD = 0, gradeF = 0;
        
        for (Grades grade : gradeDB.values()) {
            grade.setCourseObject(getCourse(grade.getCourseID()));
            String letterGrade = grade.getLetterGrade();
            
            switch (letterGrade) {
                case "A":
                case "A-":
                    gradeA++;
                    break;
                case "B+":
                case "B":
                case "B-":
                    gradeB++;
                    break;
                case "C+":
                case "C":
                    gradeC++;
                    break;
                case "D":
                    gradeD++;
                    break;
                case "F":
                    gradeF++;
                    break;
            }
        }
        
        return new int[] {gradeA, gradeB, gradeC, gradeD, gradeF};
    }
    
    // Get department performance data
    public Map<String, Double> getDepartmentPerformanceData() {
        Map<String, Double> departmentData = new HashMap<>();
        
        // Group students by department/major and calculate statistics
        HashMap<String, List<Student>> studentsByMajor = new HashMap<>();
        HashMap<String, Student> studentDB = getStudentDB();
        
        for (Student student : studentDB.values()) {
            String major = student.getMajor();
            studentsByMajor.computeIfAbsent(major, k -> new ArrayList<>()).add(student);
        }
        
        for (Map.Entry<String, List<Student>> entry : studentsByMajor.entrySet()) {
            String major = entry.getKey();
            List<Student> students = entry.getValue();
            
            // Calculate true department CGPA by considering all grades from all students in the department
            double totalQualityPoints = 0.0;
            int totalCreditHours = 0;
            
            for (Student student : students) {
                List<Grades> studentGrades = getStudentAllGrades(student.getStudentID());
                
                for (Grades grade : studentGrades) {
                    Course course = getCourse(grade.getCourseID());
                    if (course != null) {
                        grade.setCourseObject(course);
                        
                        // Get the GPA for this specific grade/course
                        double courseGPA = grade.calculateGPA();
                        
                        // Get the credit hours for the course
                        int creditHours = course.getCredit();
                        
                        // Add to total quality points (GPA * credit hours)
                        totalQualityPoints += courseGPA * creditHours;
                        totalCreditHours += creditHours;
                    }
                }
            }
            
            // Calculate the true department CGPA
            double deptCGPA;
            if(totalCreditHours > 0){
                deptCGPA = totalQualityPoints / totalCreditHours;
                departmentData.put(major, deptCGPA);

            }

            else{
                deptCGPA = 0.0;
            }

        }
        
        return departmentData;
    }
    
    // Get course performance data
    public Map<String, Double> getCoursePerformanceData() {
        Map<String, Double> courseData = new HashMap<>();
        
        // Get grades by course
        HashMap<String, List<Grades>> gradesByCourse = new HashMap<>();
        HashMap<String, Grades> gradeDB = getGradeDB();
        
        for (Grades grade : gradeDB.values()) {
            String courseId = grade.getCourseID();
            gradesByCourse.computeIfAbsent(courseId, k -> new ArrayList<>()).add(grade);
        }
        
        for (Map.Entry<String, List<Grades>> entry : gradesByCourse.entrySet()) {
            String courseId = entry.getKey();
            List<Grades> grades = entry.getValue();
            
            Course course = getCourse(courseId);
            if (course == null) continue; // Skip if course not found
            
            // Calculate average GPA for this course
            double totalGPA = 0.0;
            for (Grades grade : grades) {
                grade.setCourseObject(course);
                totalGPA += grade.calculateGPA();
            }
            
            double avgGPA;
            if (grades.size() > 0) {
                avgGPA = totalGPA / grades.size();
            } 
            
            else {
                avgGPA = 0.0;
            }
            
            // Use course name instead of ID for better x-axis labelling
            String courseName = course.getCourseName();
            courseData.put(courseName, avgGPA);
        }
        
        return courseData;
    }
    
    // Get semester performance data
    public Map<Integer, Double> getSemesterPerformanceData() {
        Map<Integer, Double> semesterData = new HashMap<>();
        
        // Group grades by semester and calculate statistics
        Map<Integer, List<Grades>> gradesBySemester = new HashMap<>();
        HashMap<String, Grades> gradeDB = getGradeDB();
        
        for (Grades grade : gradeDB.values()) {
            int semester = grade.getSemester();
            gradesBySemester.computeIfAbsent(semester, k -> new ArrayList<>()).add(grade);
        }
        
        // Process each semester
        for (Map.Entry<Integer, List<Grades>> entry : gradesBySemester.entrySet()) {
            Integer semester = entry.getKey();
            List<Grades> grades = entry.getValue();
            
            // Calculate average GPA for this semester
            double totalGPA = 0.0;
            for (Grades grade : grades) {
                Course course = getCourse(grade.getCourseID());
                if (course != null) {
                    grade.setCourseObject(course);
                    totalGPA += grade.calculateGPA();
                }
            }
            
            double avgGPA;
            if (grades.size() > 0) {
                avgGPA = totalGPA / grades.size();
            } 
            
            else {
                avgGPA = 0.0;
            }
            
            semesterData.put(semester, avgGPA);
        }
        
        return semesterData;
    }
}

