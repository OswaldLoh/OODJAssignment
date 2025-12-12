package com.mycompany.oodjassignment;
import com.mycompany.oodjassignment.Classes.*;
import com.mycompany.oodjassignment.Helpers.*;

public class CourseRecoverySystem {
    public static void main(String[]args) {
        Database database = new Database();
        Grades testGrade = database.getGrades("S015","C224");
        System.out.println(testGrade.getWeightedAssignmentMark() + " A------E " + testGrade.getWeightedExamMark());
        System.out.println("GPA: " + testGrade.calculateGPA());
    }
}
