package com.mycompany.oodjassignment.Entities;

import com.mycompany.oodjassignment.Enums.UserRole;

public class CourseAdministrator extends User {
    private static final long serialVersionUID = 1L;

    private String managedCourses;
    private String specialization;

    public CourseAdministrator() {
        super();
        setRole(UserRole.COURSE_ADMINISTRATOR);
        this.managedCourses = "";
        this.specialization = "";
    }

    public CourseAdministrator(String userID, String username, String password, String fullName, String email) {
        this(userID, username, password, fullName, email, "", "");
    }

    public CourseAdministrator(String userID, String username, String password, String fullName, String email, String managedCourses, String specialization) {
        super(userID, username, password, fullName, email, UserRole.COURSE_ADMINISTRATOR);
        this.managedCourses = managedCourses;
        this.specialization = specialization;
    }

    public String getManagedCourses() {
        return managedCourses;
    }

    public void setManagedCourses(String managedCourses) {
        this.managedCourses = managedCourses;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public String getPermissions() {
        return "Access to course recovery plans, milestone tracking, grading entry, "
                + "and course-specific reports";
    }

    @Override
    public String toFileString() {
        return super.toFileString() + "," + managedCourses + "," + specialization;
    }

    @Override
    public String toString() {
        return String.format("CourseAdministrator[ID=%s, Username=%s, Name=%s, Specialization=%s]",
                getUserID(), getUsername(), getFullName(), specialization);
    }
}

