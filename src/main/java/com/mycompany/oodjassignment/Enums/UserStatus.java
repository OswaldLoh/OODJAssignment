package com.mycompany.oodjassignment.Enums;
public enum UserStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    DEACTIVATED("Deactivated");

    private final String displayName;

    UserStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

