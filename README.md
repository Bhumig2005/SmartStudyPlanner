## Project Title

Smart Study Planner

## Problem Statement

This project helps students manage subjects, create study and revision tasks, track progress, view schedules, receive reminders, and export reports using a Java Swing desktop application connected to MySQL.

## Technologies Used

- Java
- Swing GUI
- MySQL
- JDBC
- Eclipse IDE

## OOP Concepts Used

- Class and Object:
  `Subject`, `Student`, `StudyTask`, `RevisionTask`
- Encapsulation:
  Private fields with getters and setters in model classes
- Inheritance:
  `StudyTask` and `RevisionTask` extend `AbstractTask`
- Abstraction:
  `AbstractTask` is an abstract class
- Interface:
  `Scheduleable` and `Trackable`
- Polymorphism:
  `AbstractTask` reference stores both `StudyTask` and `RevisionTask`

## Important Packages

- `com.studyplanner.model`
- `com.studyplanner.view`
- `com.studyplanner.service`
- `com.studyplanner.db`
- `com.studyplanner.exception`
- `com.studyplanner.util`
- `com.studyplanner.test`

## Database Tables

- `students`
- `subjects`
- `tasks`

## Important Features

- Add and delete subjects
- Add, complete, and delete tasks
- Schedule view for pending tasks
- Progress tracking
- Background reminder thread
- Report export using file handling
