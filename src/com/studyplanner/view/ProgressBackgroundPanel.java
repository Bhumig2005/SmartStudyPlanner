package com.studyplanner.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ProgressBackgroundPanel extends BackgroundPanel {
    private static final long serialVersionUID = 1L;
    private Image motivationImage;

    public ProgressBackgroundPanel(LayoutManager layout) {
        super(layout);
        loadMotivationImage();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (motivationImage == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int imageWidth = motivationImage.getWidth(this);
        int imageHeight = motivationImage.getHeight(this);
        double scale = Math.max((double) panelWidth / imageWidth, (double) panelHeight / imageHeight);
        int drawWidth = (int) Math.round(imageWidth * scale);
        int drawHeight = (int) Math.round(imageHeight * scale);
        int x = (panelWidth - drawWidth) / 2;
        int y = (panelHeight - drawHeight) / 2;

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.20f));
        g2.drawImage(motivationImage, x, y, drawWidth, drawHeight, this);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.40f));
        g2.setColor(new Color(2, 10, 24));
        g2.fillRect(0, 0, panelWidth, panelHeight);
        g2.dispose();
    }

    private void loadMotivationImage() {
        try {
            File imageFile = new File("assets/progress_motivation.png");
            if (imageFile.exists()) {
                motivationImage = ImageIO.read(imageFile);
            }
        } catch (IOException e) {
            motivationImage = null;
        }
    }
}
