package com.loadmap.chocho.loadmap;

import java.util.Date;

/**
 * Created by q on 2017-01-23.
 */

public class Task {
    private String username;
    private Course subject;
    private String taskType;
    private long dateTime;
    private long duration;

    public Task(String username, Course subject, String taskType, long dateTime, long duration) {
        this.username = username;
        this.subject = subject;
        this.taskType = taskType;
        this.dateTime = dateTime;
        this.duration = duration;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Course getSubject() {
        return subject;
    }
    public void setSubject(Course subject) {
        this.subject = subject;
    }
    public String getTaskType() {
        return taskType;
    }
    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }
    public long getDateTime() {
        return dateTime;
    }
    public void setDateTime(long startTime) {
        this.dateTime = dateTime;
    }
    public long getDuration() {
        return duration;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }
}
