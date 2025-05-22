package com.example.backend.Enum;


public enum ParticipantRole {
    CREATOR("Người tạo nhóm"),
    MOD("Quản trị viên"),
    MEMBER("Thành viên");

    private final String displayName;

    ParticipantRole(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }
}
