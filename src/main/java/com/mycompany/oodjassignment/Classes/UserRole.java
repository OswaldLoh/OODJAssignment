package com.mycompany.oodjassignment.Classes;
public enum UserRole {
    ACADEMIC_OFFICER("Academic Officer"),
    COURSE_ADMINISTRATOR("Course Administrator");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static UserRole fromLabel(String label) {
        if (label == null) {
            return null;
        }
        String normalized = label.trim();
        for (UserRole role : values()) {
            if (role.displayName.equalsIgnoreCase(normalized) || role.name().equalsIgnoreCase(normalized.replace(' ', '_'))) {
                return role;
            }
        }
        if ("Admin Officer".equalsIgnoreCase(normalized)) {
            return COURSE_ADMINISTRATOR;
        }
        return null;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

