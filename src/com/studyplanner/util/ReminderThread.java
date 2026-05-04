package com.studyplanner.util;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.studyplanner.model.AbstractTask;
import com.studyplanner.service.StudyService;

public class ReminderThread implements Runnable {
    private final StudyService studyService = new StudyService();
    private final Set<Integer> remindedTaskIds = new HashSet<Integer>();
    private volatile boolean running = true;

    @Override
    public void run() {
        while (running) {
            checkDueTasks();

            try {
                Thread.sleep(TimerHelper.minutesToMillis(1));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }
    }

    public void stopReminder() {
        running = false;
    }

    private void checkDueTasks() {
        try {
            List<AbstractTask> tasks = studyService.getPendingTasks();

            for (AbstractTask task : tasks) {
                if (task.getDeadline().equals(LocalDate.now()) && !remindedTaskIds.contains(task.getTaskId())) {
                    remindedTaskIds.add(task.getTaskId());
                    showReminder(task);
                }
            }
        } catch (SQLException e) {
            System.out.println("Reminder check failed: " + e.getMessage());
        }
    }

    private void showReminder(AbstractTask task) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null,
                "Reminder: " + task.getTitle() + " is due today.\nPriority: " + task.getPriority(),
                "Study Reminder",
                JOptionPane.INFORMATION_MESSAGE));
    }
}
