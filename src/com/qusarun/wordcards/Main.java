package com.qusarun.wordcards;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        QWC.INSTANCE.load();
        final JFrame frame = new JFrame("QWC");
        frame.setSize(1000, 750);
        frame.add(QWC.INSTANCE.getGui());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
