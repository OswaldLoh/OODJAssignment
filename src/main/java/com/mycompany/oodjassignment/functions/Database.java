package com.mycompany.oodjassignment.functions;
import com.mycompany.oodjassignment.classes.*;

import java.util.HashMap;

public class Database {
    HashMap<String, RecoveryPlan> recoveryPlans;
    HashMap<String, Student> studentMap;

    public Database() {
        this.recoveryPlans = new HashMap<>();
        this.studentMap = new HashMap<>();
    }
    public HashMap<String, Student> getStudentMap() { return studentMap; }
    public HashMap<String, RecoveryPlan> getRecoveryPlanMap() { return recoveryPlans; }
}
