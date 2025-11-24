package com.mycompany.oodjassignment.academicofficerGUI;

import com.mycompany.oodjassignment.functions.Database;

import javax.swing.*;

public class RecoveryPlanDashboard {
    private Database database;
    private JPanel recoveryPlanDashboardPanel;
    private JTable planListTable;
    private JButton Search;
    private JTextField txtPlanID;
    private JButton modifyTasksButton;
    private JButton deletePlanButton;
    private JButton addPlanButton;
    private JLabel recoveryPlanDashboardTitle;
    private JScrollPane planListScroll;

    public RecoveryPlanDashboard(Database database) {
        this.database = database;


    }
    public JPanel getRecoveryPlanDashboardPanel() {
        return recoveryPlanDashboardPanel;
    }
}
