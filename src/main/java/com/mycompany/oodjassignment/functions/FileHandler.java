package com.mycompany.oodjassignment.functions;
import com.mycompany.oodjassignment.classes.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CallToPrintStackTrace")

public class FileHandler {
    public void appendFile(String filename, String contents) {          // completed, method for appending text file
        try (PrintWriter fileInput = new PrintWriter(filename)) {
            fileInput.append(contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Student> parseFile(String filename){          // completed, method for reading text file
        ArrayList<Student> students = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line=fileReader.readLine()) != null) {
                String[] studentDetails = line.split(",");
                students.add(new Student(studentDetails[0],studentDetails[1],studentDetails[2],studentDetails[3],studentDetails[4]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }
}
