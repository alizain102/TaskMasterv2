package com.alihassan.taskmasterv2;

public class Task {
    private String title;
    private String description;
    private String dueDate;
    private String notes;
    private String priority;
    private String username;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isStatus() {
        return status;
    }

    private boolean status=false;

    public Task(String title, String description, String dueDate, String notes, String priority, String username, boolean status) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.notes = notes;
        this.priority = priority;
        this.username = username;
        this.status=status;
    }

    public String getUsername() {
        return username;
    }

    public String toString() {
        return title;
    }

    public void setStatus(boolean status)
    {
        this.status=status;
    }
}