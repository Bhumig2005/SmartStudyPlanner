package com.studyplanner.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;

public class UiStyle {
    public static final Color GOLD = new Color(255, 218, 74);
    public static final Color CYAN = new Color(119, 224, 255);
    public static final Color TEXT = new Color(229, 244, 255);
    public static final Color CARD = new Color(8, 20, 45, 224);
    public static final Color FIELD = new Color(235, 246, 255);

    private UiStyle() {
    }

    public static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 26));
        label.setForeground(GOLD);
        return label;
    }

    public static void label(JLabel label) {
        label.setForeground(TEXT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    public static void button(JButton button) {
        button.setBackground(GOLD);
        button.setForeground(new Color(8, 16, 38));
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
    }

    public static void textField(JTextField field) {
        field.setBackground(FIELD);
        field.setForeground(new Color(8, 16, 38));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    public static JPanel card(JPanel panel) {
        panel.setBackground(CARD);
        panel.setOpaque(true);
        panel.setBorder(new EmptyBorder(16, 18, 16, 18));
        return panel;
    }

    public static void table(JTable table) {
        table.setBackground(new Color(12, 30, 58));
        table.setForeground(TEXT);
        table.setGridColor(new Color(56, 101, 132));
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(255, 218, 74));
        table.setSelectionForeground(new Color(8, 16, 38));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(8, 20, 45));
        header.setForeground(GOLD);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    public static void scrollPane(JScrollPane scrollPane) {
        scrollPane.getViewport().setBackground(new Color(12, 30, 58));
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
    }

    public static void textArea(JTextArea textArea) {
        textArea.setBackground(new Color(12, 30, 58));
        textArea.setForeground(TEXT);
        textArea.setCaretColor(TEXT);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        textArea.setBorder(new EmptyBorder(12, 12, 12, 12));
    }
}
