import java.util.Arrays;
import java.util.HashMap;

class Dfa {

    private String currentState;
    private String[] alphabets;
    private String[] states;

    private String[] finalStates;
    private HashMap<String[], String> transmissions;

    Dfa(String start, String[] alphabets, String[] states, String[] finalStates, HashMap<String[], String> transmissions) {
        this.alphabets = alphabets;

        this.currentState = start;

        this.finalStates = finalStates;
        this.transmissions = transmissions;
        this.states = states;

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
