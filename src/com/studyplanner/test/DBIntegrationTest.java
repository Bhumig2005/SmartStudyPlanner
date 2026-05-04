package com.studyplanner.test;

import com.studyplanner.db.DatabaseManager;

public class DBIntegrationTest {
    public static void main(String[] args) {
        if (DatabaseManager.testConnection()) {
            System.out.println("Database connected successfully.");
        } else {
            System.out.println("Database connection failed. Check MySQL, password, and connector jar.");
        }
    }
}
