import java.util.*;
import java.io.*;
class State {
    State zero; // only one transition is possible by 0 or 1
    State one;  // null if empty
    ArrayList<State> epsilon;

    // constructor
    State(State zero, State one, State[] epsilon) {
        this.zero = zero;
        this.one = one;
        if(epsilon == null) {
            this.epsilon = new ArrayList<State>();
        }
        else {
            this.epsilon = new ArrayList<State>(Arrays.asList(epsilon));
        }
    }
    State() {
        this(null, null, null);
    }
    // states reachable by epsilon transitions
    public static Set<State> E(Set<State> q) {
        Set<State> E_q = new HashSet<State>();
        Set<State> temp = new HashSet<State>();
        temp.addAll(q);
        while(!temp.isEmpty()) {
            E_q.addAll(temp);
            Set<State> next = new HashSet<State>();
            for(State s : temp) {
                next.addAll(s.epsilon);
            }
            temp = next;
        }
        return E_q;
    }
    public static Set<State> E(State q) {
        Set<State> qSet = new HashSet<State>();
        qSet.add(q);
        return E(qSet);
    }
}

class NFA {
    State initState;
    State finalState;    // only one final state can exist, using the algorithm "fromString"

    // constructor
    NFA(State i, State f) {
        this.initState = i;
        this.finalState = f;
    }
    // create NFA from postfix string
    public static NFA fromString(String exp) {
        Stack<NFA> stk = new Stack<NFA>();
        for(int i=0;i<exp.length();i++) {
            State init = null;        // initial state of new NFA
            State fin = null;        // final state of new NFA
            NFA fst = null;            // two NFAs from stack used to create new NFA
            NFA snd = null;
            switch(exp.charAt(i)) {    // create new NFA
            case '0':
                fin = new State();
                init = new State(fin, null, null);
                break;
            case '1':
                fin = new State();
                init = new State(null, fin, null);
                break;
            case '+':
                snd = stk.pop();
                fst = stk.pop();
                fin = new State();
                init = new State(null, null, new State[]{fst.initState, snd.initState});
                fst.finalState.epsilon.add(fin);
                snd.finalState.epsilon.add(fin);
                break;
            case '*':
                fst = stk.pop();
                fin = new State();
                init = new State(null, null, new State[]{fst.initState, fin});
                fst.finalState.epsilon.add(fst.initState);
                fst.finalState.epsilon.add(fin);
                break;
            case '.':
                snd = stk.pop();
                fst = stk.pop();
                fin = snd.finalState;
                init = fst.initState;
                fst.finalState.epsilon.add(snd.initState);
                break;
            default:
                break;
            }
            stk.push(new NFA(init, fin));    // push new NFA
        }
        return stk.pop();    // pop final NFA left in the stack
    }

    // check if NFA accepts the string
    public boolean accepts(String str) {
        Set<State> currentState = new HashSet<State>();
        currentState.addAll(State.E(initState));
        for(int i=0;i<str.length();i++) {
            Set<State> afterDelta = new HashSet<State>();
            for(State s : currentState) {
                if(str.charAt(i) == '0') {
                    if(s.zero != null) afterDelta.add(s.zero);
                }
                else {
                    if(s.one != null) afterDelta.add(s.one);
                }
            }
            currentState = State.E(afterDelta);
        }
        return currentState.contains(finalState);
    }
}

public class RegExpMatching {
    public static void main(String[] args) {
        try {
            BufferedReader input = new BufferedReader(new FileReader("input.txt"));
            BufferedWriter output = new BufferedWriter(new FileWriter("2016_14190.out"));
            int T = Integer.parseInt(input.readLine());    // number of test cases
            for(int i=0;i<T;i++) {
                String rexp[] = input.readLine().split(" ");
                String exp = rexp[1];    // regular expression
                String file = rexp[2];    // file name
                NFA regNFA = NFA.fromString(postfixOf(exp));
                output.write(regNFA.accepts(file) ? "yes" : "no");
                output.newLine();
            }
            input.close();
            output.close();
        }
        catch(IOException e) {
            System.err.println("File I/O Error");
        }
    }
    // change regular expression to postfix form
    private static String postfixOf(String exp) {
        String postfixExp = new String();
        Stack<Character> oper = new Stack<Character>();
        for(int i=0;i<exp.length();i++) {
            switch(exp.charAt(i)) {
            case '0':
            case '1':
                postfixExp += Character.toString(exp.charAt(i));
                break;
            case '+':
            case '*':
            case '.':
                oper.push(exp.charAt(i));
                break;
            case ')':
                postfixExp += Character.toString(oper.pop());
                break;
            default:
                break;
            }
        }
        while(!oper.empty()) {
            postfixExp += Character.toString(oper.pop());
        }
        return postfixExp;
    }
}

