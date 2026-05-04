package com.studyplanner.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.studyplanner.model.Subject;

public class SubjectDAO {
    public int addSubject(Subject subject) throws SQLException {
        String sql = "INSERT INTO subjects (name, description) VALUES (?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, subject.getName());
            statement.setString(2, subject.getDescription());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }

        return 0;
    }

    public List<Subject> getAllSubjects() throws SQLException {
        List<Subject> subjects = new ArrayList<Subject>();
        String sql = "SELECT subject_id, name, description FROM subjects ORDER BY name";

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                subjects.add(mapSubject(resultSet));
            }
        }

        return subjects;
    }

    public Subject findSubjectById(int subjectId) throws SQLException {
        String sql = "SELECT subject_id, name, description FROM subjects WHERE subject_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, subjectId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapSubject(resultSet);
                }
            }
        }

        return null;
    }

    public boolean updateSubject(Subject subject) throws SQLException {
        String sql = "UPDATE subjects SET name = ?, description = ? WHERE subject_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, subject.getName());
            statement.setString(2, subject.getDescription());
            statement.setInt(3, subject.getSubjectId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteSubject(int subjectId) throws SQLException {
        String sql = "DELETE FROM subjects WHERE subject_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, subjectId);
            return statement.executeUpdate() > 0;
        }
    }

    private Subject mapSubject(ResultSet resultSet) throws SQLException {
        return new Subject(
                resultSet.getInt("subject_id"),
                resultSet.getString("name"),
                resultSet.getString("description"));
    }
}
