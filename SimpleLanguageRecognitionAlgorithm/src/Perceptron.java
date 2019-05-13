

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Perceptron {

    private Double[] weights;
    private double threshold;
    private double alpha;


    public Perceptron(){
    }
    Perceptron( List<UnitLine> trainingList) {
        startPerceptron(trainingList);
    }

    public Double[] getWeights() {
        return weights;
    }

    public void startPerceptron(List<UnitLine> list){
        System.out.println("Enter epoches");
        int epoch = new Scanner(System.in).nextInt();
        System.out.println("Enter learning rate"); //bigger than 0 but less than 1
        alpha = new Scanner(System.in).nextDouble();

        threshold = 0.2;
        weights = startWeight(list.get(0).values.length);

        for (int i = 0; i < epoch; i++) {
            learning(list);
        }
    }

    private Double[] startWeight(int dim) {
        Double[] temp = new Double[dim];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = 0.0;
        }
        return temp;
    }
    private void learning(List<UnitLine> list) {
        for (UnitLine u : list) {
            int output= PerceptronOutput(u.values);;
            computation(output,u);
        }
    }
    private void computation(int output,UnitLine unit){
        for (int i = 0; i < weights.length; i++) {
            weights[i] = weights[i] + (unit.prediction - output) * alpha * unit.values[i];
        }
        threshold = threshold + (unit.prediction - output) * alpha * -1;
    }

    private int PerceptronOutput(Double[] input) {
        double sum = 0;
        for (int i = 0; i < input.length; i++) {
            sum += weights[i] * input[i];
        }
        if (sum > threshold)
            return 1;
        else
            return 0;
    }
}
