package com.studyplanner.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.studyplanner.model.AbstractTask;
import com.studyplanner.service.StudyService;

public class ProgressPanel extends BackgroundPanel {
    private static final long serialVersionUID = 1L;
    private final StudyService studyService = new StudyService();
    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JTextArea summaryArea = new JTextArea();

    public ProgressPanel() {
        super(new BorderLayout(18, 18));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 28, 24, 28));

        progressBar.setStringPainted(true);
        progressBar.setForeground(new java.awt.Color(92, 255, 198));
        progressBar.setBackground(new java.awt.Color(17, 40, 70));

        summaryArea.setEditable(false);
        UiStyle.textArea(summaryArea);
        summaryArea.setOpaque(false);

        JButton refreshButton = new JButton("Refresh");
        UiStyle.button(refreshButton);

        JPanel topPanel = UiStyle.card(new JPanel(new BorderLayout(12, 12)));
        topPanel.add(UiStyle.title("Progress"), BorderLayout.NORTH);
        topPanel.add(progressBar, BorderLayout.CENTER);
        topPanel.add(refreshButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new BorderLayout(18, 18));
        centerPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(summaryArea);
        UiStyle.scrollPane(scrollPane);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));

        JPanel summaryCard = UiStyle.card(new JPanel(new BorderLayout()));
        summaryCard.setBackground(new java.awt.Color(8, 20, 45, 170));
        summaryCard.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(summaryCard, BorderLayout.WEST);

        add(centerPanel, BorderLayout.CENTER);

        refreshButton.addActionListener(event -> loadProgress());
        loadProgress();
    }

    private void loadProgress() {
        try {
            List<AbstractTask> tasks = studyService.getAllTasks();
            int totalTasks = tasks.size();
            int completedTasks = 0;

            for (AbstractTask task : tasks) {
                if ("Completed".equalsIgnoreCase(task.getStatus())) {
                    completedTasks++;
                }
            }

            int pendingTasks = totalTasks - completedTasks;
            int progressValue = (int) Math.round(studyService.calculateProgress(tasks));

            progressBar.setValue(progressValue);
            progressBar.setString(progressValue + "% Completed");

            summaryArea.setFont(new Font("Segoe UI", Font.BOLD, 22));
            summaryArea.setText("Total Tasks: " + totalTasks
                    + "\nCompleted: " + completedTasks
                    + "\nPending: " + pendingTasks
                    + "\nProgress: " + progressValue + "%");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not load progress: " + e.getMessage());
        }
    }
}
