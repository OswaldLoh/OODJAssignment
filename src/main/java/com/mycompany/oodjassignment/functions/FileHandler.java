package com.mycompany.oodjassignment.functions;
import com.mycompany.oodjassignment.classes.*;
import java.io.*;
import java.util.HashMap;

@SuppressWarnings("CallToPrintStackTrace")

public class FileHandler {

    // parse lines from CSV files into objects and add them into a HashMap
    public static <Class extends CSVParser<Class>> HashMap<String, Class> readCSV(Class parseTarget) {
        HashMap<String, Class> resultHashMap = new HashMap<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(parseTarget.getFilename()))) {
            String line;
            fileReader.readLine();
            while ((line = fileReader.readLine()) != null) {
                String[] details = line.split(",");
                resultHashMap.put(details[0], parseTarget.fromCSV(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultHashMap;
    }

    // writing back to CSV file based on the objects in the HashMap
    public static <Class extends CSVParser<Class>> void writeCSV(Class parseTarget, HashMap<String, Class> sourceHashMap) {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(parseTarget.getFilename()))) {
            printWriter.println(parseTarget.getFileHeader());
            for (Class object : sourceHashMap.values()) {
                printWriter.println(object.toCSV());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
