package com.example.backend.Enum;

public enum UserStatus {
    ONLINE("Online"),
    OFFLINE("Offline");

    private final String displayName;

    UserStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}