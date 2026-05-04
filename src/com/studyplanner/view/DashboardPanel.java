package com.studyplanner.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.studyplanner.model.AbstractTask;
import com.studyplanner.model.Student;
import com.studyplanner.model.Subject;
import com.studyplanner.service.StudyService;

public class DashboardPanel extends BackgroundPanel {
    private static final long serialVersionUID = 1L;
    private final StudyService studyService = new StudyService();
    private final JLabel subtitleLabel = new JLabel("Plan your subjects, finish your tasks, and track study progress.");
    private final JLabel subjectCountLabel = new JLabel("0", SwingConstants.CENTER);
    private final JLabel taskCountLabel = new JLabel("0", SwingConstants.CENTER);
    private final JLabel completedCountLabel = new JLabel("0", SwingConstants.CENTER);
    private final JLabel pendingCountLabel = new JLabel("0", SwingConstants.CENTER);
    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JTextArea upcomingArea = new JTextArea();
    private Image dashboardImage;

    public DashboardPanel() {
        super(new BorderLayout(18, 18));
        setBorder(new EmptyBorder(24, 28, 24, 28));
        loadDashboardImage();

        add(createHeader(), BorderLayout.NORTH);
        add(createCenterContent(), BorderLayout.CENTER);

        loadDashboard();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (dashboardImage == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int imageWidth = dashboardImage.getWidth(this);
        int imageHeight = dashboardImage.getHeight(this);
        double scale = Math.max((double) panelWidth / imageWidth, (double) panelHeight / imageHeight);
        int drawWidth = (int) Math.round(imageWidth * scale);
        int drawHeight = (int) Math.round(imageHeight * scale);
        int x = (panelWidth - drawWidth) / 2;
        int y = (panelHeight - drawHeight) / 2;

        g2.drawImage(dashboardImage, x, y, drawWidth, drawHeight, this);
        g2.setColor(new Color(2, 10, 24, 90));
        g2.fillRect(0, 0, panelWidth, panelHeight);
        g2.dispose();
    }

    private JPanel createHeader() {
        JPanel header = new TransparentPanel(new BorderLayout(12, 12));

        JLabel titleLabel = new JLabel("Smart Study Planner");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        titleLabel.setForeground(new Color(255, 218, 74));

        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitleLabel.setForeground(new Color(224, 242, 255));

        JPanel titlePanel = new TransparentPanel(new GridLayout(2, 1));
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshButton.setBackground(new Color(255, 218, 74));
        refreshButton.setForeground(new Color(8, 16, 38));
        refreshButton.setPreferredSize(new Dimension(92, 34));
        refreshButton.addActionListener(event -> loadDashboard());

        header.add(titlePanel, BorderLayout.CENTER);
        header.add(refreshButton, BorderLayout.EAST);

        return header;
    }

    private JPanel createCenterContent() {
        JPanel content = new TransparentPanel(new BorderLayout(18, 18));
        content.add(createStatsPanel(), BorderLayout.NORTH);
        content.add(createDetailPanel(), BorderLayout.CENTER);
        return content;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new TransparentPanel(new GridLayout(1, 4, 14, 14));
        statsPanel.add(createStatCard("Subjects", subjectCountLabel, new Color(119, 224, 255)));
        statsPanel.add(createStatCard("Total Tasks", taskCountLabel, new Color(255, 218, 74)));
        statsPanel.add(createStatCard("Completed", completedCountLabel, new Color(92, 255, 198)));
        statsPanel.add(createStatCard("Pending", pendingCountLabel, new Color(255, 154, 91)));
        return statsPanel;
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new RoundedPanel(new BorderLayout(8, 8), new Color(8, 20, 45, 218));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(229, 244, 255));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(accentColor);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.setPreferredSize(new Dimension(150, 115));

        return card;
    }

