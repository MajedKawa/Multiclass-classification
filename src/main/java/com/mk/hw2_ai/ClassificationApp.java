package com.mk.hw2_ai;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ClassificationApp extends JFrame implements ActionListener, ChangeListener, MouseListener {

    private JSlider learningRateSlider;
    private JLabel learningRateLabel;
    private JTextField maxIterationsField;
    private JTextField errorThresholdField;
    private JButton trainButton;
    private JButton clearButton;
    private GraphPanel graphPanel;
    private JComboBox<String> labelComboBox;
    private JTextArea infoTextArea;
    private JSpinner numClassesSpinner;

    public static Perceptron[] classifiers;
    public static ArrayList<double[]> trainingData;
    public static ArrayList<Integer> labels;

    public ClassificationApp() {
        initComponents();
        initGUI();
        initializeData();
    }

    private void initComponents() {
        learningRateSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        learningRateSlider.addChangeListener(this);
        learningRateLabel = new JLabel("Learning Rate: 0.5");
        maxIterationsField = new JTextField(10);
        errorThresholdField = new JTextField(10);
        trainButton = new JButton("Train");
        trainButton.addActionListener(this);
        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        graphPanel = new GraphPanel();
        graphPanel.addMouseListener(this);
        labelComboBox = new JComboBox<>(new String[]{"Red", "Blue", "Yellow", "Green"});
        infoTextArea = new JTextArea(10, 20);
        infoTextArea.setEditable(false);
        numClassesSpinner = new JSpinner(new SpinnerNumberModel(4, 2, 4, 1));
    }

    private void initGUI() {
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.add(learningRateLabel);
        topPanel.add(learningRateSlider);
        topPanel.add(new JLabel("Max Iterations:"));
        topPanel.add(maxIterationsField);
        topPanel.add(new JLabel("Error Threshold:"));
        topPanel.add(errorThresholdField);
        topPanel.add(new JLabel("Number of Classes:"));
        topPanel.add(numClassesSpinner);
        topPanel.add(new JLabel("Classes:"));
        topPanel.add(labelComboBox);
        add(topPanel, BorderLayout.NORTH);
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(trainButton);
        bottomPanel.add(clearButton);
        add(bottomPanel, BorderLayout.SOUTH);
        add(graphPanel, BorderLayout.CENTER);
        add(new JScrollPane(infoTextArea), BorderLayout.EAST);

        setTitle("Classification App");
        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initializeData() {
        trainingData = new ArrayList<>();
        labels = new ArrayList<>();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == trainButton) {
            double learningRate = learningRateSlider.getValue() / 100.0;
            int maxIterations = Integer.parseInt(maxIterationsField.getText());
            int errorThreshold = Integer.parseInt(errorThresholdField.getText());
            int numClasses = (Integer) numClassesSpinner.getValue();

            classifiers = new Perceptron[numClasses];
            for (int i = 0; i < numClasses; i++) { //initialization 
                classifiers[i] = new Perceptron(2, learningRate);
                for (int j = 0; j < classifiers[i].weights.length; j++) {//-0.5 - 0.5
                    classifiers[i].weights[j] = Math.random() - 0.5;
                }
            }

            double[][] trainingDataArray = trainingData.toArray(new double[trainingData.size()][2]);
            int[] labelsArray = labels.stream().mapToInt(Integer::intValue).toArray();

            StringBuilder infoText = new StringBuilder();
            for (int iteration = 0; iteration < maxIterations; iteration++) {
                infoText.append("Iteration: " + (iteration + 1) + "\n");
                int numErrors = 0;
                for (int c = 0; c < numClasses; c++) {
                    double[] weights = classifiers[c].weights;
                    for (int i = 0; i < trainingData.size(); i++) {
                        int label = (labelsArray[i] == c) ? 1 : 0;
                        int prediction = classifiers[c].predict(trainingDataArray[i]);
                        double error = label - prediction;
                        if (error != 0) {
                            numErrors++;
                            weights[0] += learningRate * error;
                            for (int j = 0; j < trainingDataArray[i].length; j++) {
                                weights[j + 1] += learningRate * error * trainingDataArray[i][j];
                            }
                        }
                    }
                }
                infoText.append("Number of errors: " + numErrors + "\n");
                if (numErrors <= errorThreshold) {
                    break;
                }
            }
            infoTextArea.setText(infoText.toString());
            graphPanel.repaint();
        } else if (e.getSource() == clearButton) {
            trainingData.clear();
            labels.clear();
            classifiers = null;
            infoTextArea.setText("");
            graphPanel.repaint();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == learningRateSlider) {
            double learningRate = learningRateSlider.getValue() / 100.0;
            learningRateLabel.setText("Learning Rate: " + learningRate);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            double x = (double) e.getX() / graphPanel.getWidth();
            double y = (double) e.getY() / graphPanel.getHeight();
            trainingData.add(new double[]{x, y});
            labels.add(labelComboBox.getSelectedIndex());
            graphPanel.repaint();
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            if (!trainingData.isEmpty()) {
                trainingData.remove(trainingData.size() - 1);
                labels.remove(labels.size() - 1);
                graphPanel.repaint();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public static void main(String[] args) {
        new ClassificationApp();
    }

}
