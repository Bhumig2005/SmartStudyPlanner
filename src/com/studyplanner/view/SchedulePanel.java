package com.studyplanner.view;

import java.awt.BorderLayout;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.studyplanner.db.SubjectDAO;
import com.studyplanner.model.AbstractTask;
import com.studyplanner.model.Subject;
import com.studyplanner.service.StudyService;

public class SchedulePanel extends BackgroundPanel {
    private static final long serialVersionUID = 1L;
    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final StudyService studyService = new StudyService();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[] { "Task", "Subject", "Deadline", "Priority", "Status" }, 0);

    public SchedulePanel() {
        super(new BorderLayout(18, 18));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JPanel topPanel = UiStyle.card(new JPanel(new BorderLayout()));
        topPanel.add(UiStyle.title("Upcoming Schedule"), BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        UiStyle.button(refreshButton);
        topPanel.add(refreshButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        JTable table = new JTable(tableModel);
        UiStyle.table(table);
        JScrollPane scrollPane = new JScrollPane(table);
        UiStyle.scrollPane(scrollPane);
        add(scrollPane, BorderLayout.CENTER);

        refreshButton.addActionListener(event -> loadSchedule());
        loadSchedule();
    }

    private void loadSchedule() {
        tableModel.setRowCount(0);

        try {
            Map<Integer, String> subjectNames = getSubjectNames();
            List<AbstractTask> tasks = studyService.getPendingTasks();

            for (AbstractTask task : tasks) {
                tableModel.addRow(new Object[] {
                        task.getTitle(),
                        subjectNames.getOrDefault(task.getSubjectId(), "Subject " + task.getSubjectId()),
                        task.getDeadline(),
                        task.getPriority(),
                        task.getStatus()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not load schedule: " + e.getMessage());
        }
    }

    private Map<Integer, String> getSubjectNames() throws SQLException {
        Map<Integer, String> subjectNames = new HashMap<Integer, String>();
        List<Subject> subjects = subjectDAO.getAllSubjects();

        for (Subject subject : subjects) {
            subjectNames.put(subject.getSubjectId(), subject.getName());
        }

        return subjectNames;
    }
}
