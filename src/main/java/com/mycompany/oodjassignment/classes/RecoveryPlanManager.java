package com.mycompany.oodjassignment.classes;
import com.mycompany.oodjassignment.functions.*;

import java.util.ArrayList;

public class RecoveryPlanManager {
    private Database database;

    public RecoveryPlanManager(Database database) {
        this.database = database;
    }




    public void addPlan(String targetStudentID, String userID) {
        int failedCourseCount = 0;
        Course selectedCourse;
        ArrayList<Grades> targetStudentGrades = database.getStudentAllGrades(targetStudentID);  // Student found, finding all grades for the student
        ArrayList<Course> failedCourses = new ArrayList<>();
        // Displaying failed modules of student
        System.out.println();
        System.out.println("Failed Modules for Student " + targetStudentID);
        for (Grades grade : targetStudentGrades) {
            Course course = database.getCourse(grade.getCourseID());
            grade.setCourseObject(course);
            if (grade.calculateGPA() < 2.0) {
                failedCourseCount++;
                System.out.println(failedCourseCount + ". " + course.getCourseName() + "-" + course.getCourseID() + "    GPA: " + grade.calculateGPA());
                failedCourses.add(course);
            }
        }
        if (failedCourses.isEmpty()) {
            System.out.println("Error. Student has no failed modules.");
            return;
        }
        ArrayList<RecoveryPlan> studentExistingPlans = database.getStudentRecoveryPlan(targetStudentID);

        // Ask user to pick which course to add recovery plan ( Second Menu )
        System.out.println();
        System.out.println("Please choose course to add Recovery Plan.");
        int courseSelection = InputValidation.readInt(">>>   ",1,failedCourses.size());
        selectedCourse = failedCourses.get(courseSelection-1);

        for (RecoveryPlan plan : studentExistingPlans) {
            if (plan.getStudentID().equals(targetStudentID) && plan.getCourseID().equals(selectedCourse.getCourseID())) {
                System.out.println("Error. Student '" + targetStudentID +"' already has a recovery plan for this course.");
                return;
            }
        }
        IDManager idManager = new IDManager(database.getRecPlanDB());       // Generate new Plan ID
        idManager.getHighestTaskID();
        String nextPlanID = "P" + idManager.generateNewID();

        String component = "hi";
        RecoveryPlan newPlan = new RecoveryPlan(nextPlanID, targetStudentID, component, selectedCourse.getCourseID(), userID, "0.00");
        database.addRecoveryPlan(newPlan);
        addTask(nextPlanID);
        System.out.println();
        System.out.println("Recovery Plan and Task successfully added for Student " + targetStudentID + ".");
    }




    public void deletePlan(String targetStudentID) {
        int number = 1;
        ArrayList<RecoveryPlan> studentPlan = database.getStudentRecoveryPlan(targetStudentID);
        int planCount = studentPlan.size();
        if (planCount == 0) {
            System.out.println("Error. Student " + targetStudentID + " has no recovery plans.");
            return;
        }
        // Displaying the RecoveryPlans for student
        System.out.println("Recovery Plans for Student " + targetStudentID);
        for (RecoveryPlan plan : studentPlan) {
            System.out.println(number + ". " + plan.getPlanID());
            number += 1;
        }
        System.out.println("Choose PlanID to delete");

        int planSelection = InputValidation.readInt(">>>   ",1,studentPlan.size());
        String planID = studentPlan.get(planSelection-1).getPlanID();
        database.removeRecoveryPlan(planID);
        database.removeAllRecoveryTask(planID);
        System.out.println("Plan " + planID + " has been successfully deleted.");
    }




    public void updatePlan(String targetPlanID) {
        int taskCount = 1;
        ArrayList<RecoveryTask> planTasks = database.getPlanRecoveryTask(targetPlanID);
        for (RecoveryTask task : planTasks) {
            System.out.println(taskCount+". "+ task.getTaskID() + "      Completed? : " + task.getCompletion());
            taskCount++;
        }
        System.out.println();
        int index = InputValidation.readInt("Choose Recovery Task to update: ",1,planTasks.size());
        String targetTaskID = planTasks.get(index-1).getTaskID();
        updateTask(targetTaskID);
        database.updatePlanProgress(targetPlanID);
    }




