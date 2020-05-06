import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {


        Dfa dfa = new Dfa("textFiles\\DFA_Input_1.txt");

        System.out.println("Please enter input string: ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();


        if (dfa.isStringAccepted(input)) {
            System.out.println("This input string is accepted by  DFA_Input_1  ");
        } else {
            System.out.println("This input string is 'not' accepted by DFA_Input_1 ");
        }

        Nfa nfa = new Nfa("textFiles/NFA_Input_2.txt");

       // System.out.println(nfa.deltaStar("q0","0"));
        nfa.nfa2dfa();





    }
}
