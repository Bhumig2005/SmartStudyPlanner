CREATE DATABASE IF NOT EXISTS smart_study_planner;

USE smart_study_planner;

CREATE TABLE IF NOT EXISTS students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    course VARCHAR(100),
    email VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS subjects (
    subject_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS tasks (
    task_id INT PRIMARY KEY AUTO_INCREMENT,
    subject_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    task_type VARCHAR(30) NOT NULL,
    deadline DATE NOT NULL,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_tasks_subjects
        FOREIGN KEY (subject_id)
        REFERENCES subjects(subject_id)
        ON DELETE CASCADE
);

INSERT INTO subjects (name, description)
VALUES
    ('Java', 'Object oriented programming and Swing practice'),
    ('DBMS', 'MySQL, SQL queries, and database design')
ON DUPLICATE KEY UPDATE description = VALUES(description);
