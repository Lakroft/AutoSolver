package com.lakroft.autosolver;

import com.lakroft.autosolver.rulesmasters.PentagrammaRulesMaster;
import com.lakroft.autosolver.solvers.RunnableBasedSolver;
import com.lakroft.autosolver.solvers.SingleThreadSolver;

import java.util.List;

public class Main {

    public static void main(String[] args) {
//        List<State> allStates = new SingleThreadSolver(new PentagrammaRulesMaster()).solve();
        List<State> allStates = new RunnableBasedSolver(new PentagrammaRulesMaster()).solve();
        System.out.println("O - light off;\t| - light on");
        System.out.println(" 0 1 2 3 4");
        for (State state : allStates) {
            System.out.println(state);
        }
    }
}
