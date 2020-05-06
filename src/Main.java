import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {


        Dfa dfa = new Dfa("textFiles\\DFA_Input_1.txt");

        System.out.println("Please enter input string to check weather it is accepted by Dfa machine or not: ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();


        if (dfa.isStringAccepted(input)) {
            System.out.println("This input string: <"+input+"> is accepted by  DFA_Input_1  ");
        } else {
            System.out.println("This input string: <"+input+"> is 'not' accepted by DFA_Input_1 ");
        }
        System.out.println();

        System.out.println("Now wait for converting NFA to Dfa:");
        Nfa nfa = new Nfa("textFiles/NFA_Input_2.txt");
        nfa.nfa2dfa();
        System.out.println("Done!, The dfa machine characteristics is written in its file.");





    }
}
