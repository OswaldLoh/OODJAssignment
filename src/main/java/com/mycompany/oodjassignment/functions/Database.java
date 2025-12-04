package com.mycompany.oodjassignment.functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
    public void removeRecoveryTask(String taskID) { recTaskDB.remove(taskID); }
    public void removeAllRecoveryTask(String targetPlanID) {
        ArrayList<RecoveryTask> taskPlans = getPlanRecoveryTask(targetPlanID);
        for (RecoveryTask task : taskPlans) {
            recTaskDB.remove(task.getTaskID());
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

    public  ArrayList<Grades> getStudentAllGrades(String targetStudentID) {
        ArrayList<Grades> studentGrades = new ArrayList<>();
        for (Grades grade : gradesDB.values()) {
            if (grade.getStudentID().equals(targetStudentID)) {
                studentGrades.add(grade);
            }
        }
        return studentGrades;
    }

    // Check existence in database
    public boolean courseExist(String targetCourseID) {
        return courseDB.containsKey(targetCourseID);
    }

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
            for (Grades grade : courseAttempts) {
                if (grade.getAttempt() == attemptCount) {
                    if (grade.getWeightedAssignmentMark() < 40 && grade.getWeightedExamMark() < 40) {
                        retakenComponent = "Examination and Assignment";
                    } else if (grade.getWeightedExamMark() < 40) {
                        retakenComponent = "Examination";
                    } else if (grade.getWeightedAssignmentMark() < 40) {
                        retakenComponent = "Assignment";
                    }
                }
            }
        }
        return retakenComponent;
    }
    public ArrayList<String> getStudentCourse(String targetStudentID) {
        ArrayList<String> studentCourse = new ArrayList<>();
        for (Grades grade : gradesDB.values()) {
            if (grade.getStudentID().equals(targetStudentID)) {
                studentCourse.add(grade.getCourseID());
            }
        }
        return studentCourse;
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
    public ArrayList<RecoveryPlan> getStudentRecoveryPlan(String targetStudentID) {
        ArrayList<RecoveryPlan> studentPlans = new ArrayList<>();
        for (RecoveryPlan plan : recPlanDB.values()) {        // Finding if student has recovery plans and add them into a list if yes
            if ((targetStudentID).equals(plan.getStudentID())) {
                studentPlans.add(plan);
            }
        }
        return studentPlans;
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
}

