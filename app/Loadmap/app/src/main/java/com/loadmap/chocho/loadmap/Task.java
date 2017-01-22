package com.loadmap.chocho.loadmap;

import java.util.Date;

/**
 * Created by q on 2017-01-23.
 */

public class Task {
    private String username;
    private String subject;
    private String taskType;
    private long startTime;
    private long duration;

    public Task(String username, String subject, String taskType, long startTime, long duration) {
        this.username = username;
        this.subject = subject;
        this.taskType = taskType;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getTaskType() {
        return taskType;
    }
    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }
    public long getStartTime() {
        return startTime;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public long getDuration() {
        return duration;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }
}
