package com.studyplanner.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.studyplanner.model.AbstractTask;
import com.studyplanner.model.RevisionTask;
import com.studyplanner.model.StudyTask;

public class TaskDAO {
    public int addTask(AbstractTask task) throws SQLException {
        String sql = "INSERT INTO tasks (subject_id, title, task_type, deadline, priority, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fillTaskStatement(statement, task);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }

        return 0;
    }

    public List<AbstractTask> getAllTasks() throws SQLException {
        List<AbstractTask> tasks = new ArrayList<AbstractTask>();
        String sql = "SELECT task_id, subject_id, title, task_type, deadline, priority, status FROM tasks ORDER BY deadline";

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                tasks.add(mapTask(resultSet));
            }
        }

        return tasks;
    }

    public List<AbstractTask> getTasksBySubject(int subjectId) throws SQLException {
        List<AbstractTask> tasks = new ArrayList<AbstractTask>();
        String sql = "SELECT task_id, subject_id, title, task_type, deadline, priority, status FROM tasks WHERE subject_id = ? ORDER BY deadline";

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, subjectId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tasks.add(mapTask(resultSet));
                }
            }
        }

        return tasks;
    }

    public List<AbstractTask> getPendingTasks() throws SQLException {
        List<AbstractTask> tasks = new ArrayList<AbstractTask>();
        String sql = "SELECT task_id, subject_id, title, task_type, deadline, priority, status FROM tasks WHERE status = ? ORDER BY deadline";

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "Pending");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tasks.add(mapTask(resultSet));
                }
            }
        }

        return tasks;
    }

    public boolean updateTask(AbstractTask task) throws SQLException {
        String sql = "UPDATE tasks SET subject_id = ?, title = ?, task_type = ?, deadline = ?, priority = ?, status = ? WHERE task_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            fillTaskStatement(statement, task);
            statement.setInt(7, task.getTaskId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean markTaskCompleted(int taskId) throws SQLException {
        String sql = "UPDATE tasks SET status = ? WHERE task_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "Completed");
            statement.setInt(2, taskId);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteTask(int taskId) throws SQLException {
        String sql = "DELETE FROM tasks WHERE task_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, taskId);
            return statement.executeUpdate() > 0;
        }
    }

    private void fillTaskStatement(PreparedStatement statement, AbstractTask task) throws SQLException {
        statement.setInt(1, task.getSubjectId());
        statement.setString(2, task.getTitle());
        statement.setString(3, task.getTaskType());
        statement.setDate(4, Date.valueOf(task.getDeadline()));
        statement.setString(5, task.getPriority());
        statement.setString(6, task.getStatus());
    }

    private AbstractTask mapTask(ResultSet resultSet) throws SQLException {
        int taskId = resultSet.getInt("task_id");
        int subjectId = resultSet.getInt("subject_id");
        String title = resultSet.getString("title");
        String taskType = resultSet.getString("task_type");
        LocalDate deadline = resultSet.getDate("deadline").toLocalDate();
        String priority = resultSet.getString("priority");
        String status = resultSet.getString("status");

        if ("Revision".equalsIgnoreCase(taskType)) {
            return new RevisionTask(taskId, subjectId, title, deadline, priority, status);
        }

        return new StudyTask(taskId, subjectId, title, deadline, priority, status);
    }
}
