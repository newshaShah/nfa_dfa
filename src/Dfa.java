import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class Dfa {

    private String currentState;
    private String[] alphabets;
    private String[] states;

    private String[] finalStates;
    private HashMap<String[], String> transmissions;

    Dfa(String fileName) throws IOException {
        ArrayList<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);

            }
        }

        alphabets = lines.get(0).split(" ");

        states = lines.get(1).split(" ");
        currentState = lines.get(2);
        finalStates = lines.get(3).split(" ");
        transmissions = new HashMap<>();
        for (int i = 4; i < lines.size(); i++) {
            String[] key = new String[2];
            String[] t = lines.get(i).split(" ");
            key[0] = t[0];
            key[1] = t[1];


            transmissions.put(key, t[2]);


        }

    }

    private boolean IsFinalState() {
        for (String finalState : finalStates) {

            if (currentState.equals(finalState))
                return true;

        }
        return false;
    }

    private void nextState(String input) {
        String[] currInput = new String[2];

        currInput[0] = currentState;
        if (Arrays.asList(alphabets).contains(input)) {
            currInput[1] = input;
            for (String[] str : transmissions.keySet()) {
                if (Arrays.equals(currInput, str)) {

                    currentState = transmissions.get(str);
                    if (!Arrays.asList(states).contains(currentState))
                        System.out.println("Wrong input file for DFA machine\n"
                                + currentState + " is not in states of this machine");
                }

            }
        }


    }

    boolean isStringAccepted(String input) {
        for (int i = 0; i < input.length(); i++) {
            String inputI = Character.toString(input.charAt(i));
            if (Arrays.asList(alphabets).contains(inputI)) {

                nextState(inputI);
            } else
                return false;


        }
        return IsFinalState();
    }

}
