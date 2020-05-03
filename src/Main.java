import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        ArrayList<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("textFiles\\DFA_Input_1.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);

            }
        }

        String[] alphabets = lines.get(0).split(" ");

        String[] states = lines.get(1).split(" ");
        String start = lines.get(2);
        String[] finalStates = lines.get(3).split(" ");
        HashMap<String[],String> transmissions = new HashMap<>();
        for (int i = 4; i <lines.size() ; i++) {
            String [] key = new String[2];
            String[] t = lines.get(i).split(" ");
            key[0] = t[0];
            key[1] = t[1];


            transmissions.put(key,t[2]);


        }


        Dfa dfa = new Dfa(start,alphabets,states,finalStates,transmissions);

        System.out.println("Please enter input string: ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();



        if(dfa.isStringAccepted(input)){
            System.out.println("This input string is accepted by  DFA_Input_1  ");
        }
        else {
            System.out.println("This input string is 'not' accepted by DFA_Input_1 ");
        }
    }
}
