package com.mycompany.oodjassignment.usermanagement.service;

import com.mycompany.oodjassignment.classes.*;
import com.mycompany.oodjassignment.usermanagement.util.PasswordUtil;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    
    private static final String USER_FILE = "data/users.csv";
    private static final String DEFAULT_USER_RESOURCE = "/users.csv";
    private List<User> users;
    
    public UserManager() {
        this.users = new ArrayList<>();
        ensureDataDirectory();
        loadUsers();
    }
    
    private void ensureDataDirectory() {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
            System.out.println("Created data directory at: " + dir.getAbsolutePath());
        } else {
            System.out.println("Data directory exists at: " + dir.getAbsolutePath());
        }
        
        File userFile = new File(USER_FILE);
        if (!userFile.exists()) {
            if (!copyDefaultUsersFromResource(userFile)) {
                createDefaultUsers();
            }
            System.out.println("Created users file at: " + userFile.getAbsolutePath());
        } else {
            System.out.println("Users file exists at: " + userFile.getAbsolutePath());
        }
    }

    private boolean copyDefaultUsersFromResource(File destination) {
        try (InputStream in = UserManager.class.getResourceAsStream(DEFAULT_USER_RESOURCE)) {
            if (in == null) {
                return false;
            }
            try (OutputStream out = new FileOutputStream(destination)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    private void createDefaultUsers() {
        AcademicOfficer defaultOfficer = new AcademicOfficer(
            "AO001",
            "admin",
            PasswordUtil.hashPassword("admin123"),
            "System Administrator",
            "admin@crs.edu",
            "Administration",
            "Main Office"
        );
        
        CourseAdministrator defaultAdmin = new CourseAdministrator(
            "CA001",
            "course_admin",
            PasswordUtil.hashPassword("admin123"),
            "Course Admin",
            "courseadmin@crs.edu",
            "Computer Science",
            "IT"
        );
        
        users.add(defaultOfficer);
        users.add(defaultAdmin);
        saveUsers();
    }
    
    private void loadUsers() {
        File file = new File(USER_FILE);
        if (!file.exists()) {
            return;
        }
        
        users.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                User user = parseUserFromString(line);
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private User parseUserFromString(String line) {
        try {
            String[] parts = line.split(",", -1);
            if (parts.length < 9) {
                return null;
            }
            
            String userId = parts[0].trim();
            String username = parts[1].trim();
            String password = parts[2].trim();
            String fullName = parts[3].trim();
            String email = parts[4].trim();
            UserRole role = UserRole.valueOf(parts[5].trim());
            UserStatus status = UserStatus.valueOf(parts[6].trim());
            LocalDateTime createdDate = LocalDateTime.parse(parts[7].trim(), 
                DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime lastModifiedDate = LocalDateTime.parse(parts[8].trim(), 
                DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            
            User user;
            if (role == UserRole.ACADEMIC_OFFICER) {
                String department = parts.length > 9 ? parts[9].trim() : "";
                String officeLocation = parts.length > 10 ? parts[10].trim() : "";
                AcademicOfficer officer = new AcademicOfficer(userId, username, 
                    password, fullName, email, department, officeLocation);
                officer.setStatus(status);
                officer.setCreatedDate(createdDate);
                officer.setLastModifiedDate(lastModifiedDate);
                user = officer;
            } else {
                String managedCourses = parts.length > 9 ? parts[9].trim() : "";
                String specialization = parts.length > 10 ? parts[10].trim() : "";
                CourseAdministrator admin = new CourseAdministrator(userId, username, 
                    password, fullName, email, managedCourses, specialization);
                admin.setStatus(status);
                admin.setCreatedDate(createdDate);
                admin.setLastModifiedDate(lastModifiedDate);
                user = admin;
            }
            
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (User user : users) {
                bw.write(user.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean addUser(User user) {
        if (getUserByUsername(user.getUsername()) != null) {
            return false;
        }
        
        if (getUserById(user.getUserId()) != null) {
            return false;
        }
        
        users.add(user);
        saveUsers();
        return true;
    }
    
    public boolean updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(updatedUser.getUserId())) {
                users.set(i, updatedUser);
                saveUsers();
                return true;
            }
        }
        return false;
    }
    
    public boolean deactivateUser(String userId) {
        User user = getUserById(userId);
        if (user != null) {
            user.deactivate();
            saveUsers();
            return true;
        }
        return false;
    }
    
    public boolean activateUser(String userId) {
        User user = getUserById(userId);
        if (user != null) {
            user.activate();
            saveUsers();
            return true;
        }
        return false;
    }
    
    public boolean deleteUser(String userId) {
        User user = getUserById(userId);
        if (user != null) {
            users.remove(user);
            saveUsers();
            return true;
        }
        return false;
    }
    
    public User getUserById(String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
    
    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    
    public User getUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    public List<User> getActiveUsers() {
        List<User> activeUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getStatus() == UserStatus.ACTIVE) {
                activeUsers.add(user);
            }
        }
        return activeUsers;
    }
    
    public List<User> getUsersByRole(UserRole role) {
        List<User> roleUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getRole() == role) {
                roleUsers.add(user);
            }
        }
        return roleUsers;
    }
    
    public String generateNextUserId(UserRole role) {
        String prefix = role == UserRole.ACADEMIC_OFFICER ? "AO" : "CA";
        int maxNumber = 0;
        
        for (User user : users) {
            if (user.getUserId().startsWith(prefix)) {
                try {
                    int number = Integer.parseInt(user.getUserId().substring(2));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
        
        return String.format("%s%03d", prefix, maxNumber + 1);
    }
    
    public List<User> searchUsers(String keyword) {
        List<User> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        
        for (User user : users) {
            if (user.getUsername().toLowerCase().contains(lowerKeyword) ||
                user.getFullName().toLowerCase().contains(lowerKeyword) ||
                user.getEmail().toLowerCase().contains(lowerKeyword) ||
                user.getUserId().toLowerCase().contains(lowerKeyword)) {
                results.add(user);
            }
        }
        
        return results;
    }
}

