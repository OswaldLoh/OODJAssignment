package com.mycompany.oodjassignment.usermanagement.gui;

import com.mycompany.oodjassignment.classes.User;
import com.mycompany.oodjassignment.classes.UserRole;
import com.mycompany.oodjassignment.classes.UserStatus;
import com.mycompany.oodjassignment.usermanagement.service.AuthenticationService;
import com.mycompany.oodjassignment.usermanagement.service.UserManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Main User Management Frame
 * Demonstrates GUI implementation with table-based user management
 */
public class UserManagementFrame extends JFrame {
    
    private AuthenticationService authService;
    private UserManager userManager;
    
    // GUI Components
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton updateButton;
    private JButton deactivateButton;
    private JButton activateButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton viewLogsButton;
    private JButton changePasswordButton;
    private JButton logoutButton;
    private JTextField searchField;
    private JButton searchButton;
    private JLabel currentUserLabel;
    private JComboBox<String> filterComboBox;
    
    public UserManagementFrame(AuthenticationService authService) {
        this.authService = authService;
        this.userManager = new UserManager();
        initComponents();
        loadUsers();
    }
    
    private void initComponents() {
        setTitle("CRS - User Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        
        // Search Panel
        JPanel searchPanel = createSearchPanel();
        
        // Table Panel
        JPanel tablePanel = createTablePanel();
        
        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        
        // Combine search and table
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        
        // Add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("User Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        User currentUser = authService.getCurrentUser();
        currentUserLabel = new JLabel("Logged in as: " + currentUser.getFullName() + 
                                     " (" + currentUser.getRole().getDisplayName() + ")");
        currentUserLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        currentUserLabel.setForeground(Color.WHITE);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(currentUserLabel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(new Color(240, 248, 255));
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.PLAIN, 14));
        searchButton.addActionListener(e -> performSearch());
        
        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        filterComboBox = new JComboBox<>(new String[]{
            "All Users", "Active Only", "Deactivated", 
            "Academic Officers", "Course Administrators"
        });
        filterComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        filterComboBox.addActionListener(e -> applyFilter());
        
        refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 14));
        refreshButton.addActionListener(e -> loadUsers());
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(filterLabel);
        searchPanel.add(filterComboBox);
        searchPanel.add(refreshButton);
        
        return searchPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        
        // Create table model
        String[] columnNames = {"User ID", "Username", "Full Name", "Email", 
                               "Role", "Status", "Created Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        userTable = new JTable(tableModel);
        userTable.setFont(new Font("Arial", Font.PLAIN, 13));
        userTable.setRowHeight(25);
        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        userTable.getTableHeader().setBackground(new Color(70, 130, 180));
        userTable.getTableHeader().setForeground(Color.BLACK);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));
        
        addButton = createStyledButton("Add User", new Color(34, 139, 34));
        addButton.addActionListener(e -> showAddUserDialog());
        
        updateButton = createStyledButton("Update User", new Color(70, 130, 180));
        updateButton.addActionListener(e -> showUpdateUserDialog());
        
        activateButton = createStyledButton("Activate", new Color(34, 139, 34));
        activateButton.addActionListener(e -> activateUser());
        
        deactivateButton = createStyledButton("Deactivate", new Color(255, 140, 0));
        deactivateButton.addActionListener(e -> deactivateUser());
        
        deleteButton = createStyledButton("Delete", new Color(220, 20, 60));
        deleteButton.addActionListener(e -> deleteUser());
        
        viewLogsButton = createStyledButton("View Login Logs", new Color(138, 43, 226));
        viewLogsButton.addActionListener(e -> showLoginLogs());
        
        changePasswordButton = createStyledButton("Change Password", new Color(70, 130, 180));
        changePasswordButton.addActionListener(e -> showChangePasswordDialog());
        
        logoutButton = createStyledButton("Logout", new Color(128, 128, 128));
        logoutButton.addActionListener(e -> logout());
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(activateButton);
        buttonPanel.add(deactivateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewLogsButton);
        buttonPanel.add(changePasswordButton);
        buttonPanel.add(logoutButton);
        
        return buttonPanel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 35));
        return button;
    }
    
    private void loadUsers() {
        tableModel.setRowCount(0);
        List<User> users = userManager.getAllUsers();
        
        for (User user : users) {
            Object[] row = {
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().getDisplayName(),
                user.getStatus(),
                user.getCreatedDate().toLocalDate()
            };
            tableModel.addRow(row);
        }
    }
    
    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadUsers();
            return;
        }
        
        tableModel.setRowCount(0);
        List<User> users = userManager.searchUsers(keyword);
        
        for (User user : users) {
            Object[] row = {
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().getDisplayName(),
                user.getStatus(),
                user.getCreatedDate().toLocalDate()
            };
            tableModel.addRow(row);
        }
    }
    
    private void applyFilter() {
        String filter = (String) filterComboBox.getSelectedItem();
        tableModel.setRowCount(0);
        List<User> users;
        
        switch (filter) {
            case "Active Only":
                users = userManager.getActiveUsers();
                break;
            case "Deactivated":
                users = userManager.getAllUsers();
                users.removeIf(u -> u.getStatus() != UserStatus.DEACTIVATED);
                break;
            case "Academic Officers":
                users = userManager.getUsersByRole(UserRole.ACADEMIC_OFFICER);
                break;
            case "Course Administrators":
                users = userManager.getUsersByRole(UserRole.COURSE_ADMINISTRATOR);
                break;
            default:
                users = userManager.getAllUsers();
        }
        
        for (User user : users) {
            Object[] row = {
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().getDisplayName(),
                user.getStatus(),
                user.getCreatedDate().toLocalDate()
            };
            tableModel.addRow(row);
        }
    }
    
    private void showAddUserDialog() {
        AddUserDialog dialog = new AddUserDialog(this, userManager);
        dialog.setVisible(true);
        if (dialog.isUserAdded()) {
            loadUsers();
        }
    }
    
    private void showUpdateUserDialog() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to update",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String userId = (String) tableModel.getValueAt(selectedRow, 0);
        User user = userManager.getUserById(userId);
        
        UpdateUserDialog dialog = new UpdateUserDialog(this, userManager, user);
        dialog.setVisible(true);
        if (dialog.isUserUpdated()) {
            loadUsers();
        }
    }
    
    private void activateUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to activate",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String userId = (String) tableModel.getValueAt(selectedRow, 0);
        if (userManager.activateUser(userId)) {
            JOptionPane.showMessageDialog(this, "User activated successfully",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            loadUsers();
        }
    }
    
    private void deactivateUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to deactivate",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String userId = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to deactivate this user?",
            "Confirm Deactivation", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (userManager.deactivateUser(userId)) {
                JOptionPane.showMessageDialog(this, "User deactivated successfully",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUsers();
            }
        }
    }
    
    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String userId = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to permanently delete this user?\nThis action cannot be undone!",
            "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (userManager.deleteUser(userId)) {
                JOptionPane.showMessageDialog(this, "User deleted successfully",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUsers();
            }
        }
    }
    
    private void showLoginLogs() {
        LoginLogDialog dialog = new LoginLogDialog(this, authService);
        dialog.setVisible(true);
    }
    
    private void showChangePasswordDialog() {
        PasswordChangeDialog dialog = new PasswordChangeDialog(this, authService);
        dialog.setVisible(true);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            authService.logout();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            dispose();
        }
    }
}

