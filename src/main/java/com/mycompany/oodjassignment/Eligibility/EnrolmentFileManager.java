package com.mycompany.oodjassignment.Eligibility;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles reading and writing enrolment records to a binary file.
 * This encapsulates file handling logic away from the GUI.
 */
public class EnrolmentFileManager {

    // Name of the binary file used to store enrolment records.
    private static final String FILE_NAME = "enrolments.dat";

    /**
     * Loads all enrolment records from the binary file.
     * If the file does not exist or cannot be read, an empty list is returned.
     */
    @SuppressWarnings("unchecked")
    public static List<EnrolmentRecord> loadEnrolments() {
        File f = new File(FILE_NAME);
        if (!f.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (List<EnrolmentRecord>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Writes the entire list of enrolment records to the binary file.
     * This method overwrites the existing file content.
     */
    public static void saveEnrolments(List<EnrolmentRecord> records) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(records);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Appends a single enrolment record by reading existing data,
     * adding the new record, and writing all records back to the file.
     */
    public static void appendEnrolment(EnrolmentRecord record) {
        List<EnrolmentRecord> current = loadEnrolments();
        current.add(record);
        saveEnrolments(current);
    }
}

