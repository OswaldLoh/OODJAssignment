package com.mycompany.oodjassignment.Helpers;

import java.util.ArrayList;
import java.util.List;

public class TaskGenerator {

    public static List<String> getTaskDescriptions(String planType) {
        List<String> descriptions = new ArrayList<>();

        switch (planType) {
            case "Examination":
                descriptions.add("Review all core and weak topics through notes, slides, and tutorials.");
                descriptions.add("Practice past-year papers and timed mock questions.");
                descriptions.add("Attend consultation sessions to clarify unclear concepts.");
                descriptions.add("Sit for the recovery examination.");
                break;
            case "Assignment":
                descriptions.add("Break the assignment into manageable subtasks with checkpoints.");
                descriptions.add("Revisit feedback and identify components requiring improvement.");
                descriptions.add("Consult the lecturer or tutor to confirm expectations and requirements.");
                descriptions.add("Submit the recovery assignment.");
                break;
            case "Examination and Assignment":
                descriptions.add("Create a balanced weekly schedule for revision and assignment progress.");
                descriptions.add("Strengthen weak areas and refine assignment sections based on feedback.");
                descriptions.add("Meet with lecturer/tutor to confirm both exam topics and assignment expectations.");
                descriptions.add("Complete the assignment and take the recovery examination.");
                break;
            case "Module Retake":
                descriptions.add("Attend all required lectures, tutorials, and learning activities consistently.");
                descriptions.add("Rebuild understanding through continuous weekly revision and practice.");
                descriptions.add("Complete all continuous assessments with early feedback.");
                descriptions.add("Attend final consultation to confirm assignment requirements and progress");
                descriptions.add("Sit for the final examination for the retaken module.");
                break;
        }
        return descriptions;
    }
}

