package com.studyplanner.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.studyplanner.db.StudentDAO;
import com.studyplanner.db.SubjectDAO;
import com.studyplanner.db.TaskDAO;
import com.studyplanner.exception.InvalidScheduleException;
import com.studyplanner.exception.SubjectNotFoundException;
import com.studyplanner.model.AbstractTask;
import com.studyplanner.model.RevisionTask;
import com.studyplanner.model.Student;
import com.studyplanner.model.StudyTask;
import com.studyplanner.model.Subject;

public class StudyService {
    private final StudentDAO studentDAO = new StudentDAO();
    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final TaskDAO taskDAO = new TaskDAO();

    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.getAllStudents();
    }

    public int addSubject(String name, String description) throws SQLException, InvalidScheduleException {
        String cleanName = clean(name);

        if (cleanName.isEmpty()) {
            throw new InvalidScheduleException("Subject name is required.");
        }

        return subjectDAO.addSubject(new Subject(0, cleanName, clean(description)));
    }

    public List<Subject> getAllSubjects() throws SQLException {
        return subjectDAO.getAllSubjects();
    }

    public boolean deleteSubject(int subjectId) throws SQLException, SubjectNotFoundException {
        if (subjectId <= 0) {
            throw new SubjectNotFoundException("Select a valid subject.");
        }

        return subjectDAO.deleteSubject(subjectId);
    }

    public int addTask(Subject subject, String title, String taskType, String deadlineText, String priority)
            throws SQLException, InvalidScheduleException, SubjectNotFoundException {
        if (subject == null || subject.getSubjectId() <= 0) {
            throw new SubjectNotFoundException("Add a subject before adding tasks.");
        }

        String cleanTitle = clean(title);
        if (cleanTitle.isEmpty()) {
            throw new InvalidScheduleException("Task title is required.");
        }

        LocalDate deadline = parseDeadline(deadlineText);
        String cleanPriority = clean(priority);
        String cleanTaskType = clean(taskType);

        AbstractTask task;
        if ("Revision".equalsIgnoreCase(cleanTaskType)) {
            task = new RevisionTask(0, subject.getSubjectId(), cleanTitle, deadline, cleanPriority, "Pending");
        } else {
            task = new StudyTask(0, subject.getSubjectId(), cleanTitle, deadline, cleanPriority, "Pending");
        }

        return taskDAO.addTask(task);
    }

    public List<AbstractTask> getAllTasks() throws SQLException {
        return taskDAO.getAllTasks();
    }

    public List<AbstractTask> getPendingTasks() throws SQLException {
        return taskDAO.getPendingTasks();
    }

    public boolean markTaskCompleted(int taskId) throws SQLException, InvalidScheduleException {
        if (taskId <= 0) {
            throw new InvalidScheduleException("Select a valid task.");
        }

        return taskDAO.markTaskCompleted(taskId);
    }

    public boolean deleteTask(int taskId) throws SQLException, InvalidScheduleException {
        if (taskId <= 0) {
            throw new InvalidScheduleException("Select a valid task.");
        }

        return taskDAO.deleteTask(taskId);
    }

    public double calculateProgress(List<AbstractTask> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return 0;
        }

        long completedCount = tasks.stream()
                .filter(task -> "Completed".equalsIgnoreCase(task.getStatus()))
                .count();

        return (completedCount * 100.0) / tasks.size();
    }

    public String generateStudyReport() throws SQLException {
        List<Student> students = getAllStudents();
        List<Subject> subjects = getAllSubjects();
        List<AbstractTask> tasks = getAllTasks();
        Map<Integer, String> subjectNames = new HashMap<Integer, String>();

        for (Subject subject : subjects) {
            subjectNames.put(subject.getSubjectId(), subject.getName());
        }

        int totalTasks = tasks.size();
        int completedTasks = 0;

        for (AbstractTask task : tasks) {
            if ("Completed".equalsIgnoreCase(task.getStatus())) {
                completedTasks++;
            }
        }

        int pendingTasks = totalTasks - completedTasks;
        int progress = (int) Math.round(calculateProgress(tasks));

        StringBuilder report = new StringBuilder();
        report.append("Smart Study Planner Report").append(System.lineSeparator());
        report.append("==========================").append(System.lineSeparator());
        report.append("Total students: ").append(students.size()).append(System.lineSeparator());
        report.append("Total subjects: ").append(subjects.size()).append(System.lineSeparator());
        report.append("Total tasks: ").append(totalTasks).append(System.lineSeparator());
        report.append("Completed tasks: ").append(completedTasks).append(System.lineSeparator());
        report.append("Pending tasks: ").append(pendingTasks).append(System.lineSeparator());
        report.append("Progress: ").append(progress).append("%").append(System.lineSeparator());
        report.append(System.lineSeparator());

        report.append("Students").append(System.lineSeparator());
        report.append("--------").append(System.lineSeparator());
        for (Student student : students) {
            report.append(student.getStudentId()).append(". ")
                    .append(student.getName())
                    .append(" | ")
                    .append(student.getCourse())
                    .append(" | ")
                    .append(student.getEmail())
                    .append(System.lineSeparator());
        }

        report.append(System.lineSeparator());
        report.append("Subjects").append(System.lineSeparator());
        report.append("--------").append(System.lineSeparator());
        for (Subject subject : subjects) {
            report.append(subject.getSubjectId()).append(". ")
                    .append(subject.getName()).append(" - ")
                    .append(subject.getDescription() == null ? "" : subject.getDescription())
                    .append(System.lineSeparator());
        }

        report.append(System.lineSeparator());
        report.append("Tasks").append(System.lineSeparator());
        report.append("-----").append(System.lineSeparator());
        for (AbstractTask task : tasks) {
            report.append("ID: ").append(task.getTaskId())
                    .append(" | Subject: ").append(subjectNames.getOrDefault(task.getSubjectId(), "Subject " + task.getSubjectId()))
                    .append(" | Title: ").append(task.getTitle())
                    .append(" | Type: ").append(task.getTaskType())
                    .append(" | Deadline: ").append(task.getDeadline())
                    .append(" | Priority: ").append(task.getPriority())
                    .append(" | Status: ").append(task.getStatus())
                    .append(System.lineSeparator());
        }

        return report.toString();
    }

    private LocalDate parseDeadline(String deadlineText) throws InvalidScheduleException {
        try {
            return LocalDate.parse(clean(deadlineText));
        } catch (DateTimeParseException e) {
            throw new InvalidScheduleException("Enter deadline in YYYY-MM-DD format.");
        }
    }

    private String clean(String value) {
        if (value == null) {
            return "";
        }

        return value.trim();
    }
}
