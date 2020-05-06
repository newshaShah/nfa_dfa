import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

class Dfa {

    private String currentState;
    private String[] alphabets;
    private ArrayList<String> states;
    private String start;

    private ArrayList<String> finalStates;
    private HashMap<String[], String> transmissions;
    //A constructor for dfa use file direction for input
    Dfa(String fileName) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        states = new ArrayList<>();
        finalStates = new ArrayList<>();


        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);

            }
        }

        alphabets = lines.get(0).split(" ");

        Collections.addAll(states, lines.get(1).split(" "));
        //states = lines.get(1).split(" ");
        start = lines.get(2);
        currentState = start;
        Collections.addAll(finalStates, lines.get(3).split(" "));
        //finalStates = lines.get(3).split(" ");
        transmissions = new HashMap<>();
        for (int i = 4; i < lines.size(); i++) {
            String[] key = new String[2];
            String[] t = lines.get(i).split(" ");
            key[0] = t[0];
            key[1] = t[1];


            transmissions.put(key, t[2]);


        }

    }

    public Dfa(String start,String[] alphabets){
        states = new ArrayList<>();
        transmissions = new HashMap<>();
        finalStates = new ArrayList<>();
        this.start = start;
        this.alphabets = alphabets;
        states.add(start);




    }

//    public void setAlphabets(String[] alphabets) {
//        this.alphabets = alphabets;
//    }
//
    public void addFinalStates(String finalState) {
        //if(!finalStates.contains(finalState))
            finalStates.add(finalState);
    }
//
    public void addTransmissions(String[] key,String value) {
        transmissions.put(key,value);
    }
    public void addStates(String state) {
        states.add(state);
    }

    public HashMap<String[], String> getTransmissions() {
        return transmissions;
    }

    public ArrayList<String> getFinalStates() {
        return finalStates;
    }

    public String getStart() {
        return start;
    }

    public ArrayList<String> getStates() {
        return states;
    }

    //
//    public void setStart(String start) {
//        this.start = start;
//    }

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
                    if (!states.contains(currentState))
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
