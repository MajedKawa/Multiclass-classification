package com.mk.hw2_ai;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

class GraphPanel extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the classification regions
        if (ClassificationApp.classifiers != null) {
            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    double[] point = new double[]{(double) x / getWidth(), (double) y / getHeight()};
                    int prediction = -1;
                    int maxScore = Integer.MIN_VALUE;
                    for (int c = 0; c < ClassificationApp.classifiers.length; c++) {//num of classes
                        int score = ClassificationApp.classifiers[c].predict(point);
                        if (score > maxScore) {
                            maxScore = score;
                            prediction = c;
                        }
                    }
                    if (prediction == 0) {
                        g.setColor(new Color(255, 200, 200));//Light RED
                    } else if (prediction == 1) {
                        g.setColor(new Color(200, 200, 255));//Light Blue
                    } else if (prediction == 2) {
                        g.setColor(new Color(255, 255, 200));//Light Yellow
                    } else if (prediction == 3) {
                        g.setColor(new Color(200, 255, 200));//Light Green
                    }
                    g.fillRect(x, y, 1, 1);//draws a small filled rectangle (pixel) at the (x, y) coordinate with the selected color
                }
            }
        }

        // Draw the data points
        for (int i = 0; i < ClassificationApp.trainingData.size(); i++) {
            double[] point = ClassificationApp.trainingData.get(i);
            int x = (int) (point[0] * getWidth());
            int y = (int) (point[1] * getHeight());
            if (ClassificationApp.labels.get(i) == 0) {
                g.setColor(Color.RED);
            } else if (ClassificationApp.labels.get(i) == 1) {
                g.setColor(Color.BLUE);
            } else if (ClassificationApp.labels.get(i) == 2) {
                g.setColor(Color.YELLOW);
            } else if (ClassificationApp.labels.get(i) == 3) {
                g.setColor(Color.GREEN);
            }
            g.fillOval(x - 4, y - 4, 8, 8);//draws a small filled oval (circle) centered at the (x, y) coordinates of the data point with the selected color
        }
    }
}
