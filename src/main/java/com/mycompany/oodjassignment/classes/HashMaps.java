package com.mycompany.oodjassignment.classes;
import java.util.HashMap;

public class HashMaps {
    HashMap<String, RecoveryPlan> recoveryPlans;
    HashMap<String, Student> studentMap;

    public HashMaps() {};
    public HashMaps(HashMap<String, RecoveryPlan> recoveryPlans, HashMap<String, Student> studentMap) {
        this.recoveryPlans = recoveryPlans;
        this.studentMap = studentMap;
    }
    public HashMap<String, RecoveryPlan> getRecoveryPlanMap() { return recoveryPlans; }
    public HashMap<String, Student> getStudentMap() { return studentMap; }
}
