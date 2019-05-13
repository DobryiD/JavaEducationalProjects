

import java.io.*;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class FileFinder {

    static void findLanguages(File directory, List<String> languages) {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                languages.add(file.getName());
            }
        }
    }
    static void findFiles(File directory, List<File> trainingFiles) {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                for (File subFile : Objects.requireNonNull(file.listFiles())) {
                    if (subFile.isFile()) {
                        trainingFiles.add(subFile);
                    }
                }
            }
        }
    }
    static Double[] readFromFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        Map<Character, Double> mapOfChars = new HashMap<>(26);
        while (reader.ready()) {
            String line = reader.readLine();
            countLettersInText(line,mapOfChars);
        }
        checkForAllLetters(mapOfChars);
        return mapOfChars.values().toArray(new Double[0]);
    }
    static void countLettersInText(String text,Map<Character,Double>mapOfChars){
        text = refactorString(text);
        for (int i = 0; i < text.length(); ++i) {
            char charAt = text.charAt(i);
            if (!mapOfChars.containsKey(charAt)) {
                mapOfChars.put(charAt, 1.0);
            } else {
                mapOfChars.put(charAt, mapOfChars.get(charAt) + 1);
            }
        }
    }
    static void checkForAllLetters(Map<Character,Double>mapOfChars){
        char iterator = 'a';
        while ((int)iterator <= (int)'z') {
            if (!mapOfChars.containsKey(iterator)) {
                mapOfChars.put(iterator, 0.0);
            }
            iterator = (char) (iterator + 1);
        }
    }
    private static String refactorString(String string) {
        string = Normalizer.normalize(string, Normalizer.Form.NFKD);
        string = string.replaceAll("[^\\p{ASCII}]", "");
        string = string.replaceAll("[^a-zA-Z]", "");

        return string.toLowerCase();
    }
}
