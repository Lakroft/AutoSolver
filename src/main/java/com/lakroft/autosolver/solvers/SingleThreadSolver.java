package com.lakroft.autosolver.solvers;

import com.lakroft.autosolver.RulesMaster;
import com.lakroft.autosolver.Solver;
import com.lakroft.autosolver.State;

import java.util.*;
import java.util.stream.Collectors;

public class SingleThreadSolver extends Solver {
    public SingleThreadSolver(RulesMaster rulesMaster) {
        super(rulesMaster);
    }

    public List<State> solve() {
        Queue<LinkedList<State>> mainQueue = new LinkedList<LinkedList<State>>();
        Set<State> everSeenStates = new HashSet<>();

        State startState = rulesMaster.getStartState();
        everSeenStates.add(startState);
        mainQueue.add(new LinkedList<>(Arrays.asList(startState)));
        while (true) {
            LinkedList<State> currentChain = mainQueue.poll();
            State lastState = currentChain.size() > 1 ? currentChain.getLast() : currentChain.getFirst();

            List<State> nextStates = rulesMaster.availableMoves(lastState).stream().filter(p -> !everSeenStates.contains(p)).collect(Collectors.toList());
            everSeenStates.addAll(nextStates);

            for (State state : nextStates) {
                if (rulesMaster.isSolved(state)) {
                    currentChain.add(state);
                    return currentChain;
                }

                LinkedList<State> newChain = new LinkedList<>(currentChain);
                newChain.add(state);
                mainQueue.add(newChain);
            }
        }
    }
}
