package com.mycompany.oodjassignment.functions;
import com.mycompany.oodjassignment.classes.*;

import java.util.HashMap;

public class Database {
    private HashMap<String, RecoveryPlan> recoveryPlans;
    private HashMap<String, Student> studentMap;
    private HashMap<String, RecoveryTask> recoveryTasks;

    public Database() {
        this.recoveryPlans = new HashMap<>();
        this.studentMap = new HashMap<>();
        this.recoveryTasks = new HashMap<>();
    }

    public HashMap<String, Student> getStudentMap() { return studentMap; }
    public HashMap<String, RecoveryPlan> getRecoveryPlanMap() { return recoveryPlans; }
    public HashMap<String, RecoveryTask> getRecoveryTaskMap() { return recoveryTasks; }
}
