package com.studyplanner.model;

import java.time.LocalDate;

public abstract class AbstractTask implements Scheduleable, Trackable {
    private int taskId;
    private int subjectId;
    private String title;
    private LocalDate deadline;
    private String priority;
    private String status;

    public AbstractTask() {
    }

    public AbstractTask(int taskId, int subjectId, String title, LocalDate deadline, String priority, String status) {
        this.taskId = taskId;
        this.subjectId = subjectId;
        this.title = title;
        this.deadline = deadline;
        this.priority = priority;
        this.status = status;
    }

    public abstract String getTaskType();

    @Override
    public boolean isDueToday() {
        return deadline != null && deadline.equals(LocalDate.now());
    }

    @Override
    public void markCompleted() {
        this.status = "Completed";
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
