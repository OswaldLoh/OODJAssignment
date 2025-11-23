package com.mycompany.oodjassignment.functions;

import java.util.ArrayList;
import java.util.HashMap;

import com.mycompany.oodjassignment.classes.Course;
import com.mycompany.oodjassignment.classes.Grades;
import com.mycompany.oodjassignment.classes.RecoveryPlan;
import com.mycompany.oodjassignment.classes.RecoveryTask;
import com.mycompany.oodjassignment.classes.Student;

public class Database {
    private RecoveryPlan recPlanInterface;
    private RecoveryTask recTaskInterface;
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
    public void removeRecoverytask(String targetPlanID) {
        RecoveryTask targetRecTask = null;
        for (RecoveryTask task : recTaskDB.values()) {
            if (task.getPlanID().equals(targetPlanID)) {
                targetRecTask = task;
            }
        }
        if (targetRecTask != null) {
            recTaskDB.remove(targetRecTask.getTaskID(),targetRecTask);
        }

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


    public ArrayList<Student> getFailedStudents(String targetCourseID, Database database) {
        ArrayList<Student> failedStudentsList = new ArrayList<>();
        for (Grades grade : gradesDB.values()) {
            if (grade.getCourseID().equals(targetCourseID)) {
                grade.setCourseObject(database.getCourse(grade.getCourseID()));
                if (grade.calculateGPA() < 2.0) {
                    Student student = studentDB.get(grade.getStudentID());
                    if (student != null) {
                        failedStudentsList.add(student);
                    }
                }
            }
        }
        return failedStudentsList;
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

    public boolean planExist(String targetPlanID) {
        return recPlanDB.containsKey(targetPlanID);
    }

    // RecoveryPlan helper methods
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


    public ArrayList<RecoveryTask> findPlanRecoveryTask(String targetRecoveryPlanID) {
        ArrayList<RecoveryTask> taskInPlan = new ArrayList<>();
        for (RecoveryTask task : recTaskDB.values()) {
            if ((targetRecoveryPlanID).equals(task.getPlanID())) {
                taskInPlan.add(task);
            }
        }
        return taskInPlan;
    }
    public ArrayList<RecoveryPlan> findStudentRecoveryPlan(String targetStudentID) {
        ArrayList<RecoveryPlan> studentPlanID = new ArrayList<>();
        for (RecoveryPlan plan : recPlanDB.values()) {        // Finding if student has recovery plans and add them into a list if yes
            if ((targetStudentID).equals(plan.getStudentID())) {
                studentPlanID.add(plan);
            }
        }
        return studentPlanID;
    }
    public int getStudentRecoveryPlanCount(String targetStudentID) {
        int planCount = 0;
        for (RecoveryPlan plan : recPlanDB.values()) {        // Finding if student has recovery plans and add them into a list if yes
            if ((targetStudentID).equals(plan.getStudentID())) {
                planCount += 1;
            }
        }
        return planCount;
    }
    
    // Check if a student has grades for a specific year (at least one semester)
    public boolean studentYearExist(String targetStudentID, int targetYear) {
        if (!studentExist(targetStudentID)) {
            return false;
        }

        // Check if the student has grades for at least one semester in the target year
        // Assuming 2 semesters per year (Year 1: Sem 1 & 2, Year 2: Sem 3 & 4, etc.)
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
        
        // Determine which semesters belong to the target year
        // Assuming 2 semesters per year (Year 1: Sem 1 & 2, Year 2: Sem 3 & 4, etc.)
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
        return courseCount > 0 ? totalGPA / courseCount : 0.0;
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
    public java.util.Set<Integer> getAvailableYears(String targetStudentID) {
        java.util.Set<Integer> years = new java.util.HashSet<>();
        
        for (Grades grade : gradesDB.values()) {
            if (grade.getStudentID().equals(targetStudentID)) {
                // Calculate which year this semester belongs to
                int year = (int) Math.ceil(grade.getSemester() / 2.0);
                years.add(year);
            }
        }
        
        return years;
    }
}

