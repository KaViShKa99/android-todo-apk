package com.example.todoapp.Models;

import androidx.annotation.NonNull;

public class TodoItem {
    private int id;
    private String title;
    private String description;
    private String dueDate;
    private String tags;
    private String user;
    private boolean isCompleted;

    public TodoItem(int id, String title, String description, String dueDate, String tags, String user, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.tags = tags;
        this.user = user;
        this.isCompleted = isCompleted;
    }

    public TodoItem(String title, String description, String dueDate, String tags, String user, boolean isCompleted) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.tags = tags;
        this.user = user;
        this.isCompleted = isCompleted;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getTags() {
        return tags;
    }

    public String getUser() {
        return user;
    }

    @NonNull
    @Override
    public String toString() {
        return "TodoItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", tags='" + tags + '\'' +
                ", user='" + user + '\'' +
                ", isCompleted=" + isCompleted +
                '}';
    }
}
