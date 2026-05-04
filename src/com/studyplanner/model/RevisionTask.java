package com.studyplanner.model;

import java.time.LocalDate;

public class RevisionTask extends AbstractTask {
    public RevisionTask() {
    }

    public RevisionTask(int taskId, int subjectId, String title, LocalDate deadline, String priority, String status) {
        super(taskId, subjectId, title, deadline, priority, status);
    }

    @Override
    public String getTaskType() {
        return "Revision";
    }
}
