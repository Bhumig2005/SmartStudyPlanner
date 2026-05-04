package com.studyplanner.model;

import java.time.LocalDate;

public class StudyTask extends AbstractTask {
    public StudyTask() {
    }

    public StudyTask(int taskId, int subjectId, String title, LocalDate deadline, String priority, String status) {
        super(taskId, subjectId, title, deadline, priority, status);
    }

    @Override
    public String getTaskType() {
        return "Study";
    }
}
