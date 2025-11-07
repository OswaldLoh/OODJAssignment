package com.mycompany.oodjassignment.functions;
import java.io.*;

@SuppressWarnings("CallToPrintStackTrace")

public class FileHandler {
    public void appendFile(String filename, String contents) {          // completed, method for appending text file
        try (PrintWriter fileInput = new PrintWriter(filename)) {
            fileInput.append(contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile(String filename){          // completed, method for reading text file
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line=fileReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseFile(String filename) {

    }
}
