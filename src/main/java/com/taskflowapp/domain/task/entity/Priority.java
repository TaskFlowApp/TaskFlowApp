package com.taskflowapp.domain.task.entity;

public enum Priority {
    HIGH("HIGH"),
    MEDIUM("MEDIUM"),
    LOW("LOW");

    private final String value;

    Priority(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
// Priority.HIGH.getValue() → "high" 반환