package com.loadmap.chocho.loadmap;

/**
 * Created by q on 2017-01-23.
 */

public class TaskType {
    private String taskType;
    private long totalDuration;
    private Task[] tasks;

    public String getTaskType() {
        return taskType;
    }
    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }
    public long getTotalDuration() {
        return totalDuration;
    }
    public void setTotalDuration(long totalDuration) {
        this.totalDuration = totalDuration;
    }
    public Task[] getTasks() {
        return tasks;
    }
    public void setTasks(Task[] tasks) {
        this.tasks = tasks;
    }
}
