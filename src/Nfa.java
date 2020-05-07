import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;



public class Nfa {

    private String[] alphabets;
    private String[] states;

    private String[] finalStates;
    private String start;
    //There are more than one next state for one current state and input in NFA
    // therefore I used this structure for transmissions in Nfa class
    private ArrayList<String[]> transmissions;

    /**
     *
     * @param fileName is the path of Nfa_Input file
     * @throws IOException for reading file
     */
    Nfa(String fileName) throws IOException {
        //Lines of input file for NFA machine
        transmissions = new ArrayList<>();

        //lines of the NFA file
        ArrayList<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);

            }
        }

        alphabets = lines.get(0).split(" ");

        states = lines.get(1).split(" ");
        start = lines.get(2);

        finalStates = lines.get(3).split(" ");
        for (int i = 4; i < lines.size(); i++) {
            transmissions.add(lines.get(i).split(" "));

        }
    }
    // Method to return delta* of a state and its input


    /**
     * This method is for returning δ*(state,input)
     * @param state is current state in δ*
     * @param input is input in δ*
     * @return δ*
     */
    ArrayList<String> deltaStar(String state, String input) {
        String lambda = "λ";
//for those those transmissions that are like "state input ..."
        ArrayList<String> deltastar = new ArrayList<>();
        for (String[] transmission : transmissions) {
            if (transmission[0].equals(state)) {
                if (transmission[1].equals(input)) {
                    if (!deltastar.contains(transmission[2]))
                        deltastar.add(transmission[2]);

                }
            }
        }
///for those those transmissions that are like "state λ state2","state2 λ state3",... or λ_closure state
        ArrayList<String> temp = new ArrayList<>();
        for (String[] transmission : transmissions) {
            if (transmission[0].equals(state) || temp.contains(transmission[0])) {
                if (transmission[1].equals(lambda)) {
                    temp.add(transmission[2]);



                }
            }
        }
///for those transmissions that are like "λ_closure input ..."

        for (int i = 0; i <temp.size() ; i++) {
            for (int j = 0; j <transmissions.size() ; j++) {
                if(transmissions.get(j)[0].equals(temp.get(i))){
                    if(transmissions.get(j)[1].equals(input)){
                        if (!deltastar.contains(transmissions.get(j)[2]))
                            deltastar.add(transmissions.get(j)[2]);
                    }
                }
            }
        }


///for transmissions that have λ after giving input such as state3 in "state input state2","state2 λ state3"


        for (int i = 0; i < deltastar.size(); i++) {
            for (String[] transmission : transmissions) {
                if (transmission[0].equals(deltastar.get(i))) {
                    if (transmission[1].equals(lambda)) {
                        if (!deltastar.contains(transmission[2]))
                            deltastar.add(transmission[2]);

                    }
                }
            }

        }

        return deltastar;
    }

    /**
     * Method for creating DFA from given nfa
     * @throws IOException for reading nfa file
     */
    void nfa2dfa() throws IOException {

        Dfa dfa = new Dfa(start, alphabets);



        while (!isAlgorithmDone(dfa)) {
            findNextStates(dfa);

        }
        setFinalStates(dfa);
        writeDfaToFile(dfa,"textFiles/DFA_Output _2.txt");


    }

    /**
     *
     * @param dfa is an object of Dfa class
     * @param s file path for writing dfa characters in it
     * @throws IOException for writing file
     */
    private void writeDfaToFile(Dfa dfa, String s) throws IOException {
        PrintWriter writer = new PrintWriter(s, StandardCharsets.UTF_8);
        ArrayList<String> lines = new ArrayList<>();
        String dfaAlphabets = "";
        for (String alphabet : alphabets) {
            dfaAlphabets = dfaAlphabets.concat(alphabet);
            dfaAlphabets = dfaAlphabets.concat(" ");



        }
        String dfaStates  = "";
        ArrayList<String> states = new ArrayList<>();
        states.add(dfa.getStates().get(0));
        for (int i = 0; i <dfa.getStates().size() ; i++) {

            if(!isStateExist(states,dfa.getStates().get(i)))
                states.add(dfa.getStates().get(i));


        }
        for (String state : states) {
             dfaStates = dfaStates.concat(state);
             dfaStates = dfaStates.concat(" ");


        }

        String dfaFinalStates  = "";
        ArrayList<String> finalStates = new ArrayList<>();
        finalStates.add(dfa.getFinalStates().get(0));
        for (int i = 0; i <dfa.getFinalStates().size() ; i++) {

            if(!isStateExist(finalStates,dfa.getFinalStates().get(i)))
                finalStates.add(dfa.getFinalStates().get(i));


        }
        for (String state : finalStates) {
            dfaFinalStates = dfaFinalStates.concat(state);
            dfaFinalStates = dfaFinalStates.concat(" ");


        }


        lines.add(dfaAlphabets);
        lines.add(dfaStates);
        lines.add(dfa.getStart());
        lines.add(dfaFinalStates);

        for (String[] key: dfa.getTransmissions().keySet()) {
            String transmission = "";
            transmission = transmission.concat(key[0]);
            transmission = transmission.concat(" ");
            transmission = transmission.concat(key[1]);
            transmission = transmission.concat(" ");
            transmission = transmission.concat(dfa.getTransmissions().get(key));
            if(!lines.contains(transmission))
                lines.add(transmission);


        }


        for (String line : lines) {
            writer.println(line);
        }
        writer.close();
    }

    /**
     * set final states of dfa witch is is defined by nfa final states
     * @param dfa object of Dfa
     */
    private void setFinalStates(Dfa dfa) {
        for (int i = 0; i <dfa.getStates().size() ; i++) {
            for (String finalState : finalStates) {
                if (dfa.getStates().get(i).contains(finalState)) {
                    dfa.addFinalStates(dfa.getStates().get(i));
                }
            }
        }
    }


    /**
     *If foe each state there was 'number of alphabets' transmissions algorithm is done
     * @param dfa object of Dfa
     * @return true if algorithm is done
     */
    private boolean isAlgorithmDone(Dfa dfa) {
        for (int i = 0; i < dfa.getStates().size(); i++) {

            for (int l = 0; l < alphabets.length; l++) {

                if(!isEdgeExist(dfa,dfa.getStates().get(i),alphabets[l])){
                    return false;
                }
            }

        }
        return true;

    }

    /**
     * Fine next transmissions in each state of algorithm
     * @param dfa is object of Dfa
     */
    private void findNextStates(Dfa dfa) {

        for (int i = 0; i < dfa.getStates().size(); i++) {
            if(!dfa.getStates().get(i).equals("Φ")) {
                for (int j = 0; j < alphabets.length; j++) {
                    List<String> temp = new ArrayList<>();

                    String nextState = "";

                    if (!isEdgeExist(dfa, dfa.getStates().get(i), alphabets[j])) {

                        for (int k = 0; k < states.length; k++) {
                            if (dfa.getStates().get(i).contains(states[k])) {
                                // temp.add(deltaStar(states[k],alphabets[j]));
                                temp.addAll(deltaStar(states[k], alphabets[j]));
                            }
                        }
                        if (!temp.isEmpty()) {
                            for (int k = 0; k < temp.size(); k++) {
                                if (!nextState.contains(temp.get(k))) {
                                    nextState = nextState.concat(temp.get(k));
                                }
                            }
                        } else {
                            nextState = "Φ";
                        }
                        if(!dfa.getStates().contains(nextState))
                            dfa.addStates(nextState);
                        String[] key = new String[2];
                        key[0] = dfa.getStates().get(i);
                        key[1] = alphabets[j];
                        dfa.addTransmissions(key, nextState);


                    }
                }
            }

            if(dfa.getStates().get(i).equals("Φ")){
                for (String alphabet : alphabets) {
                    String[] key = new String[2];
                    key[0] = "Φ";
                    key[1] = alphabet;
                    dfa.addTransmissions(key, "Φ");
                }
            }
        }
    }

    /**
     *
     * @param dfa object of Dfa
     * @param s in a state
     * @param alphabet is input for a state
     * @return true if transmission for 'state input ...' exist in dfa transmission
     */
    private boolean isEdgeExist(Dfa dfa, String s, String alphabet) {
        String[] key = new String[2];
        key[0] = s;
        key[1] = alphabet;
        List<String> wordList1 = Arrays.asList(key[0].split(""));




        for (String[] str : dfa.getTransmissions().keySet()) {
            List<String> wordList2 = Arrays.asList(str[0].split(""));

            if (wordList2.containsAll(wordList1) && wordList1.size()==wordList2.size() && str[1].equals(alphabet) )
                return true;
        }

        return false;
    }

    /**
     *
     * @param states is list of states
     * @param s is a state
     * @return true if state s exists in states
     */
    private boolean isStateExist(ArrayList<String> states, String s){
        for (int i = 0; i <states.size() ; i++) {
            List<String> wordList1 =  Arrays.asList(states.get(i).split(""));
            List<String> wordList2 =  Arrays.asList(s.split(""));
            //for example state q1q2q3 is similar to qq3q1q2 in this dfa
            if(wordList1.containsAll(wordList2) && wordList1.size()==wordList2.size())
                return true;



        }
        return false;

    }
}