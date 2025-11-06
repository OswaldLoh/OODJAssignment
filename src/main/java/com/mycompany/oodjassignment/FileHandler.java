package com.mycompany.oodjassignment;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("CallToPrintStackTrace")
public class FileHandler {
    public static void main(String[]args) {
        try {
            FileWriter output = new FileWriter("testing.txt");
            output.write("im testing here");
            output.close();

            FileReader input = new FileReader("testing.txt");
            int code = input.read();
            System.out.println((char) code);wd
            input.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeFile(String filename, String contents) {
        try {
            

        } catch(IOException e) {

        }
    }
}
