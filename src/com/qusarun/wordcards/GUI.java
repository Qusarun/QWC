package com.qusarun.wordcards;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JPanel {
    private final Color backGround = new Color(30, 35, 40), button = backGround.darker(), text = new Color(220, 220, 220);
    private final List<String> words = new ArrayList<>();
    private String word;
    private long pickedTime;
    private int correctOption, pickedOption;

    public GUI() {
        super();

        regenerate();

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                updateUI();

                final int partX = QWC.INSTANCE.getGui().getWidth() / 19, partY = QWC.INSTANCE.getGui().getHeight() / 21;
                final int x = Math.round(e.getX() / (float) partX), y = Math.round(e.getY() / (float) partY);
                if (e.getY() > 10 && e.getY() < 30 && e.getX() > 10 && e.getX() < 10 + QWC.INSTANCE.getGui().getGraphics().getFontMetrics().stringWidth("Language: " + QWC.INSTANCE.getLanguage()) * 2) {
                    QWC.INSTANCE.switchLanguage();
                    regenerate();
                }

                boolean a = x >= 1 && x <= 8, b = y >= 3 && y <= 8, c = x >= 10 && x <= 18, d = y >= 12 && y <= 20;
                if (a && b)
                    check(0);
                else if (c && b)
                    check(1);
                else if (a && d)
                    check(2);
                else if (c && d)
                    check(3);
            }

            @Override public void mousePressed(final MouseEvent e) { }
            @Override public void mouseReleased(final MouseEvent e) { }
            @Override public void mouseEntered(final MouseEvent e) { }
            @Override public void mouseExited(final MouseEvent e) { }
        });

        new Thread(() -> {
            try {
                while (true) {
                    if (pickedTime != 0 && System.currentTimeMillis() - pickedTime > 1000L) {
                        regenerate();
                        pickedTime = 0;
                    }

                    updateUI();
                    Thread.sleep(250L);
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.setBackground(backGround);
        g.setColor(button);
        drawRect(g, 1 , 3 , 8, 8);
        drawRect(g, 10, 3 , 8, 8);
        drawRect(g, 1 , 12, 8, 8);
        drawRect(g, 10, 12, 8, 8);
        g.setColor(text);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Language: " + QWC.INSTANCE.getLanguage(), 10, 30);

        if (pickedTime != 0) {
            g.setColor(pickedOption == correctOption? new Color(0, 255, 0) : new Color(255, 0, 0));
            drawCentered(g, word.split(":")[1] + " -> " + word.split(":")[0], 19, 3);
            return;
        }

        g.setColor(text);

        drawCentered(g, word.split(":")[1], 19, 3);
        drawCentered(g, words.get(0).split(":")[0], 10, 14);
        drawCentered(g, words.get(1).split(":")[0], 28, 14);
        drawCentered(g, words.get(2).split(":")[0], 10, 32);
        drawCentered(g, words.get(3).split(":")[0], 28, 32);
    }

    private void drawHalfCentered(final Graphics g, final String s, final int x, final int y) {
        final int partX = this.getWidth() / 19, partY = this.getHeight() / 21;
        g.drawString(s, x * partX / 2 - g.getFontMetrics().stringWidth(s) / 2, y * partY + g.getFontMetrics().getHeight());
    }

    private void drawCentered(final Graphics g, final String s, final int x, final int y) {
        final int partX = this.getWidth() / 19, partY = this.getHeight() / 21;
        g.drawString(s, x * partX / 2 - g.getFontMetrics().stringWidth(s) / 2, y * partY / 2 + g.getFontMetrics().getHeight() / 2);
    }

    private void drawRect(final Graphics g, final int x, final int y, final int width, final int height) {
        final int partX = this.getWidth() / 19, partY = this.getHeight() / 21;
        g.fillRect(x * partX, y * partY, width * partX, height * partY);
    }

    private List<String> getWords() {
        return QWC.INSTANCE.getLanguages().get(QWC.INSTANCE.getLanguage());
    }

    private String getRandomWord() {
        String s = getWords().get((int) (Math.random() * getWords().size()));
        while (words.contains(s) && getWords().size() > 5)
            s = getWords().get((int) (Math.random() * getWords().size()));
        words.add(s);
        return s;
    }

    private void regenerate() {
        final List<String> words = new ArrayList<>();
        word = getRandomWord();
        for (int i = 0; i < 3; i++) words.add(getRandomWord());
        words.add((int) (Math.random() * 4), word);
        this.words.clear();
        this.words.addAll(words);
        words.clear();
    }

    private void check(final int i) {
        correctOption = words.indexOf(word);
        pickedOption  = i;
        pickedTime    = System.currentTimeMillis();
    }

    private BufferedImage read(final String s) {
        try {
            return ImageIO.read(new File("languages/" + QWC.INSTANCE.getLanguage() + "/" + getWords().indexOf(s)));
        } catch (final Exception ignored) {
            return null;
        }
    }
}