package Eligibility;

// Interface representing a general eligibility rule.
public interface EligibilityRule {

    // Determines if a given student is eligible based on the implemented rule.
    // return true if eligible, false otherwise
    boolean isEligible(Student student);
}
