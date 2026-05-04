package com.studyplanner.exception;

public class SubjectNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public SubjectNotFoundException(String message) {
        super(message);
    }
}
