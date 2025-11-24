package com.mycompany.oodjassignment.Eligibility;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles binary file storage for enrolment history.
 * Provides load, save, and append operations.
 */
public class EnrolmentFileManager {

    private static final String FILE_NAME = "enrolments.dat";

    /**
     * Loads the entire list of enrolment records.
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
     * Saves the full list of enrolment records back to the binary file.
     */
    public static void saveEnrolments(List<EnrolmentRecord> records) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(records);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds one new enrolment entry to the file.
     * Uses load → add → save pattern.
     */
    public static void appendEnrolment(EnrolmentRecord record) {
        List<EnrolmentRecord> records = loadEnrolments();
        records.add(record);
        saveEnrolments(records);
    }
}
