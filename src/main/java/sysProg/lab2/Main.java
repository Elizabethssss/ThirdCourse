package sysProg.lab2;

import java.util.Optional;
import java.util.Scanner;

import static sysProg.lab2.NFA.readFrom;

public class Main {
    public static void main(String[] args) {
        final Scanner in = new Scanner(System.in);

        System.out.print("Enter the path to the file:\n");
        String path = in.next();
        System.out.print("Enter testWord:\n");
        String testWord = in.next();

        final Optional<NFA> nfa = Optional.ofNullable(readFrom(path));
        nfa.ifPresent(value -> value.compute(testWord));
    }
}