    private JPanel createDetailPanel() {
        JPanel detailPanel = new TransparentPanel(new GridLayout(1, 2, 18, 18));

        JPanel progressCard = new RoundedPanel(new BorderLayout(12, 12), new Color(8, 20, 45, 188));
        progressCard.setBorder(new EmptyBorder(22, 22, 22, 22));

        JLabel progressTitle = new JLabel("Overall Progress");
        progressTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        progressTitle.setForeground(new Color(255, 218, 74));

        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        progressBar.setPreferredSize(new Dimension(100, 36));
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        progressBar.setForeground(new Color(92, 255, 198));
        progressBar.setBackground(new Color(17, 40, 70));

        JPanel progressCenter = new TransparentPanel(new BorderLayout());
        progressCenter.setBorder(new EmptyBorder(55, 0, 55, 0));
        progressCenter.add(progressBar, BorderLayout.NORTH);

        progressCard.add(progressTitle, BorderLayout.NORTH);
        progressCard.add(progressCenter, BorderLayout.CENTER);

        JPanel upcomingCard = new RoundedPanel(new BorderLayout(12, 12), new Color(8, 20, 45, 188));
        upcomingCard.setBorder(new EmptyBorder(22, 22, 22, 22));

        JLabel upcomingTitle = new JLabel("Upcoming Pending Tasks");
        upcomingTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        upcomingTitle.setForeground(new Color(255, 218, 74));

        upcomingArea.setEditable(false);
        upcomingArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        upcomingArea.setForeground(new Color(229, 244, 255));
        upcomingArea.setOpaque(false);
        upcomingArea.setLineWrap(true);
        upcomingArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(upcomingArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        upcomingCard.add(upcomingTitle, BorderLayout.NORTH);
        upcomingCard.add(scrollPane, BorderLayout.CENTER);

        detailPanel.add(progressCard);
        detailPanel.add(upcomingCard);

        return detailPanel;
    }

    private void loadDashboard() {
        try {
            List<Student> students = studyService.getAllStudents();
            List<Subject> subjects = studyService.getAllSubjects();
            List<AbstractTask> tasks = studyService.getAllTasks();
            List<AbstractTask> pendingTasks = studyService.getPendingTasks();

            int completedTasks = 0;
            for (AbstractTask task : tasks) {
                if ("Completed".equalsIgnoreCase(task.getStatus())) {
                    completedTasks++;
                }
            }

            int progress = (int) Math.round(studyService.calculateProgress(tasks));

            subjectCountLabel.setText(String.valueOf(subjects.size()));
            taskCountLabel.setText(String.valueOf(tasks.size()));
            completedCountLabel.setText(String.valueOf(completedTasks));
            pendingCountLabel.setText(String.valueOf(pendingTasks.size()));
            progressBar.setValue(progress);
            progressBar.setString(progress + "% completed");

            if (!students.isEmpty()) {
                String firstStudent = students.get(0).getName();
                String studentInfo = "Students: " + students.size() + " | First profile: " + firstStudent;
                subtitleLabel.setText(studentInfo);
            } else {
                subtitleLabel.setText("Plan your subjects, finish your tasks, and track study progress.");
            }

            updateUpcomingTasks(pendingTasks);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not load dashboard: " + e.getMessage());
        }
    }

    private void updateUpcomingTasks(List<AbstractTask> pendingTasks) {
        if (pendingTasks.isEmpty()) {
            upcomingArea.setText("No pending tasks. Your plan is clear.");
            return;
        }

        StringBuilder builder = new StringBuilder();
        int limit = Math.min(5, pendingTasks.size());

        for (int i = 0; i < limit; i++) {
            AbstractTask task = pendingTasks.get(i);
            builder.append(i + 1).append(". ")
                    .append(task.getTitle())
                    .append(" | ")
                    .append(task.getTaskType())
                    .append(" | Due: ")
                    .append(task.getDeadline())
                    .append(" | Priority: ")
                    .append(task.getPriority())
                    .append(System.lineSeparator());
        }

        upcomingArea.setText(builder.toString());
    }

    private void loadDashboardImage() {
        try {
            File imageFile = new File("assets/dashboard_study_background.png");
            if (imageFile.exists()) {
                dashboardImage = ImageIO.read(imageFile);
            }
        } catch (IOException e) {
            dashboardImage = null;
        }
    }

    private static class TransparentPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        TransparentPanel(java.awt.LayoutManager layout) {
            super(layout);
            setOpaque(false);
        }
    }

    private static class RoundedPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private final Color backgroundColor;

        RoundedPanel(java.awt.LayoutManager layout, Color backgroundColor) {
            super(layout);
            this.backgroundColor = backgroundColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.dispose();
            super.paintComponent(graphics);
        }
    }
}
