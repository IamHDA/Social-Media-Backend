package com.example.backend.Enum;

public enum MediaType {
    IMAGE("Image"),
    VIDEO("Video");

    private final String displayName;

     MediaType(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
         return displayName;
    }
}
