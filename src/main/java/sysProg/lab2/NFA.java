package sysProg.lab2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class NFA {
    private List<String> alphabet;
    private List<String> states;
    private String initial;
    private List<String> finals;
    private String[][] transition;
    private int counter = 0;

    public static NFA readFrom(String path) {
        final NFA current = new NFA();
        if (current.readNFA(path)) {
            return current;
        } else {
            return null;
        }
    }

    private boolean readNFA(String path) {
        try (BufferedReader in = new BufferedReader(new FileReader(path))) {
            alphabet = Arrays.asList(in.readLine().split(","));
            states = Arrays.asList(in.readLine().split(","));
            initial = in.readLine();
            finals = Arrays.asList(in.readLine().split(","));
            String[][] statesMatrix = new String[states.size()][alphabet.size()];
            for (String[] strings : statesMatrix) {
                Arrays.fill(strings, "x");
            }
            String currentLine;
            String toState;
            String value;
            int posLetter;
            int fromState;
            while ((currentLine = in.readLine()) != null) {
                fromState = states.indexOf(String.valueOf(currentLine.charAt(0)));
                posLetter = alphabet.indexOf(String.valueOf(currentLine.charAt(1)));
                toState = String.valueOf(currentLine.charAt(2));
                value = statesMatrix[fromState][posLetter];
                if (value.equals("x")) {
                    statesMatrix[fromState][posLetter] = toState;
                } else if (!value.startsWith("{")) {
                    value = "{" + value + ";" + toState + "}";
                    statesMatrix[fromState][posLetter] = value;
                } else {
                    value = value.substring(0, value.length() - 1);
                    value = value + ";" + toState + "}";
                    statesMatrix[fromState][posLetter] = value;
                }
            }
            transition = statesMatrix;
            return true;
        }
        catch (FileNotFoundException e) {
            System.out.printf("File does not exist!");
            return false;
        }
        catch (IOException e) {
            System.out.printf("Wrong file input!");
            return false;
        }
    }

    public void compute(String testWord) {
        if (isResponsible(testWord)) {
            machineProcess(testWord, initial);
        } else {
            System.out.println("Word contains incorrect characters!");
        }
    }

    private boolean isResponsible(String testWord) {
        for (int i = 0; i < testWord.length(); i++) {
            if (!alphabet.contains(String.valueOf(testWord.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    private void machineProcess(String testWord, String currentState) {
        if (!testWord.equals("")) {
            counter++;
            int posLetter = alphabet.indexOf(String.valueOf(testWord.charAt(0)));
            int posState = states.indexOf(currentState);
            String newState = transition[posState][posLetter];
            if (newState.equals("x")) {
                System.out.println("Transition not defined, stop!");
                return;
            }

            if (newState.startsWith("{")) {
                newState = newState.substring(1);
                newState = newState.substring(0, newState.length() - 1);
                String[] currentStates = newState.split(";");
                for (String state : currentStates) {
                    makeStepAndRecognizeWord(testWord, state);
                }
                return;
            }
            makeStepAndRecognizeWord(testWord, newState);
        }

    }

    private void makeStepAndRecognizeWord(String testWord, String state) {
        System.out.print("Position" + counter + ":\t");
        System.out.println("Letter: '" + testWord.charAt(0) + "', to state: " + state);
        if (testWord.length() == 1) {
            if (isFinal(state)) {
                System.out.println("Word was recognized!");
            } else {
                System.out.println("Error, word wasn`t recognized!");
            }
            counter = 1;
        } else {
            machineProcess(testWord.substring(1), state);
        }
    }

    private boolean isFinal(String state) {
        return finals.contains(state);
    }

}

