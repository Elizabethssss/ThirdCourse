package sysProg.lab1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordProcess {
    protected static final List<String> RESULT = new ArrayList<>();
    public static final String VOWELS = "aeiouy";

    public static void main(String[] args) {
        readText();
        for (String res : RESULT) {
            System.out.println(res);
        }
    }

    public static void readText() {
        try (BufferedReader reader = new BufferedReader(new FileReader("text.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = removePunctuationAndGetArrayOfWords(line);
                for (String word : words) {
                    char[] charBox = word.toLowerCase().toCharArray();
                    int counter = countVowels(charBox);
                    addWordToResult(word, charBox, counter);
                }
            }
        } catch (IOException e) {
            System.out.println("Can't read file!");
        }
    }

    private static int countVowels(char[] charBox) {
        int counter = 0;
        for (char c : charBox) {
            if (Character.isLetter(c)) {
                if (VOWELS.indexOf(c) != -1) {
                    counter++;
                } else {
                    break;
                }
            }
        }
        return counter;
    }

    private static String[] removePunctuationAndGetArrayOfWords(String line) {
        line = line.replaceAll("[^a-zA-Z\\s]", "").replaceAll("\\s+", " ");
        return line.split(" ");
    }

    private static void addWordToResult(String word, char[] charBox, int counter) {
        if (counter == charBox.length && !RESULT.contains(word)) {
            RESULT.add(word);
        }
    }

}