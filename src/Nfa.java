import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;



public class Nfa {

    private String[] alphabets;
    private String[] states;

    private String[] finalStates;
    private String start;
    //There are more than one next state for one current state and input
    private ArrayList<String[]> transmissions;

    Nfa(String fileName) throws IOException {
        //Lines of input file for NFA machine
        transmissions = new ArrayList<>();

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

    ArrayList<String> deltaStar(String state, String input) {
        String lambda = "λ";
//اضافه کردن آن هایی که در state قرار داردند و ورودی آنها input است
        ArrayList<String> deltastar = new ArrayList<>();
        for (String[] transmission : transmissions) {
            if (transmission[0].equals(state)) {
                if (transmission[1].equals(input)) {
                    if (!deltastar.contains(transmission[2]))
                        deltastar.add(transmission[2]);

                }
            }
        }
///اگر اولش تعدادی لاندا اومد بعد input اومد هم باید اضافه بشه
        ArrayList<String> temp = new ArrayList<>();
        for (String[] transmission : transmissions) {
            if (transmission[0].equals(state) || temp.contains(transmission[0])) {
                if (transmission[1].equals(lambda)) {
                    temp.add(transmission[2]);

//                    if (!temp.contains(transmission[2]))
//                        temp.add(transmission[2]);

                }
            }
        }

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

    void nfa2dfa() throws IOException {

        Dfa dfa = new Dfa(start, alphabets);


        //flag is weather each state in dfa has all outer
        while (!isAlgorithmDone(dfa)) {
            findNextStates(dfa);

        }
        setFinalStates(dfa);
        writeDfaToFile(dfa,"textFiles/DFA_Output _2.txt");


    }

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

    private void setFinalStates(Dfa dfa) {
        for (int i = 0; i <dfa.getStates().size() ; i++) {
            for (String finalState : finalStates) {
                if (dfa.getStates().get(i).contains(finalState)) {
                    dfa.addFinalStates(dfa.getStates().get(i));
                }
            }
        }
    }

    //اگر به تعداد الفبا یال خروجی برای هر state وجود داشته باشد الگوریتم به پایان رسیده است.

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

    private void findNextStates(Dfa dfa) {
        //به ازای همه qi های موجود در یک state  درصورتی که یال های خروجی به تعداد الفبا وجود نداشته باشد، دلتا* برای هر کدام از الفبا ها محاسبه شود و دلتا* qi ها با هم اجتماع گرفته شود
        for (int i = 0; i < dfa.getStates().size(); i++) {
            if(!dfa.getStates().get(i).equals("Φ")) {
                for (int j = 0; j < alphabets.length; j++) {
                    List<String> temp = new ArrayList<>();
//
                    String nextState = "";

                    if (!isEdgeExist(dfa, dfa.getStates().get(i), alphabets[j])) {
                        //////////////////
                        for (int k = 0; k < states.length; k++) {
                            if (dfa.getStates().get(i).contains(states[k])) {
                                // temp.add(deltaStar(states[k],alphabets[j]));
                                temp.addAll(deltaStar(states[k], alphabets[j]));
                                System.out.println(temp);
//                                Set<String> fooSet = new LinkedHashSet<>(temp);
//                                fooSet.addAll(deltaStar(states[k], alphabets[j]));
//                                List<String> finalFoo = new ArrayList<>(fooSet);
//                                System.out.println(finalFoo);
//                                temp =  finalFoo;
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

    private boolean isEdgeExist(Dfa dfa, String s, String alphabet) {
        String[] key = new String[2];
        key[0] = s;
        key[1] = alphabet;
        List<String> wordList1 = Arrays.asList(key[0].split(""));


        // if(dfa.getTransmissions().keySet().contains(key))
//        for (String[] str : dfa.getTransmissions().keySet()) {
//            if (Arrays.equals(key, str))
//                return true;
//        }

        for (String[] str : dfa.getTransmissions().keySet()) {
            List<String> wordList2 = Arrays.asList(str[0].split(""));

            if (wordList2.containsAll(wordList1) && wordList1.size()==wordList2.size() && str[1].equals(alphabet) )
                return true;
        }

        return false;
    }

    boolean isStateExist(ArrayList<String> states, String s){
        for (int i = 0; i <states.size() ; i++) {
            List<String> wordList1 =  Arrays.asList(states.get(i).split(""));
            List<String> wordList2 =  Arrays.asList(s.split(""));
            if(wordList1.containsAll(wordList2) && wordList1.size()==wordList2.size())
                return true;



        }
        return false;

    }
}