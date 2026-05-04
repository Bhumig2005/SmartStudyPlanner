package com.studyplanner.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.studyplanner.util.ReminderThread;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private final ReminderThread reminderThread = new ReminderThread();

    public MainFrame() {
        setTitle("Smart Study Planner");
        setSize(1050, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Dashboard", new DashboardPanel());
        tabs.addTab("Subjects", new SubjectPanel());
        tabs.addTab("Tasks", new TaskPanel());
        tabs.addTab("Schedule", new SchedulePanel());
        tabs.addTab("Progress", new ProgressPanel());
        tabs.addTab("Reports", new ReportPanel());

        add(tabs, BorderLayout.CENTER);

        startReminderThread();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }

    private void startReminderThread() {
        Thread thread = new Thread(reminderThread, "Study Reminder Thread");
        thread.setDaemon(true);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    @Override
    public void dispose() {
        reminderThread.stopReminder();
        super.dispose();
    }
}
