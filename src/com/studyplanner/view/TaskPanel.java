package com.studyplanner.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.studyplanner.exception.InvalidScheduleException;
import com.studyplanner.exception.SubjectNotFoundException;
import com.studyplanner.model.AbstractTask;
import com.studyplanner.model.Subject;
import com.studyplanner.service.StudyService;

public class TaskPanel extends BackgroundPanel {
    private static final long serialVersionUID = 1L;
    private final StudyService studyService = new StudyService();
    private final JComboBox<Subject> subjectBox = new JComboBox<Subject>();
    private final JTextField titleField = new JTextField(14);
    private final JComboBox<String> typeBox = new JComboBox<String>(new String[] { "Study", "Revision" });
    private final JComboBox<String> priorityBox = new JComboBox<String>(new String[] { "Low", "Medium", "High" });
    private final JTextField deadlineField = new JTextField("YYYY-MM-DD", 10);
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[] { "ID", "Subject", "Title", "Type", "Deadline", "Priority", "Status" }, 0);
    private final JTable table = new JTable(tableModel);

    public TaskPanel() {
        super(new BorderLayout(18, 18));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JPanel formPanel = UiStyle.card(new JPanel(new GridLayout(2, 1, 8, 8)));
        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        JLabel subjectLabel = new JLabel("Subject");
        UiStyle.label(subjectLabel);
        inputPanel.add(subjectLabel);
        inputPanel.add(subjectBox);
        JLabel titleLabel = new JLabel("Title");
        UiStyle.label(titleLabel);
        inputPanel.add(titleLabel);
        UiStyle.textField(titleField);
        inputPanel.add(titleField);
        JLabel typeLabel = new JLabel("Type");
        UiStyle.label(typeLabel);
        inputPanel.add(typeLabel);
        inputPanel.add(typeBox);
        JLabel priorityLabel = new JLabel("Priority");
        UiStyle.label(priorityLabel);
        inputPanel.add(priorityLabel);
        inputPanel.add(priorityBox);
        JLabel deadlineLabel = new JLabel("Deadline");
        UiStyle.label(deadlineLabel);
        inputPanel.add(deadlineLabel);
        UiStyle.textField(deadlineField);
        inputPanel.add(deadlineField);

        JButton addButton = new JButton("Add");
        JButton completeButton = new JButton("Mark Completed");
        JButton deleteButton = new JButton("Delete Selected");
        JButton refreshButton = new JButton("Refresh");
        UiStyle.button(addButton);
        UiStyle.button(completeButton);
        UiStyle.button(deleteButton);
        UiStyle.button(refreshButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(addButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        formPanel.add(inputPanel);
        formPanel.add(buttonPanel);

        UiStyle.table(table);
        JScrollPane scrollPane = new JScrollPane(table);
        UiStyle.scrollPane(scrollPane);

        JPanel contentPanel = new JPanel(new BorderLayout(18, 18));
        contentPanel.setOpaque(false);
        contentPanel.add(formPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        add(UiStyle.title("Tasks"), BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        addButton.addActionListener(event -> addTask());
        completeButton.addActionListener(event -> markSelectedTaskCompleted());
        deleteButton.addActionListener(event -> deleteSelectedTask());
        refreshButton.addActionListener(event -> refreshData());

        refreshData();
    }

    private void refreshData() {
        loadSubjects();
        loadTasks();
    }

    private void loadSubjects() {
        subjectBox.removeAllItems();

        try {
            List<Subject> subjects = studyService.getAllSubjects();
            for (Subject subject : subjects) {
                subjectBox.addItem(subject);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not load subjects: " + e.getMessage());
        }
    }

    private void loadTasks() {
        tableModel.setRowCount(0);

        try {
            Map<Integer, String> subjectNames = getSubjectNames();
            List<AbstractTask> tasks = studyService.getAllTasks();
            for (AbstractTask task : tasks) {
                tableModel.addRow(new Object[] {
                        task.getTaskId(),
                        subjectNames.getOrDefault(task.getSubjectId(), "Subject " + task.getSubjectId()),
                        task.getTitle(),
                        task.getTaskType(),
                        task.getDeadline(),
                        task.getPriority(),
                        task.getStatus()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not load tasks: " + e.getMessage());
        }
    }

    private void addTask() {
        Subject selectedSubject = (Subject) subjectBox.getSelectedItem();
        String title = titleField.getText().trim();

        try {
            String priority = (String) priorityBox.getSelectedItem();
            String type = (String) typeBox.getSelectedItem();

            studyService.addTask(selectedSubject, title, type, deadlineField.getText(), priority);
            titleField.setText("");
            deadlineField.setText("YYYY-MM-DD");
            loadTasks();
            JOptionPane.showMessageDialog(this, "Task added successfully.");
        } catch (InvalidScheduleException | SubjectNotFoundException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not add task: " + e.getMessage());
        }
    }

    private void markSelectedTaskCompleted() {
        int taskId = getSelectedTaskId();
        if (taskId == 0) {
            return;
        }

        try {
            studyService.markTaskCompleted(taskId);
            loadTasks();
            JOptionPane.showMessageDialog(this, "Task marked completed.");
        } catch (InvalidScheduleException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not update task: " + e.getMessage());
        }
    }

    private void deleteSelectedTask() {
        int taskId = getSelectedTaskId();
        if (taskId == 0) {
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, "Delete selected task?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            studyService.deleteTask(taskId);
            loadTasks();
            JOptionPane.showMessageDialog(this, "Task deleted successfully.");
        } catch (InvalidScheduleException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not delete task: " + e.getMessage());
        }
    }

    private int getSelectedTaskId() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a task first.");
            return 0;
        }

        return (Integer) tableModel.getValueAt(selectedRow, 0);
    }

    private Map<Integer, String> getSubjectNames() throws SQLException {
        Map<Integer, String> subjectNames = new HashMap<Integer, String>();
        List<Subject> subjects = studyService.getAllSubjects();

        for (Subject subject : subjects) {
            subjectNames.put(subject.getSubjectId(), subject.getName());
        }

        return subjectNames;
    }
}
