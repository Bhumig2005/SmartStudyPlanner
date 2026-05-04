package com.studyplanner.test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.studyplanner.model.AbstractTask;
import com.studyplanner.model.RevisionTask;
import com.studyplanner.model.StudyTask;
import com.studyplanner.service.StudyService;

public class StudyServiceTest {
    public static void main(String[] args) {
        StudyService studyService = new StudyService();
        List<AbstractTask> tasks = new ArrayList<AbstractTask>();

        tasks.add(new StudyTask(1, 1, "Finish class notes", LocalDate.of(2026, 5, 8), "High", "Completed"));
        tasks.add(new RevisionTask(2, 1, "Revise JDBC", LocalDate.of(2026, 5, 9), "Medium", "Pending"));
        tasks.add(new StudyTask(3, 2, "Solve DBMS questions", LocalDate.of(2026, 5, 10), "High", "Completed"));
        tasks.add(new RevisionTask(4, 2, "Review joins", LocalDate.of(2026, 5, 11), "Low", "Pending"));

        double progress = studyService.calculateProgress(tasks);

        System.out.println("StudyService unit test");
        System.out.println("Expected progress: 50.0");
        System.out.println("Actual progress: " + progress);

        if (progress == 50.0) {
            System.out.println("Result: PASS");
        } else {
            System.out.println("Result: FAIL");
        }
    }
}
