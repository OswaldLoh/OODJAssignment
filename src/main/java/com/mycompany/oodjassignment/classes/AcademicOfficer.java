package com.mycompany.oodjassignment.classes;

import com.mycompany.oodjassignment.functions.Database;
import com.mycompany.oodjassignment.functions.IDManager;
import java.util.ArrayList;
import java.util.Scanner;
public class AcademicOfficer extends User {
    private static final long serialVersionUID = 1L;

    private String department;
    private String officeLocation;

    public AcademicOfficer() {
        super();
        setRole(UserRole.ACADEMIC_OFFICER);
        this.department = "";
        this.officeLocation = "";
    }

    public AcademicOfficer(String userID,
                           String username,
                           String password,
                           String fullName,
                           String email) {
        this(userID, username, password, fullName, email, "", "");
    }

    public AcademicOfficer(String userID,
                           String username,
                           String password,
                           String fullName,
                           String email,
                           String department,
                           String officeLocation) {
        super(userID, username, password, fullName, email, UserRole.ACADEMIC_OFFICER);
        this.department = department;
        this.officeLocation = officeLocation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    @Override
    public String getPermissions() {
        return "Full access to student records, course recovery plans, eligibility "
                + "checks, enrollment management, and academic reporting";
    }

    @Override
    public String toFileString() {
        return super.toFileString() + "|" + department + "|" + officeLocation;
    }

    @Override
    public String toString() {
        return String.format("AcademicOfficer[ID=%s, Username=%s, Name=%s, Department=%s]",
                getUserID(), getUsername(), getFullName(), department);
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
                double courseGPA = grade.calculateGPA(database);
                Course course = database.getCourseDB().get(grade.getCourseID());
                if (courseGPA < 2.0) {
                    courseCount++;
                    System.out.println(courseCount + ". " + course.getCourseName() + "-" + course.getCourseID());
                    System.out.println("   GPA: " + grade.calculateGPA(database));
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

        // Displaying failed students for the user by iterating through the array list
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
        IDManager IDManager = new IDManager();
        IDManager.getHighestTaskID(database.getRecPlanDB());
        String nextPlanID = "P"+IDManager.generateNewID();

        // make new RecoveryPlan object
        RecoveryPlan newPlan = new RecoveryPlan(nextPlanID,targetStudentID,userID,"0.00");
        RecoveryTask newTask = newPlan.addNewTask(database);     // Call instance to create RecoveryTask
        database.addRecoveryPlan(newPlan);
        database.addRecoveryTask(newTask);
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
