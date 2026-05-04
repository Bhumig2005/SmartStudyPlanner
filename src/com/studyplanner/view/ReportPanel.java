package com.studyplanner.view;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.studyplanner.service.StudyService;
import com.studyplanner.util.FileHandler;

public class ReportPanel extends BackgroundPanel {
    private static final long serialVersionUID = 1L;
    private final StudyService studyService = new StudyService();
    private final FileHandler fileHandler = new FileHandler();
    private final JTextArea reportArea = new JTextArea("Report preview will appear here.");

    public ReportPanel() {
        super(new BorderLayout(18, 18));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JButton previewButton = new JButton("Preview Report");
        JButton exportButton = new JButton("Export Report");
        UiStyle.button(previewButton);
        UiStyle.button(exportButton);

        JPanel buttonPanel = UiStyle.card(new JPanel());
        buttonPanel.add(UiStyle.title("Reports"));
        buttonPanel.add(previewButton);
        buttonPanel.add(exportButton);

        reportArea.setEditable(false);
        UiStyle.textArea(reportArea);

        add(buttonPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(reportArea);
        UiStyle.scrollPane(scrollPane);
        add(scrollPane, BorderLayout.CENTER);

        previewButton.addActionListener(event -> previewReport());
        exportButton.addActionListener(event -> exportReport());
    }

    private void previewReport() {
        try {
            reportArea.setText(studyService.generateStudyReport());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not create report: " + e.getMessage());
        }
    }

    private void exportReport() {
        try {
            String report = studyService.generateStudyReport();
            File reportsDirectory = new File("reports");

            if (!reportsDirectory.exists()) {
                reportsDirectory.mkdirs();
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            File reportFile = new File(reportsDirectory, "study_report_" + timestamp + ".txt");

            fileHandler.writeReport(reportFile.getPath(), report);
            reportArea.setText(fileHandler.readReport(reportFile.getPath()));
            JOptionPane.showMessageDialog(this, "Report exported to: " + reportFile.getAbsolutePath());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not create report: " + e.getMessage());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not save report: " + e.getMessage());
        }
    }
}
