package com.mycompany.oodjassignment.functions;

import java.io.*;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("CallToPrintStackTrace")
public class FileHandler {
    public static void writeFile(String filename, String contents) {           // method for inputting data
        try (PrintWriter fileInput = new PrintWriter(filename)) {
            fileInput.print(contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile(String filename){
        try (InputStream targetFile = getClass().getClassLoader().getResourceAsStream(filename);
             BufferedReader fileOutput = new BufferedReader(new InputStreamReader(targetFile))) {
            String line;
            while ((line = fileOutput.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file.");
            e.printStackTrace();
        }
    }
}
