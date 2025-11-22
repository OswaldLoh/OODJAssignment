package com.mycompany.oodjassignment.functions;

import com.mycompany.oodjassignment.classes.*;

import java.util.ArrayList;
import java.util.HashMap;

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
    public void removeRecoveryTask(String targetPlanID) {
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
    public boolean studentExist(String targetStudentID) {
        return studentDB.containsKey(targetStudentID);
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
}

