package com.mycompany.oodjassignment.Eligibility;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple audit logger for enrolment activities.
 * Every action is appended to a timestamped text file.
 */
public class EnrolmentAudit {

    private static final String LOG_FILE = "enrolment_log.txt";
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String studentID, String name, double cgpa, int fails, String action) {
        String timestamp = LocalDateTime.now().format(FMT);

        String line = String.format(
                "%s - %s %s - %s (CGPA %.2f, Failed %d)\n",
                timestamp, studentID, name, action, cgpa, fails
        );

        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            fw.write(line);
        } catch (IOException e) {
            System.out.println("Failed to write audit log.");
        }
    }
}