    public void monitorPlan(String targetStudentID) {
        int planCount = 1, taskCount = 1;
        System.out.println("--- Monitor Recovery Plan --- ");
        System.out.println("Showing Recovery Plans for Student '" + targetStudentID +  "' :");
        System.out.println("---------------------");
        // Display recovery plans registered under student
        ArrayList<RecoveryPlan> targetStudentPlans = database.getStudentRecoveryPlan(targetStudentID);
        if (targetStudentPlans.isEmpty()) {
            System.out.println("Error. Student '" +targetStudentID+ "' has no active recovery plans.");
            return;
        }
        for (RecoveryPlan plan : targetStudentPlans) {
            System.out.println(planCount+". " + plan.getPlanID() + "      CourseID: " + plan.getCourseID()+ "        Progress: " + plan.getProgress());
        }
        int planSelection = InputValidation.readInt(">>>   ",1,targetStudentPlans.size());
        RecoveryPlan targetPlan = targetStudentPlans.get(planSelection-1);

        System.out.println("Showing Recovery Task for Student '" + targetPlan.getPlanID() +  "' :");
        System.out.println("---------------------");
        // Display recovery tasks registered under the recovery plan
        ArrayList<RecoveryTask> planTasks = database.getPlanRecoveryTask(targetPlan.getPlanID());

        for (RecoveryTask task : planTasks) {
            System.out.println(taskCount+". "+ task.getTaskID() + "      Completed? : " + task.getCompletion());
            System.out.println("Task Description: " + task.getDescription());
            System.out.println();
            taskCount++;
        }
        System.out.println("Overall Recovery Plan Progress: " + targetPlan.getProgress());
    }





    public void addTask(String planID) {
        RecoveryPlan targetPlan = database.getRecoveryPlan(planID);
        Grades grade = database.getGrades(targetPlan.getStudentID(), targetPlan.getCourseID());
        RecoveryTask newTask = new RecoveryTask();
        if (grade.calculateGPA() > 2.0) {
            System.out.println("Student is not eligible for Recovery Plan.");
            System.out.println("GPA: " + grade.calculateGPA());
            return;
        }
        System.out.println();
        System.out.println("Student Grades");
        System.out.println("-----------------------");
        System.out.println("Assignment Mark: " + grade.getWeightedAssignmentMark() + "/100");
        System.out.println("Final Examination Mark: " + grade.getWeightedExamMark()+ "/100");
        System.out.println("GPA: "+grade.calculateGPA());
        System.out.println();
        System.out.print("Recommended Recovery Task: ");

        if (grade.getWeightedAssignmentMark()< 40 && grade.getWeightedAssignmentMark() < 40) {
            System.out.println("Whole Module");
        } else if (grade.getWeightedExamMark() < 40) {
            System.out.println("Final Examination");
        } else if (grade.getWeightedAssignmentMark() < 40) {
            System.out.println("Assignment");
        }

        String newDescription = InputValidation.readString("Enter Description: ");
        newTask.setDescription(newDescription);
        int newDuration = InputValidation.readInt("Enter duration for this task (days): ");
        newTask.setDuration(newDuration);

        // Creating IDManager object to generate new ID for recTask
        IDManager recTaskIDManager = new IDManager(database.getRecTaskDB());
        recTaskIDManager.getHighestTaskID();
        String nextTaskID = "T" + recTaskIDManager.generateNewID();

        newTask.setTaskID(nextTaskID);
        newTask.setPlanID(targetPlan.getPlanID());
        database.addRecoveryTask(newTask);
        database.updatePlanProgress(targetPlan.getPlanID());
    }






    public void deleteTask(String targetPlanID) {
        boolean validPlan = false;
        int taskCount = 1;
        do {
            ArrayList<RecoveryTask> planTasks = database.getPlanRecoveryTask(targetPlanID);
            if (planTasks.size() == 1) {
                System.out.println("Error. You cannot delete the last remaining recovery task under this recovery plan.");
                System.out.println();
                return;
            } else {
                validPlan = true;
                for (RecoveryTask task : planTasks) {
                    System.out.println(taskCount+". "+ task.getTaskID() + "      Completed? : " + task.getCompletion());
                    taskCount++;
                }
                System.out.println();
                int index = InputValidation.readInt("Choose Recovery Task to delete: ",1,planTasks.size());
                String targetTaskID = planTasks.get(index-1).getTaskID();
                database.removeRecoveryTask(targetTaskID);
                System.out.println();
                System.out.println("Recovery Task '" + targetTaskID + "' has been successfully deleted.");
            }
        } while (!validPlan);
        database.updatePlanProgress(targetPlanID);
    }





    public void updateTask(String targetTaskID) {
        System.out.println();
        System.out.println("Please enter detail to modify");
        System.out.println("1. Description");
        System.out.println("2. Duration:");
        System.out.println("3. Completion Status");
        int detailSelection = InputValidation.readInt(">>>   ",1,3);
        switch(detailSelection) {
            case 1:
                String newDescription = InputValidation.readString("Please enter new description: ");
                database.getRecoveryTask(targetTaskID).setDescription(newDescription);
                break;
            case 2:
                int newDuration = InputValidation.readInt("Please enter new duration: ");
                database.getRecoveryTask(targetTaskID).setDuration(newDuration);
                break;
            case 3:
                System.out.println("Please choose completion status:");
                System.out.println("1. Completed");
                System.out.println("2. Incomplete");
                int completedSelection = InputValidation.readInt(">>>   ",1,2);
                if (completedSelection == 1) {
                    database.getRecoveryTask(targetTaskID).setCompletion(true);
                } else if (completedSelection == 2) {
                    database.getRecoveryTask(targetTaskID).setCompletion(false);
                }
                break;
        }
        System.out.println("Recovery Task has been updated successfully.");
    }
}
