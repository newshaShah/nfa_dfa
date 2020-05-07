import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


class Dfa {

    private String currentState;
    private String[] alphabets;
    private ArrayList<String> states;
    private String start;

    private ArrayList<String> finalStates;
    private HashMap<String[], String> transmissions;


    /**
     * A constructor for dfa use dfa file path for input
     * @param fileName is path of dfa file
     * @throws IOException for reading file
     */
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
        start = lines.get(2);
        currentState = start;
        Collections.addAll(finalStates, lines.get(3).split(" "));
        transmissions = new HashMap<>();
        for (int i = 4; i < lines.size(); i++) {
            String[] key = new String[2];
            String[] t = lines.get(i).split(" ");
            key[0] = t[0];
            key[1] = t[1];


            transmissions.put(key, t[2]);


        }

    }

    /**
     * A constructor that use param start and alphabet of dfa for creating it witch is used in nfa class
     * @param start is start state of dfa
     * @param alphabets is alphabets of dfa
     */
    public Dfa(String start,String[] alphabets){
        states = new ArrayList<>();
        transmissions = new HashMap<>();
        finalStates = new ArrayList<>();
        this.start = start;
        this.alphabets = alphabets;
        states.add(start);




    }


    public void addFinalStates(String finalState) {
            finalStates.add(finalState);
    }

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

//Checks whether a state is final state of a dfa or not
    private boolean IsFinalState() {
        for (String finalState : finalStates) {

            if (currentState.equals(finalState))
                return true;

        }
        return false;
    }

    //By using transmission , current state and input specify next state and then shift current state to next state
    private void nextState(String input) {
        String[] currInput = new String[2];

        currInput[0] = currentState;
        if (Arrays.asList(alphabets).contains(input)) {
            currInput[1] = input;
            List<String> wordList1 = Arrays.asList(currInput[0].split(""));
            for (String[] str : transmissions.keySet()) {



                    List<String> wordList2 = Arrays.asList(str[0].split(""));

                    if (wordList2.containsAll(wordList1) && wordList1.size() == wordList2.size() && str[1].equals(currInput[1]))
                        currentState = transmissions.get(str);




            }
        }


    }

    /**
     *
     * @param input is input string foe dfa machine
     * @return true whether input string is accepted
     */
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
