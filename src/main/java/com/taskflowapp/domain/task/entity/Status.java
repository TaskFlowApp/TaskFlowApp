package com.taskflowapp.domain.task.entity;

public enum  Status {
    TODO("TO_DO"),
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE");

    private final String value;

    Status(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}
