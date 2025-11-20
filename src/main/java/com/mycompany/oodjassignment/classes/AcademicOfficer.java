package com.mycompany.oodjassignment.classes;
import com.mycompany.oodjassignment.functions.*;
import java.util.*;

public class AcademicOfficer extends User {
    public AcademicOfficer() {
        setRole("Academic Officer");
    }
    public void searchStudent(Database database) {
        Scanner userInput = new Scanner(System.in);
        int courseCount = 0;
        boolean studentFound;
        Student student;
        do {
            System.out.print("Please enter Student ID: ");
            String targetStudentID = userInput.nextLine();
            student = database.getStudent(targetStudentID);
            if (student == null) {
                System.out.println("Student is not found inside database. Please try again.");
                studentFound = false;
            } else {
                studentFound = true;
            }
        } while (!studentFound);
        System.out.println();
        System.out.println("----------------------");
        System.out.println("Student ID: " + student.getStudentID());
        System.out.println("Name: " + student.getFirstName() + " "+ student.getLastName());
        System.out.println("Major: " + student.getMajor());
        System.out.println("----------------------");
        System.out.println("Failed Modules: ");
        System.out.println();
        for (Grades grade : database.getGradeDB().values()) {
            if (grade.getStudentID().equals(student.getStudentID())) {
                Course course = database.getCourse(grade.getCourseID());
                grade.setCourseObject(course);
                double courseGPA = grade.calculateGPA();
                if (courseGPA < 2.0) {
                    courseCount++;
                    System.out.println(courseCount + ". " + course.getCourseName() + "-" + course.getCourseID());
                    System.out.println("   GPA: " + grade.calculateGPA());
                }
            }
        }
        if (courseCount == 0) {
            System.out.println("Student has no failed modules.");
        }
    }

    // Adding new recovery plan after checking existence of user ID ( Fully Working )
    public void addRecoveryPlan(Database database) {
        Scanner userInput = new Scanner(System.in);
        int failStudentCount = 0, studentSelection;
        boolean studentFound = false, courseFound;
        String targetStudentID = "", targetCourseID;
        ArrayList<Student> failedStudents;

        // parsing failed students object into array list
        do {
            System.out.print("Please enter Course ID: ");
            targetCourseID = userInput.nextLine();
            failedStudents = database.getFailedStudents(targetCourseID, database);
            if (failedStudents == null || failedStudents.isEmpty()) {
                System.out.println("Error. Course ID " + targetCourseID + " is not found inside database, or may not have any failing students. Please try again.");
                System.out.println();
                courseFound = false;
            } else {
                courseFound = true;
            }
        } while (!courseFound);
        Course selectedCourse = database.getCourse(targetCourseID);
        // Displaying failed students for the user by iterating through the array list
        System.out.println("Failed Students for Module: " + selectedCourse.getCourseID() + "-" + selectedCourse.getCourseName());
        for (Student student : failedStudents) {
            failStudentCount++;
            System.out.println(failStudentCount+". "+student.getStudentID()+" "+student.getFirstName()+" "+student.getLastName());
        }

        // GUI based selection by indexing through array list of failed students
        do {
            System.out.print(">>>   ");
            studentSelection = userInput.nextInt();
            if (studentSelection <= 0 || studentSelection > failedStudents.size()) {
                System.out.println("Invalid selection. Please try again.");
                System.out.println();
            } else {
                final int listIndex = studentSelection - 1;
                targetStudentID = failedStudents.get(listIndex).getStudentID();
                studentFound = true;
            }
        } while (!studentFound);

        // Create an instance of IDManager to generate next ID for PlanID
        IDManager IDManager = new IDManager(database.getRecPlanDB());
        IDManager.getHighestTaskID();
        String nextPlanID = "P"+IDManager.generateNewID();

        // make new RecoveryPlan object
        Grades targetGrade = database.getGrades(targetStudentID,targetCourseID);
        RecoveryPlan newPlan = new RecoveryPlan(nextPlanID,targetStudentID,targetCourseID,userID,"0.00");
        RecoveryTask newTask = newPlan.addNewTask(targetGrade, database);     // Call instance to create RecoveryTask
        database.addRecoveryPlan(newPlan);
        database.addRecoveryTask(newTask);
        System.out.println("Recovery Plan and Task successfully added for Student " + targetStudentID);
    }

    // delete recovery plan
    public void deleteRecoveryPlan(Database database) {
        Scanner userInput = new Scanner(System.in);
        String targetStudentID;
        boolean studentFound, planDelete = false;
        int planSelection;
        Student student;

        do {
            System.out.print("Please enter Student ID: ");
            targetStudentID = userInput.nextLine();
            student = database.getStudent(targetStudentID);
            if (student == null) {
                System.out.println("Student is not found inside database. Please try again.");
                studentFound = false;
            } else {
                studentFound = true;
            }
        } while (!studentFound);

        ArrayList<String> studentPlanID = database.findStudentRecoveryPlan(targetStudentID);
        int planCount = database.getStudentRecoveryPlanCount(targetStudentID);

        if (planCount == 0) {               // no recovery plan is found for student
            System.out.println("Error. Student " + targetStudentID + " has no recovery plans.");
        } else {                            // student has more than 1 or more recovery plans
            int number = 1;
            System.out.println("Recovery Plans for Student " + targetStudentID);
            for (String planID : studentPlanID) {
                System.out.println(number+". "+planID);
                number +=1;
            }
            System.out.println("Choose PlanID to delete");
            do {
                System.out.print(">>>   ");
                planSelection = userInput.nextInt();
                if (planSelection <= 0 || planSelection > studentPlanID.size()) {
                    System.out.println("Invalid selection. Please try again.");
                    System.out.println();
                } else {
                    final int listIndex = planSelection - 1;
                    database.removeRecoveryPlan(studentPlanID.get(listIndex));
                    database.removeRecoverytask(studentPlanID.get(listIndex));
                    planDelete = true;
                }
            } while (!planDelete);
        }
    }

    @Override
    public void showMenu() {
        System.out.println("------------------------------");
        System.out.println("1. Add Recovery Plan");
        System.out.println("2. Update Recovery Plans (Haven't Done)");
        System.out.println("3. Delete Recovery Plans");
        System.out.println("4. Search student and show failed components");
        System.out.println("4. Exit");
    }
}
