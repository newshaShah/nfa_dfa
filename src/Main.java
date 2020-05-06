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
//        String str1 = "q0q2q1";
//        String str2 = "q2q0q1";
//
//        String[] words1 = str1.split("");
//        String[] words2 = str2.split("");
//
//
//        List<String> wordList1 = Arrays.asList(words1);
//        List<String> wordList2 = Arrays.asList(words2);
//        System.out.println(wordList2.containsAll(wordList1));

       // System.out.println(nfa.deltaStar("q0","0"));
        nfa.nfa2dfa();





    }
}
