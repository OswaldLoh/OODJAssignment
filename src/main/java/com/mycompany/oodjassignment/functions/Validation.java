package com.mycompany.oodjassignment.functions;

import com.mycompany.oodjassignment.classes.Student;
import java.util.HashMap;

public class Validation {

    public boolean validateStudentID(String targetStudentID, HashMap<String, Student> studentDB) {
        boolean studentFound = false;
        if (studentDB.containsKey(targetStudentID)) {
            System.out.println("Student is found inside the hash map.");
            studentFound = true;
        }   else {
            System.out.println("Student is NOT FOUND.");
        }
        return studentFound;
    }




}
