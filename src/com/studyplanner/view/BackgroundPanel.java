package com.studyplanner.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public BackgroundPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(8, 16, 38));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(new Color(10, 30, 58, 180));
        g2.fillOval(-50, getHeight() - 220, 320, 180);
        g2.fillOval(getWidth() - 260, 40, 220, 110);
        g2.fillOval(getWidth() - 340, getHeight() - 180, 280, 150);

        g2.setColor(new Color(16, 66, 92, 120));
        g2.fillOval(180, 70, 260, 120);
        g2.fillOval(getWidth() / 2 - 120, getHeight() - 160, 240, 120);

        g2.dispose();
        super.paintComponent(graphics);
    }
}
