package com.studyplanner.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.studyplanner.exception.InvalidScheduleException;
import com.studyplanner.exception.SubjectNotFoundException;
import com.studyplanner.model.Subject;
import com.studyplanner.service.StudyService;

public class SubjectPanel extends BackgroundPanel {
    private static final long serialVersionUID = 1L;
    private final StudyService studyService = new StudyService();
    private final JTextField nameField = new JTextField(15);
    private final JTextField descriptionField = new JTextField(20);
    private final DefaultTableModel tableModel = new DefaultTableModel(new Object[] { "ID", "Name", "Description" }, 0);
    private final JTable table = new JTable(tableModel);

    public SubjectPanel() {
        super(new BorderLayout(18, 18));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JPanel formPanel = UiStyle.card(new JPanel(new GridLayout(2, 1, 8, 8)));
        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        JLabel subjectLabel = new JLabel("Subject");
        UiStyle.label(subjectLabel);
        inputPanel.add(subjectLabel);
        UiStyle.textField(nameField);
        inputPanel.add(nameField);
        JLabel descriptionLabel = new JLabel("Description");
        UiStyle.label(descriptionLabel);
        inputPanel.add(descriptionLabel);
        UiStyle.textField(descriptionField);
        inputPanel.add(descriptionField);

        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete Selected");
        JButton refreshButton = new JButton("Refresh");
        UiStyle.button(addButton);
        UiStyle.button(deleteButton);
        UiStyle.button(refreshButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(addButton);
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

        add(UiStyle.title("Subjects"), BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        addButton.addActionListener(event -> addSubject());
        deleteButton.addActionListener(event -> deleteSelectedSubject());
        refreshButton.addActionListener(event -> loadSubjects());

        loadSubjects();
    }

    private void addSubject() {
        String name = nameField.getText().trim();
        String description = descriptionField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Subject name is required.");
            return;
        }

        try {
            studyService.addSubject(name, description);
            nameField.setText("");
            descriptionField.setText("");
            loadSubjects();
            JOptionPane.showMessageDialog(this, "Subject added successfully.");
        } catch (InvalidScheduleException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not add subject: " + e.getMessage());
        }
    }

    private void deleteSelectedSubject() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a subject first.");
            return;
        }

        int subjectId = (Integer) tableModel.getValueAt(selectedRow, 0);
        int choice = JOptionPane.showConfirmDialog(this, "Delete selected subject and its tasks?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            studyService.deleteSubject(subjectId);
            loadSubjects();
            JOptionPane.showMessageDialog(this, "Subject deleted successfully.");
        } catch (SubjectNotFoundException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not delete subject: " + e.getMessage());
        }
    }

    private void loadSubjects() {
        tableModel.setRowCount(0);

        try {
            List<Subject> subjects = studyService.getAllSubjects();
            for (Subject subject : subjects) {
                tableModel.addRow(new Object[] {
                        subject.getSubjectId(),
                        subject.getName(),
                        subject.getDescription()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not load subjects: " + e.getMessage());
        }
    }
}
