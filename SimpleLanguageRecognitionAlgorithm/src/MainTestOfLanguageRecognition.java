
import java.io.*;
import java.util.*;

public class MainTestOfLanguageRecognition {

    public static void main(String[] args) throws IOException {
        File trainingDirectory = new File("trainingLanguages");
        File testDirectory = new File("testLanguages");

        Map<Perceptron, String> perceptronLanguageMap = new HashMap<>();
        Map<UnitLine, String> trainInputVectors = new HashMap<>();
        Map<UnitLine, String> testInputVectors = new HashMap<>();

        List<File> trainingFiles = new ArrayList<>();
        List<File> testFiles = new ArrayList<>();

        FileFinder.findFiles(trainingDirectory, trainingFiles);
        FileFinder.findFiles(testDirectory, testFiles);
        List<String> languages = new ArrayList<>();

        FileFinder.findLanguages(trainingDirectory, languages);

        for (int i = 0; i < trainingFiles.size(); i++) {
            Double[] values;
            values = FileFinder.readFromFile(trainingFiles.get(i));
            values = normalization(values);
            trainInputVectors.put(new UnitLine(values), languages.get(i));
            perceptronLanguageMap.put(new Perceptron(), languages.get(i));
        }

        assingUnitsToPerceptron(perceptronLanguageMap, trainInputVectors);
        boolean userInput = true;
        System.out.println("Do you like to enter test case manually? Yes/No");
        String line = new Scanner(System.in).nextLine();

        if (line.toLowerCase().equals("no")) {
            userInput = false;
        }
        if (userInput) {
            userInput(perceptronLanguageMap);
        } else {

            FileFinder.findLanguages(testDirectory, languages);
            for (int i = 0; i < testFiles.size(); i++) {
                Double[] values;
                values = FileFinder.readFromFile(testFiles.get(i));
                values = normalization(values);
                testInputVectors.put(new UnitLine(values), languages.get(i));
            }

            for (UnitLine unit : testInputVectors.keySet()) {
                System.out.println("Observed text: " + testInputVectors.get(unit));
                testPerceptrons(perceptronLanguageMap, unit);
            }

        }
    }

    private static void userInput(Map<Perceptron, String> perceptronStringMap) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String text;
            while (!(text = reader.readLine()).toLowerCase().equals("Stop")) {
                Map<Character, Double> mapOfChars = new HashMap<>(26);
                FileFinder.countLettersInText(text, mapOfChars);
                FileFinder.checkForAllLetters(mapOfChars);

                Double[] values = mapOfChars.values().toArray(new Double[0]);
                testPerceptrons(perceptronStringMap, new UnitLine(normalization(values)));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void testPerceptrons(Map<Perceptron, String> perceptronStringMap, UnitLine testInputs) {
        List<Perceptron> list = new ArrayList<>(perceptronStringMap.keySet());
        Double[] dotProduct = new Double[list.size()];
        int iter = 0;
        for (Perceptron per : list) {
            dotProduct[iter] = dotProduct(per.getWeights(), testInputs.getValues());
            ++iter;
        }
        double max = 0;
        int index = 0;
        for (int i = 0; i < dotProduct.length; i++) {
            if (max < dotProduct[i]) {
                max = dotProduct[i];
                index = i;
            }
        }

        System.out.println("Perceptrons' decision: " + perceptronStringMap.get(list.get(index)));
    }

    private static double dotProduct(Double[] weights, Double[] input) {
        double product = 0;
        for (int i = 0; i < weights.length; i++) {
            product += weights[i] * input[i];
        }
        return product;
    }

    private static void assingUnitsToPerceptron(Map<Perceptron, String> perceptronsLanguage, Map<UnitLine, String> inputs) {
        System.out.println("Setting neurons up");
        Set<Perceptron> perceptrons = perceptronsLanguage.keySet();
        Set<UnitLine> units = inputs.keySet();
        for (Perceptron per : perceptrons) {
            List<UnitLine> inputForPerceptron = new ArrayList<>();
            for (UnitLine unit : units) {
                if (perceptronsLanguage.get(per).equals(inputs.get(unit))) {
                    inputForPerceptron.add(new UnitLine(unit.getValues(), 1));
                } else {
                    inputForPerceptron.add(new UnitLine(unit.getValues(), 0));
                }
            }
            per.startPerceptron(inputForPerceptron);
        }
    }

    private static Double[] normalization(Double[] values) {
        double magnitude;
        double sum = 0;
        for (Double value : values) {
            sum += Math.pow(value, 2);
        }
        magnitude = Math.sqrt(sum);
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i] / magnitude;
        }
        return values;
    }
}
