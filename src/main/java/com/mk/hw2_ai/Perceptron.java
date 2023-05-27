package com.mk.hw2_ai;

public class Perceptron {

    private int numInputs;// the number of inputs expected by the perceptron.
    public double[] weights;//array that holds the weights associated with each input
    private double learningRate;

    public Perceptron(int numInputs, double learningRate) {
        this.numInputs = numInputs;
        this.weights = new double[numInputs + 1];
        this.learningRate = learningRate;
    }

    public double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    //takes an array of input values and returns the predicted class label.
    public int predict(double[] inputs) {
        double sum = weights[0];
        for (int i = 0; i < inputs.length; i++) {
            sum += weights[i + 1] * inputs[i];
        }
        double activation = sigmoid(sum);
        return (activation >= 0.5) ? 1 : 0;
    }

    public void train(double[][] trainingData, int[] labels) {
        for (int i = 0; i < trainingData.length; i++) {
            int prediction = predict(trainingData[i]);
            double error = labels[i] - prediction; // expected - actual 
            weights[0] += learningRate * error;
            for (int j = 0; j < trainingData[i].length; j++) {
                weights[j + 1] += learningRate * error * trainingData[i][j];
            }
        }
    }
}
